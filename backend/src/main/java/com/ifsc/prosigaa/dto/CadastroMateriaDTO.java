package com.ifsc.prosigaa.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para cadastrar matéria e associá-la a uma fase.")
public record CadastroMateriaDTO(String nome, String codigo, Long faseId) {
}
