package com.ifsc.prosigaa.model;

import jakarta.persistence.*;

@Entity
public class AnexoPostagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeOriginal;

    @Column(nullable = false, unique = true)
    private String chaveArquivo;

    @Column(nullable = false)
    private String tipoConteudo;

    @Column(nullable = false)
    private long tamanhoBytes;

    @ManyToOne(optional = false)
    private Postagem postagem;

    protected AnexoPostagem() {
    }

    public AnexoPostagem(String nomeOriginal, String chaveArquivo, String tipoConteudo,
                         long tamanhoBytes, Postagem postagem) {
        this.nomeOriginal = nomeOriginal;
        this.chaveArquivo = chaveArquivo;
        this.tipoConteudo = tipoConteudo;
        this.tamanhoBytes = tamanhoBytes;
        this.postagem = postagem;
    }

    public Long getId() {
        return id;
    }

    public String getNomeOriginal() {
        return nomeOriginal;
    }

    public String getChaveArquivo() {
        return chaveArquivo;
    }

    public String getTipoConteudo() {
        return tipoConteudo;
    }

    public long getTamanhoBytes() {
        return tamanhoBytes;
    }

    public Postagem getPostagem() {
        return postagem;
    }
}
