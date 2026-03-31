package org.meristem.oneapp.walletservice.integrations;

import org.meristem.oneapp.walletservice.integrations.requests.SmileIdEnhancedKycRequest;
import org.meristem.oneapp.walletservice.integrations.responses.SmileIdWebhookNotification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(contentType = MediaType.APPLICATION_JSON_VALUE)
public interface SmileIdClient {

    @PostExchange(url = "/v1/id_verification")
    SmileIdWebhookNotification enhancedBvnQuery(@RequestBody SmileIdEnhancedKycRequest request);

    @PostExchange(url = "/v2/verify")
    SmileIdWebhookNotification basicIdQuery(@RequestBody SmileIdEnhancedKycRequest request);
}
