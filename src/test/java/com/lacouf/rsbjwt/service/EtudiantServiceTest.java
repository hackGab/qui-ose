package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.presentation.EtudiantController;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class EtudiantServiceTest {

    private EtudiantService etudiantService;
    private EtudiantController etudiantController;

    @BeforeEach
    void setUp() {
        etudiantService = Mockito.mock(EtudiantService.class);
        etudiantController = new EtudiantController(etudiantService);
    }

    @Test
    void shouldCreateEtudiant() {
        // Arrange
        EtudiantDTO newEtudiant = new EtudiantDTO("John", "Doe", null, null);
        EtudiantDTO savedEtudiant = new EtudiantDTO("John", "Doe", null, null);

        when(etudiantService.creerEtudiant(any(EtudiantDTO.class)))
                .thenReturn(Optional.of(savedEtudiant));

        // Act
        ResponseEntity<EtudiantDTO> response = etudiantController.creerEtudiant(newEtudiant);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedEtudiant, response.getBody());
    }

    @Test
    void shouldReturnConflictWhenEtudiantNotCreated() {
        // Arrange
        EtudiantDTO newEtudiant = new EtudiantDTO("John", "Doe", null, null);

        when(etudiantService.creerEtudiant(any(EtudiantDTO.class)))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<EtudiantDTO> response = etudiantController.creerEtudiant(newEtudiant);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void shouldReturnEtudiantWhenFound() {
        // Arrange
        Long etudiantId = 1L;
        EtudiantDTO foundEtudiant = new EtudiantDTO("Jane", "Doe", null, null);

        when(etudiantService.getEtudiantById(etudiantId))
                .thenReturn(Optional.of(foundEtudiant));

        // Act
        ResponseEntity<EtudiantDTO> response = etudiantController.getEtudiantById(etudiantId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(foundEtudiant, response.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenEtudiantNotFound() {
        // Arrange
        Long etudiantId = 1L;

        when(etudiantService.getEtudiantById(etudiantId))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<EtudiantDTO> response = etudiantController.getEtudiantById(etudiantId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
