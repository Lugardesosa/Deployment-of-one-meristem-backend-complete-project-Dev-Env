package org.meristem.oneapp.coreservice.config.configProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "one-app.service-names")
public record ServiceNamesProperties(String usersService, String walletService, String coreService, String reportService) {
}
