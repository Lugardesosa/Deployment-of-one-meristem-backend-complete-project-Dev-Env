package org.meristem.oneapp.reportservice.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.reportservice.config.configProperties.MiddleWareConfigProperties;
import org.meristem.oneapp.reportservice.config.configProperties.OneAppProperties;
import org.meristem.oneapp.reportservice.config.configProperties.ServicesUrlProperties;
import org.meristem.oneapp.reportservice.integrations.MiddleWareClient;
import org.meristem.oneapp.reportservice.integrations.UserServiceClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Bean
    UserServiceClient userServiceClient(@Qualifier("restClientBuilderInternal") RestClient.Builder restClientBuilder, OneAppProperties oneAppProperties, @Value("${spring.application.name}") String applicationName,
                                        ServicesUrlProperties servicesProperties, OAuth2AuthorizedClientManager authorizedClientManager) {

        OAuth2ClientHttpRequestInterceptor interceptor = new OAuth2ClientHttpRequestInterceptor(authorizedClientManager);

        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClientBuilder.requestInterceptors(c -> c.add(interceptor))
                        .defaultRequest(r -> r.attributes(clientRegistrationId(applicationName)))
                        .baseUrl(servicesProperties.usersService())
                        .defaultHeader(oneAppProperties.defaultHeaderName(), oneAppProperties.usersService().applicationName())
                        .build())).build().createClient(UserServiceClient.class);
    }

    @Bean
    MiddleWareClient middleWareClient(RestClient.Builder restClientBuilder, MiddleWareConfigProperties middleWareConfigProperties, OneAppProperties oneAppProperties) {

        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClientBuilder
                        .baseUrl(middleWareConfigProperties.baseUrl())
                        .defaultHeaders(c -> {
                            c.set(oneAppProperties.defaultHeaderName(), middleWareConfigProperties.clientName());
                            c.set("X-API-KEY", middleWareConfigProperties.apiKey());
                        }).build()))
                .build().createClient(MiddleWareClient.class);
    }
}
