package com.lacouf.rsbjwt.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lacouf.rsbjwt.service.*;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import com.lacouf.rsbjwt.service.dto.OffreDeStageDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
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
    private PasswordEncoder passwordEncoder;

    @MockBean
    private EmployeurService employeurService;



    @MockBean
    private ProfesseurService professeurService;

    @MockBean
    private GestionnaireService gestionnaireService;


    @MockBean
    private UserAppService userService;

    @Test
    @WithMockUser(username = "user", roles = {"ETUDIANT"})
    public void shouldCreateEtudiant() throws Exception {
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
    public void shouldReturnNotFoundIfEtudiantNotFoundById() throws Exception {
        Mockito.when(etudiantService.getEtudiantById(1L))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/etudiant/1")
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

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/etudiant/credentials/" + email)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(etudiantDTO)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"ETUDIANT"})
    public void shouldReturnNotFoundIfEtudiantNotFoundByEmail() throws Exception {
        String email = "emailnotfound@gmail.com";

        when(etudiantService.getEtudiantByEmail(email))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/etudiant/credentials/" + email)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"ETUDIANT"})
    public void shouldReturnAllEtudiants() throws Exception {
        EtudiantDTO etudiantDTO = new EtudiantDTO("John", "Doe", null, null, null, null);
        when(etudiantService.getAllEtudiants())
                .thenReturn(Arrays.asList(etudiantDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/etudiant/all")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(Arrays.asList(etudiantDTO))));
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
    public void shouldReturnNotFoundIfAddingOffreDeStageFails() throws Exception {
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
    public void shouldRemoveOffreDeStageSuccessfully() throws Exception {
        String etudiantEmail = "john.doe@example.com";
        Long offreId = 1L;
        EtudiantDTO etudiantDTO = new EtudiantDTO("John", "Doe", null, null, null, null);

        when(etudiantService.retirerOffreDeStage(etudiantEmail, offreId))
                .thenReturn(Optional.of(etudiantDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/etudiant/" + etudiantEmail + "/retirerOffre/" + offreId)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(etudiantDTO)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"ETUDIANT"})
    public void shouldReturnNotFoundIfRemovingOffreDeStageFails() throws Exception {
        String etudiantEmail = "john.doe@example.com";
        Long offreId = 1L;

        when(etudiantService.retirerOffreDeStage(etudiantEmail, offreId))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/etudiant/" + etudiantEmail + "/retirerOffre/" + offreId)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"ETUDIANT"})
    public void shouldReturnOffresDeStage() throws Exception {
        String etudiantEmail = "john.doe@example.com";
        OffreDeStageDTO offreDeStageDTO = new OffreDeStageDTO();
        when(etudiantService.getOffresDeStage(etudiantEmail))
                .thenReturn(Arrays.asList(offreDeStageDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/etudiant/" + etudiantEmail + "/offres")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(Arrays.asList(offreDeStageDTO))));
    }
}
