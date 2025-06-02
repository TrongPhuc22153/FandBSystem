package com.phucx.phucxfandb.config;

import com.phucx.phucxfandb.enums.RoleName;
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
                .requestMatchers(Authenticated.ALL).authenticated()
                .requestMatchers(Customer.ALL).hasRole(RoleName.CUSTOMER.name())
                .requestMatchers(Employee.ALL).hasRole(RoleName.EMPLOYEE.name())
                .requestMatchers(Admin.ALL).hasRole(RoleName.ADMIN.name())
                .requestMatchers(HttpMethod.GET, Public.GET).permitAll()
                .requestMatchers(HttpMethod.GET, Authenticated.GET).authenticated()
                .requestMatchers(HttpMethod.GET, Employee.GET).hasRole(RoleName.EMPLOYEE.name())
                .requestMatchers(HttpMethod.GET, Admin.GET).hasRole(RoleName.ADMIN.name())
                .requestMatchers(HttpMethod.PATCH, Authenticated.PATCH).authenticated()
                .requestMatchers(HttpMethod.PATCH, Customer.PATCH).hasRole(RoleName.CUSTOMER.name())
                .requestMatchers(HttpMethod.PATCH, Employee.PATCH).hasRole(RoleName.EMPLOYEE.name())
                .requestMatchers(HttpMethod.PATCH, Admin.PATCH).hasRole(RoleName.ADMIN.name())
                .requestMatchers(HttpMethod.POST, Public.POST).permitAll()
                .requestMatchers(HttpMethod.POST, Authenticated.POST).authenticated()
                .requestMatchers(HttpMethod.POST, Employee.POST).hasRole(RoleName.EMPLOYEE.name())
                .requestMatchers(HttpMethod.POST, Admin.POST).hasRole(RoleName.ADMIN.name())
                .requestMatchers(HttpMethod.PUT, Employee.PUT).hasRole(RoleName.EMPLOYEE.name())
                .requestMatchers(HttpMethod.PUT, Admin.PUT).hasRole(RoleName.ADMIN.name())
                .requestMatchers(HttpMethod.DELETE, Employee.DELETE).hasRole(RoleName.EMPLOYEE.name())
                .anyRequest().authenticated());

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
