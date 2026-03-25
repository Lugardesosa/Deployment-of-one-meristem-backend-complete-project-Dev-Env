package org.meristem.oneapp.coreservices.wealth.integrations;


import org.springframework.http.MediaType;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(contentType = MediaType.APPLICATION_JSON_VALUE)
public interface UserServiceClient {
}
