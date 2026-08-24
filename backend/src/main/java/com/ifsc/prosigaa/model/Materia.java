package com.ifsc.prosigaa.model;

import jakarta.persistence.*;

@Entity
public class Materia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String codigo;

    @OneToMany
    private Fase fase;

    protected Materia() {
    }
}
