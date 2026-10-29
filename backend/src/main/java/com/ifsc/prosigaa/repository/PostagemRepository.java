package com.ifsc.prosigaa.repository;

import com.ifsc.prosigaa.model.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostagemRepository extends JpaRepository<Postagem, Long> {
    boolean existsByFaseIdAndMateriaId(Long faseId, Long materiaId);
    List<Postagem> findByFaseIdAndMateriaIdOrderByCriadoEmDesc(Long faseId, Long materiaId);
    List<Postagem> findByFaseIdOrderByCriadoEmDesc(Long faseId);
    List<Postagem> findByMateriaIdOrderByCriadoEmDesc(Long materiaId);
    List<Postagem> findAllByOrderByCriadoEmDesc();
}
