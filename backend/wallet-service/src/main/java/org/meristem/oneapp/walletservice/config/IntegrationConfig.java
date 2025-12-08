package org.meristem.oneapp.walletservice.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.walletservice.config.configProperties.OneAppProperties;
import org.meristem.oneapp.walletservice.config.configProperties.ProvidusConfigProperties;
import org.meristem.oneapp.walletservice.integrations.PaystackClient;
import org.meristem.oneapp.walletservice.integrations.ProvidusClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

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
}
