package org.meristem.oneapp.notificationservice.services;

import org.meristem.oneapp.kafka.dtos.PushNotificationDto;

public interface IPushNotificationService {
    void sendPushNotification(PushNotificationDto notifications);
    void recoverPushNotificationCircuit(PushNotificationDto notifications, Throwable throwable);
}
