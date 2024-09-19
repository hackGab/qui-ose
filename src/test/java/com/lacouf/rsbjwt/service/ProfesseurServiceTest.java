package com.lacouf.rsbjwt.service;

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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ProfesseurServiceTest {

    private ProfesseurRepository professeurRepository;
    private ProfesseurService professeurService;
    private ProfesseurController professeurController;

    private ProfesseurDTO newProfesseur;
    private Professeur professeurEntity;

    @BeforeEach
    void setUp() {
        professeurRepository = Mockito.mock(ProfesseurRepository.class);
        professeurService = Mockito.mock(ProfesseurService.class);
        professeurController = new ProfesseurController(professeurService);

        CredentialDTO credentials = new CredentialDTO("email@gmail.com", "password", "password");
        newProfesseur = new ProfesseurDTO("John", "Doe", Role.PROFESSEUR, "23456789", credentials);
        professeurEntity = new Professeur("John", "Doe", "email@gmail.com", "password", "23456789");
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
    void shouldReturnConflictWhenProfesseurNotCreated() {
        // Arrange
        when(professeurService.creerProfesseur(any(ProfesseurDTO.class)))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<ProfesseurDTO> response = professeurController.creerProfesseur(newProfesseur);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
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
