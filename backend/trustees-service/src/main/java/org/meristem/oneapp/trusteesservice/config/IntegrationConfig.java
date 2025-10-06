package org.meristem.oneapp.trusteesservice.config;


import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.trusteesservice.config.configProperties.OneAppProperties;
import org.meristem.oneapp.trusteesservice.config.configProperties.ServicesProperties;
import org.meristem.oneapp.trusteesservice.config.configProperties.TrusteesServiceProperties;
import org.meristem.oneapp.trusteesservice.integrations.UserServiceClient;
import org.meristem.oneapp.trusteesservice.utils.AppUtil;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@Slf4j
@Configuration
public class IntegrationConfig {

    @Bean
    UserServiceClient userServiceClient(RestClient.Builder restClientBuilder, DiscoveryClient discoveryClient, OneAppProperties oneAppProperties,
                                        TrusteesServiceProperties trusteesServiceProperties, ServicesProperties servicesProperties, OAuth2AuthorizedClientManager authorizedClientManager) {

        List<ServiceInstance> instances = discoveryClient.getInstances(servicesProperties.usersService().getFirst());
        OAuth2ClientHttpRequestInterceptor interceptor = new OAuth2ClientHttpRequestInterceptor(authorizedClientManager);

        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClientBuilder.requestInterceptors(c -> c.add(interceptor))
                        .defaultRequest(r -> r.attributes(clientRegistrationId(servicesProperties.trusteesService().getFirst())))
                        .baseUrl(AppUtil.getServiceUrl(instances, servicesProperties.usersService()))
                        .defaultHeader(oneAppProperties.defaultHeaderName(), trusteesServiceProperties.clientName())
                        .build())).build().createClient(UserServiceClient.class);
    }
}
