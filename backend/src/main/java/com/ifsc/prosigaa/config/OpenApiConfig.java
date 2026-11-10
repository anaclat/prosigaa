package com.ifsc.prosigaa.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// OpenAPI descreve o contrato HTTP (rotas, parâmetros, corpos e respostas) para ferramentas como Swagger UI.
// Para documentar os canais e eventos WebSocket/STOMP, normalmente é necessário um contrato complementar,
// como AsyncAPI; OpenAPI não transforma a API em HATEOAS nem define sua implementação.
// Este projeto usa REST orientado a recursos e JSON; SOAP costuma usar envelopes XML/WSDL,
// enquanto RPC/gRPC expõe chamadas de operações (gRPC usa Protocol Buffers e HTTP/2).
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
