package com.lacouf.rsbjwt.repository;

import com.lacouf.rsbjwt.model.CandidatAccepter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CandidatAccepterRepository extends JpaRepository<CandidatAccepter, Long> {
    Optional<CandidatAccepter> findByEntrevueId(Long entrevueId);
}
