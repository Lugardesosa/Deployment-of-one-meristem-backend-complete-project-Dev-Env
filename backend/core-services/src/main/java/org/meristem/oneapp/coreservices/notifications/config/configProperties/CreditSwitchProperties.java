package org.meristem.oneapp.coreservices.notifications.config.configProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "notification-service.credit-switch")
public record CreditSwitchProperties(String loginId, String publicKey, String baseUrl, String senderId, String privateKey, String password) {
}
