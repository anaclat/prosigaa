package com.ifsc.prosigaa.service;

import com.ifsc.prosigaa.dto.CadastroComentarioDTO;
import com.ifsc.prosigaa.model.Comentario;
import com.ifsc.prosigaa.model.Postagem;
import com.ifsc.prosigaa.model.Usuario;
import com.ifsc.prosigaa.repository.ComentarioRepository;
import org.springframework.stereotype.Service;

@Service
public class ComentarioService {
    private final ComentarioRepository comentarioRepository;
    private final UsuarioService usuarioService;
    private final PostagemService postagemService;
    private final NotificacaoService notificacaoService;

    public ComentarioService(ComentarioRepository comentarioRepository,
                             UsuarioService usuarioService,
                             PostagemService postagemService,
                             NotificacaoService notificacaoService) {
        this.comentarioRepository = comentarioRepository;
        this.usuarioService = usuarioService;
        this.postagemService = postagemService;
        this.notificacaoService = notificacaoService;
    }

    public Comentario cadastrar(Long postagemId, CadastroComentarioDTO dto) {
        if (dto.conteudo() == null || dto.conteudo().isBlank()) {
            throw new IllegalArgumentException("Comentário não pode ser vazio.");
        }

        Usuario usuario = usuarioService.buscar(dto.usuarioId());
        Postagem postagem = postagemService.buscar(postagemId);

        Comentario comentario = new Comentario(dto.conteudo(), usuario, postagem);
        postagem.adicionarComentario(comentario);
        comentarioRepository.save(comentario);

        if (!postagem.getUsuario().getId().equals(usuario.getId())) {
            notificacaoService.notificarNovoComentario(
                    postagem.getUsuario().getId(),
                    postagem.getId(),
                    usuario.getNome()
            );
        }

        return comentario;
    }

    public void excluir(Long comentarioId) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new com.ifsc.prosigaa.exception.ResourceNotFoundException("Comentário não encontrado."));
        comentarioRepository.delete(comentario);
    }

}
