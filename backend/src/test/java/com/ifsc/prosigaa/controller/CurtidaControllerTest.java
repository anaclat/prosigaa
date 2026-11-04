package com.ifsc.prosigaa.controller;

import com.ifsc.prosigaa.service.CurtidaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CurtidaController.class)
class CurtidaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CurtidaService curtidaService;

    @Test
    void deveCurtirPostagemComSucesso() throws Exception {
        doNothing().when(curtidaService).curtirPostagem(1L, 10L);

        mockMvc.perform(post("/curtidas/postagens/1").contentType(APPLICATION_JSON).content("{\"usuarioId\":10}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.mensagem").value("Postagem curtida."));
    }
}
