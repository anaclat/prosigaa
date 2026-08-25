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

    public ComentarioService(ComentarioRepository comentarioRepository,
                             UsuarioService usuarioService,
                             PostagemService postagemService) {
        this.comentarioRepository = comentarioRepository;
        this.usuarioService = usuarioService;
        this.postagemService = postagemService;
    }

    public Comentario cadastrar(Long postagemId, CadastroComentarioDTO dto) {
        if (dto.conteudo() == null || dto.conteudo().isBlank()) {
            throw new IllegalArgumentException("Comentário não pode ser vazio.");
        }

        Usuario usuario = usuarioService.buscar(dto.usuarioId());
        Postagem postagem = postagemService.buscar(postagemId);

        Comentario comentario = new Comentario(dto.conteudo(), usuario, postagem);
        postagem.adicionarComentario(comentario);
        return comentarioRepository.save(comentario);
    }
}
