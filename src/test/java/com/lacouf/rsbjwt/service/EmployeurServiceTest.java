package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.Employeur;
import com.lacouf.rsbjwt.model.auth.Credentials;
import com.lacouf.rsbjwt.model.auth.Role;
import com.lacouf.rsbjwt.presentation.EmployeurController;
import com.lacouf.rsbjwt.repository.EmployeurRepository;
import com.lacouf.rsbjwt.service.dto.CredentialDTO;
import com.lacouf.rsbjwt.service.dto.EmployeurDTO;
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

public class EmployeurServiceTest {
    private EmployeurRepository employeurRepository;
    private EmployeurService employeurService;
    private EmployeurController employeurController;

    private EmployeurDTO newEmployeur;
    private Employeur employeurEntity;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        employeurRepository = Mockito.mock(EmployeurRepository.class);
        employeurService = new EmployeurService(employeurRepository, passwordEncoder);
        employeurController = new EmployeurController(employeurService);

        CredentialDTO credentials = new CredentialDTO("email@gmail.com", "password", "password");
        newEmployeur = new EmployeurDTO("John", "Doe", "123456789", Role.EMPLOYEUR, credentials, "Entreprise");

        employeurEntity = new Employeur("John", "Doe", "email@gmail.com", "password", "123456789", "Entreprise");
    }

    @Test
    void shouldCreateEmployeur() {
        // Arrange
        when(employeurRepository.save(any(Employeur.class)))
                .thenReturn(employeurEntity);

        // Act
        ResponseEntity<EmployeurDTO> response = employeurController.creerEmployeur(newEmployeur);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(newEmployeur.getFirstName(), response.getBody().getFirstName());
        assertEquals(newEmployeur.getLastName(), response.getBody().getLastName());
    }

    @Test
    void shouldReturnEmptyWhenExceptionIsThrown() {
        // Arrange
        when(employeurRepository.save(any(Employeur.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        Optional<EmployeurDTO> response = employeurService.creerEmployeur(newEmployeur);

        // Assert
        assertTrue(response.isEmpty());
    }

    @Test
    void shouldReturnEmployeurById() {
        // Arrange
        Long employeurId = 1L;

        when(employeurRepository.findById(employeurId))
                .thenReturn(Optional.of(employeurEntity));

        // Act
        Optional<EmployeurDTO> response = employeurService.getEmployeurById(employeurId);

        // Assert
        assertTrue(response.isPresent());
        assertEquals(employeurEntity.getFirstName(), response.get().getFirstName());
        assertEquals(employeurEntity.getLastName(), response.get().getLastName());
        assertEquals(employeurEntity.getEntreprise(), response.get().getEntreprise());
    }

    @Test
    void shouldReturnNotFoundWhenEmployeurNotFound() {
        // Arrange
        Long employeurId = 1L;

        when(employeurService.getEmployeurById(employeurId))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<EmployeurDTO> response = employeurController.getEmployeurById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
