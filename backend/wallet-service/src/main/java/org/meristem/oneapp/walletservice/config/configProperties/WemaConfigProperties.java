package org.meristem.oneapp.walletservice.config.configProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "one-app.wallet-service.wema")
public record WemaConfigProperties(String url, String accountPrefix) {
}
