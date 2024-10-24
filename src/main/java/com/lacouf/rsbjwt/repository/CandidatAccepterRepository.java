package com.lacouf.rsbjwt.repository;

import com.lacouf.rsbjwt.model.CandidatAccepter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidatAccepterRepository extends JpaRepository<CandidatAccepter, Long> {
    Optional<CandidatAccepter> findByEntrevueId(Long entrevueId);
}
