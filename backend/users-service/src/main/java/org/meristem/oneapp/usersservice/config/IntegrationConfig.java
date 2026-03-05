package org.meristem.oneapp.usersservice.config;


import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.meristem.oneapp.usersservice.config.configProperties.*;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.exception.exceptions.ResourceNotFoundException;
import org.meristem.oneapp.usersservice.exception.exceptions.UpstreamServiceException;
import org.meristem.oneapp.usersservice.integrations.DojahClient;
import org.meristem.oneapp.usersservice.integrations.MiddleWareClient;
import org.meristem.oneapp.usersservice.integrations.PastelClient;
import org.meristem.oneapp.usersservice.integrations.SmileIdClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.io.IOException;
import java.net.URI;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class IntegrationConfig {

    @Bean
    SmileIdClient smileIdClient(RestClient.Builder restClientBuilder, SmileIdProperties smileIdProperties, OneAppProperties oneAppProperties) {
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClientBuilder.clone()
                        .baseUrl(smileIdProperties.url())
                        .defaultStatusHandler(errorHandler())
                        .defaultHeader(oneAppProperties.defaultHeaderName(), smileIdProperties.clientName())
                        .build())).build().createClient(SmileIdClient.class);
    }

    @Bean
    PastelClient pastelClient(RestClient.Builder restClientBuilder, PastelProperties pastelProperties, OneAppProperties oneAppProperties) {
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClientBuilder.clone()
                        .baseUrl(pastelProperties.url())
                        .defaultStatusHandler(errorHandler())
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
                        .defaultStatusHandler(errorHandlerMiddleware())
                        .defaultHeaders(c -> {
                            c.set(oneAppProperties.defaultHeaderName(), middleWareConfigProperties.clientName());
                            c.set("X-API-KEY", middleWareConfigProperties.apiKey());
                        }).build()))
                .build().createClient(MiddleWareClient.class);
    }

    @Bean
    DojahClient dojahClient(RestClient.Builder restClientBuilder, DojahProperties dojahProperties, OneAppProperties oneAppProperties) {
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClientBuilder.clone()
                        .baseUrl(dojahProperties.baseUrl())
                        .defaultStatusHandler(errorHandler())
                        .defaultHeaders(h -> {
                            h.set(oneAppProperties.defaultHeaderName(), dojahProperties.clientName());
                            h.set("AppId", dojahProperties.appId());
                            h.set("Authorization", dojahProperties.privateKey());
                        })
                        .build())).build().createClient(DojahClient.class);
    }

    ResponseErrorHandler errorHandlerMiddleware() {
        return new ResponseErrorHandler() {

            @Override
            public boolean hasError(@NonNull ClientHttpResponse response) throws IOException {
                return response.getStatusCode().isError();
            }

            @Override
            public void handleError(@NonNull URI url, @NonNull HttpMethod method, @NonNull ClientHttpResponse response) throws IOException {
                HttpStatusCode status = response.getStatusCode();

                if (status.value() == 404) {
                } else if (status.is4xxClientError()) {
                    throw new BadRequestException("Check your request. Response message: " + response.getStatusText());
                } else if (status.is5xxServerError()) {
                    throw new UpstreamServiceException("Upstream Server error. Response message: " + response.getStatusText());
                } else {
                    throw new RuntimeException("Unexpected error. Response message: " + response.getStatusText());
                }
            }
        };
    }

    ResponseErrorHandler errorHandler() {
        return new ResponseErrorHandler() {

            @Override
            public boolean hasError(@NonNull ClientHttpResponse response) throws IOException {
                return response.getStatusCode().isError();
            }

            @Override
            public void handleError(@NonNull URI url, @NonNull HttpMethod method, @NonNull ClientHttpResponse response) throws IOException {
                HttpStatusCode status = response.getStatusCode();

                if (status.value() == 404) {
                    throw new ResourceNotFoundException("Check your request. Response message: " + response.getStatusText(), "", "");
                } else if (status.is4xxClientError()) {
                    throw new BadRequestException("Check your request. Response message: " + response.getStatusText());
                } else if (status.is5xxServerError()) {
                    throw new UpstreamServiceException("Upstream Server error. Response message: " + response.getStatusText());
                } else {
                    throw new RuntimeException("Unexpected error. Response message: " + response.getStatusText());
                }
            }
        };
    }
}
