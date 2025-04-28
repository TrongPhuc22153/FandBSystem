package com.phucx.phucxfandb.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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
