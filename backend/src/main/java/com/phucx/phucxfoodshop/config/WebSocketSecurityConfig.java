package com.phucx.phucxfoodshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

@Configuration
@EnableWebSocketSecurity
public class WebSocketSecurityConfig {
    @Bean
    public AuthorizationManager<Message<?>> messageAuthorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder builder){
        builder
            .anyMessage().permitAll();
            // .nullDestMatcher().authenticated()
            // .simpDestMatchers("/app/customer/**").hasRole("CUSTOMER")
            // .simpDestMatchers("/app/employee/**").hasRole("EMPLOYEE")
            // .simpSubscribeDestMatchers("/user/**").hasAnyRole("CUSTOMER", "EMPLOYEE")
            // .simpSubscribeDestMatchers("/topic/order/**").hasRole("EMPLOYEE")
            // .simpSubscribeDestMatchers("/topic/employee.notification.order/**").hasRole("EMPLOYEE")
            // .anyMessage().denyAll();
        return builder.build();
    }
    
}
