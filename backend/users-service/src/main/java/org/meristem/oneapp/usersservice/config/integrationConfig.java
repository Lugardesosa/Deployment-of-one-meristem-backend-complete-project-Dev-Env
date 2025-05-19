package org.meristem.oneapp.usersservice.config;


import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.usersservice.config.configProperties.SmileIdProperties;
import org.meristem.oneapp.usersservice.integrations.SmileIdClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class integrationConfig {

    private final SmileIdProperties smileIdProperties;

    @Bean
    SmileIdClient smileIdClient(WebClient.Builder webClientBuilder) {
        return HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClientBuilder.baseUrl(smileIdProperties.url()).build()))
                .build().createClient(SmileIdClient.class);
    }
}
