package com.ifsc.prosigaa.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Representação pública de comentário e total de curtidas.")
public record ComentarioDTO(
        Long id,
        String conteudo,
        LocalDateTime criadoEm,
        UsuarioResumoDTO usuario,
        int curtidas
) {
}
