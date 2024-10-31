package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.Departement;
import com.lacouf.rsbjwt.model.Professeur;
import com.lacouf.rsbjwt.model.auth.Role;
import com.lacouf.rsbjwt.presentation.ProfesseurController;
import com.lacouf.rsbjwt.repository.ProfesseurRepository;
import com.lacouf.rsbjwt.service.dto.CredentialDTO;
import com.lacouf.rsbjwt.service.dto.ProfesseurDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ProfesseurServiceTest {

    private ProfesseurRepository professeurRepository;
    private ProfesseurService professeurService;
    private ProfesseurController professeurController;

    private ProfesseurDTO newProfesseur;
    private Professeur professeurEntity;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        professeurRepository = Mockito.mock(ProfesseurRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        professeurService = new ProfesseurService(professeurRepository, passwordEncoder);
        professeurController = new ProfesseurController(professeurService);

        CredentialDTO credentials = new CredentialDTO("email@gmail.com", "password");
        newProfesseur = new ProfesseurDTO("John", "Doe", Role.PROFESSEUR, "23456789", credentials, Departement.TECHNIQUES_INFORMATIQUE );
        professeurEntity = new Professeur("John", "Doe", "email@gmail.com", "password", "23456789", Departement.TECHNIQUES_INFORMATIQUE);
    }

    @Test
    void shouldCreateProfesseur() {
        // Arrange
        when(professeurRepository.save(any(Professeur.class)))
                .thenReturn(professeurEntity);
        // Act
        ResponseEntity<ProfesseurDTO> response = professeurController.creerProfesseur(newProfesseur);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(newProfesseur.getFirstName(), response.getBody().getFirstName());
        assertEquals(newProfesseur.getLastName(), response.getBody().getLastName());
    }

    @Test
    void shouldReturnEmptyWhenExceptionIsThrown() {
        // Arrange
        when(professeurRepository.save(any(Professeur.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        Optional<ProfesseurDTO> response = professeurService.creerProfesseur(newProfesseur);

        // Assert
        assertTrue(response.isEmpty());
    }

    @Test
void shouldReturnProfesseurById() {
        // Arrange
        Long professeurId = 1L;

        when(professeurRepository.findById(professeurId))
                .thenReturn(Optional.of(professeurEntity));

        // Act
        Optional<ProfesseurDTO> response = professeurService.getProfesseurById(professeurId);

        // Assert
        assertTrue(response.isPresent());
        assertEquals(professeurEntity.getFirstName(), response.get().getFirstName());
        assertEquals(professeurEntity.getLastName(), response.get().getLastName());
        assertEquals(professeurEntity.getEmail(), response.get().getCredentials().getEmail());
    }

    @Test
    void shouldReturnNotFoundWhenProfesseurNotFound() {
        // Arrange
        Long professeurId = 1L;
        when(professeurService.getProfesseurById(professeurId))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<ProfesseurDTO> response = professeurController.getProfesseurById(professeurId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
