package com.lacouf.rsbjwt.repository;

import com.lacouf.rsbjwt.model.Etudiant;
import com.lacouf.rsbjwt.model.EvaluationStageProf;
import com.lacouf.rsbjwt.model.Professeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationStageProfRepository extends JpaRepository<EvaluationStageProf, Long> {
    void deleteByEtudiant(Etudiant etudiant);

    List<EvaluationStageProf> findAllByProfesseur(Professeur professeur);
}
