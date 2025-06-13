package org.meristem.oneapp.notificationservice.config.configProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "credit-switch")
public record CreditSwitchProperties(String loginId, String publicKey, String baseUrl, String senderId, String privateKey, String password) {
}
