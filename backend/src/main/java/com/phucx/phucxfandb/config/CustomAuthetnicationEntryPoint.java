package com.phucx.phucxfandb.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomAuthetnicationEntryPoint implements AuthenticationEntryPoint{

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        // send error message back to client for falied authentication
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        Map<String, Object> data = new HashMap<>();
        data.put("timestamp", System.currentTimeMillis());
        data.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        data.put("error", "Unauthorized");
        data.put("message", authException.getMessage());
        data.put("path", request.getRequestURI());

        ObjectMapper objectMapper = new ObjectMapper();
        response.getOutputStream().println(objectMapper.writeValueAsString(data));
    }
    
}
