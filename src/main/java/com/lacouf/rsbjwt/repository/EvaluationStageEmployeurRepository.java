package com.lacouf.rsbjwt.repository;

import com.lacouf.rsbjwt.model.Employeur;
import com.lacouf.rsbjwt.model.Etudiant;
import com.lacouf.rsbjwt.model.EvaluationStageEmployeur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EvaluationStageEmployeurRepository extends JpaRepository<EvaluationStageEmployeur, Long> {
    Optional<EvaluationStageEmployeur> findByEmployeurAndEtudiant(Employeur employeur, Etudiant etudiant);
}
