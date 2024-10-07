package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.CV;
import com.lacouf.rsbjwt.model.Gestionnaire;
import com.lacouf.rsbjwt.model.auth.Role;
import com.lacouf.rsbjwt.repository.CVRepository;
import com.lacouf.rsbjwt.repository.EtudiantRepository;
import com.lacouf.rsbjwt.repository.GestionnaireRepository;
import com.lacouf.rsbjwt.service.dto.CVDTO;
import com.lacouf.rsbjwt.service.dto.CredentialDTO;
import com.lacouf.rsbjwt.service.dto.GestionnaireDTO;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GestionnaireServiceTest {

    private Gestionnaire gestionnaireEntity;
    private GestionnaireDTO gestionnaireDTO;

    private GestionnaireService gestionnaireService;
    private GestionnaireRepository gestionnaireRepository;
    private EtudiantRepository etudiantRepository;
    private CVRepository cvRepository;
    private PasswordEncoder passwordEncoder;

    private CV cvEntity;
    @BeforeEach
    void setUp() {
        gestionnaireRepository = Mockito.mock(GestionnaireRepository.class);
        cvRepository = Mockito.mock(CVRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        gestionnaireService = new GestionnaireService(gestionnaireRepository, cvRepository, etudiantRepository, passwordEncoder);

        gestionnaireEntity = new Gestionnaire("Thiraiyan", "Moon", "titi@gmail.com", "password", "123-456-7890");
        gestionnaireDTO = new GestionnaireDTO(gestionnaireEntity);

        cvEntity = new CV();
        cvEntity.setId(1L);
        cvEntity.setStatus("attend");
    }

    @Test
    public void testCreerGestionnaire() {
        // Arrange
        when(gestionnaireRepository.save(any(Gestionnaire.class)))
                .thenReturn(gestionnaireEntity);

        // Act
        Optional<GestionnaireDTO> response = gestionnaireService.creerGestionnaire(gestionnaireDTO);
        GestionnaireDTO gestionnaireRecu = response.get();

        // Assert
        assert response.isPresent();
        assertEquals(gestionnaireDTO.getFirstName(), gestionnaireRecu.getFirstName());
        assertEquals(gestionnaireDTO.getLastName(), gestionnaireRecu.getLastName());
        assertEquals(gestionnaireDTO.getPhoneNumber(), gestionnaireRecu.getPhoneNumber());
    }

    @Test
    public void testCreerGestionnaireWithException() {
        // Arrange
        when(gestionnaireRepository.save(any(Gestionnaire.class)))
                .thenThrow(new RuntimeException());

        // Act
        Optional<GestionnaireDTO> response = gestionnaireService.creerGestionnaire(gestionnaireDTO);

        // Assert
        assert !response.isPresent();
    }

    @Test
    public void testValiderCV() {
        // Arrange
        when(cvRepository.findById(1L)).thenReturn(Optional.of(cvEntity));
        when(cvRepository.save(any(CV.class))).thenReturn(cvEntity);

        // Act
        Optional<CVDTO> result = gestionnaireService.validerOuRejeterCV(1L, "accepté", "");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("accepté", result.get().getStatus());
    }

    @Test
    public void testRejeterCV() {
        // Arrange
        when(cvRepository.findById(1L)).thenReturn(Optional.of(cvEntity));
        when(cvRepository.save(any(CV.class))).thenReturn(cvEntity);

        // Act
        Optional<CVDTO> result = gestionnaireService.validerOuRejeterCV(1L, "rejeté", "raison");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("rejeté", result.get().getStatus());
    }

    @Test
    public void testCVNonExistant() {
        // Arrange
        when(cvRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<CVDTO> result = gestionnaireService.validerOuRejeterCV(1L, "accepté", "");

        // Assert
        assertFalse(result.isPresent());
    }


}
