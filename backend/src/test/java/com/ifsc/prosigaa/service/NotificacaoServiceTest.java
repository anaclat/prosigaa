package com.ifsc.prosigaa.service;

import com.ifsc.prosigaa.dto.NotificacaoDTO;
import com.ifsc.prosigaa.enums.TipoNotificacao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificacaoServiceTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private NotificacaoService notificacaoService;

    @Test
    void deveEnviarCurtidaParaOCanalDoAutorDaPostagem() {
        notificacaoService.notificarCurtidaPostagem(42L, 7L, "Ana");

        var notificacao = ArgumentCaptor.forClass(NotificacaoDTO.class);
        verify(messagingTemplate).convertAndSend(
                eq("/topic/usuarios/42/notifications"),
                notificacao.capture()
        );
        assertEquals(TipoNotificacao.CURTIDA, notificacao.getValue().tipoNotificacao());
        assertEquals(7L, notificacao.getValue().postagemId());
        assertEquals("Ana curtiu sua postagem.", notificacao.getValue().mensagem());
    }
}
