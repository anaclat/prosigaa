package com.ifsc.prosigaa.controller;

import com.ifsc.prosigaa.dto.*;
import com.ifsc.prosigaa.model.Materia;
import com.ifsc.prosigaa.service.MateriaService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materias")
@Tag(name = "Matérias")
// O caminho nomeia a coleção de matérias; o ID da fase é um filtro/contexto da relação.
public class MateriaController {
    private final MateriaService service;

    public MateriaController(MateriaService s) {
        service = s;
    }

    @GetMapping
    // GET é seguro e idempotente; faseId apenas filtra a representação retornada.
    // Cada chamada carrega seus parâmetros, sem depender de sessão HTTP no servidor.
    @Operation(summary = "Listar matérias", description = "Pode receber faseId para filtrar por fase.")
    @ApiResponse(responseCode = "200", description = "Lista de matérias")
    @ApiResponse(responseCode = "400", description = "Identificador de fase inválido", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    public List<MateriaDTO> listar(@RequestParam(required = false) Long faseId) {
        return service.listar(faseId).stream().map(m -> new MateriaDTO(m.getId(), m.getNome(), m.getCodigo())).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    // POST cria uma matéria associada à fase informada no corpo; repetição pode duplicar o recurso.
    // 201 indica criação; 404 é usado pelo handler global se a fase referenciada não existir.
    @Operation(summary = "Cadastrar matéria", description = "Cria uma matéria associada a uma fase existente.")
    @ApiResponse(responseCode = "201", description = "Matéria criada")
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "404", description = "Fase não encontrada", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    public MateriaDTO cadastrar(@RequestBody CadastroMateriaDTO dto) {
        Materia m = service.cadastrar(dto.nome(), dto.codigo(), dto.faseId());
        return new MateriaDTO(m.getId(), m.getNome(), m.getCodigo());
    }
}
