package com.phucx.phucxfandb.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class WebAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ResponseDTO<String> responseDTO = ResponseDTO.<String>builder()
                .error("FORBIDDEN")
                .message(accessDeniedException.getMessage())
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(responseDTO));
    }
}
