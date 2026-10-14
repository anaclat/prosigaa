package com.ifsc.prosigaa.dto;

import com.ifsc.prosigaa.enums.TipoNotificacao;

import java.time.LocalDateTime;

public record NotificacaoDTO(
        Long id,
        String mensagem,
        TipoNotificacao tipoNotificacao,
        Long postagemId,
        String remetenteNome,
        LocalDateTime criadoEm
) {
}
