package org.meristem.oneapp.wealthservice.config.configProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "one-app")
public record OneAppProperties(String serverUrl, String email, String serverVersion, String serverAppName, String defaultHeaderName) {
}