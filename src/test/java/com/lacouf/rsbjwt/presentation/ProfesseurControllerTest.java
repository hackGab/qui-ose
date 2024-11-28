package com.lacouf.rsbjwt.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lacouf.rsbjwt.model.Departement;
import com.lacouf.rsbjwt.model.Etudiant;
import com.lacouf.rsbjwt.model.EvaluationStageProf;
import com.lacouf.rsbjwt.model.Professeur;
import com.lacouf.rsbjwt.model.auth.Role;
import com.lacouf.rsbjwt.service.*;
import com.lacouf.rsbjwt.service.dto.CredentialDTO;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import com.lacouf.rsbjwt.service.dto.EvaluationStageProfDTO;
import com.lacouf.rsbjwt.service.dto.ProfesseurDTO;
import jakarta.persistence.GeneratedValue;
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

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(ProfesseurController.class)
public class ProfesseurControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfesseurService professeurService;

    @MockBean
    private EtudiantService etudiantService;

    @MockBean
    private EmployeurService employeurService;

    @MockBean
    private GestionnaireService gestionnaireService;


    @MockBean
    private UserAppService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();



    @Test
    @WithMockUser(username = "user", roles = {"PROFESSEUR"})
    public void shouldCreateProfesseur() throws Exception {
        ProfesseurDTO professeurDTO = new ProfesseurDTO("John", "Doe", null, null, null,null);
        Mockito.when(professeurService.creerProfesseur(any(ProfesseurDTO.class)))
                .thenReturn(Optional.of(professeurDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/professeur/creerProfesseur")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(new ObjectMapper().writeValueAsString(professeurDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(professeurDTO)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"PROFESSEUR"})
    public void shouldReturnProfesseurWhenFound() throws Exception {
        ProfesseurDTO professeurDTO = new ProfesseurDTO("John", "Doe", null, null, null,null);
        Mockito.when(professeurService.getProfesseurById(1L))
                .thenReturn(Optional.of(professeurDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/professeur/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(professeurDTO)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"PROFESSEUR"})
    public void getAllProfesseurs_shouldReturnListOfProfesseurs() throws Exception {
        ProfesseurDTO professeurDTO = new ProfesseurDTO("John", "Doe", null, null, null, null);
        List<ProfesseurDTO> professeurs = List.of(professeurDTO);

        Mockito.when(professeurService.getAllProfesseurs()).thenReturn(professeurs);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/professeur/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(professeurs)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"PROFESSEUR"})
    public void assignerEtudiants_shouldAssignEtudiantsToProfesseur() throws Exception {
        String professeurEmail = "email@gmail.com";
        List<String> etudiantsEmails = List.of("student1@gmail.com");
        ProfesseurDTO professeurDTO = new ProfesseurDTO("John", "Doe", null, null, null, null);

        Mockito.when(professeurService.assignerEtudiants(professeurEmail, etudiantsEmails)).thenReturn(Optional.of(professeurDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/professeur/assignerEtudiants/" + professeurEmail)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(etudiantsEmails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(professeurDTO)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"PROFESSEUR"})
    public void assignerEtudiants_shouldReturnBadRequestForInvalidData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/professeur/assignerEtudiants/email@gmail.com")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(Collections.emptyList()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user", roles = {"PROFESSEUR"})
    public void assignerEtudiants_shouldReturnNotFoundWhenProfesseurNotFound() throws Exception {
        String professeurEmail = "email@gmail.com";
        List<String> etudiantsEmails = List.of("student1@gmail.com");

        Mockito.when(professeurService.assignerEtudiants(professeurEmail, etudiantsEmails)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/professeur/assignerEtudiants/" + professeurEmail)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(etudiantsEmails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"PROFESSEUR"})
    public void getEtudiants_shouldReturnListOfEtudiants() throws Exception {
        String professeurEmail = "email@gmail.com";
        CredentialDTO credentials = new CredentialDTO("email@gmail.com", "password");
        EtudiantDTO etudiantDTO = new EtudiantDTO("John", "Doe", Role.ETUDIANT, "123456789", credentials, Departement.TECHNIQUES_INFORMATIQUE);
        List<EtudiantDTO> etudiants = List.of(etudiantDTO);

        Mockito.when(professeurService.getEtudiants(professeurEmail)).thenReturn(etudiants);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/professeur/etudiants/" + professeurEmail)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(etudiants)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"PROFESSEUR"})
    public void getEtudiants_shouldReturnBadRequestWhenEmailIsMissing() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/professeur/etudiants/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"PROFESSEUR"})
    public void getEtudiants_shouldReturnEmptyListWhenProfesseurNotFound() throws Exception {
        String professeurEmail = "email@gmail.com";

        Mockito.when(professeurService.getEtudiants(professeurEmail)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/professeur/etudiants/" + professeurEmail)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(Collections.emptyList())));
    }

    @Test
    @WithMockUser(username = "user", roles = {"PROFESSEUR"})
    void getEtudiantsByDepartement() {
        EtudiantDTO etudiantDTO = new EtudiantDTO("John", "Doe", Role.ETUDIANT, "123456789", null, Departement.TECHNIQUES_INFORMATIQUE);
        List<EtudiantDTO> etudiants = List.of(etudiantDTO);
        Departement departement = Departement.TECHNIQUES_INFORMATIQUE;

        Mockito.when(etudiantService.getEtudiantsAvecContratByDepartement(departement)).thenReturn(etudiants);

        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .get("/professeur/etudiants/departement/" + departement)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(etudiants)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @WithMockUser(username = "user", roles = {"PROFESSEUR"})
    void getEvaluations() {
        Etudiant etudiant = new Etudiant("John", "Doe", "allo", "123456789", null, Departement.TECHNIQUES_INFORMATIQUE);
        Professeur professeur = new Professeur("John", "Doe", "allo", "123456789", null, Departement.TECHNIQUES_INFORMATIQUE);

        EvaluationStageProf evaluationStageProf = new EvaluationStageProf();
        evaluationStageProf.setEtudiant(etudiant);
        evaluationStageProf.setProfesseur(professeur);
        evaluationStageProf.setId(1L);
        evaluationStageProf.setDateStage(LocalDate.now());

        EvaluationStageProfDTO evaluationStageProfDTO = new EvaluationStageProfDTO(evaluationStageProf);

        List<EvaluationStageProfDTO> evaluations = List.of(evaluationStageProfDTO);

        Mockito.when(professeurService.getEvaluationsStageProf(any(String.class))).thenReturn(evaluations);

        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .get("/professeur/evaluations/" + professeur.getEmail())
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(evaluations)));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    @WithMockUser(username = "user", roles = {"PROFESSEUR"})
    void evaluerStage() {
        EvaluationStageProfDTO evaluationStageProfDTO = new EvaluationStageProfDTO();

        Etudiant etudiant = new Etudiant("John", "Doe", "allo", "123456789", null, Departement.TECHNIQUES_INFORMATIQUE);
        etudiant.setId(1L);
        Professeur professeur = new Professeur("John", "Doe", "allo", "123456789", null, Departement.TECHNIQUES_INFORMATIQUE);
        professeur.setId(1L);

        Mockito.when(professeurService.evaluerStage(any(EvaluationStageProfDTO.class))).thenReturn(Optional.of(evaluationStageProfDTO));

        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .put("/professeur/evaluerStage")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .content(objectMapper.writeValueAsString(evaluationStageProfDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(evaluationStageProfDTO)));
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
