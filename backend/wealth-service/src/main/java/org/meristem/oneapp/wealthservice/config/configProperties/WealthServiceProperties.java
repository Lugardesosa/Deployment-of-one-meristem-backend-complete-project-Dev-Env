package org.meristem.oneapp.wealthservice.config.configProperties;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "one-app.trustees-service")
public record WealthServiceProperties(String contextPath, String applicationName) {
}
