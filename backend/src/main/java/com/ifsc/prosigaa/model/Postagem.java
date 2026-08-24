package com.ifsc.prosigaa.model;

import com.ifsc.prosigaa.enums.TipoPostagem;
import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
public class Postagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private TipoPostagem tipoPostagem;

    @OneToMany
    private Materia materia;

    @OneToMany
    private Usuario usuario;

    @ManyToOne
    private ArrayList<Comentario> comentarios;

    @ManyToOne
    private ArrayList<CurtidaPostagem> curtidaPostagems;

    protected Postagem() {
    }
}
