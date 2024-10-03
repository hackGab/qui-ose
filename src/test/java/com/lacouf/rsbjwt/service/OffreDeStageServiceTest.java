package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.Employeur;
import com.lacouf.rsbjwt.model.OffreDeStage;
import com.lacouf.rsbjwt.repository.OffreDeStageRepository;
import com.lacouf.rsbjwt.service.dto.OffreDeStageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class OffreDeStageServiceTest {

    private OffreDeStageRepository offreDeStageRepository;
    private EmployeurService employeurService;
    private OffreDeStageService offreDeStageService;


    @BeforeEach
    public void setUp() {
        offreDeStageRepository = Mockito.mock(OffreDeStageRepository.class);
        employeurService = Mockito.mock(EmployeurService.class);
        offreDeStageService = new OffreDeStageService(offreDeStageRepository, employeurService);
    }


    @Test
    public void test_create_offre_de_stage_with_valid_data() {
        String email = "employer@example.com";
        Employeur employeur = new Employeur(
                "John",
                "Doe",
                email,
                "password",
                "1234567890",
                "TechCorp"
        );

        OffreDeStageDTO offreDeStageDTO = new OffreDeStageDTO();
        offreDeStageDTO.setTitre("Internship");

        Mockito.when(employeurService.findByEmail(email)).thenReturn(Optional.of(employeur));
        Mockito.when(offreDeStageRepository.save(Mockito.any(OffreDeStage.class))).thenReturn(new OffreDeStage());

        Optional<OffreDeStageDTO> result = offreDeStageService.creerOffreDeStage(offreDeStageDTO, email);

        assertTrue(result.isPresent());
        assertEquals("Internship", result.get().getTitre());
    }



}
