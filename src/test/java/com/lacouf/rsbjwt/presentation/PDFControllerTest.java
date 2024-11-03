package com.lacouf.rsbjwt.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lacouf.rsbjwt.service.*;
import com.lacouf.rsbjwt.service.dto.ContratDTO;
import org.junit.jupiter.api.Test;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = PDFController.class)

class PDFControllerTest {
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GestionnaireService gestionnaireService;

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

    @MockBean
    private CandidatAccepterService candidatAccepterService;

    @MockBean
    private SystemeService systemeService;

    @Test
    @WithMockUser(username = "user", roles = {"GESTIONNAIRE"})
    void Should_GenerateContratPDF() throws Exception {
        ContratDTO contratDTO = new ContratDTO();
        byte[] pdfBytes = "Fake PDF Content".getBytes();

        when(systemeService.generateContratPDF(contratDTO)).thenReturn(pdfBytes);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/generatePDF/contrat")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contratDTO))
                        .accept(MediaType.APPLICATION_PDF))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=Contrat_Stage.pdf"))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }

    @Test
    @WithMockUser(username = "user", roles = {"GESTIONNAIRE"})
    void Should_ReturnNotFound_When_GenerateContratPDF_With_InvalidContrat() throws Exception {
        ContratDTO contratDTO = new ContratDTO();

        when(systemeService.generateContratPDF(any(ContratDTO.class))).thenThrow(new IllegalArgumentException());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/generatePDF/contrat")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contratDTO))
                        .accept(MediaType.APPLICATION_PDF))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"GESTIONNAIRE"})
    void Should_ReturnInternalServerError_When_GenerateContratPDF_With_Exception() throws Exception {
        ContratDTO contratDTO = new ContratDTO();

        when(systemeService.generateContratPDF(any(ContratDTO.class))).thenThrow(new Exception());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/generatePDF/contrat")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contratDTO))
                        .accept(MediaType.APPLICATION_PDF))
                .andExpect(status().isInternalServerError());
    }

}
