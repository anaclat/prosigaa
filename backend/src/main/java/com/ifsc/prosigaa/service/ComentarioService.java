package com.ifsc.prosigaa.service;

import com.ifsc.prosigaa.dto.CadastroComentarioDTO;
import com.ifsc.prosigaa.model.Comentario;
import com.ifsc.prosigaa.model.Postagem;
import com.ifsc.prosigaa.model.Usuario;
import com.ifsc.prosigaa.repository.ComentarioRepository;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ComentarioService {
    private final ComentarioRepository comentarioRepository;
    private final UsuarioService usuarioService;
    private final PostagemService postagemService;
    private final ApplicationEventPublisher eventPublisher;

    public ComentarioService(ComentarioRepository comentarioRepository,
            UsuarioService usuarioService,
            PostagemService postagemService,
            ApplicationEventPublisher eventPublisher) {
        this.comentarioRepository = comentarioRepository;
        this.usuarioService = usuarioService;
        this.postagemService = postagemService;
        this.eventPublisher = eventPublisher;
    }

    public record NovoComentarioEvent(Long autorPostagemId, Long postagemId, String nomeAutorComentario) {
    }

    @Transactional
    public Comentario cadastrar(Long postagemId, CadastroComentarioDTO dto) {
        Usuario usuario = usuarioService.buscar(dto.usuarioId());
        Postagem postagem = postagemService.buscar(postagemId);

        Comentario comentario = new Comentario(dto.conteudo(), usuario, postagem);

        postagem.adicionarComentario(comentario);

        comentarioRepository.save(comentario);

        if (!postagem.getUsuario().getId().equals(usuario.getId())) {
            // Apenas publica o evento; não faz I/O de rede aqui
            eventPublisher.publishEvent(new NovoComentarioEvent(
                    postagem.getUsuario().getId(), postagem.getId(), usuario.getNome()));
        }
        return comentario;
    }

    public void excluir(Long comentarioId, Long usuarioId) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("Informe o usuário que está excluindo o comentário.");
        }

        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(
                        () -> new com.ifsc.prosigaa.exception.ResourceNotFoundException("Comentário não encontrado."));

        if (!comentario.getUsuario().getId().equals(usuarioId)) {
            throw new IllegalArgumentException("Você só pode excluir seus próprios comentários.");
        }

        comentarioRepository.delete(comentario);
    }

}
