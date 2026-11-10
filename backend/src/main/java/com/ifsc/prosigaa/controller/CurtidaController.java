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

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/curtidas")
@Tag(name = "Curtidas")
// Curtidas modelam relações entre usuários e conteúdo; estas rotas oferecem criar/remover.
public class CurtidaController {
    private final CurtidaService service;

    public CurtidaController(CurtidaService service) {
        this.service = service;
    }

    @PostMapping("/postagens/{postagemId}")
    @ResponseStatus(HttpStatus.CREATED)
    // POST registra a relação de curtida. Repetir POST não é garantido idempotente pelo HTTP;
    // a regra de duplicidade, se necessária, pertence ao serviço/domínio. 201 confirma criação.
    @Operation(summary = "Curtir postagem", description = "O usuário autor da ação é informado no corpo JSON.")
    @ApiResponse(responseCode = "201", description = "Curtida registrada")
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "404", description = "Postagem ou usuário inexistente", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    public Map<String, String> curtirPostagem(@PathVariable Long postagemId, @RequestBody CurtidaDTO dto) {
        service.curtirPostagem(postagemId, dto.usuarioId());
        Map<String, String> resposta = new LinkedHashMap<>();
        resposta.put("mensagem", "Postagem curtida.");
        return resposta;
    }

    @PostMapping("/comentarios/{comentarioId}")
    @ResponseStatus(HttpStatus.CREATED)
    // Mesmo padrão para curtida de comentário: POST cria uma relação e pode alterar o estado.
    @Operation(summary = "Curtir comentário", description = "Registra uma curtida do usuário indicado no corpo JSON.")
    @ApiResponse(responseCode = "201", description = "Curtida registrada")
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "404", description = "Comentário ou usuário inexistente", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    public Map<String, String> curtirComentario(@PathVariable Long comentarioId, @RequestBody CurtidaDTO dto) {
        service.curtirComentario(comentarioId, dto.usuarioId());
        Map<String, String> resposta = new LinkedHashMap<>();
        resposta.put("mensagem", "Comentário curtido.");
        return resposta;
    }

    @DeleteMapping("/postagens/{postagemId}")
    // DELETE remove a relação de curtida; repetir tende ao mesmo estado final (sem curtida).
    // Este contrato responde 200 com uma mensagem; DELETE também poderia usar 204 sem corpo.
    @Operation(summary = "Remover curtida de postagem", description = "Remove a curtida do usuário indicado no corpo JSON.")
    @ApiResponse(responseCode = "200", description = "Curtida removida")
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "404", description = "Postagem ou usuário inexistente", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    public Map<String, String> descurtirPostagem(@PathVariable Long postagemId, @RequestBody CurtidaDTO dto) {
        service.descurtirPostagem(postagemId, dto.usuarioId());
        Map<String, String> resposta = new LinkedHashMap<>();
        resposta.put("mensagem", "Curtida removida.");
        return resposta;
    }

    @DeleteMapping("/comentarios/{comentarioId}")
    // Remoção de curtida do comentário: DELETE é idempotente quanto ao estado final.
    @Operation(summary = "Remover curtida de comentário", description = "Remove a curtida do usuário indicado no corpo JSON.")
    @ApiResponse(responseCode = "200", description = "Curtida removida")
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "404", description = "Comentário ou usuário inexistente", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    public Map<String, String> descurtirComentario(@PathVariable Long comentarioId, @RequestBody CurtidaDTO dto) {
        service.descurtirComentario(comentarioId, dto.usuarioId());
        Map<String, String> resposta = new LinkedHashMap<>();
        resposta.put("mensagem", "Curtida removida.");
        return resposta;
    }

    @DeleteMapping("/postagens/{postagemId}/usuarios/{usuarioId}")
    // Variante que transporta os dois identificadores na rota; também usa DELETE para remoção.
    @Operation(summary = "Remover curtida de postagem", description = "Remove a curtida identificada pela postagem e pelo usuário na rota.")
    @ApiResponse(responseCode = "200", description = "Curtida removida")
    @ApiResponse(responseCode = "400", description = "Identificador inválido", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "404", description = "Postagem ou usuário inexistente", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    public Map<String, String> descurtirPostagem(
            @PathVariable Long postagemId,
            @PathVariable Long usuarioId
    ) {
        service.descurtirPostagem(postagemId, usuarioId);
        Map<String, String> resposta = new LinkedHashMap<>();
        resposta.put("mensagem", "Curtida removida.");
        return resposta;
    }

    @DeleteMapping("/comentarios/{comentarioId}/usuarios/{usuarioId}")
    // Outra representação da mesma remoção; manter rotas equivalentes aumenta a superfície da API.
    @Operation(summary = "Remover curtida de comentário", description = "Remove a curtida identificada pelo comentário e pelo usuário na rota.")
    @ApiResponse(responseCode = "200", description = "Curtida removida")
    @ApiResponse(responseCode = "400", description = "Identificador inválido", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "404", description = "Comentário ou usuário inexistente", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    public Map<String, String> descurtirComentario(
            @PathVariable Long comentarioId,
            @PathVariable Long usuarioId
    ) {
        service.descurtirComentario(comentarioId, usuarioId);
        Map<String, String> resposta = new LinkedHashMap<>();
        resposta.put("mensagem", "Curtida removida.");
        return resposta;
    }
}
