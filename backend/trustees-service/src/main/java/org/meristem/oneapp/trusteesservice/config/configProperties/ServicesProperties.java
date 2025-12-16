package org.meristem.oneapp.trusteesservice.config.configProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.util.Pair;

@ConfigurationProperties(prefix = "one-app.services-url")
public record ServicesProperties(String usersService, String walletService, String trusteesService, String reportService) {
}
