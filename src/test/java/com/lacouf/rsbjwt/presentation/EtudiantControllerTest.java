package com.lacouf.rsbjwt.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lacouf.rsbjwt.presentation.EtudiantController;
import com.lacouf.rsbjwt.service.*;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(EtudiantController.class)
class EtudiantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EtudiantService etudiantService;

    @MockBean
    private GestionnaireService gestionnaireService;

    @MockBean
    private ProfesseurService professeurService;

    @MockBean
    private EmployeurService employeurService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserAppService userService;

    @Test
    @WithMockUser(username = "user", roles = {"ETUDIANT"})
    public void shouldCreateEtudiant() throws Exception {
        System.out.println("shouldCreateEtudiant");
        EtudiantDTO etudiantDTO = new EtudiantDTO("John", "Doe", null, null, null, null);
        Mockito.when(etudiantService.creerEtudiant(any(EtudiantDTO.class)))
                .thenReturn(Optional.of(etudiantDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/etudiant/creerEtudiant")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(new ObjectMapper().writeValueAsString(etudiantDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(etudiantDTO)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"ETUDIANT"})
    public void shouldReturnEtudiantWhenFound() throws Exception {
        EtudiantDTO etudiantDTO = new EtudiantDTO("John", "Doe", null, null, null, null);
        Mockito.when(etudiantService.getEtudiantById(1L))
                .thenReturn(Optional.of(etudiantDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/etudiant/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(etudiantDTO)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"ETUDIANT"})
    public void shouldAddOffreDeStageSuccessfully() throws Exception {
        String etudiantEmail = "john.doe@example.com";
        Long offreId = 1L;
        EtudiantDTO etudiantDTO = new EtudiantDTO("John", "Doe", null, null, null, null);

        when(etudiantService.ajouterOffreDeStage(etudiantEmail, offreId))
                .thenReturn(Optional.of(etudiantDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/etudiant/" + etudiantEmail + "/offre/" + offreId)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(etudiantDTO)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"ETUDIANT"})
    public void shouldReturnNotFoundIfEtudiantNotFound() throws Exception {
        String etudiantEmail = "john.doe@example.com";
        Long offreId = 1L;

        when(etudiantService.ajouterOffreDeStage(etudiantEmail, offreId))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/etudiant/" + etudiantEmail + "/offre/" + offreId)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"ETUDIANT"})
    public void shouldReturnEtudiantByEmail() throws Exception {
        String email = "email@gmail.com";
        EtudiantDTO etudiantDTO = new EtudiantDTO("John", "Doe", null, null, null, null);

        when(etudiantService.getEtudiantByEmail(email))
                .thenReturn(Optional.of(etudiantDTO));
    }

    @Test
    @WithMockUser(username = "user", roles = {"ETUDIANT"})
    public void shouldRemoveOffreDeStage() throws Exception {
        String etudiantEmail = "email@gmail.com";
        Long offreId = 1L;
        EtudiantDTO etudiantDTO = new EtudiantDTO("John", "Doe", null, null, null, null);

        when(etudiantService.retirerOffreDeStage(etudiantEmail, offreId))
                .thenReturn(Optional.of(etudiantDTO));
    }

    @Test
    @WithMockUser(username = "user", roles = {"ETUDIANT"})
    public void shouldReturnNotFoundIfEtudiantOrOffreNotFound() throws Exception {
        String etudiantEmail = "emailnot@gmail.com";
        Long offreId = 1L;

        when(etudiantService.retirerOffreDeStage(etudiantEmail, offreId))
                .thenReturn(Optional.empty());
    }

    @Test
    @WithMockUser(username = "user", roles = {"ETUDIANT"})
    public void shouldReturnAllEtudiants() throws Exception {
        EtudiantDTO etudiantDTO = new EtudiantDTO("John", "Doe", null, null, null, null);
        when(etudiantService.getAllEtudiants())
                .thenReturn(java.util.Arrays.asList(etudiantDTO));
    }

    @Test
    @WithMockUser(username = "user", roles = {"ETUDIANT"})
    public void shouldReturnOffresDeStage() throws Exception {
        String etudiantEmail = "email@gmail.com";
        when(etudiantService.getOffresDeStage(etudiantEmail))
                .thenReturn(java.util.Arrays.asList());
    }

    @Test
    @WithMockUser(username = "user", roles = {"ETUDIANT"})
    public void shouldReturnBadRequestIfEmailOrOffreIdIsNull() throws Exception {
        String email = null;
        Long offreId = null;
        when(etudiantService.retirerOffreDeStage(email, offreId))
                .thenReturn(Optional.empty());
    }

    @Test
    @WithMockUser(username = "user", roles = {"ETUDIANT"})
    public void shouldReturnBadRequestIfEmailIsNull() throws Exception {
        String email = null;
        Long offreId = 1L;
        when(etudiantService.retirerOffreDeStage(email, offreId))
                .thenReturn(Optional.empty());
    }

    @Test
    @WithMockUser(username = "user", roles = {"ETUDIANT"})
    public void shouldReturnBadRequestIfOffreIdIsNull() throws Exception {
        String email = "email@gmail.com";
        Long offreId = null;
        when(etudiantService.retirerOffreDeStage(email, offreId))
                .thenReturn(Optional.empty());
    }
}