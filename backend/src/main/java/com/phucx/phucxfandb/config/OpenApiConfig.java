package com.phucx.phucxfandb.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {

    @Bean
    public OpenAPI myOpenAPI(){
        Server serverUrl = new Server();
        serverUrl.setDescription("Server Url in Development environment");
        serverUrl.setUrl("http://localhost:8080");

        Contact contact = new Contact();
        contact.setName("phucx");
        contact.setEmail("trongphuc22153@gmail.com");

        Info info = new Info();
        info.description("Phucxfoodshop API").contact(contact).version("1.0.0").title("Phucxfoodshop API");
        return new OpenAPI().info(info)
            .servers(List.of(serverUrl))
            .components(new Components()
                    .addSecuritySchemes("bearerAuth", new SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")))
            .security(List.of(new SecurityRequirement().addList("bearerAuth")));
    }
}
