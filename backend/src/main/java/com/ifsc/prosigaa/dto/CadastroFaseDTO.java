package com.ifsc.prosigaa.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para cadastrar uma fase curricular.")
public record CadastroFaseDTO(String nome, int numero) {
}
