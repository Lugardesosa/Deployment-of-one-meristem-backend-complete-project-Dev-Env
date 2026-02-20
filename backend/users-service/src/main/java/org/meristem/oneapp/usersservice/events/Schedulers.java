package org.meristem.oneapp.usersservice.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.domains.enums.OutboxStatus;
import org.meristem.oneapp.usersservice.domains.enums.UserPinStatus;
import org.meristem.oneapp.usersservice.models.OutboxEvent;
import org.meristem.oneapp.usersservice.models.UserPin;
import org.meristem.oneapp.usersservice.repositories.OutboxEventRepository;
import org.meristem.oneapp.usersservice.repositories.UserPinRepository;
import org.meristem.oneapp.usersservice.services.IKafkaSenderService;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Component
@RequiredArgsConstructor
public class Schedulers {

    private final UserPinRepository userPinRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final IKafkaSenderService kafkaSenderService;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRateString = "${outbox.cron.fix-rate}", timeUnit = TimeUnit.MINUTES)
    @SchedulerLock(name = "OutboxTaskLock", lockAtMostFor = "1m", lockAtLeastFor = "30s")
    public void publishOutbox() {

        log.info("publishing outbox");
        List<OutboxEvent> events = outboxEventRepository.findAllByOutboxStatus(OutboxStatus.PENDING.getValue(), Sort.by(Sort.Order.asc("created_date")), Limit.of(100));

        do {

            List<OutboxEvent> toDelete = new ArrayList<>();
            for (OutboxEvent event : events) {
                try {
                    Class<?> clazz = Class.forName(event.getEventClass());
                    Object payload = objectMapper.readValue(event.getPayload(), clazz);
                    if (hasText((event.getEventKey()))) {
                        kafkaSenderService.send(event.getEventType(), event.getEventKey(), payload);
                    } else {
                        kafkaSenderService.send(event.getEventType(), payload);
                    }
                    toDelete.add(event);
                } catch (Exception e) {
                    event.setRetryCount(event.getRetryCount() + 1);
                    event.setLastError(e.getMessage());
                    if (event.getRetryCount() == AppConstants.OUTBOX_MAX_RETRY_COUNT) {
                        event.setOutboxStatus(OutboxStatus.FAILED.getValue());
                    } else {
                        event.setOutboxStatus(OutboxStatus.PENDING.getValue());
                    }
                    outboxEventRepository.save(event);
                    log.error("Error sending outbox event with id: {}", event.getId(), e);
                }
            }
            outboxEventRepository.deleteAll(toDelete);
            events = outboxEventRepository.findAllByOutboxStatus(OutboxStatus.PENDING.getValue(), Sort.by(Sort.Order.asc("created_date")), Limit.of(100));
        } while (!events.isEmpty());
    }

    @Scheduled(fixedRateString = "${pin-unlock.cron.fix-rate}", timeUnit = TimeUnit.MINUTES)
    @SchedulerLock(name = "UnlockTaskLock", lockAtMostFor = "5m", lockAtLeastFor = "1m")
    public void unlockPin() {

        log.info("Unlocking pins");
        int count = 0;
        List<UserPin> userPins = userPinRepository.findAllByStatusAndLockUntilBefore(UserPinStatus.LOCKED.getStatus(), LocalDateTime.now(), Limit.of(100));

        do {
            count += userPins.size();
            for (UserPin userPin : userPins) {
                userPin.setStatus(UserPinStatus.ACTIVE.getStatus());
                userPin.setFailedAttempts(0);
                userPin.setLockUntil(null);
                userPinRepository.save(userPin);
            }
            userPins = userPinRepository.findAllByStatusAndLockUntilBefore(UserPinStatus.LOCKED.getStatus(), LocalDateTime.now(), Limit.of(100));
        } while (!userPins.isEmpty());

        log.info("{} pins unlocked", count);
    }
}
