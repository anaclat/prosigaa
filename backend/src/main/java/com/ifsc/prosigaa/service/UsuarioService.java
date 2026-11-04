package com.ifsc.prosigaa.service;

import com.ifsc.prosigaa.dto.UsuarioDTO;
import com.ifsc.prosigaa.enums.TipoUsuario;
import com.ifsc.prosigaa.model.Usuario;
import com.ifsc.prosigaa.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.ifsc.prosigaa.exception.ResourceNotFoundException;
import com.ifsc.prosigaa.exception.ConflictException;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioDTO cadastrar(String nome, String email, String senha, TipoUsuario tipo) {
        validarEmail(email);

        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new ConflictException("E-mail já cadastrado.");
        }

        if (tipo == null) {
            tipo = TipoUsuario.ALUNO;
        }

        Usuario usuario = usuarioRepository.save(new Usuario(nome, email, passwordEncoder.encode(validarSenha(senha)), tipo));
        return toDTO(usuario);
    }

    public UsuarioDTO login(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("E-mail ou senha inválidos."));

        if (!usuario.autenticar(senha, passwordEncoder)) {
            throw new IllegalArgumentException("E-mail ou senha inválidos.");
        }
        if (!usuario.possuiHashBCrypt()) {
            usuario.atualizarSenhaHash(passwordEncoder.encode(senha));
            usuarioRepository.save(usuario);
        }
        return toDTO(usuario);
    }

    public Usuario buscar(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
    }

    public List<com.ifsc.prosigaa.dto.UsuarioResumoDTO> listar() {
        return usuarioRepository.findAll().stream()
                .map(u -> new com.ifsc.prosigaa.dto.UsuarioResumoDTO(u.getId(), u.getNome(), u.getTipo()))
                .toList();
    }

    private String validarSenha(String senha) {
        if (senha == null || senha.length() < 8)
            throw new IllegalArgumentException("A senha deve possuir ao menos 8 caracteres.");
        return senha;
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
