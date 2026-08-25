package com.ifsc.prosigaa.repository;

import com.ifsc.prosigaa.model.CurtidaPostagem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurtidaPostagemRepository extends JpaRepository<CurtidaPostagem, Long> {
    boolean existsByUsuarioIdAndPostagemId(Long usuarioId, Long postagemId);
}
