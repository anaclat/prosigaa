package com.ifsc.prosigaa.model;

import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
public class Fase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private int numero;

    @ManyToOne
    private ArrayList<Materia> materias;

    @ManyToOne
    private ArrayList<Postagem> postagems;

    // Required by JPA/Hibernate specification
    protected Fase() {
    }

    public Fase(String nome, int numero) {
        this.nome = nome;
        this.numero = numero;
    }

    public Fase(Long id, int numero, String nome) {
        this.id = id;
        this.numero = numero;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getNumero() {
        return numero;
    }
}
