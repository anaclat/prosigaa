package com.ifsc.prosigaa.controller;

import com.ifsc.prosigaa.service.NotificacaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificacaoWebSocketController.class)
class NotificacaoWebSocketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SimpMessagingTemplate messagingTemplate;

    @MockitoBean
    private NotificacaoService notificacaoService;

    @Test
    void deveEnviarNotificacaoDeTesteParaCanalPublico() throws Exception {
        mockMvc.perform(post("/api/test/notify")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"mensagem\":\"Teste de notificação\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("enviado"))
        .andExpect(jsonPath("$.mensagem").value("Teste de notificação"));

        verify(messagingTemplate).convertAndSend(eq("/topic/notifications"), org.mockito.ArgumentMatchers.<Object>any());
    }

    @Test
    void deveEnviarNotificacaoDeTesteParaCanalDoUsuarioInformado() throws Exception {
        mockMvc.perform(post("/api/test/notify")
        .param("userId", "10")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"mensagem\":\"Teste para o usuário\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("enviado"));

        verify(messagingTemplate).convertAndSend(
        eq("/topic/usuarios/10/notifications"),
        org.mockito.ArgumentMatchers.<Object>any()
        );
    }
}
