package org.meristem.oneapp.notificationservice.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.meristem.oneapp.notificationservice.constants.KafkaListenerConstants;
import org.meristem.oneapp.kafka.dtos.MessageDto;
import org.meristem.oneapp.notificationservice.services.interfaces.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class KafkaListenerService {

    private final Map<String, NotificationService<MessageDto>> notificationServices;

    @KafkaListener(topicPattern = KafkaListenerConstants.KAFKA_OTP_TOPIC)
    public void sendOtp(ConsumerRecord<String, MessageDto> otpRequest) {
        MessageDto notificationRequest = otpRequest.value();
        log.info("Received OTP request: {}", notificationRequest);
//        log.info("OUTPUT OTP request: {}", notificationRequest.message());
//        log.info("OUTPUT OTP request: {}-, {}-, {}-", notificationRequest.message().body(), notificationRequest.message().subject(), notificationRequest.message().recipient());
//        log.info("""
//                -----
//                -----
//                ----
//                -----
//                """);
        NotificationService<MessageDto> messageDtoNotificationService = notificationServices.get(notificationRequest.medium().getLabel());
        messageDtoNotificationService.send(notificationRequest);

    }
}
