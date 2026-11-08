package com.ifsc.prosigaa.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para cadastrar uma fase curricular.")
public record CadastroFaseDTO(
        @Schema(description = "Nome da fase", example = "1º período", requiredMode = Schema.RequiredMode.REQUIRED) String nome,
        @Schema(description = "Número sequencial da fase", example = "1", requiredMode = Schema.RequiredMode.REQUIRED) int numero
) {
}
