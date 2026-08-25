package com.ifsc.prosigaa.model;

import com.ifsc.prosigaa.enums.TipoPostagem;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Postagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String conteudo;

    private String urlImagem;

    @Enumerated(EnumType.STRING)
    private TipoPostagem tipo;

    private LocalDateTime criadoEm;

    @ManyToOne(optional = false)
    private Usuario usuario;

    @ManyToOne(optional = false)
    private Fase fase;

    @ManyToOne(optional = false)
    private Materia materia;

    @OneToMany(mappedBy = "postagem", cascade = CascadeType.ALL)
    private List<Comentario> comentarios = new ArrayList<>();

    @OneToMany(mappedBy = "postagem", cascade = CascadeType.ALL)
    private List<CurtidaPostagem> curtidas = new ArrayList<>();

    protected Postagem() {}

    public Postagem(String titulo, String conteudo, String urlImagem, TipoPostagem tipo,
                    Usuario usuario, Fase fase, Materia materia) {
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.urlImagem = urlImagem;
        this.tipo = tipo;
        this.usuario = usuario;
        this.fase = fase;
        this.materia = materia;
        this.criadoEm = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getConteudo() { return conteudo; }
    public String getUrlImagem() { return urlImagem; }
    public TipoPostagem getTipo() { return tipo; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public Usuario getUsuario() { return usuario; }
    public Fase getFase() { return fase; }
    public Materia getMateria() { return materia; }
    public List<Comentario> getComentarios() { return comentarios; }
    public List<CurtidaPostagem> getCurtidas() { return curtidas; }

    public void adicionarComentario(Comentario comentario) {
        comentarios.add(comentario);
    }
}
