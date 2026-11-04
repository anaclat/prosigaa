package com.ifsc.prosigaa.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import com.ifsc.prosigaa.enums.TipoUsuario;

@Schema(description = "Dados mínimos de identificação pública do usuário.")
public record UsuarioResumoDTO(Long id, String nome, TipoUsuario tipo) {
}
