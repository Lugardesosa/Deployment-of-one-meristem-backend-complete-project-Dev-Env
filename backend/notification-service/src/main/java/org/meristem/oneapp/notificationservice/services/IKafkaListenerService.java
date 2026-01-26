package org.meristem.oneapp.notificationservice.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.meristem.oneapp.kafka.dtos.MessageDto;
import org.meristem.oneapp.kafka.dtos.PushNotificationDto;
import org.meristem.oneapp.kafka.dtos.WebSocketDto;

public interface IKafkaListenerService {
    void sendOtp(ConsumerRecord<String, MessageDto> otpRequest);
    void sendWebSocketNotifications(ConsumerRecord<String, WebSocketDto> event);
    void sendPushNotifications(ConsumerRecord<String, PushNotificationDto> event);
}
