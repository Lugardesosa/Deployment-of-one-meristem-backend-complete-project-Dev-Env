package org.meristem.oneapp.notificationservice.integrations;

import org.meristem.oneapp.notificationservice.integrations.requests.ExpoPushNotificationRequest;
import org.meristem.oneapp.notificationservice.integrations.requests.ExpoPushRecieptRequest;
import org.meristem.oneapp.notificationservice.integrations.responses.ExpoPushNotificationResponse;
import org.meristem.oneapp.notificationservice.integrations.responses.ExpoPushReceiptResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@HttpExchange()
public interface ExpoPushNotificationClient {

    @PostExchange(url = "/push/send")
    ExpoPushNotificationResponse sendPushNotification(@RequestBody ExpoPushNotificationRequest request);

    @PostExchange(url = "/push/getReceipts")
    ExpoPushReceiptResponse sendPushNotification(@RequestBody ExpoPushRecieptRequest request);
}
