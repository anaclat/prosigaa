package com.ifsc.prosigaa.controller;

import com.ifsc.prosigaa.service.CurtidaService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/curtida")
public class CurtidaController {
    private final CurtidaService curtidaService;

    public CurtidaController(CurtidaService curtidaService) {
        this.curtidaService = curtidaService;
    }

    @PostMapping("/postagem/{postagemId}")
    public Map<String, String> curtirPostagem(
            @PathVariable Long postagemId,
            @RequestParam Long usuarioId) {
        curtidaService.curtirPostagem(postagemId, usuarioId);
        return Map.of("mensagem", "Postagem curtida.");
    }

    @PostMapping("/comentario/{comentarioId}")
    public Map<String, String> curtirComentario(
            @PathVariable Long comentarioId,
            @RequestParam Long usuarioId) {
        curtidaService.curtirComentario(comentarioId, usuarioId);
        return Map.of("mensagem", "Comentário curtido.");
    }
}
