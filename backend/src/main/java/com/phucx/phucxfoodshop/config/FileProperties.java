package com.phucx.phucxfoodshop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Configuration
@Data @ToString
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "file")
public class FileProperties {
    private String productImageLocation;
    private String categoryImageLocation;
    private String customerImageLocation;
    private String employeeImageLocation;
}
