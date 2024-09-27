package com.lacouf.rsbjwt.repository;

import com.lacouf.rsbjwt.model.Gestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GestionnaireRepository extends JpaRepository<Gestionnaire, Long>{}
