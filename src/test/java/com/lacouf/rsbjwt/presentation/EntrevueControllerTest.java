package com.lacouf.rsbjwt.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lacouf.rsbjwt.service.*;
import com.lacouf.rsbjwt.service.dto.EntrevueDTO;
import com.lacouf.rsbjwt.repository.UserAppRepository;
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

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

@WebMvcTest(EntrevueController.class)
public class EntrevueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeurService employeurService;

    @MockBean
    private EtudiantService etudiantService;

    @MockBean
    private UserAppRepository userAppRepository;

    @MockBean
    private GestionnaireService gestionnaireService;


    @MockBean
    private ProfesseurService professeurService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserAppService userService;

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    public void shouldCreateEntrevue() throws Exception {
        EntrevueDTO entrevueDTO = new EntrevueDTO();
        Mockito.when(employeurService.createEntrevue(any(EntrevueDTO.class), anyString(), anyLong()))
                .thenReturn(Optional.of(entrevueDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/entrevues/creerEntrevue/test@example.com/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(new ObjectMapper().writeValueAsString(entrevueDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(entrevueDTO)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    public void shouldGetEntrevueById() throws Exception {
        EntrevueDTO entrevueDTO = new EntrevueDTO();
        Mockito.when(employeurService.getEntrevueById(anyLong()))
                .thenReturn(Optional.of(entrevueDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/entrevues/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(entrevueDTO)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    public void shouldReturnNotFoundWhenEntrevueNotFound() throws Exception {
        Mockito.when(employeurService.getEntrevueById(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/entrevues/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"ETUDIANT"})
    public void shouldGetEntrevuesByEtudiant() throws Exception {
        List<EntrevueDTO> entrevues = List.of(new EntrevueDTO(), new EntrevueDTO());
        Mockito.when(etudiantService.getEntrevuesByEtudiantAndSession(anyString(), anyString()))
                .thenReturn(entrevues);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/entrevues/etudiant/test@example.com/session/HIVER25")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(entrevues)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"ETUDIANT"})
    public void shouldGetEntrevuesEnAttenteByEtudiant() throws Exception {
        List<EntrevueDTO> entrevues = List.of(new EntrevueDTO());
        Mockito.when(etudiantService.getEntrevuesEnAttenteByEtudiant(anyString()))
                .thenReturn(entrevues);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/entrevues/enAttente/etudiant/test@example.com")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(entrevues)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    public void shouldGetEntrevuesByOffre() throws Exception {
        List<EntrevueDTO> entrevues = List.of(new EntrevueDTO());
        Mockito.when(employeurService.getEntrevuesByOffre(anyLong()))
                .thenReturn(entrevues);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/entrevues/offre/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(entrevues)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"ETUDIANT"})
    public void shouldChangeEntrevueStatus() throws Exception {
        EntrevueDTO entrevueDTO = new EntrevueDTO();
        Mockito.when(etudiantService.changerStatusEntrevue(anyString(), anyLong(), anyString()))
                .thenReturn(Optional.of(entrevueDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/entrevues/changerStatus/test@example.com/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content("Accepted")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(entrevueDTO)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"ETUDIANT"})
    public void shouldReturnBadRequestWhenEntrevueStatusChangeFails() throws Exception {
        Mockito.when(etudiantService.changerStatusEntrevue(anyString(), anyLong(), anyString()))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/entrevues/changerStatus/test@example.com/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content("Accepted")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    public void shouldGetEntrevuesAccepteesByOffre() throws Exception {
        List<EntrevueDTO> entrevues = List.of(new EntrevueDTO());
        Mockito.when(employeurService.getEntrevuesAccepteesByOffre(anyLong()))
                .thenReturn(entrevues);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/entrevues/entrevueAcceptee/offre/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(entrevues)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"ETUDIANT"})
    public void shouldGetEntrevuesAccepteesByEtudiant() throws Exception {
        List<EntrevueDTO> entrevues = List.of(new EntrevueDTO());
        Mockito.when(etudiantService.getEntrevuesAccepteesByEtudiant(anyString()))
                .thenReturn(entrevues);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/entrevues/acceptees/etudiant/test@example.com")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(entrevues)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    public void shouldGetEntrevuesAccepteesParEmployeur() throws Exception {
        List<EntrevueDTO> entrevues = List.of(new EntrevueDTO());
        Mockito.when(employeurService.getEntrevuesAccepteesParEmployeur(anyString(), anyString()))
                .thenReturn(entrevues);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/entrevues/acceptees/employeur/test@example.com/session/HIVER25")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(entrevues)));
    }
}