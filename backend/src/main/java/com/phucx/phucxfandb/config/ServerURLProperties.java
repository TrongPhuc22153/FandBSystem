package com.phucx.phucxfandb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "phucx")
public class ServerURLProperties {
    private String serverDevUrl;
    private String uiUrl;
    private List<String> allowedUrls;
    private String paymentSuccessfulUrl;
    private String paymentCanceledUrl;
    private String serverUrl;
    private String redirectPaymentUrl;
    private String domain;
}
