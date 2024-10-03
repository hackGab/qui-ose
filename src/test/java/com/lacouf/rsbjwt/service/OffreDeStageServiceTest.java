package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.Employeur;
import com.lacouf.rsbjwt.model.OffreDeStage;
import com.lacouf.rsbjwt.repository.OffreDeStageRepository;
import com.lacouf.rsbjwt.service.dto.OffreDeStageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class OffreDeStageServiceTest {

    private OffreDeStageRepository offreDeStageRepository;
    private EmployeurService employeurService;
    private OffreDeStageService offreDeStageService;

    private Employeur employeurEntity;

    @BeforeEach
    public void setUp() {
        offreDeStageRepository = Mockito.mock(OffreDeStageRepository.class);

        offreDeStageService = new OffreDeStageService(offreDeStageRepository);

        employeurEntity = new Employeur("John", "Doe", "email@gmail.com", "password", "123456789", "Entreprise");

    }


    @Test
    public void test_creerOffreDeStage() {
        // Arrange
        OffreDeStage newOffre = new OffreDeStage();
        newOffre.setTitre("Internship");


        when(offreDeStageRepository.save(any(OffreDeStage.class)))
                .thenReturn(newOffre);

        newOffre.setEmployeur(employeurEntity);
        OffreDeStageDTO offreDeStageDTO = new OffreDeStageDTO(newOffre);

        when(offreDeStageService.creerOffreDeStage(offreDeStageDTO, Optional.of(employeurEntity)))
                .thenReturn(Optional.of(offreDeStageDTO));

        // Act
        Optional<OffreDeStageDTO> response = offreDeStageService.creerOffreDeStage(offreDeStageDTO, Optional.of(employeurEntity));

        // Assert
        assertTrue(response.isPresent());
        assertEquals(newOffre.getTitre(), response.get().getTitre());
    }


//    @Test
//    public void test_creerOffreDeStage_catchBlock() {
//        // Arrange
//        OffreDeStageDTO newOffreDTO = new OffreDeStageDTO();
//        String email = employeurEntity.getEmail();
//
//        when(employeurService.findByCredentials_Email(email))
//                .thenReturn(Optional.of(employeurEntity));
//
//        when(offreDeStageRepository.save(any(OffreDeStage.class)))
//                .thenThrow(new RuntimeException());
//
//        // Act
//        Optional<OffreDeStageDTO> response = offreDeStageService.creerOffreDeStage(newOffreDTO, email);
//
//        // Assert
//        assertFalse(response.isPresent());
//    }
//
//    @Test
//    public void test_creerOffreDeStage_elseBlock() {
//        // Arrange
//        OffreDeStageDTO newOffreDTO = new OffreDeStageDTO();
//        String email = employeurEntity.getEmail();
//
//        when(employeurService.findByCredentials_Email(email))
//                .thenReturn(Optional.empty());
//
//        // Act
//        Optional<OffreDeStageDTO> response = offreDeStageService.creerOffreDeStage(newOffreDTO, email);
//
//        // Assert
//        assertFalse(response.isPresent());
//    }
//
//
//
//    @Test
//    public void test_getOffreDeStageById() {
//        // Arrange
//        OffreDeStage newOffre = new OffreDeStage();
//        newOffre.setTitre("Internship");
//        Long id = 1L;
//
//        Employeur employeur = employeurEntity;
//        newOffre.setEmployeur(employeur);
//
//        when(offreDeStageRepository.findById(id))
//                .thenReturn(Optional.of(newOffre));
//
//        // Act
//        Optional<OffreDeStageDTO> response = offreDeStageService.getOffreDeStageById(id);
//
//        // Assert
//        assertTrue(response.isPresent());
//        assertEquals(newOffre.getTitre(), response.get().getTitre());
//    }

}
