package org.meristem.oneapp.walletservice.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.meristem.oneapp.walletservice.config.configProperties.*;
import org.meristem.oneapp.walletservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.walletservice.exception.exceptions.ResourceNotFoundException;
import org.meristem.oneapp.walletservice.exception.exceptions.UpstreamServiceException;
import org.meristem.oneapp.walletservice.integrations.MiddleWareClient;
import org.meristem.oneapp.walletservice.integrations.PaystackClient;
import org.meristem.oneapp.walletservice.integrations.SmileIdClient;
import org.meristem.oneapp.walletservice.integrations.UserServiceClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.io.IOException;
import java.net.URI;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class IntegrationConfig {

    public static final String BEARER = "Bearer ";

    @Bean
    PaystackClient paystackClient(RestClient.Builder restClientBuilder, @Value("${paystack.api.baseUrl}") String baseUrl, @Value("${paystack.api.secretKey}") String secretKey) {
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClientBuilder
                .clone().baseUrl(baseUrl)
                .defaultHeaders(c -> c.set(HttpHeaders.AUTHORIZATION, BEARER + secretKey))
                .build())).build().createClient(PaystackClient.class);
    }

    @Bean
    UserServiceClient userServiceClient(@Qualifier("restClientBuilderInternal") RestClient.Builder restClientBuilder, OneAppProperties oneAppProperties, @Value("${spring.application.name}") String applicationName,
                                        ServicesUrlProperties servicesProperties, OAuth2AuthorizedClientManager authorizedClientManager) {

        OAuth2ClientHttpRequestInterceptor interceptor = new OAuth2ClientHttpRequestInterceptor(authorizedClientManager);

        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClientBuilder.requestInterceptors(c -> c.add(interceptor))
                        .defaultRequest(r -> r.attributes(clientRegistrationId(applicationName)))
                        .clone().baseUrl(servicesProperties.usersService())
                        .defaultHeader(oneAppProperties.defaultHeaderName(), oneAppProperties.usersService().applicationName())
                        .build())).build().createClient(UserServiceClient.class);
    }

    @Bean
    MiddleWareClient middleWareClient(RestClient.Builder restClientBuilder, MiddleWareConfigProperties middleWareConfigProperties, OneAppProperties oneAppProperties) {

        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClientBuilder
                        .clone().baseUrl(middleWareConfigProperties.baseUrl())
                        .defaultHeaders(c -> {
                            c.set(oneAppProperties.defaultHeaderName(), middleWareConfigProperties.clientName());
                            c.set("X-API-KEY", middleWareConfigProperties.apiKey());
                        }).build()))
                .build().createClient(MiddleWareClient.class);
    }

    @Bean
    SmileIdClient smileIdClient(RestClient.Builder restClientBuilder, SmileIdProperties smileIdProperties, OneAppProperties oneAppProperties) {
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClientBuilder.clone()
                        .baseUrl(smileIdProperties.url())
                        .defaultStatusHandler(errorHandler())
                        .defaultHeader(oneAppProperties.defaultHeaderName(), smileIdProperties.clientName())
                        .build())).build().createClient(SmileIdClient.class);
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
