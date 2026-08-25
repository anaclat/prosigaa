package com.ifsc.prosigaa.controller;

import com.ifsc.prosigaa.dto.CadastroFaseDTO;
import com.ifsc.prosigaa.dto.FaseDTO;
import com.ifsc.prosigaa.dto.FasesDTO;
import com.ifsc.prosigaa.dto.MateriaDTO;
import com.ifsc.prosigaa.model.Fase;
import com.ifsc.prosigaa.service.FaseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fase")
public class FaseController {
    private final FaseService faseService;

    public FaseController(FaseService faseService) {
        this.faseService = faseService;
    }

    @GetMapping
    public FasesDTO listar() {
        return faseService.listar();
    }

    @PostMapping
    public FaseDTO cadastrar(@RequestBody CadastroFaseDTO dto) {
        Fase fase = faseService.cadastrar(dto.nome(), dto.numero());
        return new FaseDTO(fase.getId(), fase.getNome(), fase.getNumero(), List.of());
    }
}
