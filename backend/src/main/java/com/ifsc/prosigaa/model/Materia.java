package com.ifsc.prosigaa.model;

import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Materia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String codigo;

    @ManyToMany(mappedBy = "materias")
    private List<Fase> fases = new ArrayList<>();

    @BatchSize(size = 50)
    @OneToMany(mappedBy = "materia")
    private List<Postagem> postagens = new ArrayList<>();

    protected Materia() {
    }

    public Materia(String nome, String codigo) {
        this.nome = nome;
        this.codigo = codigo;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public List<Fase> getFases() {
        return fases;
    }
}
