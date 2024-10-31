package com.lacouf.rsbjwt.security;

import com.lacouf.rsbjwt.model.Departement;
import com.lacouf.rsbjwt.model.Etudiant;
import com.lacouf.rsbjwt.model.UserApp;
import com.lacouf.rsbjwt.repository.UserAppRepository;
import com.lacouf.rsbjwt.security.exception.UserNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    private JwtTokenProvider tokenProvider;
    private UserAppRepository userRepository;
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        tokenProvider = Mockito.mock(JwtTokenProvider.class);
        userRepository = Mockito.mock(UserAppRepository.class);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(tokenProvider, userRepository);
    }

    @Test
    void doFilterInternal_ShouldSetAuthentication_WhenTokenIsValid() throws IOException, ServletException {
        // Arrange
        String email = "email@gmail.com";
        String token = "Bearer validToken";
        UserApp user = new Etudiant("John", "Doe", "email@gmail.com", "correctPassword", "1234567890", Departement.TECHNIQUES_INFORMATIQUE);


        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn(token);
        when(tokenProvider.getEmailFromJWT("validToken")).thenReturn(email);
        when(userRepository.findUserAppByEmail(email)).thenReturn(Optional.of(user));

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertEquals(email, authentication.getPrincipal());
    }
}
