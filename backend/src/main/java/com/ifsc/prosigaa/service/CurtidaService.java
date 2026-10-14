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
    private final NotificacaoService notificacaoService;

    public CurtidaService(CurtidaPostagemRepository curtidaPostagemRepository,
                          CurtidaComentarioRepository curtidaComentarioRepository,
                          UsuarioService usuarioService,
                          PostagemService postagemService,
                          ComentarioRepository comentarioRepository,
                          NotificacaoService notificacaoService) {
        this.curtidaPostagemRepository = curtidaPostagemRepository;
        this.curtidaComentarioRepository = curtidaComentarioRepository;
        this.usuarioService = usuarioService;
        this.postagemService = postagemService;
        this.comentarioRepository = comentarioRepository;
        this.notificacaoService = notificacaoService;
    }

    public void curtirPostagem(Long postagemId, Long usuarioId) {
        if (curtidaPostagemRepository.existsByUsuarioIdAndPostagemId(usuarioId, postagemId)) return;
        Usuario usuario = usuarioService.buscar(usuarioId);
        Postagem postagem = postagemService.buscar(postagemId);
        curtidaPostagemRepository.save(new CurtidaPostagem(usuario, postagem));

        if (!postagem.getUsuario().getId().equals(usuario.getId())) {
            notificacaoService.notificarCurtidaPostagem(
                    postagem.getUsuario().getId(),
                    postagem.getId(),
                    usuario.getNome()
            );
        }
    }

    public void curtirComentario(Long comentarioId, Long usuarioId) {
        if (curtidaComentarioRepository.existsByUsuarioIdAndComentarioId(usuarioId, comentarioId)) return;
        Usuario usuario = usuarioService.buscar(usuarioId);
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new IllegalArgumentException("Comentário não encontrado."));
        curtidaComentarioRepository.save(new CurtidaComentario(usuario, comentario));

        if (!comentario.getUsuario().getId().equals(usuario.getId())) {
            notificacaoService.notificarCurtidaComentario(
                    comentario.getUsuario().getId(),
                    comentario.getId(),
                    usuario.getNome()
            );
        }
    }
}
