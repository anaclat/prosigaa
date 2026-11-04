package com.ifsc.prosigaa.dto;

import com.ifsc.prosigaa.enums.TipoUsuario;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados necessários para criação de uma conta. A senha nunca é devolvida pela API.")
public record CadastroUsuarioDTO(
        @Schema(description = "Nome de exibição", example = "Maria Silva", requiredMode = Schema.RequiredMode.REQUIRED) String nome,
        @Schema(description = "E-mail institucional", example = "maria@ifsc.edu.br", requiredMode = Schema.RequiredMode.REQUIRED) String email,
        @Schema(description = "Senha com no mínimo oito caracteres", example = "SenhaSegura123", format = "password", requiredMode = Schema.RequiredMode.REQUIRED) String senha,
        @Schema(description = "Perfil do usuário", example = "ALUNO") TipoUsuario tipo
) {
}
