package org.meristem.oneapp.notificationservice.events;

import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.notificationservice.integrations.ExpoPushNotificationClient;
import org.meristem.oneapp.notificationservice.integrations.requests.ExpoPushRecieptRequest;
import org.meristem.oneapp.notificationservice.integrations.responses.ExpoPushReceiptResponse;
import org.meristem.oneapp.notificationservice.repositories.ExpoNotificationTicketRepository;
import org.meristem.oneapp.notificationservice.repositories.UserExpoTokensRepository;
import org.meristem.oneapp.notificationservice.utils.AppUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

    @Scheduled(cron = "${expo.query.receipts.cron}")
    public void queryExpoPushReceipts() {

        List<String> tickets = expoNotificationTicketRepository.findTicketIdByCreatedDateBefore(LocalDateTime.now().minusMinutes(20));
        ExpoPushReceiptResponse responses = expoPushNotificationClient.sendPushNotification(ExpoPushRecieptRequest.builder().ids(tickets).build());

        List<String> tokenToDelete = new ArrayList<>();
        for (Map.Entry<String, ExpoPushReceiptResponse.ExpoPushResponse> r : responses.data().entrySet()) {

            if ("error".equalsIgnoreCase(r.getValue().status()) && "DeviceNotRegistered".equalsIgnoreCase(r.getValue().details().error())) {
                tokenToDelete.add(AppUtil.extractExpoTokenWithRegex(r.getValue().message()));
            }
        }
        userExpoTokensRepository.deleteUserExpoTokensByExpoTokenIn(tokenToDelete);
    }
}
