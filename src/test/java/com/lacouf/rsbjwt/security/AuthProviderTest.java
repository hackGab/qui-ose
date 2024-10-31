package com.lacouf.rsbjwt.security;

import com.lacouf.rsbjwt.model.Departement;
import com.lacouf.rsbjwt.model.Etudiant;
import com.lacouf.rsbjwt.model.UserApp;
import com.lacouf.rsbjwt.repository.UserAppRepository;
import com.lacouf.rsbjwt.security.exception.AuthenticationException;
import com.lacouf.rsbjwt.security.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthProviderTest {

    private AuthProvider authProvider;
    private PasswordEncoder passwordEncoder;
    private UserAppRepository userAppRepository;
    private UserApp user; // Declare UserApp here

    @BeforeEach
    void setUp() {
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userAppRepository = Mockito.mock(UserAppRepository.class);
        authProvider = new AuthProvider(passwordEncoder, userAppRepository);

        user = new Etudiant("John", "Doe", "email@gmail.com", "correctPassword", "1234567890", Departement.TECHNIQUES_INFORMATIQUE);
    }

    @Test
    void authenticate_ShouldReturnAuthentication_WhenValidCredentials() {
        // Arrange
        String email = user.getEmail();
        String password = "correctPassword";

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);

        when(userAppRepository.findUserAppByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        // Act
        Authentication result = authProvider.authenticate(authentication);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getPrincipal());
    }

    @Test
    void authenticate_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        // Arrange
        String email = "nonexistent@example.com";
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, "password");

        when(userAppRepository.findUserAppByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> authProvider.authenticate(authentication));
    }

    @Test
    void authenticate_ShouldThrowAuthenticationException_WhenPasswordDoesNotMatch() {
        // Arrange
        String email = user.getEmail();
        String password = "wrongPassword";

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);

        when(userAppRepository.findUserAppByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        // Act
        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            authProvider.authenticate(authentication);
        });

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("Incorrect username or password", exception.getMessage());
    }
}
