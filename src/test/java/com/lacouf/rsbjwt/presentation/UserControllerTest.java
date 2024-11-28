package com.lacouf.rsbjwt.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lacouf.rsbjwt.model.auth.Role;
import com.lacouf.rsbjwt.service.*;
import com.lacouf.rsbjwt.service.dto.JWTAuthResponse;
import com.lacouf.rsbjwt.service.dto.LoginDTO;
import com.lacouf.rsbjwt.service.dto.UserDTO;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserAppService userService;

    private LoginDTO loginDTO;
    private JWTAuthResponse jwtAuthResponse;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        loginDTO = new LoginDTO("testuser", "password123");
        jwtAuthResponse = new JWTAuthResponse("testAccessToken");
        userDTO = new UserDTO("testuser", "lastname", "1234567890", Role.ETUDIANT);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void shouldAuthenticateUser() throws Exception {
        // Arrange
        when(userService.authenticateUser(any(LoginDTO.class)))
                .thenReturn("testAccessToken");

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/login")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(new ObjectMapper().writeValueAsString(loginDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content()
                        .json(new ObjectMapper().writeValueAsString(jwtAuthResponse)));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void shouldReturnUnauthorizedWhenLoginFails() throws Exception {
        // Arrange
        when(userService.authenticateUser(any(LoginDTO.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/login")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(new ObjectMapper().writeValueAsString(loginDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized()); // Statut 401 Unauthorized
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void shouldGetAuthenticatedUserDetails() throws Exception {
        // Arrange
        when(userService.getMe(any(String.class)))
                .thenReturn(userDTO);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/me")
                        .header("Authorization", "Bearer testAccessToken")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content()
                        .json(new ObjectMapper().writeValueAsString(userDTO)));
    }
}
