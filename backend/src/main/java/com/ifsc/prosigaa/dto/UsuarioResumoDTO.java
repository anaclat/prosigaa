package com.ifsc.prosigaa.dto;

import com.ifsc.prosigaa.enums.TipoUsuario;

public record UsuarioResumoDTO(Long id, String nome, TipoUsuario tipo) {}
