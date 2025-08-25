package org.meristem.oneapp.trustiesservice.config.authConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.meristem.oneapp.trustiesservice.constants.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public record RestAuthenticationEntryPoint(ObjectMapper mapper) implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        LocalDateTime currentTimeStamp = LocalDateTime.now();
        String message = (authException != null && authException.getMessage() != null) ? authException.getMessage()
                : "Unauthorized";
        String path = request.getRequestURI();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(AppConstants.APPLICATION_JSON_UTF8_VALUE);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("timestamp", currentTimeStamp);
        responseMap.put("status", HttpStatus.UNAUTHORIZED.value());
        responseMap.put("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
        responseMap.put("message", message);
        responseMap.put("path", path);
        response.getWriter().write(mapper.writeValueAsString(responseMap));
    }
}
