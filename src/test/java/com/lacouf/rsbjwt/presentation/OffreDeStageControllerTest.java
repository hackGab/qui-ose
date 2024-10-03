package com.lacouf.rsbjwt.presentation;

import com.lacouf.rsbjwt.service.EmployeurService;
import com.lacouf.rsbjwt.service.OffreDeStageService;
import com.lacouf.rsbjwt.service.dto.OffreDeStageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class OffreDeStageControllerTest {

    @Test
    public void test_create_offre_de_stage_with_valid_data() {
        OffreDeStageService offreDeStageService = Mockito.mock(OffreDeStageService.class);
        EmployeurService employeurService = Mockito.mock(EmployeurService.class);
        OffreDeStageController controller = new OffreDeStageController(offreDeStageService, employeurService);

        OffreDeStageDTO newOffre = new OffreDeStageDTO();
        newOffre.setTitre("Internship");
        String email = "employer@example.com";

        Mockito.when(offreDeStageService.creerOffreDeStage(Mockito.any(), Mockito.eq(email)))
                .thenReturn(Optional.of(newOffre));

        ResponseEntity<OffreDeStageDTO> response = controller.creerOffreDeStage(email, newOffre);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    public void test_create_offre_de_stage_with_none_data() {
        OffreDeStageService offreDeStageService = Mockito.mock(OffreDeStageService.class);
        EmployeurService employeurService = Mockito.mock(EmployeurService.class);
        OffreDeStageController controller = new OffreDeStageController(offreDeStageService, employeurService);

        ResponseEntity<OffreDeStageDTO> response = controller.creerOffreDeStage(null, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    public void test_getOffreDeStageById() {
        OffreDeStageService offreDeStageService = Mockito.mock(OffreDeStageService.class);
        OffreDeStageController controller = new OffreDeStageController(offreDeStageService, null);
        Long id = 1L;
        OffreDeStageDTO expectedOffre = new OffreDeStageDTO();
        Mockito.when(offreDeStageService.getOffreDeStageById(id)).thenReturn(Optional.of(expectedOffre));

        ResponseEntity<OffreDeStageDTO> response = controller.getOffreDeStageById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedOffre, response.getBody());
    }

    @Test
    public void test_getOffreDeStageById_withNoId() {
        OffreDeStageService offreDeStageService = Mockito.mock(OffreDeStageService.class);
        OffreDeStageController controller = new OffreDeStageController(offreDeStageService, null);

        ResponseEntity<OffreDeStageDTO> response = controller.getOffreDeStageById(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    /*private OffreDeStageService offreDeStageService;
    private EmployeurService employeurService;
    private OffreDeStageController controller;

    @BeforeEach
    public void setUp() {
        offreDeStageService = Mockito.mock(OffreDeStageService.class);
        employeurService = Mockito.mock(EmployeurService.class);
        controller = new OffreDeStageController(offreDeStageService, employeurService);
    }

    @Test
    public void test_create_offre_de_stage_with_valid_data() {
        OffreDeStageDTO newOffre = new OffreDeStageDTO();
        newOffre.setTitre("Internship");
        String email = "employer@example.com";

        Mockito.when(offreDeStageService.creerOffreDeStage(Mockito.any(), Mockito.eq(email)))
                .thenReturn(Optional.of(newOffre));

        ResponseEntity<OffreDeStageDTO> response = controller.creerOffreDeStage(email, newOffre);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void test_create_offre_de_stage_with_none_data() {
        ResponseEntity<OffreDeStageDTO> response = controller.creerOffreDeStage(null, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void test_getOffreDeStageById() {
        Long id = 1L;
        OffreDeStageDTO expectedOffre = new OffreDeStageDTO();
        Mockito.when(offreDeStageService.getOffreDeStageById(id)).thenReturn(Optional.of(expectedOffre));

        ResponseEntity<OffreDeStageDTO> response = controller.getOffreDeStageById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedOffre, response.getBody());
    }

    @Test
    public void test_getOffreDeStageById_withNoId() {
        ResponseEntity<OffreDeStageDTO> response = controller.getOffreDeStageById(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }*/




}
