package org.meristem.oneapp.coreservices.notifications.config;


import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.HttpHeaders;
import org.meristem.oneapp.coreservices.notifications.config.configProperties.CreditSwitchProperties;
import org.meristem.oneapp.coreservices.notifications.config.configProperties.HollaTagsProperties;
import org.meristem.oneapp.coreservices.notifications.config.configProperties.OneSignalProperties;
import org.meristem.oneapp.coreservices.notifications.integrations.CreditSwitchClient;
import org.meristem.oneapp.coreservices.notifications.integrations.ExpoPushNotificationClient;
import org.meristem.oneapp.coreservices.notifications.integrations.HollaTagsClient;
import org.meristem.oneapp.coreservices.notifications.integrations.OneSignalClient;
import org.meristem.oneapp.coreservices.shared.config.OneAppProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class NotificationIntegrationConfig {

    private final OneAppProperties oneAppProperties;
    private final CreditSwitchProperties creditSwitchProperties;

    @Bean
    CreditSwitchClient creditSwitchClient(RestClient.Builder restClientBuilder) {
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClientBuilder.clone().baseUrl(creditSwitchProperties.baseUrl())
                        .defaultHeader(oneAppProperties.defaultHeaderName(), "CreditSwitchClient").build()))
                .build().createClient(CreditSwitchClient.class);
    }


    @Bean
    HollaTagsClient hollaTagsClient(RestClient.Builder restClientBuilder, HollaTagsProperties properties) {
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClientBuilder.clone().baseUrl(properties.baseUrl())
                        .defaultHeader(oneAppProperties.defaultHeaderName(), "HollaTagsClient").build()))
                .build().createClient(HollaTagsClient.class);
    }

    @Bean
    ExpoPushNotificationClient expoPushNotificationClient(RestClient.Builder restClientBuilder, @Value("${expo-url}") String expoUrl, @Value("${notification-service.expo.push.notifications.token}") String accessToken) {
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClientBuilder.clone()
                        .baseUrl(expoUrl)
                        .defaultHeaders(c -> c.add(oneAppProperties.defaultHeaderName(), "ExpoPushNotificationClient"))
                        .defaultHeaders(c -> c.add(HttpHeaders.AUTHORIZATION, "Bearer ".concat(accessToken)))
                        .build())).build()
                .createClient(ExpoPushNotificationClient.class);
    }

    @Bean
    OneSignalClient oneSignalPushNotificationClient(RestClient.Builder restClientBuilder, OneSignalProperties oneSignalProperties) {
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClientBuilder.clone()
                        .baseUrl(oneSignalProperties.baseUrl())
                        .defaultHeaders(c -> c.add(oneAppProperties.defaultHeaderName(), "OneSignalClient"))
                        .defaultHeaders(c -> c.add(HttpHeaders.AUTHORIZATION, oneSignalProperties.apiKey()))
                        .build())).build()
                .createClient(OneSignalClient.class);
    }
}
