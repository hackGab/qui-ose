package com.lacouf.rsbjwt.repository;

import com.lacouf.rsbjwt.model.Gestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GestionnaireRepository extends JpaRepository<Gestionnaire, Long> {
}