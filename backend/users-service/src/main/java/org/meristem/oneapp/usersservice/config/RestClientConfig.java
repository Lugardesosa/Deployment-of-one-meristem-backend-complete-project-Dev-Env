package org.meristem.oneapp.usersservice.config;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.observation.ObservationRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.TimeValue;
import org.jspecify.annotations.NonNull;
import org.meristem.oneapp.usersservice.dtos.configs.BufferingClientHttpResponseWrapper;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.exception.exceptions.UpstreamServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Configuration
public class RestClientConfig {

    public static final String REDACTED = "[REDACTED]";
    @Value("${what-to-sanitize}")
    private List<String> bodyToSanitize;

    @Bean
    @Primary
    public RestClient.Builder restClientBuilder(ObservationRegistry observationRegistry) {
        HttpComponentsClientHttpRequestFactory requestFactory = getRequestFactory();
        return RestClient.builder().requestFactory(requestFactory).observationRegistry(observationRegistry)
                .defaultStatusHandler(errorHandler()).requestInterceptor(requestInterceptor());
    }

    private static @NonNull HttpComponentsClientHttpRequestFactory getRequestFactory() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(5);

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager)
                .evictIdleConnections(TimeValue.of(Duration.ofSeconds(30)))
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        requestFactory.setConnectTimeout(Duration.ofSeconds(3));
        requestFactory.setReadTimeout(Duration.ofSeconds(12));
        return requestFactory;
    }

    @Bean
    @LoadBalanced
    public RestClient.Builder restClientBuilderInternal(ObservationRegistry observationRegistry) {
        HttpComponentsClientHttpRequestFactory requestFactory = getRequestFactory();
        return RestClient.builder().requestFactory(requestFactory).observationRegistry(observationRegistry)
                .defaultStatusHandler(errorHandler()).requestInterceptor(requestInterceptor());
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

                if (status.is4xxClientError()) {
                    throw new BadRequestException("Check your request body. Response message: " + response.getStatusText());
                } else if (status.is5xxServerError()) {
                    throw new UpstreamServiceException("Upstream Server error. Response message: " + response.getStatusText());
                } else {
                    throw new RuntimeException("Unexpected error. Response message: " + response.getStatusText());
                }
            }
        };
    }

    ClientHttpRequestInterceptor requestInterceptor() {
        return new ClientHttpRequestInterceptor() {

            @NonNull
            @Override
            public ClientHttpResponse intercept(@NonNull HttpRequest request, byte @NonNull [] body, @NonNull ClientHttpRequestExecution execution) throws IOException {

                long startTime = System.nanoTime() / 1_000_000L;
                ClientHttpResponse response = execution.execute(request, body);
                long endTime = System.nanoTime() / 1_000_000L;

                byte[] responseBodyBytes = StreamUtils.copyToByteArray(response.getBody());

                long duration = endTime - startTime;
                int statusCode = response.getStatusCode().value();
                String requestBody = new String(body);
                String responseBody = new String(responseBodyBytes, StandardCharsets.UTF_8);
                String method = request.getMethod().name();
                String url = request.getURI().toString();
                HttpHeaders requestHeaders = request.getHeaders();
                HttpHeaders responseHeaders = response.getHeaders();

                ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
                CompletableFuture.runAsync(() -> logRequestResponse(duration, statusCode, requestBody, responseBody, method, url, requestHeaders, responseHeaders), executor);

                return new BufferingClientHttpResponseWrapper(response, responseBodyBytes);
            }
        };
    }

    private void logRequestResponse(long duration, int status, String requestBody, String responseBody, String method, String url, HttpHeaders requestHeaders, HttpHeaders responseHeaders) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            HashMap<String, Object> bodyRequest = requestBody.isBlank() ? new HashMap<>() : objectMapper.readValue(requestBody, new TypeReference<>() {});
            HashMap<String, Object> bodyResponse = responseBody.isBlank() || status == HttpStatus.NOT_FOUND.value() ? new HashMap<>() : objectMapper.readValue(responseBody, new TypeReference<>() {});
            sanitizeBody(bodyRequest, bodyResponse);

            HashMap<String, List<String>> requestHeaders1 = new HashMap<>(requestHeaders);
            HashMap<String, List<String>> responseHeaders1 = new HashMap<>(responseHeaders);
            sanitizeHeaders(requestHeaders1, responseHeaders1);
            log.info("{\"status\": {}, \"method\": \"{}\", \"uri\": \"{}\", \"requestHeaders\": {}, \"request\": {}, \"response\": {}, \"duration\": \"{}\", \"responseHeaders\": {}}",
                    status,
                    method,
                    url,
                    objectMapper.writeValueAsString(requestHeaders1),
                    objectMapper.writeValueAsString(bodyRequest),
                    objectMapper.writeValueAsString(bodyResponse),
                    duration,
                    objectMapper.writeValueAsString(responseHeaders1)
            );
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void sanitizeBody(HashMap<String, Object> bodyRequest, HashMap<String, Object> bodyResponse) {
        bodyToSanitize.forEach(k -> {
            if (bodyRequest.containsKey(k)) {
                bodyRequest.put(k, REDACTED);
            }
            if (bodyResponse.containsKey(k)) {
                bodyResponse.put(k, REDACTED);
            }
        });
    }

    private void sanitizeHeaders(Map<String, List<String>> requestHeaders, Map<String, List<String>> responseHeaders) {

        bodyToSanitize.forEach(k -> {
            requestHeaders.forEach((key, value) -> {
                if (key.equals(k)) {
                    requestHeaders.put(k, Collections.singletonList(REDACTED));
                }
            });

            responseHeaders.forEach((key, value) -> {
                if (key.equals(k)) {
                    responseHeaders.put(k, Collections.singletonList(REDACTED));
                }
            });
        });
    }
}
