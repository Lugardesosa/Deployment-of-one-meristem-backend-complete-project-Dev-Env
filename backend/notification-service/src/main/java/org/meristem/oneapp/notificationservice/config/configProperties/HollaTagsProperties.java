package org.meristem.oneapp.notificationservice.config.configProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "holla-tags")
public record HollaTagsProperties(String user, String pass, String from, String baseUrl, String callbackUrl) {
}
