package org.meristem.oneapp.usersservice.config.configProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "one-app.users-service")
public record OneAppUsersProperties(String contextPath, String applicationName, String companyName, String dataPrivacyPolicyUrl, String logoUrl, String defaultHeaderName) {
}