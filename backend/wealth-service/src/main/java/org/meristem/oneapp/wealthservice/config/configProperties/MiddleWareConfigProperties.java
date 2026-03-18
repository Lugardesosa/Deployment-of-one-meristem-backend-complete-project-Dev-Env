package org.meristem.oneapp.wealthservice.config.configProperties;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "one-app.middle-ware")
public record MiddleWareConfigProperties(String baseUrl, String clientName, String apiKey) {
}
