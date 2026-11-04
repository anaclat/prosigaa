package com.ifsc.prosigaa.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representação pública de matéria.")
public record MateriaDTO(Long id, String nome, String codigo) {
}
