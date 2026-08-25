package com.ifsc.prosigaa.dto;

import java.time.LocalDateTime;

public record ComentarioDTO(
        Long id,
        String conteudo,
        LocalDateTime criadoEm,
        UsuarioResumoDTO usuario
) {}
