package org.meristem.oneapp.reportservice.config.configProperties;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "one-app.wallet-service.middle-ware")
public record MiddleWareConfigProperties(String baseUrl, String clientName, String apiKey) {
}
