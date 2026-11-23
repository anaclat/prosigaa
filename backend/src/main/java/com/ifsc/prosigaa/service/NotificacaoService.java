package com.ifsc.prosigaa.service;

import com.ifsc.prosigaa.dto.NotificacaoDTO;
import com.ifsc.prosigaa.enums.TipoNotificacao;
import com.ifsc.prosigaa.service.ComentarioService.NovoComentarioEvent;
import com.ifsc.prosigaa.service.CurtidaService.CurtidaComentarioEvent;
import com.ifsc.prosigaa.service.CurtidaService.CurtidaPostagemEvent;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
// Serviço publica eventos de domínio no STOMP; o fluxo é assíncrono e desacoplado da resposta REST.
public class NotificacaoService {

    private final SimpMessagingTemplate messagingTemplate;

    public void enviarParaUsuario(Long usuarioId, NotificacaoDTO notification) {
        if (usuarioId == null) {
            return;
        }

        // O destino permite ao cliente assinar notificações do usuário; ele não é uma rota REST.
        messagingTemplate.convertAndSend(
                "/topic/usuarios/" + usuarioId + "/notifications",
                notification);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onNovoComentario(NovoComentarioEvent event) {
        notificarNovoComentario(event.autorPostagemId(), event.postagemId(), event.nomeAutorComentario());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCurtidaPostagem(CurtidaPostagemEvent event) {
        notificarCurtidaPostagem(event.autorPostagemId(), event.postagemId(), event.nomeUsuario());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCurtidaComentario(CurtidaComentarioEvent event) {
        notificarCurtidaComentario(event.autorComentarioId(), event.comentarioId(), event.nomeUsuario());
    }

    public void notificarNovoComentario(Long autorPostagemId, Long postagemId, String nomeAutorComentario) {
        if (autorPostagemId == null || nomeAutorComentario == null || nomeAutorComentario.isBlank()) {
            return;
        }

        NotificacaoDTO notification = new NotificacaoDTO(
                System.currentTimeMillis(),
                nomeAutorComentario + " comentou na sua postagem.",
                TipoNotificacao.COMENTARIO,
                postagemId,
                nomeAutorComentario,
                LocalDateTime.now());

        enviarParaUsuario(autorPostagemId, notification);
    }

    public void notificarCurtidaPostagem(Long autorPostagemId, Long postagemId, String nomeUsuario) {
        if (autorPostagemId == null || nomeUsuario == null || nomeUsuario.isBlank()) {
            return;
        }

        NotificacaoDTO notification = new NotificacaoDTO(
                System.currentTimeMillis(),
                nomeUsuario + " curtiu sua postagem.",
                TipoNotificacao.CURTIDA,
                postagemId,
                nomeUsuario,
                LocalDateTime.now());

        enviarParaUsuario(autorPostagemId, notification);
    }

    public void notificarCurtidaComentario(Long autorComentarioId, Long comentarioId, String nomeUsuario) {
        if (autorComentarioId == null || nomeUsuario == null || nomeUsuario.isBlank()) {
            return;
        }

        NotificacaoDTO notification = new NotificacaoDTO(
                System.currentTimeMillis(),
                nomeUsuario + " curtiu o seu comentário.",
                TipoNotificacao.CURTIDA,
                comentarioId,
                nomeUsuario,
                LocalDateTime.now());

        enviarParaUsuario(autorComentarioId, notification);
    }
}
