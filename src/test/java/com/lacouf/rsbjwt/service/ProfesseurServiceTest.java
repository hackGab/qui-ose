package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.presentation.ProfesseurController;
import com.lacouf.rsbjwt.service.dto.ProfesseurDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ProfesseurServiceTest {

    private ProfesseurService professeurService;
    private ProfesseurController professeurController;

    @BeforeEach
    void setUp() {
        professeurService = Mockito.mock(ProfesseurService.class);
        professeurController = new ProfesseurController(professeurService);
    }

    @Test
    void shouldCreateProfesseur() {
        // Arrange
        ProfesseurDTO newProfesseur = new ProfesseurDTO("John", "Doe", null, null, null);
        ProfesseurDTO savedProfesseur = new ProfesseurDTO("John", "Doe", null, null, null);

        when(professeurService.creerProfesseur(any(ProfesseurDTO.class)))
                .thenReturn(Optional.of(savedProfesseur));

        // Act
        ResponseEntity<ProfesseurDTO> response = professeurController.creerProfesseur(newProfesseur);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedProfesseur, response.getBody());
    }

    @Test
    void shouldReturnConflictWhenProfesseurNotCreated() {
        // Arrange
        ProfesseurDTO newProfesseur = new ProfesseurDTO("John", "Doe", null, null, null);

        when(professeurService.creerProfesseur(any(ProfesseurDTO.class)))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<ProfesseurDTO> response = professeurController.creerProfesseur(newProfesseur);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void shouldReturnProfesseurWhenFound() {
        // Arrange
        Long professeurId = 1L;
        ProfesseurDTO professeur = new ProfesseurDTO("John", "Doe", null, null, null);

        when(professeurService.getProfesseurById(professeurId))
                .thenReturn(Optional.of(professeur));

        // Act
        ResponseEntity<ProfesseurDTO> response = professeurController.getProfesseurById(professeurId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(professeur, response.getBody());
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
