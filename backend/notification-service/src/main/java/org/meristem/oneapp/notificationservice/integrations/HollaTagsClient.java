package org.meristem.oneapp.notificationservice.integrations;

import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface HollaTagsClient {

    @PostExchange(url = "/send", contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String sendSms(@RequestBody MultiValueMap<String, Object> request);
}
