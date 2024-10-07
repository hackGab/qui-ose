package com.lacouf.rsbjwt.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lacouf.rsbjwt.service.*;
import com.lacouf.rsbjwt.service.dto.EmployeurDTO;
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
}
