package com.lacouf.rsbjwt.repository;

import com.lacouf.rsbjwt.model.Departement;
import com.lacouf.rsbjwt.model.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    @Query("SELECT e FROM Etudiant e WHERE e.credentials.email = ?1")
    Optional<Etudiant> findByEmail(String email);

    List<Etudiant> findAllByDepartement(Departement departementEnum);

    @Query("SELECT e FROM Etudiant e WHERE e.professeur.credentials.email = ?1")
    List<Etudiant> findAllByEmailIn(List<String> etudiantsEmails);
}