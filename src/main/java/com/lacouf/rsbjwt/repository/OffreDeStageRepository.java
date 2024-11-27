package com.lacouf.rsbjwt.repository;

import com.lacouf.rsbjwt.model.Employeur;
import com.lacouf.rsbjwt.model.OffreDeStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OffreDeStageRepository extends JpaRepository<OffreDeStage, Long> {

        List<OffreDeStage> findBySession(String session);

        @Query("SELECT o FROM OffreDeStage o WHERE " +
                "o.employeur = :employeur AND " +
                "o.session = :session")
        List<OffreDeStage> findByEmployeurAndSession(@Param("employeur") Employeur employeur, @Param("session") String session);

}
