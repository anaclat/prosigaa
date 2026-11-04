package com.ifsc.prosigaa.repository;

import com.ifsc.prosigaa.model.Fase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FaseRepository extends JpaRepository<Fase, Long> {
    Optional<Fase> findByNumero(int numero);

    List<Fase> findAllByOrderByNumeroAsc();
}
