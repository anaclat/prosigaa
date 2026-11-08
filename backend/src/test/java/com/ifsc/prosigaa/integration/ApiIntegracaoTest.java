package com.ifsc.prosigaa.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiIntegracaoTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveConsultarFasesPelaApiComAplicacaoIntegrada() throws Exception {
        mockMvc.perform(get("/fases"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.fases").isArray())
        .andExpect(jsonPath("$.fases.length()").value(5))
        .andExpect(jsonPath("$.fases[0].nome").value("1º período"))
        .andExpect(jsonPath("$.fases[0].materias.length()").value(6))
        .andExpect(jsonPath("$.fases[4].materias.length()").value(7));
    }

    @Test
    void deveRetornarListaDePostagens() throws Exception {
        mockMvc.perform(get("/postagens"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(4));

        mockMvc.perform(get("/postagens/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.comentarios.length()").value(3));
    }

    @Test
    void devePublicarDocumentacaoOpenApiComRotasECodigosDeCriacao() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.paths['/usuarios'].post.responses['201'].description").value("Usuário criado"))
                .andExpect(jsonPath("$.paths['/postagens'].post.responses['201'].description").value("Postagem criada"))
                .andExpect(jsonPath("$.paths['/postagens/{id}'].delete.responses['204'].description").value("Postagem excluída"))
                .andExpect(jsonPath("$.paths['/postagens/{id}'].get.responses['404'].description").value("Postagem não encontrada"))
                .andExpect(jsonPath("$.paths['/curtidas/postagens/{postagemId}'].delete.responses['200'].description").value("Curtida removida"))
                .andExpect(jsonPath("$.components.schemas.ApiErrorDTO.properties.mensagem.example").value("Postagem não encontrada."));
    }
}
