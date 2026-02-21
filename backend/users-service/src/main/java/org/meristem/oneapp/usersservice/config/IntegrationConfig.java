package org.meristem.oneapp.usersservice.config;


import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.usersservice.config.configProperties.*;
import org.meristem.oneapp.usersservice.integrations.DojahClient;
import org.meristem.oneapp.usersservice.integrations.MiddleWareClient;
import org.meristem.oneapp.usersservice.integrations.PastelClient;
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
                .builderFor(RestClientAdapter.create(restClientBuilder.clone().baseUrl(smileIdProperties.url())
                        .defaultHeader(oneAppProperties.defaultHeaderName(), smileIdProperties.clientName())
                        .build())).build().createClient(SmileIdClient.class);
    }

    @Bean
    PastelClient pastelClient(RestClient.Builder restClientBuilder, PastelProperties pastelProperties, OneAppProperties oneAppProperties) {
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClientBuilder.clone().baseUrl(pastelProperties.url())
                        .defaultHeader(oneAppProperties.defaultHeaderName(), pastelProperties.clientName())
                        .defaultHeaders(h -> {
                            h.add("apiKey", pastelProperties.apiKey());
                            h.add("apiSecret", pastelProperties.secret());
                        })
                        .build())).build().createClient(PastelClient.class);
    }

    @Bean
    MiddleWareClient middleWareClient(RestClient.Builder restClientBuilder, MiddleWareConfigProperties middleWareConfigProperties, OneAppProperties oneAppProperties) {

        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClientBuilder.clone()
                        .baseUrl(middleWareConfigProperties.baseUrl())
                        .defaultHeaders(c -> {
                            c.set(oneAppProperties.defaultHeaderName(), middleWareConfigProperties.clientName());
                            c.set("X-API-KEY", middleWareConfigProperties.apiKey());
                        }).build()))
                .build().createClient(MiddleWareClient.class);
    }

    @Bean
    DojahClient dojahClient(RestClient.Builder restClientBuilder, DojahProperties dojahProperties, OneAppProperties oneAppProperties) {
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClientBuilder.clone().baseUrl(dojahProperties.baseUrl())
                        .defaultHeaders(h -> {
                            h.set(oneAppProperties.defaultHeaderName(), dojahProperties.clientName());
                            h.set("AppId", dojahProperties.appId());
                            h.set("Authorization", dojahProperties.privateKey());
                        })
                        .build())).build().createClient(DojahClient.class);
    }
}
