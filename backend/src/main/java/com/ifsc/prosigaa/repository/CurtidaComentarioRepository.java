package com.ifsc.prosigaa.repository;

import com.ifsc.prosigaa.model.CurtidaComentario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurtidaComentarioRepository extends JpaRepository<CurtidaComentario, Long> {
    boolean existsByUsuarioIdAndComentarioId(Long usuarioId, Long comentarioId);

    void deleteByUsuarioIdAndComentarioId(Long usuarioId, Long comentarioId);
}
