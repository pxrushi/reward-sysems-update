package com.nt.rewardsystem.security;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        
        // Set the HTTP response status to 403 Forbidden
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        
        // Set the response type to JSON
        response.setContentType("application/json");
        
        // Create a custom JSON response with your message
        String errorResponse = "{"
                + "\"error\": \"You do not have permission to perform this action.\","
                + "\"status\": 403,"
                + "\"timestamp\": \"" + java.time.LocalDateTime.now() + "\","
                + "\"path\": \"" + request.getRequestURI() + "\""
                + "}";
        response.getWriter().write(errorResponse);
    }
}
