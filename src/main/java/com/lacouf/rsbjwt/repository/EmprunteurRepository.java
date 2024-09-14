package com.lacouf.rsbjwt.repository;

import com.lacouf.rsbjwt.model.Emprunteur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmprunteurRepository extends JpaRepository<Emprunteur, Long> {
}