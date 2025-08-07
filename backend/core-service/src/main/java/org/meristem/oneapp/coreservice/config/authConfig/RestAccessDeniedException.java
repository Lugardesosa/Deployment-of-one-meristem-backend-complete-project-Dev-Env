package org.meristem.oneapp.coreservice.config.authConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class RestAccessDeniedException implements AccessDeniedHandler {

    private final ObjectMapper mapper;
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        LocalDateTime currentTimeStamp = LocalDateTime.now();
        String message = (accessDeniedException != null && accessDeniedException.getMessage() != null) ?
                accessDeniedException.getMessage() : "Authorization failed";
        String path = request.getRequestURI();
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("timestamp", currentTimeStamp);
        responseMap.put("status", HttpStatus.FORBIDDEN.value());
        responseMap.put("error", HttpStatus.FORBIDDEN.getReasonPhrase());
        responseMap.put("message", message);
        responseMap.put("path", path);
        response.getWriter().write(mapper.writeValueAsString(responseMap));
    }
}
