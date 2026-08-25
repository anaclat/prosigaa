package com.ifsc.prosigaa.dto;

import java.util.List;

public record FaseDTO(Long id, String nome, int numero, List<MateriaDTO> materias) {}
