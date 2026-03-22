package org.meristem.oneapp.coreservices.notifications.services.implementations;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.coreservices.notifications.repositories.OneSignalSubscriptionsRepository;
import org.meristem.oneapp.coreservices.notifications.repositories.UserExpoTokensRepository;
import org.meristem.oneapp.kafka.dtos.PushNotificationDto;
import org.meristem.oneapp.coreservices.notifications.config.configProperties.OneSignalProperties;
import org.meristem.oneapp.coreservices.notifications.constants.KafkaTopics;
import org.meristem.oneapp.coreservices.notifications.integrations.ExpoPushNotificationClient;
import org.meristem.oneapp.coreservices.notifications.integrations.OneSignalClient;
import org.meristem.oneapp.coreservices.notifications.integrations.requests.ExpoPushNotificationRequest;
import org.meristem.oneapp.coreservices.notifications.integrations.requests.OneSignalPushNotificationRequest;
import org.meristem.oneapp.coreservices.notifications.integrations.responses.ExpoPushNotificationResponse;
import org.meristem.oneapp.coreservices.notifications.integrations.responses.OneSignalPushNotificationResponse;
import org.meristem.oneapp.coreservices.notifications.models.ExpoNotificationTicket;
import org.meristem.oneapp.coreservices.shared.repositories.CustomRepository;
import org.meristem.oneapp.coreservices.notifications.services.IKafkaSenderService;
import org.meristem.oneapp.coreservices.notifications.services.IPushNotificationService;
import org.meristem.oneapp.coreservices.notifications.utils.AppUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PushNotificationService implements IPushNotificationService {

    @Value("${notification-service.expo.push.notifications.token}")
    private String expoToken;
    private final UserExpoTokensRepository userExpoTokensRepository;
    private final OneSignalSubscriptionsRepository oneSignalSubscriptionsRepository;
    private final ExpoPushNotificationClient expoPushNotificationClient;
    private final IKafkaSenderService kafkaSenderService;
    private final CustomRepository customRepository;
    private final OneSignalProperties onesignalProperties;
    private final OneSignalClient oneSignalClient;

    @CircuitBreaker(name = "expo", fallbackMethod = "recoverExpoPushNotificationCircuit")
    @Override
    public void sendExpoPushNotification(PushNotificationDto notifications) {

        List<String> to = new ArrayList<>();

        if (notifications.toAll()) {
            to.addAll(userExpoTokensRepository.findAllExpoTokens());
        }
        if (notifications.userId() != null) {
            to.addAll(userExpoTokensRepository.findAllExpoTokensByUserId(notifications.userId()));
        } else {
            return;
        }
        if (to.isEmpty()) {
            return;
        }
        ExpoPushNotificationRequest request = ExpoPushNotificationRequest.builder()
                .to(to)
                .title(notifications.title())
                .body(notifications.body())
                .data(notifications.data() == null ? Map.of() : notifications.data())
                .build();
        List<ExpoPushNotificationResponse.ExpoPushResponse> response = expoPushNotificationClient.sendPushNotification(request).data();

        List<ExpoNotificationTicket> tickets = new ArrayList<>();
        List<String> tokenToDelete = new ArrayList<>();
        for (ExpoPushNotificationResponse.ExpoPushResponse r : response) {

            if ("ok".equalsIgnoreCase(r.status())) {
                tickets.add(ExpoNotificationTicket.builder().ticketId(r.id()).build());
            } else {
                if ("DeviceNotRegistered".equalsIgnoreCase(r.details().error())) {
                    String token = r.details().expoPushToken();
                    tokenToDelete.add(StringUtils.hasText(token) ? token : AppUtil.extractExpoTokenWithRegex(r.message()));
                }
            }
        }
        customRepository.saveAll(tickets);
        userExpoTokensRepository.deleteUserExpoTokensByExpoTokenIn(tokenToDelete);
        log.info("Push notification sent to {} users", tickets.size());
    }

    @Override
    public void recoverExpoPushNotificationCircuit(PushNotificationDto notifications, Throwable throwable) {
        kafkaSenderService.send(notifications, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_PUSH_NOTIFICATION_TOPIC));
    }

    @Override
    @CircuitBreaker(name = "one-signal", fallbackMethod = "recoverPushNotificationCircuit")
    public void sendPushNotification(PushNotificationDto notifications) {

        List<String> to = new ArrayList<>();

        if (notifications.toAll()) {
            to.addAll(oneSignalSubscriptionsRepository.findAllSubscriptionIds());
        }
        if (notifications.userId() != null) {
            to.addAll(oneSignalSubscriptionsRepository.findAllSubscriptionIdsByUserId(notifications.userId()));
        } else {
            return;
        }
        if (to.isEmpty()) {
            return;
        }
        OneSignalPushNotificationRequest request = OneSignalPushNotificationRequest.builder()
                .appId(onesignalProperties.appId())
                .contents(new OneSignalPushNotificationRequest.Contents(notifications.body()))
                .subscriptionIds(to)
                .build();
        OneSignalPushNotificationResponse response = oneSignalClient.sendPushNotification(request);

        log.info("Push notification sent to {} users, response id: {}", to.size(), response.id());
    }

    @Override
    public void recoverPushNotificationCircuit(PushNotificationDto notifications, Throwable throwable) {
        kafkaSenderService.send(notifications, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_PUSH_NOTIFICATION_TOPIC));
    }
}
