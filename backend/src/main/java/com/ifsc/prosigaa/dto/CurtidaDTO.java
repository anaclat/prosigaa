package com.ifsc.prosigaa.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Identificador do usuário que executa a curtida")
public record CurtidaDTO(@Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED) Long usuarioId) {
}
