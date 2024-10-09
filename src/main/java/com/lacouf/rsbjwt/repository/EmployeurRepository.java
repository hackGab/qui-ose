package com.lacouf.rsbjwt.repository;

import com.lacouf.rsbjwt.model.Employeur;
import com.lacouf.rsbjwt.service.dto.EmployeurDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeurRepository extends JpaRepository<Employeur, Long> {
    Optional<Employeur> findByCredentials_email(String email);

}
