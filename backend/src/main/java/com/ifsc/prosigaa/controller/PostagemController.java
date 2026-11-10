package com.ifsc.prosigaa.controller;

import com.ifsc.prosigaa.dto.*;
import com.ifsc.prosigaa.service.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/postagens")
@Tag(name = "Postagens", description = "Publicações e comentários acadêmicos")
// Postagens são recursos; comentários são ações de criação associadas a uma postagem.
public class PostagemController {
    private final PostagemService postagemService;
    private final ComentarioService comentarioService;

    public PostagemController(PostagemService p, ComentarioService c) {
        postagemService = p;
        comentarioService = c;
    }

    @GetMapping
    // GET de coleção: seguro e idempotente; filtros não alteram postagens.
    // Os filtros tornam o contexto explícito em cada requisição, sem sessão HTTP.
    @Operation(summary = "Listar postagens", description = "Permite filtrar por faseId e/ou materiaId.")
    @ApiResponse(responseCode = "200", description = "Postagens encontradas")
    @ApiResponse(responseCode = "400", description = "Identificador de filtro inválido", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    public List<PostagemDTO> listar(@RequestParam(required = false) Long faseId, @RequestParam(required = false) Long materiaId) {
        return postagemService.listar(faseId, materiaId);
    }

    @GetMapping("/{id}")
    // GET de um recurso identificado: repetir a consulta não muda seu estado.
    // A resposta é JSON simples, sem links navegáveis HATEOAS.
    @Operation(summary = "Consultar postagem", description = "Busca uma postagem pelo identificador.")
    @ApiResponse(responseCode = "200", description = "Postagem encontrada")
    @ApiResponse(responseCode = "400", description = "Identificador inválido", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "404", description = "Postagem não encontrada", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    public PostagemDTO buscar(@PathVariable Long id) {
        return postagemService.toDTO(postagemService.buscar(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    // POST cria uma postagem; repetir a mesma requisição pode gerar outra postagem.
    // O corpo traz autor e relações necessárias, já que o servidor não usa sessão de usuário.
    @Operation(summary = "Criar postagem", description = "Publica conteúdo associado a um usuário, fase e matéria.")
    @ApiResponse(responseCode = "201", description = "Postagem criada")
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "404", description = "Usuário, fase ou matéria não encontrado", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    public PostagemDTO cadastrar(@RequestBody CadastroPostagemDTO dto) {
        return postagemService.cadastrar(dto);
    }

    @PostMapping({"/{id}/comentarios", "/{id}/comentario"})
    @ResponseStatus(HttpStatus.CREATED)
    // POST cria um comentário vinculado ao recurso postagem; não é idempotente por padrão.
    // 201 informa a criação e a representação retornada mostra a postagem atualizada.
    @Operation(summary = "Comentar uma postagem", description = "Adiciona um comentário à postagem indicada.")
    @ApiResponse(responseCode = "201", description = "Comentário criado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "404", description = "Postagem ou usuário não encontrado", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    public PostagemDTO comentar(@PathVariable Long id, @RequestBody CadastroComentarioDTO dto) {
        comentarioService.cadastrar(id, dto);
        return postagemService.toDTO(postagemService.buscar(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // DELETE remove o recurso. É idempotente quanto ao estado final: removê-lo de novo não o restaura.
    // 204 No Content indica sucesso sem corpo de resposta.
    @Operation(summary = "Excluir postagem")
    @ApiResponse(responseCode = "204", description = "Postagem excluída")
    @ApiResponse(responseCode = "400", description = "Identificador inválido", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "404", description = "Postagem não encontrada", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    public void excluir(@PathVariable Long id, @RequestParam Long usuarioId) {
        postagemService.excluir(id, usuarioId);
    }

    @DeleteMapping("/comentarios/{comentarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // DELETE também é apropriado para remover um comentário e converge para o estado "ausente".
    // usuarioId é enviado explicitamente porque esta API não mantém sessão; esse ID, sozinho, não é prova de identidade.
    @Operation(summary = "Excluir comentário")
    @ApiResponse(responseCode = "204", description = "Comentário excluído")
    @ApiResponse(responseCode = "400", description = "Identificador inválido", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "404", description = "Comentário não encontrado", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    public void excluirComentario(@PathVariable Long comentarioId, @RequestParam Long usuarioId) {
        comentarioService.excluir(comentarioId, usuarioId);
    }
}
