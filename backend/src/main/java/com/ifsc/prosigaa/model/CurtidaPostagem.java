package com.ifsc.prosigaa.model;

import jakarta.persistence.*;

@Entity
public class CurtidaPostagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private Usuario usuario;

    @OneToMany
    private Postagem postagem;
}
