package org.meristem.oneapp.notificationservice.config.configProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "one-signal")
public record OneSignalProperties(String baseUrl, String appId, String apiKey) {
}
