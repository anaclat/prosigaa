package com.ifsc.prosigaa.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Conteúdo do comentário e identificador do usuário autor.")
public record CadastroComentarioDTO(
        @Schema(example = "A explicação ajudou bastante!", requiredMode = Schema.RequiredMode.REQUIRED) String conteudo,
        @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED) Long usuarioId
) {
}
