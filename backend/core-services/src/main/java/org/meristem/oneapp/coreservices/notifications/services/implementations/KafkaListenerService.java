package org.meristem.oneapp.coreservices.notifications.services.implementations;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.meristem.oneapp.coreservices.notifications.constants.KafkaTopics;
import org.meristem.oneapp.coreservices.notifications.services.IKafkaListenerService;
import org.meristem.oneapp.coreservices.notifications.services.IPushNotificationService;
import org.meristem.oneapp.coreservices.notifications.services.IWebsocketService;
import org.meristem.oneapp.coreservices.notifications.services.INotificationService;
import org.meristem.oneapp.kafka.dtos.MessageDto;
import org.meristem.oneapp.kafka.dtos.PushNotificationDto;
import org.meristem.oneapp.kafka.dtos.WebSocketDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class KafkaListenerService implements IKafkaListenerService {

    private final Map<String, INotificationService<MessageDto>> notificationServices;
    private final IWebsocketService websocketService;
    private final IPushNotificationService pushNotificationService;

    @KafkaListener(topics = {KafkaTopics.KAFKA_OTP_TOPIC, KafkaTopics.KAFKA_LOGIN_TOPIC, KafkaTopics.KAFKA_EMAIL_CONFIRMATION_TOPIC}, concurrency = "3")
    public void sendOtp(ConsumerRecord<String, MessageDto> otpRequest) {
        MessageDto notificationRequest = otpRequest.value();
        INotificationService<MessageDto> messageDtoINotificationService = notificationServices.get(notificationRequest.medium().getLabel());
        if (messageDtoINotificationService == null) {
            log.error("Messaging service not found");
            return;
        }
        messageDtoINotificationService.send(notificationRequest);
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
