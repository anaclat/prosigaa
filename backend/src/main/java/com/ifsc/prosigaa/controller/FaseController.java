package com.ifsc.prosigaa.controller;

import com.ifsc.prosigaa.dto.CadastroFaseDTO;
import com.ifsc.prosigaa.dto.FaseDTO;
import com.ifsc.prosigaa.dto.FasesDTO;
import com.ifsc.prosigaa.model.Fase;
import com.ifsc.prosigaa.service.FaseService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/fase")
public class FaseController {
    private final FaseService faseService;

    public FaseController(FaseService faseService) {
        this.faseService = faseService;
    }

    @GetMapping
    public FasesDTO listar() {
        ArrayList<Fase> fases = this.faseService.listarFases();
        return new FasesDTO(fases);
    }

    @PostMapping
    public FaseDTO cadastrar(@RequestBody CadastroFaseDTO cadastroFaseDTO) {
        Fase fase = this.faseService.cadastrarFase(cadastroFaseDTO.nome(), cadastroFaseDTO.numero());
        return new FaseDTO(fase.getId(), fase.getNome(), fase.getNumero());
    }
}
