package com.ifsc.prosigaa.controller;

import com.ifsc.prosigaa.dto.CadastroMateriaDTO;
import com.ifsc.prosigaa.dto.MateriaDTO;
import com.ifsc.prosigaa.model.Materia;
import com.ifsc.prosigaa.service.MateriaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materia")
public class MateriaController {
    private final MateriaService materiaService;

    public MateriaController(MateriaService materiaService) {
        this.materiaService = materiaService;
    }

    @GetMapping
    public List<MateriaDTO> listar(@RequestParam(required = false) Long faseId) {
        return materiaService.listar(faseId).stream()
                .map(m -> new MateriaDTO(m.getId(), m.getNome(), m.getCodigo()))
                .toList();
    }

    @PostMapping
    public MateriaDTO cadastrar(@RequestBody CadastroMateriaDTO dto) {
        Materia materia = materiaService.cadastrar(dto.nome(), dto.codigo(), dto.faseId());
        return new MateriaDTO(materia.getId(), materia.getNome(), materia.getCodigo());
    }
}
