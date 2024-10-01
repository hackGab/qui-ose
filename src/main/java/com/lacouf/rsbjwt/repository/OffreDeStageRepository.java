package com.lacouf.rsbjwt.repository;

import com.lacouf.rsbjwt.model.OffreDeStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffreDeStageRepository extends JpaRepository<OffreDeStage, Long> {

}
