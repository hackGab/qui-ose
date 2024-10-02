package com.lacouf.rsbjwt.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lacouf.rsbjwt.service.*;
import com.lacouf.rsbjwt.service.dto.CVDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;
import java.util.Optional;

@WebMvcTest(CVController.class)
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
    public void shouldCreateCV() throws Exception {
        CVDTO cvDTO = new CVDTO("pdf.pdf", "application/pdf", new Date(), "John", "Attente");
        Mockito.when(etudiantService.creerCV(cvDTO, "email@gmail.com"))
                .thenReturn(Optional.of(cvDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("cv/creerCV/email@gmail.com")
                        .content(new ObjectMapper().writeValueAsString(cvDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(cvDTO)));
    }

    @Test
    public void shouldDeleteCV() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("cv/supprimerCV/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}
