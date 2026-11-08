package com.ifsc.prosigaa.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para cadastrar matéria e associá-la a uma fase.")
public record CadastroMateriaDTO(
        @Schema(description = "Nome da matéria", example = "Algoritmos", requiredMode = Schema.RequiredMode.REQUIRED) String nome,
        @Schema(description = "Código da matéria", example = "PROG101", requiredMode = Schema.RequiredMode.REQUIRED) String codigo,
        @Schema(description = "Identificador da fase associada", example = "1", requiredMode = Schema.RequiredMode.REQUIRED) Long faseId
) {
}
