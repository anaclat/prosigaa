package com.ifsc.prosigaa.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Representação de fase curricular e matérias associadas.")
public record FaseDTO(Long id, String nome, int numero, List<MateriaDTO> materias) {
}
