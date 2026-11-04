package com.ifsc.prosigaa.model;

import com.ifsc.prosigaa.enums.TipoUsuario;
import jakarta.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;

    private LocalDateTime criadoEm;

    @OneToMany(mappedBy = "usuario")
    private List<Postagem> postagens = new ArrayList<>();

    @OneToMany(mappedBy = "usuario")
    private List<Comentario> comentarios = new ArrayList<>();

    @OneToMany(mappedBy = "usuario")
    private List<CurtidaPostagem> curtidasPostagem = new ArrayList<>();

    @OneToMany(mappedBy = "usuario")
    private List<CurtidaComentario> curtidasComentario = new ArrayList<>();

    protected Usuario() {
    }

    public Usuario(String nome, String email, String senhaHash, TipoUsuario tipo) {
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.tipo = tipo;
        this.criadoEm = LocalDateTime.now();
    }

    public boolean autenticar(String senha, PasswordEncoder passwordEncoder) {
        if (senha == null || senhaHash == null) return false;
        if (senhaHash.startsWith("$2a$") || senhaHash.startsWith("$2b$") || senhaHash.startsWith("$2y$")) {
            return passwordEncoder.matches(senha, senhaHash);
        }
        // Migração transparente de registros legados armazenados em texto puro.
        return senhaHash.equals(senha);
    }

    public boolean possuiHashBCrypt() {
        return senhaHash != null && (senhaHash.startsWith("$2a$") || senhaHash.startsWith("$2b$") || senhaHash.startsWith("$2y$"));
    }

    public void atualizarSenhaHash(String novoHash) {
        this.senhaHash = novoHash;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
