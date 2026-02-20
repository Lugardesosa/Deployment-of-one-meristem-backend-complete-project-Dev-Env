package org.meristem.oneapp.usersservice.config.configProperties;


import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "one-app.users-service.dojah")
public record DojahProperties(String appId, String secretKey, String privateKey, String baseUrl, String clientName) {
}
