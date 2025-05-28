package org.meristem.oneapp.usersservice.config.configProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "one-app")
public record OneAppProperties(String serverUrl, String email, String serverVersion, String serverAppName, String defaultHeaderName) {
}