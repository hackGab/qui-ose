package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.presentation.EmployeurController;
import com.lacouf.rsbjwt.service.dto.EmployeurDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class EmployeurServiceTest {
    private EmployeurService employeurService;
    private EmployeurController employeurController;

    @BeforeEach
    void setUp() {
        employeurService = Mockito.mock(EmployeurService.class);
        employeurController = new EmployeurController(employeurService);
    }

    @Test
    void shouldCreateEmployeur() {
        // Arrange
        EmployeurDTO newEmployeur = new EmployeurDTO("John", "Doe", null, null, null, null);
        EmployeurDTO savedEmployeur = new EmployeurDTO("John", "Doe", null, null, null, null);

        when(employeurService.creerEmployeur(any(EmployeurDTO.class)))
                .thenReturn(Optional.of(savedEmployeur));

        // Act
        ResponseEntity<EmployeurDTO> response = employeurController.creerEmployeur(newEmployeur);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedEmployeur, response.getBody());
    }

    @Test
    void shouldReturnConflictWhenEmployeurNotCreated() {
        // Arrange
        EmployeurDTO newEmployeur = new EmployeurDTO("John", "Doe", null, null, null, null);

        when(employeurService.creerEmployeur(any(EmployeurDTO.class)))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<EmployeurDTO> response = employeurController.creerEmployeur(newEmployeur);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void shouldReturnEmployeurById() {
        // Arrange
        EmployeurDTO employeur = new EmployeurDTO("John", "Doe", null, null, null, null);
        EmployeurDTO savedEmployeur = new EmployeurDTO("John", "Doe", null, null, null, null);

        when(employeurService.getEmployeurById(1L))
                .thenReturn(Optional.of(savedEmployeur));

        // Act
        ResponseEntity<EmployeurDTO> response = employeurController.getEmployeurById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedEmployeur, response.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenEmployeurNotFound() {
        // Arrange
        EmployeurDTO employeur = new EmployeurDTO("John", "Doe", null, null, null, null);

        when(employeurService.getEmployeurById(1L))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<EmployeurDTO> response = employeurController.getEmployeurById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
