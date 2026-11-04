package com.ifsc.prosigaa.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import com.ifsc.prosigaa.enums.TipoUsuario;

@Schema(description = "Representação pública do usuário; não contém senha nem hash.")
public record UsuarioDTO(Long id, String nome, String email, TipoUsuario tipo) {
}
