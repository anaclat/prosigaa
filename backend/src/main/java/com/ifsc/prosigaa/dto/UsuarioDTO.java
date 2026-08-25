package com.ifsc.prosigaa.dto;

import com.ifsc.prosigaa.enums.TipoUsuario;

public record UsuarioDTO(Long id, String nome, String email, TipoUsuario tipo) {}
