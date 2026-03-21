package org.meristem.oneapp.notificationservice.config;


import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.HttpHeaders;
import org.meristem.oneapp.notificationservice.config.configProperties.CreditSwitchProperties;
import org.meristem.oneapp.notificationservice.config.configProperties.HollaTagsProperties;
import org.meristem.oneapp.notificationservice.config.configProperties.OneAppProperties;
import org.meristem.oneapp.notificationservice.integrations.CreditSwitchClient;
import org.meristem.oneapp.notificationservice.integrations.ExpoPushNotificationClient;
import org.meristem.oneapp.notificationservice.integrations.HollaTagsClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class IntegrationConfig {

    private final OneAppProperties oneAppProperties;
    private final CreditSwitchProperties creditSwitchProperties;

    @Bean
    CreditSwitchClient creditSwitchClient(RestClient.Builder restClientBuilder) {
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClientBuilder.baseUrl(creditSwitchProperties.baseUrl())
                        .defaultHeader(oneAppProperties.defaultHeaderName(), "CreditSwitchClient").build()))
                .build().createClient(CreditSwitchClient.class);
    }


    @Bean
    HollaTagsClient hollaTagsClient(RestClient.Builder restClientBuilder, HollaTagsProperties properties) {
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClientBuilder.baseUrl(properties.baseUrl())
                        .defaultHeader(oneAppProperties.defaultHeaderName(), "HollaTagsClient").build()))
                .build().createClient(HollaTagsClient.class);
    }

    @Bean
    ExpoPushNotificationClient expoPushNotificationClient(RestClient.Builder restClientBuilder, @Value("${expo-url}") String expoUrl, @Value("${expo.push.notifications.token}") String accessToken) {
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClientBuilder
                        .baseUrl(expoUrl)
                        .defaultHeaders(c -> c.add(oneAppProperties.defaultHeaderName(), "ExpoPushNotificationClient"))
                        .defaultHeaders(c -> c.add(HttpHeaders.AUTHORIZATION, "Bearer ".concat(accessToken)))
                        .build())).build()
                .createClient(ExpoPushNotificationClient.class);
    }
}
