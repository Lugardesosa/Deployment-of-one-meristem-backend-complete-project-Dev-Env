package org.meristem.oneapp.trusteesservice.config.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.trusteesservice.dtos.events.RequestAndResponseLogEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@WebFilter(filterName = "RequestResponseLogging", urlPatterns = "/*")
@RequiredArgsConstructor
public class RequestResponseLogging extends OncePerRequestFilter {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${headers-to-filter-for}")
    private List<String> headersToFilterFor;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.nanoTime() / 1_000_000L;
        filterChain.doFilter(requestWrapper, responseWrapper);
        long endTime = System.nanoTime() / 1_000_000L;

        byte[] requestBody = nonNull(requestWrapper.getHeader(HttpHeaders.CONTENT_TYPE)) && MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(requestWrapper.getHeader(HttpHeaders.CONTENT_TYPE)) ? requestWrapper.getContentAsByteArray() : new byte[0];
        byte[] responseBody = responseWrapper.getContentAsByteArray();

        Map<String, Object> headers = new HashMap<>();
        for (String headerName : headersToFilterFor) {
            headers.put(headerName, requestWrapper.getHeader(headerName));
        }
        applicationEventPublisher.publishEvent(new RequestAndResponseLogEvent(this, requestBody, responseBody, endTime - startTime, requestWrapper.getRequestURI(),
                requestWrapper.getMethod(), requestWrapper.getParameterMap(), headers, responseWrapper.getStatus()));

        responseWrapper.copyBodyToResponse();
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        return Stream.of("/actuator", "/h2-console", "/swagger-ui", "/api-docs", "/webjars", "/ws").anyMatch(url -> requestUri.startsWith(contextPath.concat(url)));
    }
}
