package com.ifsc.prosigaa.controller;

import com.ifsc.prosigaa.dto.*;
import com.ifsc.prosigaa.model.Materia;
import com.ifsc.prosigaa.service.MateriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/materias", "/materia"})
@Tag(name = "Matérias")
public class MateriaController {
    private final MateriaService service;

    public MateriaController(MateriaService s) {
        service = s;
    }

    @GetMapping
    @Operation(summary = "Listar matérias", description = "Pode receber faseId para filtrar por fase.")
    @ApiResponse(responseCode = "200", description = "Lista de matérias")
    public List<MateriaDTO> listar(@RequestParam(required = false) Long faseId) {
        return service.listar(faseId).stream().map(m -> new MateriaDTO(m.getId(), m.getNome(), m.getCodigo())).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar matéria")
    @ApiResponse(responseCode = "201", description = "Matéria criada")
    @ApiResponse(responseCode = "404", description = "Fase não encontrada")
    public MateriaDTO cadastrar(@RequestBody CadastroMateriaDTO dto) {
        Materia m = service.cadastrar(dto.nome(), dto.codigo(), dto.faseId());
        return new MateriaDTO(m.getId(), m.getNome(), m.getCodigo());
    }
}
