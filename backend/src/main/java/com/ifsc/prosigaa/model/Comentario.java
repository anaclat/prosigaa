package com.ifsc.prosigaa.model;

import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String conteudo;

    private LocalDateTime criadoEm;

    @ManyToOne(optional = false)
    private Usuario usuario;

    @ManyToOne(optional = false)
    private Postagem postagem;

    @BatchSize(size = 50)
    @OneToMany(mappedBy = "comentario", cascade = CascadeType.ALL)
    private List<CurtidaComentario> curtidas = new ArrayList<>();

    protected Comentario() {
    }

    public Comentario(String conteudo, Usuario usuario, Postagem postagem) {
        this.conteudo = conteudo;
        this.usuario = usuario;
        this.postagem = postagem;
        this.criadoEm = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getConteudo() {
        return conteudo;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Postagem getPostagem() {
        return postagem;
    }

    public List<CurtidaComentario> getCurtidas() {
        return curtidas;
    }
}
