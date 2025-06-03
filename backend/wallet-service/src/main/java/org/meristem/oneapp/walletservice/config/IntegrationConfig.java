package org.meristem.oneapp.walletservice.config;


import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.walletservice.config.configProperties.OneAppProperties;
import org.meristem.oneapp.walletservice.integrations.WemaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class IntegrationConfig {

    private final OneAppProperties oneAppProperties;

    @Bean
    WemaClient wemaClient(RestClient.Builder restClientBuilder) {
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(RestClient.builder()
                        .defaultHeader(oneAppProperties.defaultHeaderName(), "WemaClient").build()))
                .build().createClient(WemaClient.class);
    }
}
