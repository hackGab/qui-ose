package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.Employeur;
import com.lacouf.rsbjwt.model.OffreDeStage;
import com.lacouf.rsbjwt.model.auth.Role;
import com.lacouf.rsbjwt.repository.OffreDeStageRepository;
import com.lacouf.rsbjwt.service.dto.CredentialDTO;
import com.lacouf.rsbjwt.service.dto.EmployeurDTO;
import com.lacouf.rsbjwt.service.dto.OffreDeStageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class OffreDeStageServiceTest {

    private OffreDeStageRepository offreDeStageRepository;
    private OffreDeStageService offreDeStageService;

    private Employeur employeurEntity;
    private OffreDeStage offreDeStageEntity;
    private OffreDeStageDTO newOffreDTO;
    private EmployeurDTO newEmployeurDTO;

    @BeforeEach
    public void setUp() {
        offreDeStageRepository = Mockito.mock(OffreDeStageRepository.class);
        offreDeStageService = new OffreDeStageService(offreDeStageRepository);

        CredentialDTO credentials = new CredentialDTO("email@gmail.com", "password");
        newEmployeurDTO = new EmployeurDTO("John", "Doe", "email@gmail.com", Role.EMPLOYEUR, credentials, "Entreprise");
        employeurEntity = new Employeur("John", "Doe", "email@gmail.com", "password", "123456789", "Entreprise");
        newOffreDTO = new OffreDeStageDTO(1L,"Internship", "Paris", LocalDate.now(), LocalDate.now(), "data", 5, newEmployeurDTO);
        offreDeStageEntity = new OffreDeStage("Internship", "Paris", LocalDate.now(), "data", 5);
    }


    @Test
    public void test_creerOffreDeStage() {
        // Arrange
        when(offreDeStageRepository.save(any(OffreDeStage.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Optional<OffreDeStageDTO> response = offreDeStageService.creerOffreDeStage(newOffreDTO, Optional.of(employeurEntity));

        // Assert
        assertTrue(response.isPresent());
        assertNotNull(response.get());
        assertEquals(newOffreDTO.getTitre(), response.get().getTitre());
        assertEquals(newOffreDTO.getLocalisation(), response.get().getLocalisation());
        assertEquals(newOffreDTO.getDateLimite(), response.get().getDateLimite());
        assertEquals(newOffreDTO.getData(), response.get().getData());
        assertEquals(newOffreDTO.getNbCandidats(), response.get().getNbCandidats());
        assertEquals(newOffreDTO.getEmployeur().getFirstName(), response.get().getEmployeur().getFirstName());
        assertEquals(newOffreDTO.getEmployeur().getLastName(), response.get().getEmployeur().getLastName());
    }

    @Test
    public void test_creerOffreDeStage_catchBlock() {
        // Arrange
        when(offreDeStageRepository.save(any(OffreDeStage.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        Optional<OffreDeStageDTO> response = offreDeStageService.creerOffreDeStage(newOffreDTO, Optional.of(employeurEntity));

        // Assert
        assertFalse(response.isPresent());
    }

    @Test
    public void test_creerOffreDeStage_elseBlock() {
        // Arrange
        when(offreDeStageRepository.save(any(OffreDeStage.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Optional<OffreDeStageDTO> response = offreDeStageService.creerOffreDeStage(newOffreDTO, Optional.empty());

        // Assert
        assertFalse(response.isPresent());
    }

    @Test
    public void test_returnErrorWhenIdIsNull() {
        // Arrange
        Long offreId = null;

        // Act
        Optional<OffreDeStageDTO> response = offreDeStageService.getOffreDeStageById(offreId);

        // Assert
        assertFalse(response.isPresent());
    }

    @Test
    public void test_deleteOffreDeStage() {
        // Arrange
        Long offreId = 1L;
        OffreDeStage offreDeStage = new OffreDeStage("Internship", "Paris", LocalDate.now(), "data", 5);
        offreDeStage.setId(offreId);

        // Act
        offreDeStageService.deleteOffreDeStage(offreId);

        // Assert
        Mockito.verify(offreDeStageRepository).deleteById(offreId);
    }
}
