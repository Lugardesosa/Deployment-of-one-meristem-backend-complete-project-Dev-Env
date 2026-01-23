package org.meristem.oneapp.walletservice.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.walletservice.config.configProperties.*;
import org.meristem.oneapp.walletservice.integrations.PaystackClient;
import org.meristem.oneapp.walletservice.integrations.ProvidusClient;
import org.meristem.oneapp.walletservice.integrations.UserServiceClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class IntegrationConfig {

    public static final String BEARER = "Bearer ";
    private final OneAppProperties oneAppProperties;
    private final ProvidusConfigProperties providusConfigProperties;

    @Bean
    ProvidusClient providusClient(RestClient.Builder restClientBuilder) {

        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClientBuilder
                        .baseUrl(providusConfigProperties.baseUrl())
                        .defaultHeaders(c -> {
                            c.set(HttpHeaders.AUTHORIZATION, BEARER + providusConfigProperties.secretKey());
                            c.set(oneAppProperties.defaultHeaderName(), providusConfigProperties.clientName());
                        }).build()))
                .build().createClient(ProvidusClient.class);
    }

    @Bean
    PaystackClient paystackClient(RestClient.Builder restClientBuilder, @Value("${paystack.api.baseUrl}") String baseUrl, @Value("${paystack.api.secretKey}") String secretKey) {
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClientBuilder
                        .baseUrl(baseUrl)
                        .defaultHeaders(c -> c.set(HttpHeaders.AUTHORIZATION, BEARER + secretKey))
                .build())).build().createClient(PaystackClient.class);
    }

    @Bean
    UserServiceClient userServiceClient(@Qualifier("restClientBuilderInternal") RestClient.Builder restClientBuilder, OneAppProperties oneAppProperties, @Value("${spring.application.name}") String applicationName,
                                        WalletServiceProperties walletServiceProperties, ServicesUrlProperties servicesProperties, OAuth2AuthorizedClientManager authorizedClientManager) {

        OAuth2ClientHttpRequestInterceptor interceptor = new OAuth2ClientHttpRequestInterceptor(authorizedClientManager);

        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClientBuilder.requestInterceptors(c -> c.add(interceptor))
                        .defaultRequest(r -> r.attributes(clientRegistrationId(applicationName)))
                        .baseUrl(servicesProperties.usersService())
                        .defaultHeader(oneAppProperties.defaultHeaderName(), walletServiceProperties.clientName())
                        .build())).build().createClient(UserServiceClient.class);
    }
}
