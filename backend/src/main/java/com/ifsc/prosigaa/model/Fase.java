package com.ifsc.prosigaa.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Fase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private int numero;

    @ManyToMany
    @JoinTable(
        name = "fase_materia",
        joinColumns = @JoinColumn(name = "fase_id"),
        inverseJoinColumns = @JoinColumn(name = "materia_id")
    )
    private List<Materia> materias = new ArrayList<>();

    @OneToMany(mappedBy = "fase")
    private List<Postagem> postagens = new ArrayList<>();

    protected Fase() {}

    public Fase(String nome, int numero) {
        this.nome = nome;
        this.numero = numero;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public int getNumero() { return numero; }
    public List<Materia> getMaterias() { return materias; }
}
