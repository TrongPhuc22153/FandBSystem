package com.phucx.phucxfandb.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "paypal")
public class PaypalProperties {
    private String clientId;
    private String clientSecret;
    private String mode;
}
