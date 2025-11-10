package org.meristem.oneapp.notificationservice.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.meristem.oneapp.kafka.dtos.MessageDto;
import org.meristem.oneapp.kafka.dtos.PushNotificationDto;
import org.meristem.oneapp.kafka.dtos.WebSocketDto;
import org.meristem.oneapp.notificationservice.constants.KafkaTopics;
import org.meristem.oneapp.notificationservice.services.interfaces.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class KafkaListenerService {

    private final Map<String, NotificationService<MessageDto>> notificationServices;
    private final WebsocketService websocketService;
    private final PushNotificationService pushNotificationService;

    @KafkaListener(topics = {KafkaTopics.KAFKA_OTP_TOPIC, KafkaTopics.KAFKA_LOGIN_TOPIC})
    public void sendOtp(ConsumerRecord<String, MessageDto> otpRequest) {
        MessageDto notificationRequest = otpRequest.value();
        NotificationService<MessageDto> messageDtoNotificationService = notificationServices.get(notificationRequest.medium().getLabel());
        if (messageDtoNotificationService == null) {
            log.error("Messaging service not found");
            return;
        }
        messageDtoNotificationService.send(notificationRequest);
    }

    @KafkaListener(topicPattern = KafkaTopics.KAFKA_WEB_SOCKET_TOPIC)
    public void sendWebSocketNotifications(ConsumerRecord<String, WebSocketDto> event) {
        websocketService.sendWebsocketMessage(event.value());
    }

    @KafkaListener(topicPattern = KafkaTopics.KAFKA_PUSH_NOTIFICATION_TOPIC)
    public void sendPushNotifications(ConsumerRecord<String, PushNotificationDto> event) {
        pushNotificationService.sendPushNotification(event.value());
    }
}
