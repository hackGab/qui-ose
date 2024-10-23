package com.lacouf.rsbjwt.repository;

import com.lacouf.rsbjwt.model.Entrevue;
import com.lacouf.rsbjwt.model.Etudiant;
import com.lacouf.rsbjwt.model.OffreDeStage;
import com.lacouf.rsbjwt.service.dto.EntrevueDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntrevueRepository extends JpaRepository<Entrevue, Long> {
    boolean existsByEtudiantAndOffreDeStage(Etudiant etudiant, OffreDeStage offreDeStage);
    List<Entrevue> findByOffreDeStageId(Long offreDeStageId);

    List<Entrevue> findAllByEtudiantId(Long etudiantId);
}

