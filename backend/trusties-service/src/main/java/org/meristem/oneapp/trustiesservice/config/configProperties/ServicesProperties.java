package org.meristem.oneapp.trustiesservice.config.configProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.util.Pair;

@ConfigurationProperties(prefix = "one-app.services")
public record ServicesProperties(Pair<String, String> usersService, Pair<String, String> walletService, Pair<String, String> trustiesService, Pair<String, String> reportService) {
}
