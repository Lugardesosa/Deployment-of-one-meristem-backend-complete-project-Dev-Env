package org.meristem.oneapp.coreservices.notifications.config.configProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "notification-service.holla-tags")
public record HollaTagsProperties(String user, String pass, String from, String baseUrl, String callbackUrl) {
}
