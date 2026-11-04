package com.ifsc.prosigaa.dto;

import com.ifsc.prosigaa.enums.TipoPostagem;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados necessários para publicação de conteúdo acadêmico.")
public record CadastroPostagemDTO(
        @Schema(example = "Dúvida sobre listas") String titulo,
        @Schema(example = "Alguém pode explicar listas encadeadas?") String conteudo,
        @Schema(example = "https://exemplo.com/imagem.png") String urlImagem,
        @Schema(example = "DUVIDA") TipoPostagem tipo,
        @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED) Long usuarioId,
        @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED) Long faseId,
        @Schema(example = "2", requiredMode = Schema.RequiredMode.REQUIRED) Long materiaId
) {
}
