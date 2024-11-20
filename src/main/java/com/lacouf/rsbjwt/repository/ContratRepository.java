package com.lacouf.rsbjwt.repository;

import com.lacouf.rsbjwt.model.Contrat;
import com.lacouf.rsbjwt.model.Employeur;
import com.lacouf.rsbjwt.model.Etudiant;
import com.lacouf.rsbjwt.model.Gestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContratRepository extends JpaRepository<Contrat, Long> {

    @Query("SELECT c FROM Contrat c " +
            "JOIN c.candidature candidature " +
            "JOIN candidature.entrevue.etudiant etudiant " +
            "WHERE etudiant = :etudiant")
    List<Contrat> findContratsByEtudiantEmail(@Param("etudiant")Etudiant etudiant);

    @Query("SELECT c FROM Contrat c " +
            "WHERE c.UUID = :uuid")
    Optional<Contrat> findByUUID(@Param("uuid") String uuid);

    @Query("SELECT c FROM Contrat c " +
            "JOIN c.candidature candidature " +
            "JOIN candidature.entrevue.etudiant etudiant " +
            "WHERE etudiant = :etudiant AND c.gestionnaireSigne = true")
    Optional<Contrat> findByCandidature_EtudiantAndGestionnaireSigneTrue(@Param("etudiant") Etudiant etudiant);

    @Query("SELECT c FROM Contrat c " +
            "JOIN c.candidature candidature " +
            "JOIN candidature.entrevue.offreDeStage offreDeStage " +
            "WHERE offreDeStage.session = :session")
    List<Contrat> findBySession(@Param("session") String session);

    @Query("SELECT c FROM Contrat c " +
            "JOIN c.candidature candidature " +
            "JOIN candidature.entrevue.offreDeStage offreDeStage " +
            "JOIN offreDeStage.employeur employeur " +
            "WHERE employeur = :employeur AND offreDeStage.session = :session")
    List<Contrat> findByEmployeurAndSession(Employeur employeur, String session);

    @Query("SELECT c FROM Contrat c " +
            "JOIN c.candidature candidature " +
            "JOIN candidature.entrevue.etudiant etudiant " +
            "WHERE etudiant = :etudiant AND candidature.entrevue.offreDeStage.session = :session")
    List<Contrat> findContratsByEtudiantEmailAndSession(@Param("etudiant")Etudiant etudiant, @Param("session") String session);
}
