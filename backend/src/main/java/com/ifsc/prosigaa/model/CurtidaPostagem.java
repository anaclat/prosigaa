package com.ifsc.prosigaa.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class CurtidaPostagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime criadoEm;

    @ManyToOne(optional = false)
    private Usuario usuario;

    @ManyToOne(optional = false)
    private Postagem postagem;

    protected CurtidaPostagem() {}

    public CurtidaPostagem(Usuario usuario, Postagem postagem) {
        this.usuario = usuario;
        this.postagem = postagem;
        this.criadoEm = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public Usuario getUsuario() { return usuario; }
    public Postagem getPostagem() { return postagem; }
}
