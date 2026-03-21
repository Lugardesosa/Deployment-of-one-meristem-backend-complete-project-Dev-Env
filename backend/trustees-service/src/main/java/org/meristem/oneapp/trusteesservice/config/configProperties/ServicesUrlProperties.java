package org.meristem.oneapp.trusteesservice.config.configProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "one-app.services-url")
public record ServicesUrlProperties(String usersService, String walletService, String trusteesService, String reportService) {
}
