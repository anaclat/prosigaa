package com.ifsc.prosigaa.service;

import com.ifsc.prosigaa.dto.NotificacaoDTO;
import com.ifsc.prosigaa.enums.TipoNotificacao;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Envia uma notificação para um usuário específico.
     *
     * O usuário deve estar autenticado no WebSocket e o
     * Principal.getName() dele deve corresponder ao userId
     * utilizado aqui.
     */
    public void enviarParaUsuario(
            UUID usuarioId,
            NotificacaoDTO notification
    ) {

        messagingTemplate.convertAndSendToUser(
                usuarioId.toString(),
                "/queue/notifications",
                notification
        );
    }

    /**
     * Notificação de comentário.
     */
    public void notificarNovoComentario(
            UUID autorPostagemId,
            UUID postagemId,
            String nomeAutorComentario
    ) {

        NotificacaoDTO notification = new NotificacaoDTO(
                UUID.randomUUID(),
                nomeAutorComentario + " comentou na sua postagem.",
                TipoNotificacao.COMENTARIO,
                postagemId,
                nomeAutorComentario,
                LocalDateTime.now()
        );

        enviarParaUsuario(
                autorPostagemId,
                notification
        );
    }

    /**
     * Notificação de curtida.
     */
    public void notificarCurtidaPostagem(
            UUID autorPostagemId,
            UUID postagemId,
            String nomeUsuario
    ) {

        NotificacaoDTO notification = new NotificacaoDTO(
                UUID.randomUUID(),
                nomeUsuario + " curtiu sua postagem.",
                TipoNotificacao.CURTIDA,
                postagemId,
                nomeUsuario,
                LocalDateTime.now()
        );

        enviarParaUsuario(
                autorPostagemId,
                notification
        );
    }
}
