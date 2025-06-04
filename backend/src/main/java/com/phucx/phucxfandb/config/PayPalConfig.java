package com.phucx.phucxfandb.config;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "paypal")
public class PayPalConfig {
    @Value("${paypal.client-id}")
    private String clientId;
    @Value("${paypal.client-secret}")
    private String clientSecret;
    @Value("${paypal.mode}")
    private String mode;

    @Bean
    public PayPalEnvironment payPalEnvironment() {
        return "sandbox".equals(mode) ?
                new PayPalEnvironment.Sandbox(clientId, clientSecret) :
                new PayPalEnvironment.Live(clientId, clientSecret);
    }

    @Bean
    public PayPalHttpClient payPalHttpClient(PayPalEnvironment environment) {
        PayPalHttpClient client = new PayPalHttpClient(environment);
        client.setConnectTimeout(900000);
        return client;
    }
}
