package com.ifsc.prosigaa.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Formato padronizado de erro retornado pela API.")
public record ApiErrorDTO(
        @Schema(example = "404") int status,
        @Schema(example = "Not Found") String erro,
        @Schema(example = "Postagem não encontrada.") String mensagem,
        @Schema(example = "2026-11-07T21:00:00") LocalDateTime timestamp
) {
}
