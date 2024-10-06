package com.lacouf.rsbjwt.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lacouf.rsbjwt.service.*;
import com.lacouf.rsbjwt.service.dto.JWTAuthResponse;
import com.lacouf.rsbjwt.service.dto.LoginDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfesseurService professeurService;

    @MockBean
    private EtudiantService etudiantService;

    @MockBean
    private GestionnaireService gestionnaireService;

    @MockBean
    private EmployeurService employeurService;

    @MockBean

    private GestionnaireService gestionnaireService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserAppService userService;

    @Test
    public void shouldReturnUnauthorizedWhenAuthenticationFails() throws Exception {
        // Arrange
        LoginDTO loginDTO = new LoginDTO("username", "wrongpassword");

        Mockito.when(userService.authenticateUser(any(LoginDTO.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/login")
                        .content(new ObjectMapper().writeValueAsString(loginDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}




























































//                .andExpect(MockMvcResultMatchers.content().json("{\"jwt\":\"\"}"));
