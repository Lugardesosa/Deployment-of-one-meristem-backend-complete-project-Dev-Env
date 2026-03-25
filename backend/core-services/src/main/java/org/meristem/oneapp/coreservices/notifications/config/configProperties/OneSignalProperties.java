package org.meristem.oneapp.coreservices.notifications.config.configProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "notification-service.one-signal")
public record OneSignalProperties(String baseUrl, String appId, String apiKey) {
}
