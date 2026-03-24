package org.meristem.oneapp.coreservices.wealth.config.configProperties;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wealth-service.one-app.trustees-service")
public record WealthServiceProperties(String contextPath, String applicationName) {
}
