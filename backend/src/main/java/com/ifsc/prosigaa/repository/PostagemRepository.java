package com.ifsc.prosigaa.repository;

import com.ifsc.prosigaa.model.Postagem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostagemRepository extends JpaRepository<Postagem, Long> {
    boolean existsByFaseIdAndMateriaId(Long faseId, Long materiaId);

    @EntityGraph(attributePaths = {"usuario", "fase", "materia", "comentarios", "comentarios.usuario"})
    List<Postagem> findByFaseIdAndMateriaIdOrderByCriadoEmDesc(Long faseId, Long materiaId);

    @EntityGraph(attributePaths = {"usuario", "fase", "materia", "comentarios", "comentarios.usuario"})
    List<Postagem> findByFaseIdOrderByCriadoEmDesc(Long faseId);

    @EntityGraph(attributePaths = {"usuario", "fase", "materia", "comentarios", "comentarios.usuario"})
    List<Postagem> findByMateriaIdOrderByCriadoEmDesc(Long materiaId);

    @EntityGraph(attributePaths = {"usuario", "fase", "materia", "comentarios", "comentarios.usuario"})
    List<Postagem> findAllByOrderByCriadoEmDesc();
}
