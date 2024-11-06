package com.lacouf.rsbjwt.repository;

import com.lacouf.rsbjwt.model.Etudiant;
import com.lacouf.rsbjwt.model.EvaluationStageProf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EvaluationStageProfRepository extends JpaRepository<EvaluationStageProf, Long> {
    void deleteByEtudiant(Etudiant etudiant);

    @Query("SELECT e FROM EvaluationStageProf e WHERE e.professeur.id = ?1")
    Optional<EvaluationStageProf> findByProsseurID(Long professeurId);
}
