package com.ifsc.prosigaa.dto;


import com.ifsc.prosigaa.enums.TipoNotificacao;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificacaoDTO(

        UUID id,

        String mensagem,

        TipoNotificacao tipoNotificacao,

        UUID postagemId,

        String remetenteNome,

        LocalDateTime criadoEm

) {
}
//O JSON enviado ao React ficará aproximadamente assim:
//
//        {
//        "id": "4d5c5a4e-2c1c-4a5f-b1f0-4a5c6d7e8f90",
//        "mensagem": "João comentou na sua postagem.",
//        "tipoNotificacao": "COMENTARIO",
//        "postagemId": "a7f1b7f1-1234-4567-8901-123456789abc",
//        "remetenteNome": "João",
//        "criadoEm": "2026-10-07T21:30:15"
//        }