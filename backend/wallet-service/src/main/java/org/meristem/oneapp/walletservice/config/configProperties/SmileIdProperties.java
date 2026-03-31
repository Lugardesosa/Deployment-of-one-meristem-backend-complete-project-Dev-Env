package org.meristem.oneapp.walletservice.config.configProperties;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "one-app.wallet-service.smile-id")
public record SmileIdProperties(List<String> serverIps, String apiKey, String partnerId, String url, String callbackUrl, Boolean isSingleUse, Long expiresAt,
                                String clientName) {
}
