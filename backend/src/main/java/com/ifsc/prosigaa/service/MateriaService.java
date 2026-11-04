package com.ifsc.prosigaa.service;

import com.ifsc.prosigaa.model.Fase;
import com.ifsc.prosigaa.model.Materia;
import com.ifsc.prosigaa.repository.FaseRepository;
import com.ifsc.prosigaa.repository.MateriaRepository;
import org.springframework.stereotype.Service;
import com.ifsc.prosigaa.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class MateriaService {
    private final MateriaRepository materiaRepository;
    private final FaseRepository faseRepository;

    public MateriaService(MateriaRepository materiaRepository, FaseRepository faseRepository) {
        this.materiaRepository = materiaRepository;
        this.faseRepository = faseRepository;
    }

    public List<Materia> listar(Long faseId) {
        return faseId == null
                ? materiaRepository.findAll()
                : materiaRepository.findByFasesId(faseId);
    }

    public Materia cadastrar(String nome, String codigo, Long faseId) {
        Fase fase = faseRepository.findById(faseId)
                .orElseThrow(() -> new ResourceNotFoundException("Fase não encontrada."));

        Materia materia = new Materia(nome, codigo);
        materia.getFases().add(fase);
        Materia salva = materiaRepository.save(materia);
        fase.getMaterias().add(salva);
        faseRepository.save(fase);
        return salva;
    }

    public Materia buscar(Long id) {
        return materiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matéria não encontrada."));
    }
}
