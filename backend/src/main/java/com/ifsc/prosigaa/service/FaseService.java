package com.ifsc.prosigaa.service;

import com.ifsc.prosigaa.dto.FaseDTO;
import com.ifsc.prosigaa.dto.FasesDTO;
import com.ifsc.prosigaa.dto.MateriaDTO;
import com.ifsc.prosigaa.model.Fase;
import com.ifsc.prosigaa.repository.FaseRepository;
import org.springframework.stereotype.Service;
import com.ifsc.prosigaa.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class FaseService {
    private final FaseRepository faseRepository;

    public FaseService(FaseRepository faseRepository) {
        this.faseRepository = faseRepository;
    }

    public FasesDTO listar() {
        List<FaseDTO> fases = faseRepository.findAllByOrderByNumeroAsc().stream()
                .map(f -> new FaseDTO(
                        f.getId(), f.getNome(), f.getNumero(),
                        f.getMaterias().stream()
                                .map(m -> new MateriaDTO(m.getId(), m.getNome(), m.getCodigo()))
                                .toList()
                ))
                .toList();
        return new FasesDTO(fases);
    }

    public Fase buscar(Long id) {
        return faseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fase não encontrada."));
    }

    public Fase cadastrar(String nome, int numero) {
        return faseRepository.save(new Fase(nome, numero));
    }
}
