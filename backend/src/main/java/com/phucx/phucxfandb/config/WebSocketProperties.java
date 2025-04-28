package com.phucx.phucxfandb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "websocket")
public class WebSocketProperties {
    private Integer port;
}
