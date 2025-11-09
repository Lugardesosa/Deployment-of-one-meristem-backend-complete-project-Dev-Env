package org.meristem.oneapp.notificationservice.services;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.PushNotificationDto;
import org.meristem.oneapp.notificationservice.constants.KafkaTopics;
import org.meristem.oneapp.notificationservice.integrations.ExpoPushNotificationClient;
import org.meristem.oneapp.notificationservice.integrations.requests.ExpoPushNotificationRequest;
import org.meristem.oneapp.notificationservice.integrations.responses.EmptyExpoResponse;
import org.meristem.oneapp.notificationservice.integrations.responses.ExpoPushNotificationResponse;
import org.meristem.oneapp.notificationservice.models.ExpoNotificationTicket;
import org.meristem.oneapp.notificationservice.repositories.CustomRepository;
import org.meristem.oneapp.notificationservice.repositories.UserExpoTokensRepository;
import org.meristem.oneapp.notificationservice.utils.AppUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PushNotificationService {

    @Value("${expo.push.notifications.token}")
    private String expoToken;
    private final UserExpoTokensRepository userExpoTokensRepository;
    private final ExpoPushNotificationClient expoPushNotificationClient;
    private final KafkaSenderService kafkaSenderService;
    private final CustomRepository customRepository;

    @CircuitBreaker(name = "expo", fallbackMethod = "recoverPushNotificationCircuit")
    public void sendPushNotification(PushNotificationDto notifications) {

        List<String> to;

        if (notifications.toAll()) {
            to = userExpoTokensRepository.findAllExpoTokens();
        } else {
            to = userExpoTokensRepository.findAllExpoTokensByUserId(AppUtil.getLoggedInUserId());
        }
        ExpoPushNotificationRequest request = ExpoPushNotificationRequest.builder()
                .to(to)
                .title(notifications.title())
                .body(notifications.body())
                .data(notifications.data())
                .build();
        List<ExpoPushNotificationResponse.ExpoPushResponse> response = expoPushNotificationClient.sendPushNotification(request).data();

        List<ExpoNotificationTicket> tickets = new ArrayList<>();
        List<String> tokenToDelete = new ArrayList<>();
        for (ExpoPushNotificationResponse.ExpoPushResponse r : response) {

            if ("ok".equalsIgnoreCase(r.status())) {
                tickets.add(ExpoNotificationTicket.builder().ticketId(r.id()).build());
            } else {
                if ("DeviceNotRegistered".equalsIgnoreCase(r.details().error())) {
                    tokenToDelete.add(AppUtil.extractExpoTokenWithRegex(r.message()));
                }
            }
        }
        customRepository.saveAll(tickets);
        userExpoTokensRepository.deleteUserExpoTokensByExpoTokenIn(tokenToDelete);
    }

    public EmptyExpoResponse recoverPushNotificationCircuit(Throwable throwable, PushNotificationDto notifications) {
        kafkaSenderService.send(notifications, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_PUSH_NOTIFICATION_TOPIC));
        return new EmptyExpoResponse();
    }
}
