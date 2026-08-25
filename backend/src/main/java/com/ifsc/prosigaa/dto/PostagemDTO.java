package com.ifsc.prosigaa.dto;

import com.ifsc.prosigaa.enums.TipoPostagem;

import java.time.LocalDateTime;
import java.util.List;

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
) {}
