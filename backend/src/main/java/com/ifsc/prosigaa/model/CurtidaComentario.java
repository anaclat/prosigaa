package com.ifsc.prosigaa.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class CurtidaComentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime criadoEm;

    @OneToMany
    private Usuario usuario;

    @OneToMany
    private Comentario comentario;

    public CurtidaComentario() {
    }
}
