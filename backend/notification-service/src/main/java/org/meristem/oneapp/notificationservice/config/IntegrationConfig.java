package org.meristem.oneapp.notificationservice.config;


import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.HttpHeaders;
import org.meristem.oneapp.notificationservice.config.configProperties.*;
import org.meristem.oneapp.notificationservice.integrations.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import static org.springframework.security.oauth2.client.web.ClientAttributes.clientRegistrationId;

@Configuration
@RequiredArgsConstructor
public class IntegrationConfig {

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
    ExpoPushNotificationClient expoPushNotificationClient(RestClient.Builder restClientBuilder, @Value("${expo-url}") String expoUrl, @Value("${expo.push.notifications.token}") String accessToken) {
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClientBuilder.clone()
                        .baseUrl(expoUrl)
                        .defaultHeaders(c -> c.add(oneAppProperties.defaultHeaderName(), "ExpoPushNotificationClient"))
                        .defaultHeaders(c -> c.add(HttpHeaders.AUTHORIZATION, "Bearer ".concat(accessToken)))
                        .build())).build()
                .createClient(ExpoPushNotificationClient.class);
    }

    @Bean
    OneSignalClient oneSignalPushNotificationClient(RestClient.Builder restClientBuilder, OneAppProperties oneAppProperties, OneSignalProperties oneSignalProperties) {
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClientBuilder.clone()
                        .baseUrl(oneSignalProperties.baseUrl())
                        .defaultHeaders(c -> c.add(oneAppProperties.defaultHeaderName(), "OneSignalClient"))
                        .defaultHeaders(c -> c.add(HttpHeaders.AUTHORIZATION, oneSignalProperties.apiKey()))
                        .build())).build()
                .createClient(OneSignalClient.class);
    }

    @Bean
    UserServiceClient userServiceClient(@Qualifier("restClientBuilderInternal") RestClient.Builder restClientBuilder, OneAppProperties oneAppProperties, @Value("${spring.application.name}") String applicationName,
                                        ServicesUrlProperties servicesProperties, OAuth2AuthorizedClientManager authorizedClientManager) {

        OAuth2ClientHttpRequestInterceptor interceptor = new OAuth2ClientHttpRequestInterceptor(authorizedClientManager);

        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClientBuilder.clone().requestInterceptors(c -> c.add(interceptor))
                        .defaultRequest(r -> r.attributes(clientRegistrationId(applicationName)))
                        .baseUrl(servicesProperties.usersService())
                        .defaultHeader(oneAppProperties.defaultHeaderName(), oneAppProperties.usersService().applicationName())
                        .build())).build().createClient(UserServiceClient.class);
    }
}
