package com.lacouf.rsbjwt.repository;

import com.lacouf.rsbjwt.model.Professeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfesseurRepository extends JpaRepository<Professeur, Long> {
    @Query("SELECT p FROM Professeur p WHERE p.credentials.email = ?1")
    Optional<Professeur> findByEmail(String email);

}
