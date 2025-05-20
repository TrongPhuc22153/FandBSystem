package com.phucx.phucxfandb.config;

import com.phucx.phucxfandb.constant.RoleName;
import com.phucx.phucxfandb.filter.JwtValidationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

import static com.phucx.phucxfandb.constant.ApiEndpoint.*;

@Configuration
@EnableWebSecurity
@EnableAspectJAutoProxy
@RequiredArgsConstructor
public class WebSecurityConfig {
    @Value("${phucx.allowed-url}")
    private String allowedUrl;

    private final JwtValidationFilter jwtValidationFilter;
    private final WebAccessDeniedHandler accessDeniedHandler;
    private final WebAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain dFilterChain(HttpSecurity http) throws Exception{
        http.sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(Collections.singletonList(allowedUrl));
            configuration.setAllowedMethods(Collections.singletonList("*"));
            configuration.setAllowedHeaders(Collections.singletonList("*"));
            configuration.setAllowCredentials(true);
            return configuration;
        }));

        http.csrf(AbstractHttpConfigurer::disable);

        http.addFilterBefore(jwtValidationFilter, UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests(request -> request
                .requestMatchers(HttpMethod.GET, PUBLIC_API).permitAll()
                .requestMatchers(AUTH_API).permitAll()
                .requestMatchers(AUTHENTICATED_USER_API).authenticated()
                .requestMatchers(HttpMethod.PATCH, ORDER_BY_ID_ENDPOINT).hasRole(RoleName.EMPLOYEE.name())
                .requestMatchers(HttpMethod.PATCH, RESERVATION_BY_ID_ENDPOINT).hasRole(RoleName.EMPLOYEE.name())
                .requestMatchers(CUSTOMER_API).hasRole(RoleName.CUSTOMER.name())
                .requestMatchers(EMPLOYEE_API).hasRole(RoleName.EMPLOYEE.name())
                .requestMatchers(WAITER_API).hasRole(RoleName.WAITER.name())
                .requestMatchers(CHEF_API).hasRole(RoleName.CHEF.name())
                .requestMatchers(RECEPTIONIST_API).hasRole(RoleName.RECEPTIONIST.name())
                .requestMatchers(ADMIN_API).hasRole(RoleName.ADMIN.name())
                .anyRequest().denyAll());

        http.exceptionHandling(ex -> ex
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
        );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
