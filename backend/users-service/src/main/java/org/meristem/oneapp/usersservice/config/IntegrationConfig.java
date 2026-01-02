package org.meristem.oneapp.usersservice.config;


import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.usersservice.config.configProperties.OneAppProperties;
import org.meristem.oneapp.usersservice.config.configProperties.SmileIdProperties;
import org.meristem.oneapp.usersservice.integrations.SmileIdClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class IntegrationConfig {

    @Bean
    SmileIdClient smileIdClient(RestClient.Builder restClientBuilder, SmileIdProperties smileIdProperties, OneAppProperties oneAppProperties) {
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClientBuilder.baseUrl(smileIdProperties.url())
                        .defaultHeader(oneAppProperties.defaultHeaderName(), smileIdProperties.clientName())
                        .build())).build().createClient(SmileIdClient.class);
    }
}
