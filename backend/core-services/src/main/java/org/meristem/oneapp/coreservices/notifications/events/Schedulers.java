package org.meristem.oneapp.coreservices.notifications.events;

import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.coreservices.notifications.integrations.ExpoPushNotificationClient;
import org.meristem.oneapp.coreservices.notifications.integrations.requests.ExpoPushRecieptRequest;
import org.meristem.oneapp.coreservices.notifications.integrations.responses.ExpoPushReceiptResponse;
import org.meristem.oneapp.coreservices.notifications.models.ExpoNotificationTicket;
import org.meristem.oneapp.coreservices.notifications.repositories.ExpoNotificationTicketRepository;
import org.meristem.oneapp.coreservices.notifications.repositories.UserExpoTokensRepository;
import org.meristem.oneapp.coreservices.notifications.utils.AppUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Schedulers {

    private final ExpoNotificationTicketRepository expoNotificationTicketRepository;
    private final ExpoPushNotificationClient expoPushNotificationClient;
    private final UserExpoTokensRepository userExpoTokensRepository;

    @Scheduled(cron = "${notification-service.expo.query.receipts.cron}")
    public void queryExpoPushReceipts() {

        List<String> tickets = expoNotificationTicketRepository.findTicketIdByCreatedDateBefore(LocalDateTime.now().minusMinutes(20)).stream().map(ExpoNotificationTicket::getTicketId).toList();
        ExpoPushReceiptResponse responses = expoPushNotificationClient.sendPushNotification(ExpoPushRecieptRequest.builder().ids(tickets).build());

        List<String> tokenToDelete = new ArrayList<>();
        for (Map.Entry<String, ExpoPushReceiptResponse.ExpoPushResponse> r : responses.data().entrySet()) {

            if ("error".equalsIgnoreCase(r.getValue().status()) && "DeviceNotRegistered".equalsIgnoreCase(r.getValue().details().error())) {
                String token = r.getValue().details().expoPushToken();
                tokenToDelete.add(StringUtils.hasText(token) ? token : AppUtil.extractExpoTokenWithRegex(r.getValue().message()));
            }
        }
        userExpoTokensRepository.deleteUserExpoTokensByExpoTokenIn(tokenToDelete);
    }
}
