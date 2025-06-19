package org.meristem.oneapp.reportservice.config.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.reportservice.dtos.events.RequestAndResponseLogEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
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

    private final List<String> headersToFilterFor = List.of("x-forwarded-for", "host", "user-agent");

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(requestWrapper, responseWrapper);
        long endTime = System.currentTimeMillis();

        List<String> contentTypeToSkipForBody = List.of(MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE);
        byte[] requestBody = nonNull(requestWrapper.getHeader("content-type")) && contentTypeToSkipForBody.contains(requestWrapper.getHeader("content-type")) ? new byte[0] : requestWrapper.getContentAsByteArray();
        byte[] responseBody = responseWrapper.getContentAsByteArray();

        Map<String, String> headers = new HashMap<>();
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
