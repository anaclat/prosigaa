package com.ifsc.prosigaa.repository;

import com.ifsc.prosigaa.model.Materia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MateriaRepository extends JpaRepository<Materia, Long> {
    List<Materia> findByFasesId(Long faseId);
}
