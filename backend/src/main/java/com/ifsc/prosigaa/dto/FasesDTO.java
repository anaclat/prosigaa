package com.ifsc.prosigaa.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Coleção de fases cadastradas.")
public record FasesDTO(List<FaseDTO> fases) {
}
