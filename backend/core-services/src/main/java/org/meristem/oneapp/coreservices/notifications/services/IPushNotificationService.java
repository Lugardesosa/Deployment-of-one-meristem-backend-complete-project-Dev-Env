package org.meristem.oneapp.coreservices.notifications.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.meristem.oneapp.kafka.dtos.PushNotificationDto;

public interface IPushNotificationService {
    @CircuitBreaker(name = "expo", fallbackMethod = "recoverExpoPushNotificationCircuit")
    void sendExpoPushNotification(PushNotificationDto notifications);

    void recoverExpoPushNotificationCircuit(PushNotificationDto notifications, Throwable throwable);

    void sendPushNotification(PushNotificationDto notifications);
    void recoverPushNotificationCircuit(PushNotificationDto notifications, Throwable throwable);
}
