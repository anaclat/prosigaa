package com.ifsc.prosigaa.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import com.ifsc.prosigaa.enums.TipoPostagem;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Representação de postagem com autor, classificação, comentários e contagem de curtidas.")
// Representação JSON própria da API, separada da entidade JPA. Não contém links de navegação.
// NOTA DE PROVA: para Richardson nível 3 (HATEOAS), incluir links como _links.self e _links.curtir.
public record PostagemDTO(
        Long id,
        String titulo,
        String conteudo,
        String urlImagem,
        TipoPostagem tipo,
        LocalDateTime criadoEm,
        UsuarioResumoDTO usuario,
        FaseDTO fase,
        MateriaDTO materia,
        List<ComentarioDTO> comentarios,
        int curtidas
) {
}
