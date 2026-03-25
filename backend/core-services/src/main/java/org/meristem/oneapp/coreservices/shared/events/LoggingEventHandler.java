package org.meristem.oneapp.coreservices.shared.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.coreservices.shared.dtos.events.RequestAndResponseLogEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
@Slf4j
@RequiredArgsConstructor
public class LoggingEventHandler {

    public static final String REDACTED = "[REDACTED]";

    @Value("${what-to-sanitize}")
    private final List<String> whatToSanitize;

    private final ObjectMapper objectMapper;

    @EventListener
    @Async
    public void handleLogging(RequestAndResponseLogEvent event) {

        try {

            String requestMap = new String(event.getRequestBody(), StandardCharsets.UTF_8).trim();
            requestMap = requestMap.isBlank() ? "{}" : requestMap;
            String responseMap = new String(event.getResponseBody(), StandardCharsets.UTF_8);
            responseMap = responseMap.isBlank() ? "{}" : responseMap;

            boolean isJson = MediaType.APPLICATION_JSON_VALUE.equals(event.getHeaders().get("content-type")) || isNull(event.getHeaders().get("content-type"));
            HashMap<String, Object> bodyRequest = isJson ? objectMapper.readValue(requestMap, new TypeReference<>() {
            }) : new HashMap<>();
            HashMap<String, Object> bodyResponse = isJson ? objectMapper.readValue(responseMap, new TypeReference<>() {
            }) : new HashMap<>();

            Map<String, Object> logInfo = new HashMap<>();
            logInfo.put("status", event.getStatus());
            logInfo.put("method", event.getMethod());
            logInfo.put("uri", event.getRequestURI());
            logInfo.put("headers", sanitise(event.getHeaders()));
            logInfo.put("request", sanitise(bodyRequest));
            logInfo.put("response", sanitise(bodyResponse));
            logInfo.put("duration", event.getDuration());
            logInfo.put("params", getRequestParameters(event.getParameters()));
            log.info("{}", objectMapper.writeValueAsString(logInfo));

        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }

    private Map<String, Object> sanitise(Map<String, Object> requestHeaders) {
        Map<String, Object> response = new HashMap<>();

        for (Map.Entry<String, Object> entry : requestHeaders.entrySet()) {
            // Recursively sanitizes values except when key matches
            if (whatToSanitize.stream().anyMatch(entry.getKey()::equalsIgnoreCase)) {
                response.put(entry.getKey(), REDACTED);
            } else if (nonNull(entry.getValue()) && "LinkedHashMap".equalsIgnoreCase(entry.getValue().getClass().getSimpleName())) {
                response.put(entry.getKey(), sanitise((HashMap<String, Object>) entry.getValue()));
            } else {
                response.put(entry.getKey(), entry.getValue());
            }
        }
        return response;
    }

    private Map<String, Object> getRequestParameters(Map<String, String[]> parameterMap) {
        Map<String, Object> response = new HashMap<>();

        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            if (whatToSanitize.stream().anyMatch(entry.getKey()::equalsIgnoreCase)) {
                response.put(entry.getKey(), REDACTED);
            } else {
                response.put(entry.getKey(), Arrays.toString(entry.getValue()));
            }
        }
        return response;
    }
}
