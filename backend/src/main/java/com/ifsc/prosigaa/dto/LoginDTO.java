package com.ifsc.prosigaa.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Credenciais para autenticação.")
public record LoginDTO(
        @Schema(description = "E-mail institucional", example = "aluno@ifsc.edu.br", requiredMode = Schema.RequiredMode.REQUIRED) String email,
        @Schema(description = "Senha da conta", example = "SenhaSegura123", requiredMode = Schema.RequiredMode.REQUIRED, format = "password") String senha
) {
}
