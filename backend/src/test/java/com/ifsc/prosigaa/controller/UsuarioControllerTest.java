package com.ifsc.prosigaa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifsc.prosigaa.dto.LoginDTO;
import com.ifsc.prosigaa.dto.UsuarioDTO;
import com.ifsc.prosigaa.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsuarioService usuarioService;

    @Test
    void deveFazerLoginComSucesso() throws Exception {
        LoginDTO loginDTO = new LoginDTO("teste@ifsc.edu.br", "123456");
        UsuarioDTO usuarioMock = new UsuarioDTO(1L, "Usuário Teste", "teste@ifsc.edu.br", null);

        when(usuarioService.login("teste@ifsc.edu.br", "123456")).thenReturn(usuarioMock);

        mockMvc.perform(post("/usuario/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("teste@ifsc.edu.br"));
    }

    @Test
    void deveRetornarBadRequestQuandoLancarIllegalArgumentException() throws Exception {
        LoginDTO loginDTO = new LoginDTO("invalido@ifsc.edu.br", "000");

        when(usuarioService.login("invalido@ifsc.edu.br", "000"))
                .thenThrow(new IllegalArgumentException("Credenciais inválidas."));

        mockMvc.perform(post("/usuario/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Credenciais inválidas."));
    }
}