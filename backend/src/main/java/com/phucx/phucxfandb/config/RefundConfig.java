package com.phucx.phucxfandb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "refund")
public class RefundConfig {
    private Map<String, Double> percentages;

}
