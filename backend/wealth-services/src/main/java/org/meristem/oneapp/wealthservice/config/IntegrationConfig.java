package org.meristem.oneapp.wealthservice.config;


import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.wealthservice.config.configProperties.OneAppProperties;
import org.meristem.oneapp.wealthservice.config.configProperties.ServicesUrlProperties;
import org.meristem.oneapp.wealthservice.config.configProperties.WealthServiceProperties;
import org.meristem.oneapp.wealthservice.integrations.UserServiceClient;
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
@Configuration(proxyBeanMethods = false)
public class IntegrationConfig {

    @Bean
    UserServiceClient userServiceClient(@Qualifier("restClientBuilderInternal") RestClient.Builder restClientBuilder, OneAppProperties oneAppProperties, @Value("${spring.application.name}") String applicationName,
                                        WealthServiceProperties wealthServiceProperties, ServicesUrlProperties servicesProperties, OAuth2AuthorizedClientManager authorizedClientManager) {

        OAuth2ClientHttpRequestInterceptor interceptor = new OAuth2ClientHttpRequestInterceptor(authorizedClientManager);

        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClientBuilder.requestInterceptors(c -> c.add(interceptor))
                        .defaultRequest(r -> r.attributes(clientRegistrationId(applicationName)))
                        .baseUrl(servicesProperties.usersService())
                        .defaultHeader(oneAppProperties.defaultHeaderName(), wealthServiceProperties.clientName())
                        .build())).build().createClient(UserServiceClient.class);
    }
}
