package com.ifsc.prosigaa.controller;

import com.ifsc.prosigaa.dto.*;
import com.ifsc.prosigaa.model.Fase;
import com.ifsc.prosigaa.service.FaseService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fases")
@Tag(name = "Fases")
public class FaseController {
    private final FaseService service;

    public FaseController(FaseService s) {
        service = s;
    }

    @GetMapping
    @Operation(summary = "Listar fases e matérias")
    @ApiResponse(responseCode = "200", description = "Lista de fases")
    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    public FasesDTO listar() {
        return service.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar fase", description = "Cria uma fase curricular.")
    @ApiResponse(responseCode = "201", description = "Fase criada")
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    public FaseDTO cadastrar(@RequestBody CadastroFaseDTO dto) {
        Fase f = service.cadastrar(dto.nome(), dto.numero());
        return new FaseDTO(f.getId(), f.getNome(), f.getNumero(), List.of());
    }
}
