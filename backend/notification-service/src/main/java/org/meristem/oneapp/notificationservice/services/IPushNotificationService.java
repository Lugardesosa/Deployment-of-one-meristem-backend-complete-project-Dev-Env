package org.meristem.oneapp.notificationservice.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.meristem.oneapp.kafka.dtos.PushNotificationDto;

public interface IPushNotificationService {
    @CircuitBreaker(name = "expo", fallbackMethod = "recoverExpoPushNotificationCircuit")
    void sendExpoPushNotification(PushNotificationDto notifications);


    void sendPushNotification(PushNotificationDto notifications);
}
