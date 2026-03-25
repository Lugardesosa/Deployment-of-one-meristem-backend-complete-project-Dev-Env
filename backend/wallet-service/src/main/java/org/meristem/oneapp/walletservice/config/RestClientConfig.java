package org.meristem.oneapp.walletservice.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.observation.ObservationRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.jspecify.annotations.NonNull;
import org.meristem.oneapp.walletservice.constants.AppConstants;
import org.meristem.oneapp.walletservice.dtos.config.BufferingClientHttpResponseWrapper;
import org.meristem.oneapp.walletservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.walletservice.exception.exceptions.ResourceNotFoundException;
import org.meristem.oneapp.walletservice.exception.exceptions.UpstreamServiceException;
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
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

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

    @Bean
    @Primary
    private static @NonNull HttpComponentsClientHttpRequestFactory getRequestFactory() {

        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setConnectTimeout(Timeout.of(2, TimeUnit.SECONDS)) // Recommended
                .build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultConnectionConfig(connectionConfig);
        connectionManager.setDefaultMaxPerRoute(5);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofSeconds(3))
                .setResponseTimeout(Timeout.ofSeconds(8))
                .build();

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager)
                .evictIdleConnections(TimeValue.of(Duration.ofSeconds(30))).setDefaultRequestConfig(requestConfig)
                .setRetryStrategy(new DefaultHttpRequestRetryStrategy(AppConstants.MAX_RETRY_ATTEMPTS, TimeValue.ofMilliseconds(AppConstants.HTTP_RETRY_DELAY)))
                .build();

        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    @Bean("restClientBuilderInternal")
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

                if (status.value() == 404) {
                    throw new ResourceNotFoundException("Check your request. Response message: " + response.getStatusText(), "", "");
                } else if (status.is4xxClientError()) {
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
                URI uri = request.getURI();
                String url = uri.toString();
                HttpHeaders requestHeaders = request.getHeaders();
                HttpHeaders responseHeaders = response.getHeaders();
                Map<String, Object> params = sanitizeRequestParams(uri);

                ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
                CompletableFuture.runAsync(() -> logRequestResponse(duration, statusCode, requestBody, responseBody, method, url, requestHeaders, responseHeaders, params), executor);

                return new BufferingClientHttpResponseWrapper(response, responseBodyBytes);
            }
        };
    }

    private void logRequestResponse(long duration, int status, String requestBody, String responseBody, String method, String url, HttpHeaders requestHeaders, HttpHeaders responseHeaders, Map<String, Object> params) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String firstContentType = requestHeaders.getFirst("Content-Type");
            HashMap<String, Object> bodyRequest = requestBody.isBlank() || isNull(firstContentType) || !firstContentType.equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE) ? new HashMap<>() : objectMapper.readValue(requestBody, new TypeReference<>() {
            });
            HashMap<String, Object> bodyResponse = responseBody.isBlank() || isNull(firstContentType) || !firstContentType.equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE) ? new HashMap<>() : objectMapper.readValue(responseBody, new TypeReference<>() {
            });

            MediaType requestHeadersContentType = requestHeaders.getContentType();
            MediaType responseHeadersContentType = responseHeaders.getContentType();
            if (nonNull(requestHeadersContentType) && requestHeadersContentType.toString().contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
                bodyRequest = parseUrlEncoded(requestBody);
            }
            if (nonNull(responseHeadersContentType) && responseHeadersContentType.toString().contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
                bodyResponse = parseUrlEncoded(responseBody);
            }
            boolean requestContentTypeIsText = nonNull(requestHeadersContentType) && requestHeadersContentType.toString().contains(MediaType.TEXT_HTML_VALUE);
            boolean responseContentTypeIsText = nonNull(responseHeadersContentType) && responseHeadersContentType.toString().contains(MediaType.TEXT_HTML_VALUE);

            sanitizeBody(bodyRequest);
            sanitizeBody(bodyResponse);

            HashMap<String, String> requestHeaders1 = new HashMap<>(requestHeaders.toSingleValueMap());
            HashMap<String, String> responseHeaders1 = new HashMap<>(responseHeaders.toSingleValueMap());
            sanitizeHeaders(requestHeaders1, responseHeaders1);
            Map<String, Object> logInfo = new HashMap<>();
            logInfo.put("status", status);
            logInfo.put("method", method);
            logInfo.put("uri", url);
            logInfo.put("headers", requestHeaders1);
            logInfo.put("request", requestContentTypeIsText ? requestBody : bodyRequest);
            logInfo.put("response", responseContentTypeIsText ? responseBody : bodyResponse);
            logInfo.put("duration", duration);
            logInfo.put("params", params);
            log.info("{}", objectMapper.writeValueAsString(logInfo));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private HashMap<String, Object> sanitizeBody(HashMap<String, Object> body) {

        HashMap<String, Object> response = new HashMap<>();
        body.forEach((k, v) -> {
            if (bodyToSanitize.stream().anyMatch(k::equalsIgnoreCase)) {
                response.put(k, REDACTED);
            } else if (nonNull(v) && "LinkedHashMap".equalsIgnoreCase(v.getClass().getSimpleName())) {
                response.put(k, sanitizeBody((HashMap<String, Object>) v));
            } else {
                response.put(k, v);
            }
        });
        return response;
    }

    private void sanitizeHeaders(Map<String, String> requestHeaders, Map<String, String> responseHeaders) {

        bodyToSanitize.forEach(k -> {
            if (requestHeaders.containsKey(k)) {
                requestHeaders.put(k, REDACTED);
            }

            if (responseHeaders.containsKey(k)) {
                responseHeaders.put(k, REDACTED);
            }
        });
    }

    public static HashMap<String, Object> parseUrlEncoded(String input) {
        HashMap<String, Object> result = new HashMap<>();

        for (String pair : input.split("&")) {
            String[] parts = pair.split("=", 2);

            String key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
            String value = parts.length > 1
                    ? URLDecoder.decode(parts[1], StandardCharsets.UTF_8)
                    : "";

            result.computeIfAbsent(key, _ -> value);
        }

        return result;
    }

    private Map<String, Object> sanitizeRequestParams(URI uri) {
        MultiValueMap<String, String> queryParams = UriComponentsBuilder.fromUri(uri).build().getQueryParams();
        Map<String, Object> params = new HashMap<>();
        queryParams.forEach((k, v) -> {
            if (bodyToSanitize.contains(k)) {
                params.put(k, REDACTED);
            } else {
                params.put(k, v);
            }
        });
        return params;
    }
}
