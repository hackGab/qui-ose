package com.lacouf.rsbjwt.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lacouf.rsbjwt.service.*;
import com.lacouf.rsbjwt.service.dto.CVDTO;
import com.lacouf.rsbjwt.service.dto.OffreDeStageDTO;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebMvcTest(GestionnaireController.class)
class GestionnaireControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GestionnaireService gestionnaireService;

    @MockBean
    private CandidatAccepterService candidatAccepterService;

    @MockBean
    private OffreDeStageService offreDeStageService;

    @MockBean
    private EmployeurService employeurService;

    @MockBean
    private EtudiantService etudiantService;

    @MockBean
    private ProfesseurService professeurService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserAppService userService;

    private CVDTO cvDTO;

    @BeforeEach
    void setUp() {
        cvDTO = new CVDTO();
        cvDTO.setId(1L);
        cvDTO.setStatus("attend");
    }

    @Test
    @WithMockUser(username = "gestionnaire", roles = {"GESTIONNAIRE"})
    public void shouldValiderCV() throws Exception {
        // Arrange
        when(gestionnaireService.validerOuRejeterCV(anyLong(), eq("accepté"), eq("")))
                .thenReturn(Optional.of(cvDTO));
        cvDTO.setStatus("accepté");


        mockMvc.perform(MockMvcRequestBuilders
                        .put("/gestionnaire/validerOuRejeterCV/1")
                        .content("{\"status\": \"accepté\", \"rejectionReason\": \"\"}")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(cvDTO)));
    }

    @Test
    @WithMockUser(username = "gestionnaire", roles = {"GESTIONNAIRE"})
    public void shouldRejeterCV() throws Exception {
        // Arrange
        when(gestionnaireService.validerOuRejeterCV(anyLong(), eq("rejeté"), eq("raison")))
                .thenReturn(Optional.of(cvDTO));
        cvDTO.setStatus("rejeté");

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/gestionnaire/validerOuRejeterCV/1")
                        .content("{\"status\": \"rejeté\", \"rejectionReason\": \"raison\"}")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(cvDTO)));
    }

    @Test
    @WithMockUser(username = "gestionnaire", roles = {"GESTIONNAIRE"})
    public void shouldReturnNotFoundWhenCVNotExists() throws Exception {
        // Arrange
        when(gestionnaireService.validerOuRejeterCV(anyLong(), eq("accepté"), eq("")))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/gestionnaire/validerOuRejeterCV/1")
                        .content("{\"status\": \"accepté\", \"rejectionReason\": \"\"}")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "gestionnaire", roles = {"GESTIONNAIRE"})
    void validerOuRejeterOffre() throws Exception {

        when(gestionnaireService.validerOuRejeterOffre(anyLong(), eq("accepté"), eq("")))
                .thenReturn(Optional.of(new OffreDeStageDTO()));

        // Act & Assert

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/gestionnaire/validerOuRejeterOffre/1")
                        .content("{\"status\": \"accepté\", \"rejectionReason\": \"\"}")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
