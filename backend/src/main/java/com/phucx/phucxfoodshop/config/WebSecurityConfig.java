package com.phucx.phucxfoodshop.config;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.lang.Nullable;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
@EnableAspectJAutoProxy
@ComponentScans({
    @ComponentScan("com.phucx.phucxfoodshop.aspects"),
    @ComponentScan("com.phucx.phucxfoodshop.provider"),
    @ComponentScan("com.phucx.phucxfoodshop.converter")
})
public class WebSecurityConfig {
    // token
    public final static String PREFERRED_USERNAME="preferred_username";
    public final static String REALM_ACCESS_CLAIM="realm_access";
    public final static String ROLES_CLAIM="roles";
    // roles
    public final static String ROLE_CUSTOMER = "ROLE_CUSTOMER";
    public final static String ROLE_EMPLOYEE = "ROLE_EMPLOYEE";
    public final static String ROLE_ADMIN = "ROLE_ADMIN";
    @Value("${phucx.allowed-urls}")
    private List<String> allowedUrls;

    @Bean
    public SecurityFilterChain dFilterChain(HttpSecurity http) throws Exception{
        // session
        http.sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
        // cors
        http.cors(cors -> cors.configurationSource(new CorsConfigurationSource() {

            @Override
            @Nullable
            public CorsConfiguration getCorsConfiguration(HttpServletRequest arg0) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(allowedUrls);
                configuration.setAllowedMethods(Collections.singletonList("*"));
                configuration.setAllowedHeaders(Collections.singletonList("*"));
                configuration.setAllowCredentials(true);
                return configuration;
            }
        }));
        // csrf
        http.csrf(csrf -> csrf.disable());
        // var delegate = new XorCsrfTokenRequestAttributeHandler();
        // http.csrf(csrf-> csrf.ignoringRequestMatchers("/logout", "/chat/**", "/register", "/forgot", "/reset")
        //     .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        //     .csrfTokenRequestHandler(delegate::handle));
        // request
        http.authorizeHttpRequests(request -> request
            .requestMatchers("/api/v1/account/admin/**").hasRole("ADMIN")
            .requestMatchers("/api/v1/account/customer/**", "/api/v1/notification/customer/**", "/api/v1/order/customer/**").hasRole("CUSTOMER")
            .requestMatchers("/api/v1/account/employee/**", "/api/v1/notification/employee/**", "/api/v1/order/employee/**").hasRole("EMPLOYEE")
            .requestMatchers("/api/v1/phucxfoodshop/image/**", "/api/v1/account/phoneNumber/**").permitAll()
            .requestMatchers("/api/v1/shop/home/**", "/api/v1/shop/search/**").permitAll()
            .requestMatchers("/api/v1/shop/discount/**", "/api/v1/shop/category/**", "/api/v1/shop/product/**").hasRole("ADMIN")
            .requestMatchers("/api/v1/shop/cart/**").hasRole("CUSTOMER")
            .requestMatchers("/actuator/**", "/chat/**").permitAll()
            .requestMatchers("/swagger-ui/**", "/v3/**", "/document/**").permitAll()
            .requestMatchers("/api/v1/shipping/**").hasRole("CUSTOMER")
            .requestMatchers("/api/v1/payment/paypal/pay/cancel", "/api/v1/payment/paypal/pay/successful").permitAll()
            .requestMatchers("/api/v1/payment/momo/pay/cancel", "/api/v1/paymentmomo/pay/successful").permitAll()
            .requestMatchers("/api/v1/payment/zalopay/pay/successful", "/api/v1/payment/zalopay/callback").permitAll()
            .requestMatchers("/api/v1/payment/paypal/**", "/api/v1/payment/cod/**", "/api/v1/payment/pay/**", "/api/v1/payment/methods/**", "/api/v1/payment/momo/**", "/api/v1/payment/zalopay/**").hasRole("CUSTOMER")
            .requestMatchers("/api/v1/payment/invoice/**").hasRole("CUSTOMER")
            .requestMatchers("/api/v1/payment/admin/**").hasRole("ADMIN")
            .requestMatchers("/api/v1/register/**", "/api/v1/test/**", "/api/v1/forgot/**", "/api/v1/verify/**", "/api/v1/reset/**", "/api/v1/verifyReset/**").permitAll()
            .requestMatchers("/api/v1/login/**").authenticated()
            .requestMatchers("/api/v1/address/store").permitAll()
            .requestMatchers("/api/v1/account/user/**", "/api/v1/address/**").authenticated()
            .anyRequest().denyAll());
        // login logout
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(login -> login.authenticationEntryPoint(new CustomAuthetnicationEntryPoint()));
        http.logout(logout -> logout
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID"));
        return http.build();
    }

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
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
