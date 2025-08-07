package org.meristem.oneapp.coreservice.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.coreservice.dtos.events.RequestAndResponseLogEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class LoggingEventHandler {

    public static final String REDACTED = "[REDACTED]";
    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final List<String> parametersToSanitize = List.of("password", "newPin", "oldPin", "X-MERISTEM-KEY");
    private final List<String> headersToSanitize = List.of("password", "newPin", "oldPin", "X-MERISTEM-KEY");

    private final ObjectMapper objectMapper;

    @EventListener
    @Async
    public void handleLogging(RequestAndResponseLogEvent event) throws JsonProcessingException {

        try {

            String requestMap = new String(event.getRequestBody(), StandardCharsets.UTF_8).trim();
            requestMap = requestMap.isBlank() ? "{}" : requestMap;
            String responseMap = new String(event.getResponseBody(), StandardCharsets.UTF_8);

            HashMap<String, Object> bodyRequest = objectMapper.readValue(requestMap, new TypeReference<>() {
            });
            HashMap<String, Object> bodyResponse = objectMapper.readValue(responseMap, new TypeReference<>() {
            });

            sanitize(event, bodyResponse, bodyRequest);

            log.info("{\"status\": {}, \"method\": \"{}\", \"uri\": \"{}\", \"headers\": {}, \"request\": {}, \"response\": {}, \"duration\": \"{}\", \"parameters\": {}}",
                    event.getStatus(),
                    event.getMethod(),
                    event.getRequestURI(),
                    getRequestHeaders(event.getHeaders()),
                    objectMapper.writeValueAsString(bodyRequest),
                    objectMapper.writeValueAsString(bodyResponse),
                    event.getDuration(),
                    getRequestParameters(event.getParameters())
            );

        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }

    private void sanitize(RequestAndResponseLogEvent event, HashMap<String, Object> bodyResponse, HashMap<String, Object> bodyRequest) {

    }

    private String getRequestHeaders(Map<String, String> requestHeaders) {
        StringBuilder headers = new StringBuilder();
        headers.append("{");

        for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
            if (headersToSanitize.stream().anyMatch(entry.getKey()::equalsIgnoreCase)) {
                headers.append("\"").append(entry.getKey()).append("\": ").append("\"").append(REDACTED).append("\",");
                continue;
            }
            headers.append("\"").append(entry.getKey()).append("\": ").append("\"").append(entry.getValue()).append("\",");
        }
        if (headers.charAt(headers.length() - 1) == ',') {
            headers.deleteCharAt(headers.length() - 1);
        }
        headers.append("}");
        return headers.toString();
    }

    private String getRequestParameters(Map<String, String[]> parameterMap) {
        StringBuilder parameters = new StringBuilder();
        parameters.append("{");

        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            if (parametersToSanitize.stream().anyMatch(entry.getKey()::equalsIgnoreCase)) {
                parameters.append("\"").append(entry.getKey()).append("\": ").append("\"").append(REDACTED).append("\",");
                continue;
            }
            parameters.append("\"").append(entry.getKey()).append("\": ").append("\"").append(Arrays.toString(entry.getValue())).append("\",");
        }
        if (parameters.charAt(parameters.length() - 1) == ',') {
            parameters.deleteCharAt(parameters.length() - 1);
        }
        parameters.append("}");
        return parameters.toString();
    }
}
