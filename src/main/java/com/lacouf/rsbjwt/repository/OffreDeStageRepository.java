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
        @Query("SELECT o FROM OffreDeStage o WHERE " +
                "(CAST(SUBSTRING(o.session, LENGTH(o.session) - 1, 2) AS integer) > CAST(:annee AS integer)) OR " +
                "(CAST(SUBSTRING(o.session, LENGTH(o.session) - 1, 2) AS integer) = CAST(:annee AS integer) AND " +
                "(CASE " +
                "WHEN :typeSession = 'HIVER' THEN o.session LIKE 'HIVER%' OR o.session LIKE 'AUTOMNE%' OR o.session LIKE 'ETE%' " +
                "WHEN :typeSession = 'ETE' THEN o.session LIKE 'ETE%' OR o.session LIKE 'AUTOMNE%' " +
                "WHEN :typeSession = 'AUTOMNE' THEN o.session LIKE 'AUTOMNE%' " +
                "END))")
        List<OffreDeStage> findBySessionAfter(@Param("annee") String annee, @Param("typeSession") String typeSession);

        List<OffreDeStage> findBySession(String session);

        @Query("SELECT o FROM OffreDeStage o WHERE " +
                "(CAST(SUBSTRING(o.session, LENGTH(o.session) - 1, 2) AS integer) = CAST(:annee AS integer))")
        List<OffreDeStage> findByYear(@Param("annee") String annee);

        List<OffreDeStage> findByEmployeur(Employeur employeur);

}
