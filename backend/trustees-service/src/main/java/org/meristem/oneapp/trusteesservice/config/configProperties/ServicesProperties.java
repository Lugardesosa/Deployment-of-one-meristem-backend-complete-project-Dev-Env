package org.meristem.oneapp.trusteesservice.config.configProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.util.Pair;

@ConfigurationProperties(prefix = "one-app.services")
public record ServicesProperties(Pair<String, String> usersService, Pair<String, String> walletService, Pair<String, String> trusteesService, Pair<String, String> reportService) {
}
