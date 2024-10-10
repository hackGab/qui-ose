package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.CV;
import com.lacouf.rsbjwt.model.Employeur;
import com.lacouf.rsbjwt.model.Etudiant;
import com.lacouf.rsbjwt.model.OffreDeStage;
import com.lacouf.rsbjwt.model.auth.Role;
import com.lacouf.rsbjwt.repository.CVRepository;
import com.lacouf.rsbjwt.repository.EtudiantRepository;
import com.lacouf.rsbjwt.presentation.EtudiantController;
import com.lacouf.rsbjwt.repository.OffreDeStageRepository;
import com.lacouf.rsbjwt.repository.UserAppRepository;
import com.lacouf.rsbjwt.service.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EtudiantServiceTest {

    private EtudiantRepository etudiantRepository;
    private EtudiantService etudiantService;
    private EtudiantController etudiantController;

    private UserAppRepository userAppRepository;
    private CVRepository cvRepository;
    private OffreDeStageRepository offreDeStageRepository;

    private EtudiantDTO newEtudiant;
    private Etudiant etudiantEntity;
    private CV cvEntity;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userAppRepository = Mockito.mock(UserAppRepository.class);
        cvRepository = Mockito.mock(CVRepository.class);
        etudiantRepository = Mockito.mock(EtudiantRepository.class);
        offreDeStageRepository = Mockito.mock(OffreDeStageRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        etudiantService = new EtudiantService(userAppRepository, etudiantRepository, passwordEncoder, cvRepository, offreDeStageRepository);
        etudiantController = new EtudiantController(etudiantService);

        CredentialDTO credentials = new CredentialDTO("email@gmail.com", "password");
        newEtudiant = new EtudiantDTO("John", "Doe", Role.ETUDIANT, "123456789", credentials,"departement");
        etudiantEntity = new Etudiant("John", "Doe", "email@gmail.com", "password", "123456789", "departement");
        cvEntity = new CV("cvName", "cvType", "cvData", "cvStatus");
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

    @Test
    void shouldReturnEtudiantWhenFoundByEmail() {
        // Arrange
        String email = "email@gmail.com";
        when(userAppRepository.findUserAppByEmail(email))
                .thenReturn(Optional.of(etudiantEntity));

        // Act
        Optional<EtudiantDTO> response = etudiantService.getEtudiantByEmail(email);

        // Assert
        assertTrue(response.isPresent());
        assertEquals(etudiantEntity.getFirstName(), response.get().getFirstName());
        assertEquals(etudiantEntity.getLastName(), response.get().getLastName());
        assertEquals(etudiantEntity.getCredentials().getEmail(), response.get().getCredentials().getEmail());
    }

    @Test
    void shouldReturnEmptyWhenEtudiantNotFoundByEmail() {
        // Arrange
        String email = "null";
        when(userAppRepository.findUserAppByEmail(email))
                .thenReturn(Optional.empty());

        // Act
        Optional<EtudiantDTO> response = etudiantService.getEtudiantByEmail(email);

        // Assert
        assertTrue(response.isEmpty());
    }

    @Test
    void shouldCreerUnCV() {
        // Arrange
        String email = "email@gmail.com";
        CVDTO cvDTO = new CVDTO("cvName", "cvType", "cvData", "cvStatus");
        when(cvRepository.save(any(CV.class)))
                .thenReturn(cvEntity);
        when(userAppRepository.findUserAppByEmail(email))
                .thenReturn(Optional.of(etudiantEntity));

        // Act
        Optional<CVDTO> response = etudiantService.creerCV(cvDTO, email);

        // Assert
        assertTrue(response.isPresent());
        assertEquals(cvEntity.getName(), response.get().getName());
        assertEquals(cvEntity.getType(), response.get().getType());
        assertEquals(cvEntity.getData(), response.get().getData());
        assertEquals(cvEntity.getStatus(), response.get().getStatus());
    }

    @Test
    void shouldReturnEmptyWhenExceptionIsThrownWhileCreatingCV() {
        // Arrange
        String email = "null@gmail.com";
        CVDTO cvDTO = new CVDTO("cvName", "cvType",  "cvData", "cvStatus");
        when(cvRepository.save(any(CV.class)))
                .thenThrow(new RuntimeException("Database error"));
        when(userAppRepository.findUserAppByEmail(email))
                .thenReturn(Optional.of(etudiantEntity));

        // Act
        Optional<CVDTO> response = etudiantService.creerCV(cvDTO, email);

        // Assert
        assertTrue(response.isEmpty());
    }

    @Test
    void shouldDeleteCV() {
        // Arrange
        Long cvId = 1L;

        // Act
        etudiantService.supprimerCV(cvId);

        // Assert
        verify(cvRepository, times(1)).deleteById(cvId);
    }
}
