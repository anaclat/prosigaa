package com.ifsc.prosigaa.service;

import com.ifsc.prosigaa.model.*;
import com.ifsc.prosigaa.repository.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import com.ifsc.prosigaa.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CurtidaService {
    private final CurtidaPostagemRepository curtidaPostagemRepository;
    private final CurtidaComentarioRepository curtidaComentarioRepository;
    private final UsuarioService usuarioService;
    private final PostagemService postagemService;
    private final ComentarioRepository comentarioRepository;
    private final ApplicationEventPublisher eventPublisher;

    public record CurtidaPostagemEvent(Long autorPostagemId, Long postagemId, String nomeUsuario) {
    }

    public record CurtidaComentarioEvent(Long autorComentarioId, Long comentarioId, String nomeUsuario) {
    }

    public CurtidaService(CurtidaPostagemRepository curtidaPostagemRepository,
                          CurtidaComentarioRepository curtidaComentarioRepository,
                          UsuarioService usuarioService,
                          PostagemService postagemService,
                          ComentarioRepository comentarioRepository,
                          ApplicationEventPublisher eventPublisher) {
        this.curtidaPostagemRepository = curtidaPostagemRepository;
        this.curtidaComentarioRepository = curtidaComentarioRepository;
        this.usuarioService = usuarioService;
        this.postagemService = postagemService;
        this.comentarioRepository = comentarioRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void curtirPostagem(Long postagemId, Long usuarioId) {
        if (curtidaPostagemRepository.existsByUsuarioIdAndPostagemId(usuarioId, postagemId)) return;
        Usuario usuario = usuarioService.buscar(usuarioId);
        Postagem postagem = postagemService.buscar(postagemId);
        curtidaPostagemRepository.save(new CurtidaPostagem(usuario, postagem));

        if (!postagem.getUsuario().getId().equals(usuario.getId())) {
            eventPublisher.publishEvent(new CurtidaPostagemEvent(
                    postagem.getUsuario().getId(),
                    postagem.getId(),
                    usuario.getNome()
            ));
        }
    }

    public List<Long> listarPostagensCurtidas(Long usuarioId) {
        usuarioService.buscar(usuarioId);
        return curtidaPostagemRepository.findPostagemIdsByUsuarioId(usuarioId);
    }

    @Transactional
    public void curtirComentario(Long comentarioId, Long usuarioId) {
        if (curtidaComentarioRepository.existsByUsuarioIdAndComentarioId(usuarioId, comentarioId)) return;
        Usuario usuario = usuarioService.buscar(usuarioId);
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Comentário não encontrado."));
        curtidaComentarioRepository.save(new CurtidaComentario(usuario, comentario));

        if (!comentario.getUsuario().getId().equals(usuario.getId())) {
            eventPublisher.publishEvent(new CurtidaComentarioEvent(
                    comentario.getUsuario().getId(),
                    comentario.getId(),
                    usuario.getNome()
            ));
        }
    }

    @Transactional
    public void descurtirPostagem(Long postagemId, Long usuarioId) {
        postagemService.buscar(postagemId);
        usuarioService.buscar(usuarioId);
        curtidaPostagemRepository.deleteByUsuarioIdAndPostagemId(usuarioId, postagemId);
    }

    @Transactional
    public void descurtirComentario(Long comentarioId, Long usuarioId) {
        comentarioRepository.findById(comentarioId).orElseThrow(() -> new ResourceNotFoundException("Comentário não encontrado."));
        usuarioService.buscar(usuarioId);
        curtidaComentarioRepository.deleteByUsuarioIdAndComentarioId(usuarioId, comentarioId);
    }

}
