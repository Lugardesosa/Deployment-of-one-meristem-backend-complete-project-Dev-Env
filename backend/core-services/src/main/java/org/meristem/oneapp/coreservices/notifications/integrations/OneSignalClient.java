package org.meristem.oneapp.coreservices.notifications.integrations;

import org.meristem.oneapp.coreservices.notifications.integrations.requests.OneSignalPushNotificationRequest;
import org.meristem.oneapp.coreservices.notifications.integrations.responses.OneSignalPushNotificationResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;


@HttpExchange()
public interface OneSignalClient {

    @PostExchange(url = "/notifications")
    OneSignalPushNotificationResponse sendPushNotification(@RequestParam(name = "c") String push,  @RequestBody OneSignalPushNotificationRequest request);
}
