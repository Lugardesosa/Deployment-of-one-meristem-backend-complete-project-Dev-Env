package org.meristem.oneapp.usersservice.config;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.observation.ObservationRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.TimeValue;
import org.meristem.oneapp.usersservice.dtos.configs.BufferingClientHttpResponseWrapper;
import org.meristem.oneapp.usersservice.exceptionHandler.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.exceptionHandler.exceptions.UpstreamServiceException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
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
    private final List<String> bodyToSanitize = List.of("password", "pin", "secret", "token", "authorization", "bvn", "nin", "BVN", "NIN");

    @Bean
    public RestClient.Builder restClientBuilder(ObservationRegistry observationRegistry) {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(20);

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager)
                .evictIdleConnections(TimeValue.of(Duration.ofSeconds(30)))
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        requestFactory.setConnectTimeout(Duration.ofSeconds(3));
        requestFactory.setReadTimeout(Duration.ofSeconds(30));
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
                    throw new BadRequestException("Check your request body");
                } else if (status.is5xxServerError()) {
                    throw new UpstreamServiceException("Upstream Server error");
                } else {
                    throw new RuntimeException("Unexpected error");
                }
            }
        };
    }

    ClientHttpRequestInterceptor requestInterceptor() {
        return new ClientHttpRequestInterceptor() {

            @NonNull
            @Override
            public ClientHttpResponse intercept(@NonNull HttpRequest request, @NonNull byte[] body, @NonNull ClientHttpRequestExecution execution) throws IOException {

                long startTime = System.currentTimeMillis();
                ClientHttpResponse response = execution.execute(request, body);
                long endTime = System.currentTimeMillis();

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

            HashMap<String, Object> bodyRequest = objectMapper.readValue(requestBody, new TypeReference<>() {});
            HashMap<String, Object> bodyResponse = objectMapper.readValue(responseBody, new TypeReference<>() {});
            sanitizeBody(bodyRequest, bodyResponse);
            log.info("{\"status\": {}, \"method\": \"{}\", \"uri\": \"{}\", \"headers\": {}, \"request\": {}, \"response\": {}, \"duration\": \"{}\", \"parameters\": {}}",
                    status,
                    method,
                    url,
                    requestHeaders,
                    objectMapper.writeValueAsString(bodyRequest),
                    objectMapper.writeValueAsString(bodyResponse),
                    duration,
                    responseHeaders
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

    private String sanitizeHeaders(Map<String, String> requestHeaders) {
        return "";
    }
}
