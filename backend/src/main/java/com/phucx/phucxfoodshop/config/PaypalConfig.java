package com.phucx.phucxfoodshop.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;

@Configuration
public class PaypalConfig {
    @Value("${paypal.client-id}")
    private String clientId;
    @Value("${paypal.client-secret}")
    private String clientSecret;
    @Value("${paypal.mode}")
    private String mode;

    @Bean
    public Map<String, String> paypalModeConfig(){
        Map<String, String> config = new HashMap<>();
        config.put("mode", mode);
        return config;
    }

    // @Bean
    public OAuthTokenCredential oAuthTokenCredential(){
        return new OAuthTokenCredential(clientId, clientSecret, paypalModeConfig());
    }

    @Bean
    public APIContext apiContext(){
        APIContext context = new APIContext(clientId, clientSecret, mode);
        context.setConfigurationMap(paypalModeConfig());
        return context;
    }
}
