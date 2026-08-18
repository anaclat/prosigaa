package com.ifsc.prosigaa.service;

import com.ifsc.prosigaa.repository.MateriaRepository;
import org.springframework.stereotype.Service;

@Service
public class MateriaService {
    private final MateriaRepository materiaRepository;

    public MateriaService(MateriaRepository materiaRepository) {
        this.materiaRepository = materiaRepository;
    }
}
