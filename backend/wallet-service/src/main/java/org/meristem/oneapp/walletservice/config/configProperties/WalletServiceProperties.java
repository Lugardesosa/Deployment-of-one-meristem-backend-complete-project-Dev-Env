package org.meristem.oneapp.walletservice.config.configProperties;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "one-app.wallet-service")
public record WalletServiceProperties(String contextPath, String applicationName) {
}
