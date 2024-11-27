package com.lacouf.rsbjwt.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lacouf.rsbjwt.model.Departement;
import com.lacouf.rsbjwt.model.Employeur;
import com.lacouf.rsbjwt.model.Etudiant;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import com.lacouf.rsbjwt.model.auth.Credentials;
import com.lacouf.rsbjwt.model.auth.Role;
import com.lacouf.rsbjwt.service.*;
import com.lacouf.rsbjwt.service.dto.CredentialDTO;
import com.lacouf.rsbjwt.service.dto.EmployeurDTO;
import com.lacouf.rsbjwt.service.dto.OffreDeStageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(OffreDeStageController.class)
public class OffreDeStageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfesseurService professeurService;

    @MockBean
    private EtudiantService etudiantService;

    @MockBean
    private EmployeurService employeurService;
    private OffreDeStageController controller;

    @MockBean
    private GestionnaireService gestionnaireService;

    @MockBean
    private UserAppService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    public void shouldCreateOffreDeStage() throws Exception {

        Employeur employeurEntity = new Employeur("John", "Doe",
                null, "a@b.com", "123", "Entreprise");
        employeurEntity.setId(46L);

        when(employeurService.findByCredentials_Email(any(String.class)))
                .thenReturn(Optional.of(employeurEntity));

        OffreDeStageDTO offreDeStageDTO = new OffreDeStageDTO();
        offreDeStageDTO.setTitre("Titre du stage");

        when(employeurService.creerOffreDeStage(any(OffreDeStageDTO.class), any(Optional.class)))
                .thenReturn(Optional.of(offreDeStageDTO));


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/offreDeStage/creerOffreDeStage/a@b.com")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(new ObjectMapper().writeValueAsString(offreDeStageDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(offreDeStageDTO)));
    }


    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    public void test_create_offre_de_stage_with_none_data() throws Exception {
        OffreDeStageDTO offreDeStageDTO = new OffreDeStageDTO();
        offreDeStageDTO.setTitre("Titre du stage");


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/offreDeStage/creerOffreDeStage/a@b.com")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(new ObjectMapper().writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    public void test_getOffreDeStageById() throws Exception {
        OffreDeStageDTO offreDeStageDTO = new OffreDeStageDTO();
        offreDeStageDTO.setTitre("Titre du stage");

        when(employeurService.getOffreDeStageById(anyLong()))
                .thenReturn(Optional.of(offreDeStageDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/offreDeStage/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(offreDeStageDTO)));
    }


    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    public void test_getOffreDeStageById_withNoId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/offreDeStage/")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    void getOffresEmployeur() throws Exception {

        OffreDeStageDTO offreDeStageDTO = new OffreDeStageDTO();
        offreDeStageDTO.setTitre("Titre du stage");

        OffreDeStageDTO offreDeStageDTO2 = new OffreDeStageDTO();
        offreDeStageDTO2.setTitre("Titre du stage 2");

        when(employeurService.findByCredentials_Email(any(String.class)))
                .thenReturn(Optional.of(new Employeur()));

        when(employeurService.getOffresEmployeurSession(any(Employeur.class), anyString()))
                .thenReturn(List.of(offreDeStageDTO, offreDeStageDTO2));


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/offreDeStage/offresEmployeur/a@b.vom/session/HIV25")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(List.of(offreDeStageDTO, offreDeStageDTO2))));

    }


    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    void getOffresEmployeur_with_no_email() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/offreDeStage/offresEmployeur/")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }


    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    void updateOffreDeStage() throws Exception {
        OffreDeStageDTO offreDeStageDTO = new OffreDeStageDTO();

        when(employeurService.updateOffreDeStage(anyLong(), any(OffreDeStageDTO.class)))
                .thenReturn(Optional.of(offreDeStageDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/offreDeStage/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(new ObjectMapper().writeValueAsString(offreDeStageDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    void try_to_updateOffreDeStage_with_random_id() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/offreDeStage/")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(new ObjectMapper().writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }


    @Test
    @WithMockUser(username = "user", roles = {"ETUDIANT"})
    void getOffresValidees() throws Exception {

        when(etudiantService.getOffresApprouveesParSession(anyString()))
                .thenReturn(List.of(new OffreDeStageDTO()));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/offreDeStage/offresValidees/session/HIVER25")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    public void test_getEtudiantsByOffre() throws Exception {
        Long offreId = 1L;

        EtudiantDTO etudiantDTO1 = new EtudiantDTO("John", "Doe", Role.ETUDIANT, "1234567890", new CredentialDTO("john.doe@example.com", "password"), Departement.TECHNIQUES_INFORMATIQUE);
        EtudiantDTO etudiantDTO2 = new EtudiantDTO("Jane", "Smith", Role.ETUDIANT, "0987654321", new CredentialDTO("jane.smith@example.com", "password"), Departement.TECHNIQUES_INFORMATIQUE);

        when(employeurService.getEtudiantsByOffre(offreId))
                .thenReturn(Optional.of(List.of(etudiantDTO1, etudiantDTO2)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/offreDeStage/" + offreId + "/etudiants")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(List.of(etudiantDTO1, etudiantDTO2))));
    }



    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    public void test_getEtudiantsByOffre_InvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/offreDeStage/null/etudiants")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user", roles = {"GESTIONNAIRE"})
    public void test_getOffresBySession() throws Exception {
        String session = "HIVER25";

        OffreDeStageDTO offreDeStageDTO = new OffreDeStageDTO();

        when(gestionnaireService.getOffresBySession(session))
                .thenReturn(List.of(offreDeStageDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/offreDeStage/session/" + session)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
