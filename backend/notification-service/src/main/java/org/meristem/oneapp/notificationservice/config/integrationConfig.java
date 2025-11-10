package org.meristem.oneapp.notificationservice.config;


import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.notificationservice.config.configProperties.CreditSwitchProperties;
import org.meristem.oneapp.notificationservice.config.configProperties.OneAppProperties;
import org.meristem.oneapp.notificationservice.integrations.CreditSwitchClient;
import org.meristem.oneapp.notificationservice.integrations.ExpoPushNotificationClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class integrationConfig {

    private final OneAppProperties oneAppProperties;
    private final CreditSwitchProperties creditSwitchProperties;

    @Bean
    CreditSwitchClient smileIdClient(RestClient.Builder restClientBuilder) {
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClientBuilder.baseUrl(creditSwitchProperties.baseUrl())
                        .defaultHeader(oneAppProperties.defaultHeaderName(), "CreditSwitchClient").build()))
                .build().createClient(CreditSwitchClient.class);
    }


    @Bean
    ExpoPushNotificationClient expoPushNotificationClient(RestClient.Builder restClientBuilder, @Value("${expo-url}") String expoUrl) {
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClientBuilder
                        .baseUrl(expoUrl)
                        .build())).build()
                .createClient(ExpoPushNotificationClient.class);
    }
}
