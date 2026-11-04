package com.ifsc.prosigaa.controller;

import com.ifsc.prosigaa.dto.*;
import com.ifsc.prosigaa.model.Fase;
import com.ifsc.prosigaa.service.FaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/fases", "/fase"})
@Tag(name = "Fases")
public class FaseController {
    private final FaseService service;

    public FaseController(FaseService s) {
        service = s;
    }

    @GetMapping
    @Operation(summary = "Listar fases e matérias")
    @ApiResponse(responseCode = "200", description = "Lista de fases")
    public FasesDTO listar() {
        return service.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar fase")
    @ApiResponse(responseCode = "201", description = "Fase criada")
    public FaseDTO cadastrar(@RequestBody CadastroFaseDTO dto) {
        Fase f = service.cadastrar(dto.nome(), dto.numero());
        return new FaseDTO(f.getId(), f.getNome(), f.getNumero(), List.of());
    }
}
