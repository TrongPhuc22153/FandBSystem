package com.phucx.phucxfandb.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.phucxfandb.constant.ApiEndpoint;
import com.phucx.phucxfandb.constant.JwtType;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.jwt.JwtAuthenticationService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.phucx.phucxfandb.constant.WebConstant.AUTHORIZATION_HEADER;
import static com.phucx.phucxfandb.constant.WebConstant.BEARER_PREFIX;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {
    private final JwtAuthenticationService jwtAuthenticationService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String token = extractToken(request);
        if (token != null && !token.isBlank()) {
            JwtType jwtType;
            try {
                jwtType = jwtAuthenticationService.validateToken(token);
            } catch (JwtException e) {
                ResponseDTO<Void> responseDTO = ResponseDTO.<Void>builder()
                        .error("INVALID_TOKEN")
                        .message(e.getMessage())
                        .build();

                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(objectMapper.writeValueAsString(responseDTO));
                return;
            }

            switch (jwtType){
                case BEARER -> {
                    UsernamePasswordAuthenticationToken authentication = jwtAuthenticationService.extractAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                case RESET_PASSWORD -> {
                    if(!(request.getRequestURI().equals(ApiEndpoint.RESET_PASSWORD_AUTH_ENDPOINT))){
                        ResponseDTO<Void> responseDTO = ResponseDTO.<Void>builder()
                                .error("PASSWORD_RESET_REQUIRED")
                                .message("Password reset required")
                                .build();

                        response.setStatus(HttpStatus.FORBIDDEN.value());
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        response.getWriter().write(objectMapper.writeValueAsString(responseDTO));
                        return;
                    }
                }
                default ->{
                    ResponseDTO<Void> responseDTO = ResponseDTO.<Void>builder()
                            .error("INVALID_TOKEN")
                            .message("Your token is invalid")
                            .build();

                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.getWriter().write(objectMapper.writeValueAsString(responseDTO));
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().startsWith(ApiEndpoint.LOGIN_AUTH_ENDPOINT);
    }

    private String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            return authorizationHeader.substring(BEARER_PREFIX.length()).trim();
        }
        return null;
    }

}
