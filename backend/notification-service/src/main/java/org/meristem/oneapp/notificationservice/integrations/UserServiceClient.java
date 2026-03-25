package org.meristem.oneapp.notificationservice.integrations;


import org.meristem.oneapp.notificationservice.integrations.responses.AppBaseResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(contentType = MediaType.APPLICATION_JSON_VALUE)
public interface UserServiceClient {

    @GetExchange(url = "/base/user-id/{customerId}")
    AppBaseResponse<Long> getUserId(@PathVariable String customerId);
}
