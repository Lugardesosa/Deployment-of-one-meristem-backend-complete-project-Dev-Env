package org.meristem.oneapp.trustiesservice.config.configProperties;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "one-app.core-service")
public record CoreUsersAppProperties(String contextPath, String applicationName, String companyName, String dataPrivacyPolicyUrl, String logoUrl, String defaultHeaderName, String clientName) {
}
