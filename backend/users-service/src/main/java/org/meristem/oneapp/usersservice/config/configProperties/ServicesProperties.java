package org.meristem.oneapp.usersservice.config.configProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.util.Pair;

@ConfigurationProperties(prefix = "one-app.services")
public record ServicesProperties(Pair<String, String> usersService, Pair<String, String> walletService, Pair<String, String> coreService, Pair<String, String> reportService) {
}
