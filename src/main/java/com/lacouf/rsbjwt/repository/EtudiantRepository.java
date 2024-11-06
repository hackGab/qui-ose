package com.lacouf.rsbjwt.repository;

import com.lacouf.rsbjwt.model.Departement;
import com.lacouf.rsbjwt.model.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    @Query("SELECT e FROM Etudiant e WHERE e.credentials.email = ?1")
    Optional<Etudiant> findByEmail(String email);

    List<Etudiant> findAllByDepartement(Departement departementEnum);

    @Query("SELECT e FROM Etudiant e WHERE e.credentials.email IN :emails")
    List<Etudiant> findAllByEmailIn(List<String> emails);

    @Query("SELECT e FROM Etudiant e " +
            "JOIN  Entrevue en " +
            "ON e.id = en.etudiant.id " +
            "JOIN CandidatAccepter ca " +
            "ON en.id = ca.entrevue.id " +
            "JOIN Contrat c " +
            "ON ca.id = c.candidature.id " +
            "WHERE e.departement = :departement AND c.etudiantSigne = true AND c.employeurSigne = true AND c.gestionnaireSigne = true AND en.etudiant.id = e.id" )
    List<Etudiant> findEtudiantsAvecContratByDepartement(@Param("departement") Departement departement);
}