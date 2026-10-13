package com.ifsc.prosigaa.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI prosigaaOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("ProSIGAA API")
                .version("1.0.0")
                .description("API REST para compartilhamento de postagens acadêmicas, comentários, curtidas, fases e matérias.")
                .contact(new Contact().name("Equipe ProSIGAA")));
    }
}
