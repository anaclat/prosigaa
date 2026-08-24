package com.ifsc.prosigaa.model;

import com.ifsc.prosigaa.enums.TipoUsuario;
import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senhaHash;
    private TipoUsuario tipoUsuario;

    @ManyToOne
    private ArrayList<CurtidaComentario> curtidaComentarios;

    @ManyToOne
    private ArrayList<CurtidaPostagem> curtidaPostagems;

    @ManyToOne
    private ArrayList<Comentario> comentarios;

    @ManyToOne
    private ArrayList<Postagem> postagems;

    protected Usuario() {
    }
}
