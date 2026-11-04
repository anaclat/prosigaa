package com.ifsc.prosigaa.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class CurtidaComentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime criadoEm;

    @ManyToOne(optional = false)
    private Usuario usuario;

    @ManyToOne(optional = false)
    private Comentario comentario;

    protected CurtidaComentario() {
    }

    public CurtidaComentario(Usuario usuario, Comentario comentario) {
        this.usuario = usuario;
        this.comentario = comentario;
        this.criadoEm = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Comentario getComentario() {
        return comentario;
    }
}
