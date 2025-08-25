package org.meristem.oneapp.usersservice.integrations;

import org.meristem.oneapp.usersservice.integrations.requests.SmileIdSmileLinkRequest;
import org.meristem.oneapp.usersservice.integrations.responses.SmileIdSmileLinkResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(contentType = MediaType.APPLICATION_JSON_VALUE)
public interface SmileIdClient {

    @PostExchange(url = "smile_links")
    SmileIdSmileLinkResponse createSmileLink(@RequestBody SmileIdSmileLinkRequest request);
}
