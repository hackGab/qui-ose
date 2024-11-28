package com.lacouf.rsbjwt.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lacouf.rsbjwt.service.*;
import com.lacouf.rsbjwt.service.dto.CVDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.*;

@WebMvcTest(controllers = CVController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class CVControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeurService employeurService;

    @MockBean
    private EtudiantService etudiantService;

    @MockBean
    private GestionnaireService gestionnaireService;


    @MockBean
    private ProfesseurService professeurService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserAppService userService;

    @Test
    public void shouldDeleteCV() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/cv/supprimerCV/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void shouldCreateCV() throws Exception {
        CVDTO cvDTO = new CVDTO("cvName", "cvType", "cvData", "cvStatus");
        cvDTO.setUploadDate(null);
        when(etudiantService.creerCV(any(CVDTO.class), anyString()))
                .thenReturn(Optional.of(cvDTO));

        String email = "allo@ballo.com";

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/cv/creerCV/{email}", email)
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf())
                        .content(new ObjectMapper().writeValueAsString(cvDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(cvDTO)));

    }

    @Test
    public void shouldNotCreateCV() throws Exception {
        CVDTO cvDTO = new CVDTO("cvName", "cvType", "cvData", "cvStatus");
        when(etudiantService.creerCV(cvDTO, "notemail@gmail.com"))
                .thenReturn(Optional.empty());
    }

    @Test
    public void shouldNotDeleteCV() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/cv/supprimerCV/null")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
