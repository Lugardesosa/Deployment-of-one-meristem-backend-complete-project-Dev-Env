package org.meristem.oneapp.coreservices.wealth.config.configProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "one-app.services-url")
public record ServicesUrlProperties(String usersService, String walletService, String wealthService, String trusteesService, String reportService) {
}
