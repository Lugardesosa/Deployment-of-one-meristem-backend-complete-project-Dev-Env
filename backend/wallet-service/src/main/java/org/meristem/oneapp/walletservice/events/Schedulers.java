package org.meristem.oneapp.walletservice.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.meristem.oneapp.walletservice.constants.AppConstants;
import org.meristem.oneapp.walletservice.domains.enums.OutboxStatus;
import org.meristem.oneapp.walletservice.models.OutboxEvent;
import org.meristem.oneapp.walletservice.repositories.OutboxEventRepository;
import org.meristem.oneapp.walletservice.services.IKafkaSenderService;
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

    private final OutboxEventRepository outboxEventRepository;
    private final IKafkaSenderService kafkaSenderService;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRateString = "${outbox.cron.fix-rate}", timeUnit = TimeUnit.SECONDS)
    @SchedulerLock(name = "OutboxTaskLock", lockAtMostFor = "2s", lockAtLeastFor = "1s")
    public void publishOutbox() {

        List<OutboxEvent> events = outboxEventRepository.findAllByOutboxStatus(OutboxStatus.PENDING.getValue(), Sort.by(Sort.Order.asc("created_date")), Limit.of(100));

        do {
            List<OutboxEvent> toUpdate = new ArrayList<>();
            for (OutboxEvent event : events) {
                try {
                    Class<?> clazz = Class.forName(event.getEventClass());
                    Object payload = objectMapper.readValue(event.getPayload(), clazz);
                    if (hasText((event.getEventKey()))) {
                        kafkaSenderService.send(event.getEventType(), event.getEventKey(), payload);
                    } else {
                        kafkaSenderService.send(event.getEventType(), payload);
                    }
                    event.setOutboxStatus(OutboxStatus.SENT.getValue());
                    event.setSentAt(LocalDateTime.now());
                    toUpdate.add(event);
                } catch (Exception e) {
                    event.setRetryCount(event.getRetryCount() + 1);
                    event.setLastError(e.getMessage());
                    if (event.getRetryCount() == AppConstants.OUTBOX_MAX_RETRY_COUNT) {
                        event.setOutboxStatus(OutboxStatus.FAILED.getValue());
                    } else {
                        event.setOutboxStatus(OutboxStatus.PENDING.getValue());
                    }
                    log.error("Error sending outbox event with id: {}", event.getId(), e);
                    toUpdate.add(event);
                }
            }
            outboxEventRepository.saveAll(toUpdate);
            events = outboxEventRepository.findAllByOutboxStatus(OutboxStatus.PENDING.getValue(), Sort.by(Sort.Order.asc("created_date")), Limit.of(100));
        } while (!events.isEmpty());
    }
}
