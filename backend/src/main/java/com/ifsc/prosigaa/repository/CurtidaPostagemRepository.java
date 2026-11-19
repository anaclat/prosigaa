package com.ifsc.prosigaa.repository;

import com.ifsc.prosigaa.model.CurtidaPostagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CurtidaPostagemRepository extends JpaRepository<CurtidaPostagem, Long> {
    boolean existsByUsuarioIdAndPostagemId(Long usuarioId, Long postagemId);

    void deleteByUsuarioIdAndPostagemId(Long usuarioId, Long postagemId);

    @Query("select curtida.postagem.id from CurtidaPostagem curtida where curtida.usuario.id = :usuarioId")
    List<Long> findPostagemIdsByUsuarioId(@Param("usuarioId") Long usuarioId);
}
