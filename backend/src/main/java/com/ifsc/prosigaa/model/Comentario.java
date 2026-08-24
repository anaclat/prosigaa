package com.ifsc.prosigaa.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String conteudo;
    private LocalDateTime criadoEm;

    @ManyToOne
    private ArrayList<Postagem> postagems;

    @ManyToOne
    private ArrayList<CurtidaComentario> curtidaComentarios;

    public Comentario() {
    }
}
