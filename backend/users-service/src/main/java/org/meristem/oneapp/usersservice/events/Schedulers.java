package org.meristem.oneapp.usersservice.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.meristem.oneapp.usersservice.domains.enums.UserPinStatus;
import org.meristem.oneapp.usersservice.models.UserPin;
import org.meristem.oneapp.usersservice.repositories.UserPinRepository;
import org.springframework.data.domain.Limit;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class Schedulers {

    private final UserPinRepository userPinRepository;

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
