package com.ifsc.prosigaa.dto;

import com.ifsc.prosigaa.enums.TipoPostagem;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados necessários para publicação de conteúdo acadêmico.")
public record CadastroPostagemDTO(
        @Schema(description = "Título da postagem", example = "Dúvida sobre listas", requiredMode = Schema.RequiredMode.REQUIRED) String titulo,
        @Schema(description = "Texto do conteúdo", example = "Alguém pode explicar listas encadeadas?", requiredMode = Schema.RequiredMode.REQUIRED) String conteudo,
        @Schema(description = "URL opcional de uma imagem relacionada", example = "https://exemplo.com/imagem.png") String urlImagem,
        @Schema(description = "Categoria da postagem", example = "DUVIDA", requiredMode = Schema.RequiredMode.REQUIRED) TipoPostagem tipo,
        @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED) Long usuarioId,
        @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED) Long faseId,
        @Schema(example = "2", requiredMode = Schema.RequiredMode.REQUIRED) Long materiaId
) {
}
