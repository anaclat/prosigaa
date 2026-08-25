package com.ifsc.prosigaa.controller;

import com.ifsc.prosigaa.dto.CadastroComentarioDTO;
import com.ifsc.prosigaa.dto.CadastroPostagemDTO;
import com.ifsc.prosigaa.dto.PostagemDTO;
import com.ifsc.prosigaa.model.Comentario;
import com.ifsc.prosigaa.service.ComentarioService;
import com.ifsc.prosigaa.service.PostagemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/postagem")
public class PostagemController {
    private final PostagemService postagemService;
    private final ComentarioService comentarioService;

    public PostagemController(PostagemService postagemService, ComentarioService comentarioService) {
        this.postagemService = postagemService;
        this.comentarioService = comentarioService;
    }

    @GetMapping
    public List<PostagemDTO> listar(
            @RequestParam(required = false) Long faseId,
            @RequestParam(required = false) Long materiaId) {
        return postagemService.listar(faseId, materiaId);
    }

    @GetMapping("/{id}")
    public PostagemDTO buscar(@PathVariable Long id) {
        return postagemService.toDTO(postagemService.buscar(id));
    }

    @PostMapping
    public PostagemDTO cadastrar(@RequestBody CadastroPostagemDTO dto) {
        return postagemService.cadastrar(dto);
    }

    @PostMapping("/{id}/comentario")
    public PostagemDTO comentar(@PathVariable Long id, @RequestBody CadastroComentarioDTO dto) {
        comentarioService.cadastrar(id, dto);
        return postagemService.toDTO(postagemService.buscar(id));
    }
}
