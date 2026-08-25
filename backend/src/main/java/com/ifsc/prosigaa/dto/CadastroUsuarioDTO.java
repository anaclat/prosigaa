package com.ifsc.prosigaa.dto;

import com.ifsc.prosigaa.enums.TipoUsuario;

public record CadastroUsuarioDTO(String nome, String email, String senha, TipoUsuario tipo) {}
