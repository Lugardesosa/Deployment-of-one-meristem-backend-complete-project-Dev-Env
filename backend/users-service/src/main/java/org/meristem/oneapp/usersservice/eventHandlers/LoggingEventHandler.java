package org.meristem.oneapp.usersservice.eventHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.dtos.events.RequestAndResponseLogEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class LoggingEventHandler {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final List<String> parametersToSkip = List.of("password", "pin");

    private final ObjectMapper objectMapper;

    @EventListener
    @Async
    public void handleLogging(RequestAndResponseLogEvent event) throws JsonProcessingException {

        String requestMap = new String(event.getRequestBody(), StandardCharsets.UTF_8).trim();
        requestMap = requestMap.isBlank() ? "{}" : requestMap;
        String responseMap = new String(event.getResponseBody(), StandardCharsets.UTF_8);

        HashMap<String, Object> bodyRequest = objectMapper.readValue(requestMap, new TypeReference<>() {});
        HashMap<String, Object> bodyResponse = objectMapper.readValue(responseMap, new TypeReference<>() {});

        if (event.getRequestURI().startsWith(contextPath.concat("/oauth2/token"))) {
            bodyResponse.replace("access_token", "[REDACTED]");
            bodyResponse.replace("refresh_token", "[REDACTED]");
        }

        if (event.getRequestURI().startsWith(contextPath.concat("/base")) && event.getMethod().equals("POST")) {
            bodyRequest.replace("password", "[REDACTED]");
        }

        log.info("{\"method\": \"{}\", \"uri\": \"{}\", \"headers\": {}, \"request\": {}, \"response\": {}, \"duration\": \"{}\", \"parameters\": {}}",
                event.getMethod(),
                event.getRequestURI(),
                getRequestHeaders(event.getHeaders()),
                objectMapper.writeValueAsString(bodyRequest),
                objectMapper.writeValueAsString(bodyResponse),
                event.getDuration(),
                getRequestParameters(event.getParameters())
        );
    }

    private String getRequestHeaders(Map<String, String> requestHeaders) {
        StringBuilder headers = new StringBuilder();
        headers.append("{");

        for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
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
            if (parametersToSkip.stream().anyMatch(entry.getKey()::equalsIgnoreCase)) {
                parameters.append("\"").append(entry.getKey()).append("\": ").append("\"").append("[REDACTED]").append("\",");
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
