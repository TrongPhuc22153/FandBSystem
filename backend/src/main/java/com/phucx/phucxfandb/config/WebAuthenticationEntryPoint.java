package com.phucx.phucxfandb.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class WebAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String errorCode;
        String message;

        if (authException instanceof BadCredentialsException) {
            errorCode = "INVALID_CREDENTIALS";
            message = "Invalid username or password";
        } else if (authException instanceof UsernameNotFoundException) {
            errorCode = "USER_NOT_FOUND";
            message = "User does not exist";
        } else if (authException instanceof InsufficientAuthenticationException) {
            errorCode = "INSUFFICIENT_AUTHENTICATION";
            message = "Full authentication is required to access this resource";
        } else {
            errorCode = "UNAUTHORIZED";
            message = "Unauthorized";
        }

        ResponseDTO<String> responseDTO = ResponseDTO.<String>builder()
                .error(errorCode)
                .message(message)
                .build();
        response.getWriter().write(objectMapper.writeValueAsString(responseDTO));
    }
}
