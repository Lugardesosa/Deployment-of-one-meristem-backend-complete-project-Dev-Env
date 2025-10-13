package org.meristem.oneapp.notificationservice.integrations;


import org.meristem.oneapp.notificationservice.integrations.requests.SmsNotificationRequest;
import org.meristem.oneapp.notificationservice.integrations.responses.SmsNotificationResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(contentType = MediaType.APPLICATION_JSON_VALUE)
public interface CreditSwitchClient {

    @PostExchange(url = "sendsms")
    SmsNotificationResponse sendSms(@RequestBody SmsNotificationRequest request);
}
