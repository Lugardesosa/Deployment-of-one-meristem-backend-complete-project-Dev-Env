package org.meristem.oneapp.coreservice.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.coreservice.config.configProperties.CoreUsersAppProperties;
import org.meristem.oneapp.coreservice.config.configProperties.OneAppProperties;
import org.meristem.oneapp.coreservice.config.configProperties.ServiceNamesProperties;
import org.meristem.oneapp.coreservice.integrations.UserServiceClient;
import org.meristem.oneapp.coreservice.utils.AppUtil;
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
                                        CoreUsersAppProperties coreUsersAppProperties, ServiceNamesProperties serviceNamesProperties, OAuth2AuthorizedClientManager authorizedClientManager) {

        List<ServiceInstance> instances = discoveryClient.getInstances(serviceNamesProperties.usersService());
        OAuth2ClientHttpRequestInterceptor interceptor = new OAuth2ClientHttpRequestInterceptor(authorizedClientManager);

        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClientBuilder.requestInterceptors(c -> c.add(interceptor))
                        .defaultRequest(r -> r.attributes(clientRegistrationId(serviceNamesProperties.coreService())))
                        .baseUrl(AppUtil.getServiceUrl(instances, serviceNamesProperties.usersService()))
                        .defaultHeader(oneAppProperties.defaultHeaderName(), coreUsersAppProperties.clientName())
                        .build())).build().createClient(UserServiceClient.class);
    }
}
