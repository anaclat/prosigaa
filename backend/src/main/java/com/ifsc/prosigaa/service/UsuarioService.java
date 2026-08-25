package com.ifsc.prosigaa.service;

import com.ifsc.prosigaa.dto.UsuarioDTO;
import com.ifsc.prosigaa.enums.TipoUsuario;
import com.ifsc.prosigaa.model.Usuario;
import com.ifsc.prosigaa.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioDTO cadastrar(String nome, String email, String senha, TipoUsuario tipo) {
        validarEmail(email);

        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        if (tipo == null) {
            tipo = TipoUsuario.ALUNO;
        }

        Usuario usuario = usuarioRepository.save(new Usuario(nome, email, senha, tipo));
        return toDTO(usuario);
    }

    public UsuarioDTO login(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("E-mail ou senha inválidos."));

        if (!usuario.autenticar(senha)) {
            throw new IllegalArgumentException("E-mail ou senha inválidos.");
        }

        return toDTO(usuario);
    }

    public Usuario buscar(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
    }

    public List<UsuarioDTO> listar() {
        return usuarioRepository.findAll().stream().map(this::toDTO).toList();
    }

    private void validarEmail(String email) {
        if (email == null || !(email.endsWith("@ifsc.edu.br") || email.endsWith("@aluno.ifsc.edu.br"))) {
            throw new IllegalArgumentException("Use um e-mail institucional do IFSC.");
        }
    }

    private UsuarioDTO toDTO(Usuario u) {
        return new UsuarioDTO(u.getId(), u.getNome(), u.getEmail(), u.getTipo());
    }
}
