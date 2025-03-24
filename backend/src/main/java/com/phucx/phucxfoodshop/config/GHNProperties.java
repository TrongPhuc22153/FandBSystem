package com.phucx.phucxfoodshop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Configuration
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "ghn")
public class GHNProperties {
    private String shopId;
    private String token;
    private String feeUrl;
    private String provinceUrl;
    private String districtUrl;
    private String wardUrl;
    private String servicesUrl;
}
