package com.ifsc.prosigaa.service;

import com.ifsc.prosigaa.model.*;
import com.ifsc.prosigaa.repository.*;
import org.springframework.stereotype.Service;

@Service
public class CurtidaService {
    private final CurtidaPostagemRepository curtidaPostagemRepository;
    private final CurtidaComentarioRepository curtidaComentarioRepository;
    private final UsuarioService usuarioService;
    private final PostagemService postagemService;
    private final ComentarioRepository comentarioRepository;

    public CurtidaService(CurtidaPostagemRepository curtidaPostagemRepository,
                          CurtidaComentarioRepository curtidaComentarioRepository,
                          UsuarioService usuarioService,
                          PostagemService postagemService,
                          ComentarioRepository comentarioRepository) {
        this.curtidaPostagemRepository = curtidaPostagemRepository;
        this.curtidaComentarioRepository = curtidaComentarioRepository;
        this.usuarioService = usuarioService;
        this.postagemService = postagemService;
        this.comentarioRepository = comentarioRepository;
    }

    public void curtirPostagem(Long postagemId, Long usuarioId) {
        if (curtidaPostagemRepository.existsByUsuarioIdAndPostagemId(usuarioId, postagemId)) return;
        Usuario usuario = usuarioService.buscar(usuarioId);
        Postagem postagem = postagemService.buscar(postagemId);
        curtidaPostagemRepository.save(new CurtidaPostagem(usuario, postagem));
    }

    public void curtirComentario(Long comentarioId, Long usuarioId) {
        if (curtidaComentarioRepository.existsByUsuarioIdAndComentarioId(usuarioId, comentarioId)) return;
        Usuario usuario = usuarioService.buscar(usuarioId);
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new IllegalArgumentException("Comentário não encontrado."));
        curtidaComentarioRepository.save(new CurtidaComentario(usuario, comentario));
    }
}
