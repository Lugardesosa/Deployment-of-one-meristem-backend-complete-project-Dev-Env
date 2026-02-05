package org.meristem.oneapp.usersservice.config.configProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "one-app.users-service.pastel")
public record PastelProperties(String secret, String apiKey, String url, String clientName, Integer threshold, Integer limit, String callbackUrl) {
}
