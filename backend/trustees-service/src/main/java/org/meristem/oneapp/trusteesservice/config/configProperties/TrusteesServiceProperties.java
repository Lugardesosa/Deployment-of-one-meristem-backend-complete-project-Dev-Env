package org.meristem.oneapp.trusteesservice.config.configProperties;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "one-app.trustees-service")
public record TrusteesServiceProperties(String contextPath, String applicationName, String companyName, String dataPrivacyPolicyUrl, String logoUrl, String defaultHeaderName, String clientName) {
}
