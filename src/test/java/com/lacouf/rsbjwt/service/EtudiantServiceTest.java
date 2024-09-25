package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.Etudiant;
import com.lacouf.rsbjwt.model.auth.Role;
import com.lacouf.rsbjwt.repository.EtudiantRepository;
import com.lacouf.rsbjwt.presentation.EtudiantController;
import com.lacouf.rsbjwt.service.dto.CredentialDTO;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EtudiantServiceTest {

    private EtudiantRepository etudiantRepository;
    private EtudiantService etudiantService;
    private EtudiantController etudiantController;

    private EtudiantDTO newEtudiant;
    private Etudiant etudiantEntity;

    @BeforeEach
    void setUp() {
        etudiantRepository = Mockito.mock(EtudiantRepository.class);
        etudiantService = new EtudiantService(etudiantRepository);
        etudiantController = new EtudiantController(etudiantService);

        CredentialDTO credentials = new CredentialDTO("email@gmail.com", "password", "password");
        newEtudiant = new EtudiantDTO("John", "Doe", Role.ETUDIANT, "123456789", credentials,"departement");
        etudiantEntity = new Etudiant("John", "Doe", "email@gmail.com", "password", "123456789", "departement");
    }

    @Test
    void shouldCreateEtudiant() {
        // Arrange
        when(etudiantRepository.save(any(Etudiant.class)))
                .thenReturn(etudiantEntity);

        // Act
        ResponseEntity<EtudiantDTO> response = etudiantController.creerEtudiant(newEtudiant);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(newEtudiant.getFirstName(), response.getBody().getFirstName());
        assertEquals(newEtudiant.getLastName(), response.getBody().getLastName());
    }

    @Test
    void shouldReturnEmptyWhenExceptionIsThrown() {
        // Arrange
        when(etudiantRepository.save(any(Etudiant.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        Optional<EtudiantDTO> response = etudiantService.creerEtudiant(newEtudiant);

        // Assert
        assertTrue(response.isEmpty());
    }

    @Test
    void shouldReturnEtudiantWhenFound() {
        // Arrange
        Long etudiantId = 1L;
        when(etudiantRepository.findById(etudiantId))
                .thenReturn(Optional.of(etudiantEntity));

        // Act
        Optional<EtudiantDTO> reponse = etudiantService.getEtudiantById(etudiantId);

        // Assert
        assertTrue(reponse.isPresent());
        assertEquals(etudiantEntity.getFirstName(), reponse.get().getFirstName());
        assertEquals(etudiantEntity.getLastName(), reponse.get().getLastName());
        assertEquals(etudiantEntity.getCredentials().getEmail(), reponse.get().getCredentials().getEmail());
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
