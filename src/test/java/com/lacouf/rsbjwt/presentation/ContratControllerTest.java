package com.lacouf.rsbjwt.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lacouf.rsbjwt.ReactSpringSecurityJwtApplication;
import com.lacouf.rsbjwt.model.Entrevue;
import com.lacouf.rsbjwt.repository.EntrevueRepository;
import com.lacouf.rsbjwt.service.*;
import com.lacouf.rsbjwt.service.dto.CandidatAccepterDTO;
import com.lacouf.rsbjwt.service.dto.ContratDTO;
import com.lacouf.rsbjwt.service.dto.EntrevueDTO;
import org.springframework.context.annotation.Import;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ContratController.class)
class ContratControllerTest {

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

    @Test
    @WithMockUser(username = "user", roles = {"GESTIONNAIRE"})
    void creerContrat() throws Exception {
        ContratDTO contratDTO = createContratDTO();

        when(gestionnaireService.creerContrat(any(ContratDTO.class))).thenReturn(Optional.of(contratDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/contrat/creerContrat")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(contratDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(contratDTO)));
    }

    private ContratDTO createContratDTO() {
        ContratDTO contratDTO = new ContratDTO();
        contratDTO.setEtudiantSigne(false);
        contratDTO.setEmployeurSigne(false);
        contratDTO.setGestionnaireSigne(false);
        contratDTO.setCollegeEngagement("college");
        contratDTO.setCandidature(new CandidatAccepterDTO(1L, 1L, true));
        return contratDTO;
    }

    @Test
    @WithMockUser(username = "user", roles = {"GESTIONNAIRE"})
    void creerContratBadRequest() throws Exception {
        mockMvc.perform(post("/contrat/creerContrat")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content("")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user", roles = {"GESTIONNAIRE"})
    void testGetAllContrats() throws Exception {
        ContratDTO contratDTO = createContratDTO();
        ContratDTO contratDTO1 = createContratDTO();

        when(gestionnaireService.getAllContrats()).thenReturn(new ArrayList<>(Arrays.asList(contratDTO, contratDTO1)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/contrat/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(Arrays.asList(contratDTO, contratDTO1))));



    }

    @Test
    void signerContrat() {

    }
}
