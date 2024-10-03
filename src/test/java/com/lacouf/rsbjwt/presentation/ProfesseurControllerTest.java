package com.lacouf.rsbjwt.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lacouf.rsbjwt.service.*;
import com.lacouf.rsbjwt.service.dto.ProfesseurDTO;
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
    private UserAppService userService;

    @MockBean
    private GestionnaireService gestionnaireService;

    @MockBean
    private PasswordEncoder passwordEncoder;
    
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
}
