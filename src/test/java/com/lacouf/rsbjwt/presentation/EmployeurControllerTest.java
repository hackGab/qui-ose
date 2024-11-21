package com.lacouf.rsbjwt.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lacouf.rsbjwt.service.*;
import com.lacouf.rsbjwt.service.dto.EmployeurDTO;
import com.lacouf.rsbjwt.service.dto.EvaluationStageEmployeurDTO;
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

@WebMvcTest(EmployeurController.class)
public class EmployeurControllerTest {
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

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    public void shouldCreateEmployeur() throws Exception {
        EmployeurDTO employeurDTO = new EmployeurDTO("John", "Doe", null, null, null, null);
        Mockito.when(employeurService.creerEmployeur(any(EmployeurDTO.class)))
                .thenReturn(Optional.of(employeurDTO));

        System.out.println(new ObjectMapper().writeValueAsString(employeurDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/employeur/creerEmployeur")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(new ObjectMapper().writeValueAsString(employeurDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(employeurDTO)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    public void shouldReturnEmployeurWhenFound() throws Exception {
        EmployeurDTO employeurDTO = new EmployeurDTO("John", "Doe", null, null, null, null);
        Mockito.when(employeurService.getEmployeurById(1L))
                .thenReturn(Optional.of(employeurDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employeur/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(employeurDTO)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    public void shouldCreateEvaluationEtudiant() throws Exception {
        String emailEmployeur = "employeur@example.com";
        String emailEtudiant = "etudiant@example.com";
        EvaluationStageEmployeurDTO evaluationStageEmployeurDTO = new EvaluationStageEmployeurDTO();
        // Set properties of evaluationStageEmployeurDTO as needed

        Mockito.when(employeurService.creerEvaluationEtudiant(
                        Mockito.eq(emailEmployeur), Mockito.eq(emailEtudiant), Mockito.any(EvaluationStageEmployeurDTO.class)))
                .thenReturn(Optional.of(evaluationStageEmployeurDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/employeur/creerEvaluationEtudiant/{emailEmployeur}/{emailEtudiant}", emailEmployeur, emailEtudiant)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(new ObjectMapper().writeValueAsString(evaluationStageEmployeurDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(evaluationStageEmployeurDTO)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    public void shouldReturnEvaluationEtudiantWhenFound() throws Exception {
        String emailEmployeur = "employeur@example.com";
        String emailEtudiant = "etudiant@example.com";
        EvaluationStageEmployeurDTO evaluationStageEmployeurDTO = new EvaluationStageEmployeurDTO();
        // Set properties of evaluationStageEmployeurDTO as needed

        Mockito.when(employeurService.getEvaluationEtudiant(emailEmployeur, emailEtudiant))
                .thenReturn(Optional.of(evaluationStageEmployeurDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employeur/evaluationEmployeur/{emailEmployeur}/{emailEtudiant}", emailEmployeur, emailEtudiant)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(evaluationStageEmployeurDTO)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    public void shouldReturnBadRequestWhenEmailEmployeurIsInvalid() throws Exception {
        String emailEmployeur = "";
        String emailEtudiant = "etudiant@example.com";

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employeur/evaluationEmployeur/{emailEmployeur}/{emailEtudiant}", emailEmployeur, emailEtudiant)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    public void shouldReturnBadRequestWhenEmailEtudiantIsInvalid() throws Exception {
        String emailEmployeur = "employeur@example.com";
        String emailEtudiant = "";

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employeur/evaluationEmployeur/{emailEmployeur}/{emailEtudiant}", emailEmployeur, emailEtudiant)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    public void shouldReturnNotFoundWhenEvaluationEtudiantNotFound() throws Exception {
        String emailEmployeur = "employeur@example.com";
        String emailEtudiant = "etudiant@example.com";

        Mockito.when(employeurService.getEvaluationEtudiant(emailEmployeur, emailEtudiant))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employeur/evaluationEmployeur/{emailEmployeur}/{emailEtudiant}", emailEmployeur, emailEtudiant)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEUR"})
    public void shouldReturnAllEvaluations() throws Exception {
        List<EvaluationStageEmployeurDTO> evaluations = List.of(new EvaluationStageEmployeurDTO(), new EvaluationStageEmployeurDTO());
        // Set properties of evaluations as needed

        Mockito.when(employeurService.getAllEvaluations())
                .thenReturn(evaluations);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employeur/evaluationEmployeur/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(evaluations)));
    }
}
