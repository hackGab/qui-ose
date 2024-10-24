package com.lacouf.rsbjwt.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lacouf.rsbjwt.service.CandidatAccepterService;
import com.lacouf.rsbjwt.service.dto.CandidatAccepterDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;

@WebMvcTest(CandidatAccepterController.class)
public class CandidatAccepterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CandidatAccepterService candidatAccepterService;

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    public void shouldAcceptCandidature() throws Exception {
        CandidatAccepterDTO candidatAccepterDTO = new CandidatAccepterDTO(1L, true);
        Mockito.when(candidatAccepterService.accepterCandidature(anyLong()))
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
        CandidatAccepterDTO candidatAccepterDTO = new CandidatAccepterDTO(1L, false);
        Mockito.when(candidatAccepterService.refuserCandidature(anyLong()))
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
        Mockito.when(candidatAccepterService.accepterCandidature(anyLong()))
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
        CandidatAccepterDTO candidatAccepterDTO = new CandidatAccepterDTO(1L, true);
        Mockito.when(candidatAccepterService.getCandidatureDecision(anyLong()))
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
        Mockito.when(candidatAccepterService.getCandidatureDecision(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/candidatures/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}