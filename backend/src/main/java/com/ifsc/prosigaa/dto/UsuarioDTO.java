package com.ifsc.prosigaa.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import com.ifsc.prosigaa.enums.TipoUsuario;

@Schema(description = "Representação pública do usuário; não contém senha nem hash.")
// DTO define o formato JSON público e evita expor campos internos da entidade.
// NOTA DE PROVA: DTOs atuais não oferecem links HATEOAS para ações ou recursos relacionados.
public record UsuarioDTO(Long id, String nome, String email, TipoUsuario tipo) {
}
