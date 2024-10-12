package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.*;
import com.lacouf.rsbjwt.repository.*;
import com.lacouf.rsbjwt.security.JwtTokenProvider;
import com.lacouf.rsbjwt.service.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserAppServiceTest {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private UserAppService userAppService;
    private UserAppRepository userAppRepository;
    private EtudiantRepository etudiantRepository;
    private ProfesseurRepository professeurRepository;
    private EmployeurRepository employeurRepository;
    private GestionnaireRepository gestionnaireRepository;
    private LoginDTO loginDto;

    @BeforeEach
    void setUp() {
        authenticationManager = Mockito.mock(AuthenticationManager.class);
        jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
        userAppRepository = Mockito.mock(UserAppRepository.class);
        etudiantRepository = Mockito.mock(EtudiantRepository.class);
        professeurRepository = Mockito.mock(ProfesseurRepository.class);
        employeurRepository = Mockito.mock(EmployeurRepository.class);
        gestionnaireRepository = Mockito.mock(GestionnaireRepository.class);

        userAppService = new UserAppService(authenticationManager, jwtTokenProvider, userAppRepository,
                etudiantRepository, professeurRepository, employeurRepository, gestionnaireRepository);

        loginDto = new LoginDTO("user@example.com", "password");
    }

    @Test
    void authenticateUser_ShouldReturnToken_WhenValidCredentials() {
        // Arrange
        String expectedToken = "mockJwtToken";
        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuth);
        when(jwtTokenProvider.generateToken(mockAuth)).thenReturn(expectedToken);

        // Act
        String actualToken = userAppService.authenticateUser(loginDto);

        // Assert
        assertEquals(expectedToken, actualToken);
    }

    @Test
    void authenticateUser_ShouldThrowException_WhenInvalidCredentials() {
        // Arrange
        loginDto = new LoginDTO("user@example.com", "wrongPassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        // Act
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userAppService.authenticateUser(loginDto);
        });

        // Assert
        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void getEtudiantDTO_ShouldReturnEtudiantDTO_WhenEtudiantExists() {
        // Arrange
        Long id = 1L;
        Etudiant etudiant = new Etudiant("John", "Doe", "john.doe@example.com", "password", "1234567890", "Informatique");
        when(etudiantRepository.findById(id)).thenReturn(Optional.of(etudiant));

        // Act
        EtudiantDTO result = userAppService.getEtudiantDTO(id);

        // Assert
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("Informatique", result.getDepartement());
        assertEquals("john.doe@example.com", result.getCredentials().getEmail());
    }

    @Test
    void getProfesseurDTO_ShouldReturnProfesseurDTO_WhenProfesseurExists() {
        // Arrange
        Long id = 1L;
        Professeur professeur = new Professeur("Jane", "Smith", "jane.smith@example.com", "password", "0987654321", "Mathématiques");
        when(professeurRepository.findById(id)).thenReturn(Optional.of(professeur));

        // Act
        ProfesseurDTO result = userAppService.getProfesseurDTO(id);

        // Assert
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("Mathématiques", result.getDepartement());
        assertEquals("jane.smith@example.com", result.getCredentials().getEmail());
    }

    @Test
    void getEmployeurDTO_ShouldReturnEmployeurDTO_WhenEmployeurExists() {
        // Arrange
        Long id = 1L;
        Employeur employeur = new Employeur("Company", "Bob", "contact@company.com", "0123456789", "Company Inc.", "Company Inc.");
        when(employeurRepository.findById(id)).thenReturn(Optional.of(employeur));

        // Act
        EmployeurDTO result = userAppService.getEmployeurDTO(id);

        // Assert
        assertEquals("Company", result.getFirstName());
        assertEquals("contact@company.com", result.getCredentials().getEmail());
        assertEquals("Company Inc.", result.getEntreprise());
    }

    @Test
    void getGestionnaireDTO_ShouldReturnGestionnaireDTO_WhenGestionnaireExists() {
        // Arrange
        Long id = 1L;
        Gestionnaire gestionnaire = new Gestionnaire("Admin", "bob", "admin@example.com", "0987654321", "1234567");
        when(gestionnaireRepository.findById(id)).thenReturn(Optional.of(gestionnaire));

        // Act
        GestionnaireDTO result = userAppService.getGestionnaireDTO(id);

        // Assert
        assertEquals("Admin", result.getFirstName());
        assertEquals("admin@example.com", result.getCredentials().getEmail());
    }
}
