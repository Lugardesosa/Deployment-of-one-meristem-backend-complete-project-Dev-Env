package org.meristem.oneapp.walletservice.config.configProperties;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "one-app.wallet-service.providus")
public record ProvidusConfigProperties(String baseUrl, String publicKey, String secretKey, String clientName) {
}
