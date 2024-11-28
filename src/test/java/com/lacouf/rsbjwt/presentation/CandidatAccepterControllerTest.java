package com.lacouf.rsbjwt.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lacouf.rsbjwt.repository.GestionnaireRepository;
import com.lacouf.rsbjwt.service.*;
import com.lacouf.rsbjwt.service.dto.CandidatAccepterDTO;
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

import static org.mockito.ArgumentMatchers.anyLong;

@WebMvcTest(CandidatAccepterController.class)
public class CandidatAccepterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeurService employeurService;

    @MockBean
    private EtudiantService etudiantService;

    @MockBean
    private ProfesseurService professeurService;

    @MockBean
    private GestionnaireService gestionnaireService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserAppService userService;

    @MockBean
    private GestionnaireRepository gestionnaireRepository;

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    public void shouldAcceptCandidature() throws Exception {
        CandidatAccepterDTO candidatAccepterDTO = new CandidatAccepterDTO();
        Mockito.when(employeurService.accepterCandidature(anyLong()))
                .thenReturn(Optional.of(candidatAccepterDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/candidatures/accepter/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(candidatAccepterDTO)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    public void shouldRefuseCandidature() throws Exception {
        CandidatAccepterDTO candidatAccepterDTO = new CandidatAccepterDTO();
        Mockito.when(employeurService.refuserCandidature(anyLong()))
                .thenReturn(Optional.of(candidatAccepterDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/candidatures/refuser/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(candidatAccepterDTO)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    public void shouldReturnNotFoundWhenCandidatureNotFound() throws Exception {
        Mockito.when(employeurService.accepterCandidature(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/candidatures/accepter/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    public void shouldGetCandidatureDecision() throws Exception {
        CandidatAccepterDTO candidatAccepterDTO = new CandidatAccepterDTO();
        Mockito.when(employeurService.getCandidatureDecision(anyLong()))
                .thenReturn(Optional.of(candidatAccepterDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/candidatures/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(candidatAccepterDTO)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    public void shouldReturnNotFoundWhenDecisionNotFound() throws Exception {
        Mockito.when(employeurService.getCandidatureDecision(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/candidatures/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    public void shouldGetAllCandidaturesFromSession() throws Exception {
        CandidatAccepterDTO candidatAccepterDTO = new CandidatAccepterDTO();
        Mockito.when(gestionnaireService.getCandidaturesBySession("HIVER25"))
                .thenReturn(List.of(candidatAccepterDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/candidatures/session/HIVER25")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(List.of(candidatAccepterDTO))));
    }
}