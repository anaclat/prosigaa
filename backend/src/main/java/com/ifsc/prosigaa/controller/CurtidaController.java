package com.ifsc.prosigaa.controller;

import com.ifsc.prosigaa.dto.ApiErrorDTO;
import com.ifsc.prosigaa.dto.CurtidaDTO;
import com.ifsc.prosigaa.service.CurtidaService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/curtidas")
@Tag(name = "Curtidas")
public class CurtidaController {
    private final CurtidaService service;

    public CurtidaController(CurtidaService service) {
        this.service = service;
    }

    @PostMapping("/postagens/{postagemId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Curtir postagem", description = "O usuário autor da ação é informado no corpo JSON.")
    @ApiResponse(responseCode = "201", description = "Curtida registrada")
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "404", description = "Postagem ou usuário inexistente", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    public Map<String, String> curtirPostagem(@PathVariable Long postagemId, @RequestBody CurtidaDTO dto) {
        service.curtirPostagem(postagemId, dto.usuarioId());
        return Map.of("mensagem", "Postagem curtida.");
    }

    @PostMapping("/comentarios/{comentarioId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Curtir comentário", description = "Registra uma curtida do usuário indicado no corpo JSON.")
    @ApiResponse(responseCode = "201", description = "Curtida registrada")
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "404", description = "Comentário ou usuário inexistente", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    public Map<String, String> curtirComentario(@PathVariable Long comentarioId, @RequestBody CurtidaDTO dto) {
        service.curtirComentario(comentarioId, dto.usuarioId());
        return Map.of("mensagem", "Comentário curtido.");
    }

    @DeleteMapping("/postagens/{postagemId}")
    @Operation(summary = "Remover curtida de postagem", description = "Remove a curtida do usuário indicado no corpo JSON.")
    @ApiResponse(responseCode = "200", description = "Curtida removida")
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "404", description = "Postagem ou usuário inexistente", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    public Map<String, String> descurtirPostagem(@PathVariable Long postagemId, @RequestBody CurtidaDTO dto) {
        service.descurtirPostagem(postagemId, dto.usuarioId());
        return Map.of("mensagem", "Curtida removida.");
    }

    @DeleteMapping("/comentarios/{comentarioId}")
    @Operation(summary = "Remover curtida de comentário", description = "Remove a curtida do usuário indicado no corpo JSON.")
    @ApiResponse(responseCode = "200", description = "Curtida removida")
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "404", description = "Comentário ou usuário inexistente", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    public Map<String, String> descurtirComentario(@PathVariable Long comentarioId, @RequestBody CurtidaDTO dto) {
        service.descurtirComentario(comentarioId, dto.usuarioId());
        return Map.of("mensagem", "Curtida removida.");
    }
}
