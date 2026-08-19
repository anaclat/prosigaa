package com.ifsc.prosigaa.service;

import com.ifsc.prosigaa.model.Fase;
import com.ifsc.prosigaa.repository.FaseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class FaseService {
    private final FaseRepository faseRepository;

    public FaseService(FaseRepository faseRepository) {
        this.faseRepository = faseRepository;
    }

    public ArrayList<Fase> listarFases() {
        return (ArrayList<Fase>) faseRepository.findAll();
    }

    public Fase cadastrarFase(String nome, int numero) {
        Fase fase = new Fase(nome, numero);
        faseRepository.save(fase);
        return fase;
    }
}
