package com.lacouf.rsbjwt.presentation;

import com.lacouf.rsbjwt.model.Employeur;
import com.lacouf.rsbjwt.service.EmployeurService;
import com.lacouf.rsbjwt.service.OffreDeStageService;
import com.lacouf.rsbjwt.service.dto.OffreDeStageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class OffreDeStageControllerTest {
    private OffreDeStageService offreDeStageService;
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
        Employeur employeur = new Employeur("John", "Doe", email, "password", "123456789", "Entreprise");


        Mockito.when(employeurService.findByCredentials_Email(email))
                .thenReturn(Optional.of(employeur));
        Mockito.when(offreDeStageService.creerOffreDeStage(Mockito.any(), Mockito.eq(Optional.of(employeur))))
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
    }


    @Test
    void getOffresEmployeur() {
        String email = "s@d.com";
        Employeur employeur = new Employeur("John", "Doe", email, "password", "123456789", "Entreprise");

        Mockito.when(employeurService.findByCredentials_Email(email))
                .thenReturn(Optional.of(employeur));

        Mockito.when(offreDeStageService.getOffresEmployeur(employeur)).thenReturn(Optional.of(List.of(new OffreDeStageDTO())));

        ResponseEntity<List<OffreDeStageDTO>> response = controller.getOffresEmployeur(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void getOffresEmployeur_with_no_email() {
        ResponseEntity<List<OffreDeStageDTO>> response = controller.getOffresEmployeur(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }



    @Test
    void deleteOffreDeStage() {
        Long id = 1L;
        ResponseEntity<Void> response = controller.deleteOffreDeStage(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void try_to_deleteOffreDeStage_with_no_id() {
        ResponseEntity<Void> response = controller.deleteOffreDeStage(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateOffreDeStage() {
        Long id = 1L;
        OffreDeStageDTO updatedOffre = new OffreDeStageDTO();
        Mockito.when(offreDeStageService.updateOffreDeStage(id, updatedOffre)).thenReturn(Optional.of(updatedOffre));

        ResponseEntity<OffreDeStageDTO> response = controller.updateOffreDeStage(id, updatedOffre);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedOffre, response.getBody());
    }

    @Test
    void try_to_updateOffreDeStage_with_no_id() {
        ResponseEntity<OffreDeStageDTO> response = controller.updateOffreDeStage(null, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
