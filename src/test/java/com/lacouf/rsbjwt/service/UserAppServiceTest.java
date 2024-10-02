package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.repository.*;
import com.lacouf.rsbjwt.security.JwtTokenProvider;
import com.lacouf.rsbjwt.service.dto.LoginDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

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
}
