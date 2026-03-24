package org.meristem.oneapp.coreservices.wealth.config.configProperties;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wealth-service.one-app.middle-ware")
public record MiddleWareConfigProperties(String baseUrl, String clientName, String apiKey) {
}
