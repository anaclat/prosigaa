package com.ifsc.prosigaa.dto;

import com.ifsc.prosigaa.enums.TipoPostagem;

public record CadastroPostagemDTO(
        String titulo,
        String conteudo,
        String urlImagem,
        TipoPostagem tipo,
        Long usuarioId,
        Long faseId,
        Long materiaId
) {}
