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
            .requestMatchers("/account/admin/**").hasRole("ADMIN")
            .requestMatchers("/account/customer/**", "/notification/customer/**", "/order/customer/**").hasRole("CUSTOMER")
            .requestMatchers("/account/employee/**", "/notification/employee/**", "/order/employee/**").hasRole("EMPLOYEE")
            .requestMatchers("/phucxfoodshop/image/**", "/account/phoneNumber/**").permitAll()
            .requestMatchers("/shop/home/**", "/shop/search/**").permitAll()
            .requestMatchers("/shop/discount/**", "/shop/category/**", "/shop/product/**").hasRole("ADMIN")
            .requestMatchers("/shop/cart/**").hasRole("CUSTOMER")
            .requestMatchers("/actuator/**", "/chat/**").permitAll()
            .requestMatchers("/swagger-ui/**", "/v3/**", "/document/**").permitAll()
            .requestMatchers("/shipping/**").hasRole("CUSTOMER")
            .requestMatchers("/payment/paypal/pay/cancel", "/payment/paypal/pay/successful").permitAll()
            .requestMatchers("/payment/momo/pay/cancel", "/paymentmomo/pay/successful").permitAll()
            .requestMatchers("/payment/zalopay/pay/successful", "/payment/zalopay/callback").permitAll()
            .requestMatchers("/payment/paypal/**", "/payment/cod/**", "/payment/pay/**", "/payment/methods/**", "/payment/momo/**", "/payment/zalopay/**").hasRole("CUSTOMER")
            .requestMatchers("/payment/invoice/**").hasRole("CUSTOMER")
            .requestMatchers("/payment/admin/**").hasRole("ADMIN")
            .requestMatchers("/register/**", "/test/**", "/forgot/**", "/verify/**", "/reset/**", "/verifyReset/**").permitAll()
            .requestMatchers("/login/**").authenticated()
            .requestMatchers("/address/store").permitAll()
            .requestMatchers("/account/user/**", "/address/**").authenticated()
            .anyRequest().denyAll());
        // login logout
        http.formLogin(login -> login.disable());
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
