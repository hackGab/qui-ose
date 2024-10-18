package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.Employeur;
import com.lacouf.rsbjwt.model.Etudiant;
import com.lacouf.rsbjwt.model.OffreDeStage;
import com.lacouf.rsbjwt.model.auth.Credentials;
import com.lacouf.rsbjwt.model.auth.Role;
import com.lacouf.rsbjwt.repository.EmployeurRepository;
import com.lacouf.rsbjwt.repository.OffreDeStageRepository;
import com.lacouf.rsbjwt.service.dto.CredentialDTO;
import com.lacouf.rsbjwt.service.dto.EmployeurDTO;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import com.lacouf.rsbjwt.service.dto.OffreDeStageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class OffreDeStageServiceTest {

    private OffreDeStageRepository offreDeStageRepository;
    private OffreDeStageService offreDeStageService;

    private EmployeurRepository employeurRepository;

    private Employeur employeurEntity;

    private OffreDeStage offreDeStageEntity;
    private OffreDeStageDTO newOffreDTO;
    private EmployeurDTO newEmployeurDTO;

    @BeforeEach
    public void setUp() {
        offreDeStageRepository = Mockito.mock(OffreDeStageRepository.class);
        employeurRepository = Mockito.mock(EmployeurRepository.class);
        offreDeStageService = new OffreDeStageService(offreDeStageRepository, employeurRepository);

        CredentialDTO credentials = new CredentialDTO("email@gmail.com", "password");
        newEmployeurDTO = new EmployeurDTO("John", "Doe", "email@gmail.com", Role.EMPLOYEUR, credentials, "Entreprise");
        employeurEntity = new Employeur("John", "Doe", "email@gmail.com", "password", "123456789", "Entreprise");
        newOffreDTO = new OffreDeStageDTO(1L,"Internship", "Paris", LocalDate.now(), LocalDate.now(), "data", 5,"", newEmployeurDTO, new ArrayList<>());
        offreDeStageEntity = new OffreDeStage("Internship", "Paris", LocalDate.now(), "data", 5, "status");
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
        OffreDeStage offreDeStage = new OffreDeStage("Internship", "Paris", LocalDate.now(), "data", 5, "status");
        offreDeStage.setId(offreId);

        // Act
        offreDeStageService.deleteOffreDeStage(offreId);

        // Assert
        Mockito.verify(offreDeStageRepository).deleteById(offreId);
    }

    @Test
    void getOffresEmployeur_ShouldReturnOffres_WhenEmployeurHasOffres() {
        // Arrange
        OffreDeStage offreDeStage = new OffreDeStage();
        offreDeStage.setTitre("Stage en développement");
        offreDeStage.setLocalisation("Paris");
        offreDeStage.setDateLimite(LocalDate.now().plusDays(30));
        offreDeStage.setData("Description du stage");
        offreDeStage.setNbCandidats(10);
        offreDeStage.setStatus("Ouvert");
        offreDeStage.setEmployeur(employeurEntity);

        List<OffreDeStage> offresList = List.of(offreDeStage);


        Employeur employeurEntity = new Employeur();
        employeurEntity.setId(1L);
        employeurEntity.setFirstName("John");
        employeurEntity.setLastName("Doe");
        employeurEntity.setCredentials(new Credentials("email", "password", Role.EMPLOYEUR));

        when(offreDeStageRepository.findByEmployeur(employeurEntity)).thenReturn(offresList);

        // Act
        Optional<List<OffreDeStageDTO>> response = offreDeStageService.getOffresEmployeur(employeurEntity);

        // Assert
        assertTrue(response.isPresent());
        assertEquals(1, response.get().size());

        OffreDeStageDTO returnedOffre = response.get().getFirst();
        assertEquals(offreDeStage.getTitre(), returnedOffre.getTitre());
        assertEquals(offreDeStage.getLocalisation(), returnedOffre.getLocalisation());
        assertEquals(offreDeStage.getDateLimite(), returnedOffre.getDateLimite());
        assertEquals(offreDeStage.getData(), returnedOffre.getData());
        assertEquals(offreDeStage.getNbCandidats(), returnedOffre.getNbCandidats());
        assertEquals(offreDeStage.getStatus(), returnedOffre.getStatus());
    }

    @Test
    void getOffresEmployeur_ShouldThrowException_WhenEmployeurEmailIsNull() {
        // Arrange
        Employeur employeurEntity = new Employeur();
        employeurEntity.setId(1L);
        employeurEntity.setFirstName("John");
        employeurEntity.setLastName("Doe");
        employeurEntity.setPhoneNumber("1234567890");
        employeurEntity.setEntreprise("Tech Company");

        Credentials credentials = new Credentials("", "password", Role.EMPLOYEUR);
        employeurEntity.setCredentials(credentials);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> offreDeStageService.getOffresEmployeur(employeurEntity));
    }

    @Test
    void updateOffreDeStage() {
        // Arrange
        OffreDeStageDTO updatedOffreDTO = new OffreDeStageDTO(1L, "Stage en développement", "Paris", LocalDate.now().plusDays(30), LocalDate.now().plusDays(60), "Description du stage", 10, "Ouvert", newEmployeurDTO, new ArrayList<>());

        OffreDeStage updatedOffre = new OffreDeStage();
        updatedOffre.setId(1L);
        updatedOffre.setTitre("Stage en développement");
        updatedOffre.setLocalisation("Paris");
        updatedOffre.setDateLimite(LocalDate.now().plusDays(30));
        updatedOffre.setData("Description du stage");
        updatedOffre.setNbCandidats(10);
        updatedOffre.setStatus("Ouvert");
        updatedOffre.setEmployeur(employeurEntity);

        when(offreDeStageRepository.findById(1L)).thenReturn(Optional.of(offreDeStageEntity));
        when(offreDeStageRepository.save(any(OffreDeStage.class))).thenReturn(updatedOffre);

        // Act
        Optional<OffreDeStageDTO> response = offreDeStageService.updateOffreDeStage(1L, updatedOffreDTO);

        // Assert
        assertTrue(response.isPresent());
        assertEquals(updatedOffreDTO.getTitre(), response.get().getTitre());
        assertEquals(updatedOffreDTO.getLocalisation(), response.get().getLocalisation());
        assertEquals(updatedOffreDTO.getDateLimite(), response.get().getDateLimite());
        assertEquals(updatedOffreDTO.getData(), response.get().getData());
        assertEquals(updatedOffreDTO.getNbCandidats(), response.get().getNbCandidats());
        assertEquals(updatedOffreDTO.getStatus(), response.get().getStatus());
    }

    @Test
    public void test_getEtudiantsByOffre() {
        // Arrange
        Long offreId = 1L;
        OffreDeStage offreDeStage = new OffreDeStage();
        offreDeStage.setId(offreId);

        Etudiant etudiant1 = new Etudiant("John", "Doe", "john.doe@example.com", "password", "1234567890", "Computer Science");
        Etudiant etudiant2 = new Etudiant("Jane", "Smith", "jane.smith@example.com", "password", "0987654321", "Information Technology");

        offreDeStage.setEtudiants(List.of(etudiant1, etudiant2));

        when(offreDeStageRepository.findById(offreId)).thenReturn(Optional.of(offreDeStage));

        // Act
        Optional<List<EtudiantDTO>> response = offreDeStageService.getEtudiantsByOffre(offreId);

        // Assert
        assertTrue(response.isPresent());
        List<EtudiantDTO> etudiants = response.get();
        assertEquals(2, etudiants.size());

        EtudiantDTO etudiantDTO1 = etudiants.get(0);
        assertEquals(etudiant1.getFirstName(), etudiantDTO1.getFirstName());
        assertEquals(etudiant1.getLastName(), etudiantDTO1.getLastName());
        assertEquals(etudiant1.getEmail(), etudiantDTO1.getEmail());

        EtudiantDTO etudiantDTO2 = etudiants.get(1);
        assertEquals(etudiant2.getFirstName(), etudiantDTO2.getFirstName());
        assertEquals(etudiant2.getLastName(), etudiantDTO2.getLastName());
        assertEquals(etudiant2.getEmail(), etudiantDTO2.getEmail());
    }

    @Test
    public void test_getEtudiantsByOffre_OffreNotFound() {
        // Arrange
        Long offreId = 1L;
        when(offreDeStageRepository.findById(offreId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> offreDeStageService.getEtudiantsByOffre(offreId));
    }
}
