package com.lacouf.rsbjwt.repository;

import com.lacouf.rsbjwt.model.CandidatAccepter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CandidatAccepterRepository extends JpaRepository<CandidatAccepter, Long> {
    Optional<CandidatAccepter> findByEntrevueId(Long entrevueId);

    @Query("SELECT c FROM CandidatAccepter c WHERE c.entrevue.offreDeStage.session = :session")
    List<CandidatAccepter> findByEntrevueSession(@Param("session") String session);
}
