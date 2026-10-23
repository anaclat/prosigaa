package com.ifsc.prosigaa.service;

import com.ifsc.prosigaa.dto.*;
import com.ifsc.prosigaa.enums.TipoPostagem;
import com.ifsc.prosigaa.model.*;
import com.ifsc.prosigaa.repository.PostagemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostagemService {
    private final PostagemRepository postagemRepository;
    private final UsuarioService usuarioService;
    private final FaseService faseService;
    private final MateriaService materiaService;

    public PostagemService(PostagemRepository postagemRepository,
                           UsuarioService usuarioService,
                           FaseService faseService,
                           MateriaService materiaService) {
        this.postagemRepository = postagemRepository;
        this.usuarioService = usuarioService;
        this.faseService = faseService;
        this.materiaService = materiaService;
    }

    public PostagemDTO cadastrar(CadastroPostagemDTO dto) {
        validar(dto);

        Usuario usuario = usuarioService.buscar(dto.usuarioId());
        Fase fase = faseService.buscar(dto.faseId());
        Materia materia = materiaService.buscar(dto.materiaId());

        Postagem postagem = new Postagem(
                dto.titulo(), dto.conteudo(), dto.urlImagem(),
                dto.tipo(), usuario, fase, materia
        );

        return toDTO(postagemRepository.save(postagem));
    }

    public List<PostagemDTO> listar(Long faseId, Long materiaId) {
        List<Postagem> postagens;

        if (faseId != null && materiaId != null) {
            postagens = postagemRepository.findByFaseIdAndMateriaIdOrderByCriadoEmDesc(faseId, materiaId);
        } else if (faseId != null) {
            postagens = postagemRepository.findByFaseIdOrderByCriadoEmDesc(faseId);
        } else if (materiaId != null) {
            postagens = postagemRepository.findByMateriaIdOrderByCriadoEmDesc(materiaId);
        } else {
            postagens = postagemRepository.findAllByOrderByCriadoEmDesc();
        }

        return postagens.stream().map(this::toDTO).toList();
    }

    public Postagem buscar(Long id) {
        return postagemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Postagem não encontrada."));
    }

    public PostagemDTO toDTO(Postagem p) {
        FaseDTO fase = new FaseDTO(p.getFase().getId(), p.getFase().getNome(), p.getFase().getNumero(), List.of());
        MateriaDTO materia = new MateriaDTO(p.getMateria().getId(), p.getMateria().getNome(), p.getMateria().getCodigo());
        UsuarioResumoDTO usuario = new UsuarioResumoDTO(
                p.getUsuario().getId(), p.getUsuario().getNome(), p.getUsuario().getTipo()
        );

        List<ComentarioDTO> comentarios = p.getComentarios().stream()
                .map(c -> new ComentarioDTO(
                        c.getId(), c.getConteudo(), c.getCriadoEm(),
                        new UsuarioResumoDTO(c.getUsuario().getId(), c.getUsuario().getNome(), c.getUsuario().getTipo()),
                        c.getCurtidas().size()
                ))
                .toList();

        return new PostagemDTO(
                p.getId(), p.getTitulo(), p.getConteudo(), p.getUrlImagem(),
                p.getTipo(), p.getCriadoEm(), usuario, fase, materia,
                comentarios, p.getCurtidas().size()
        );
    }

    private void validar(CadastroPostagemDTO dto) {
        if (dto.titulo() == null || dto.titulo().isBlank()) {
            throw new IllegalArgumentException("Título é obrigatório.");
        }
        if (dto.conteudo() == null || dto.conteudo().isBlank()) {
            throw new IllegalArgumentException("Conteúdo é obrigatório.");
        }
        if (dto.tipo() == null) {
            throw new IllegalArgumentException("Tipo da postagem é obrigatório.");
        }
        if (dto.usuarioId() == null || dto.faseId() == null || dto.materiaId() == null) {
            throw new IllegalArgumentException("Usuário, fase e matéria são obrigatórios.");
        }
    }
}
