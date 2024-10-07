package com.lacouf.rsbjwt.repository;

import com.lacouf.rsbjwt.model.CV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CVRepository extends JpaRepository<CV, Long> {
}