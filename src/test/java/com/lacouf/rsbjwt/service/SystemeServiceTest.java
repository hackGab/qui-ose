package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.*;
import com.lacouf.rsbjwt.model.auth.Credentials;
import com.lacouf.rsbjwt.model.auth.Role;
import com.lacouf.rsbjwt.repository.ContratRepository;
import com.lacouf.rsbjwt.repository.NotificationRepository;
import com.lacouf.rsbjwt.service.dto.CandidatAccepterDTO;
import com.lacouf.rsbjwt.service.dto.ContratDTO;
import com.lacouf.rsbjwt.service.dto.NotificationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

class SystemeServiceTest {

    private ContratRepository contratRepository;

    private SystemeService systemeService;
    private NotificationRepository notificationRepository;

    @BeforeEach
    void setUp() {
        contratRepository = Mockito.mock(ContratRepository.class);
        notificationRepository = Mockito.mock(NotificationRepository.class);
        systemeService = new SystemeService(contratRepository, notificationRepository);
    }

    @Test
    void Shoud_Generate_ContratPDF() throws Exception {
        Etudiant etudiant = new Etudiant();
        etudiant.setCredentials(new Credentials("email", "password", Role.ETUDIANT));

        Employeur employeur = new Employeur();
        employeur.setCredentials(new Credentials("email", "password", Role.EMPLOYEUR));

        OffreDeStage offreDeStage = new OffreDeStage();

        List<Etudiant> etudiants = List.of(etudiant);
        offreDeStage.setEtudiants(etudiants);
        offreDeStage.setEmployeur(employeur);

        Entrevue entrevue = new Entrevue();
        entrevue.setOffreDeStage(offreDeStage);
        entrevue.setEtudiant(etudiant);


        CandidatAccepter candidatAccepter = new CandidatAccepter();
        candidatAccepter.setEntrevue(entrevue);

        Contrat contrat = new Contrat();

        contrat.setUUID("uuid");
        contrat.setCandidature(candidatAccepter);

        CandidatAccepterDTO candidatAccepterDTO = new CandidatAccepterDTO();


        ContratDTO contratDTO = new ContratDTO();
        contratDTO.setUUID("uuid");
        contratDTO.setCandidature(candidatAccepterDTO);

        // Arrange
        Mockito.when(contratRepository.findByUUID("uuid")).thenReturn(Optional.of(contrat));

        // Act

        byte[] newContrat =  systemeService.generateContratPDF(contratDTO);

        // Assert

        assertNotNull(newContrat);
    }

    @Test
    void Should_Throw_Exception_When_Contrat_Not_Found() throws Exception {
        ContratDTO contratDTO = new ContratDTO();
        contratDTO.setUUID("uuid");

        Mockito.when(contratRepository.findByUUID("uuid")).thenReturn(Optional.empty());

        // Act & Assert : vérifie que l'exception est lancée
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> systemeService.generateContratPDF(contratDTO),
                "Expected generateContratPDF() to throw IllegalArgumentException, but it didn't"
        );

        assertEquals("Contrat non trouvé", thrown.getMessage());
    }


    @Test
    void testGetAllUnreadNotificationsByEmail() {
        Notification notification1 = new Notification();
        notification1.setVu(false);
        notification1.setDateCreation(LocalDateTime.now().minusMinutes(5)); // Set a valid date
        Notification notification2 = new Notification();
        notification2.setVu(false);
        notification2.setDateCreation(LocalDateTime.now().minusMinutes(10)); // Set a valid date
        List<Notification> notifications = Arrays.asList(notification1, notification2);

        Mockito.when(notificationRepository.findByEmail(anyString())).thenReturn(notifications);

        List<NotificationDTO> result = systemeService.getAllUnreadNotificationsByEmail("test@example.com");

        assertEquals(2, result.size());
    }

    @Test
    void testMarkNotificationAsRead() {
        Notification notification = new Notification();
        notification.setVu(false);

        Mockito.when(notificationRepository.findById(anyLong())).thenReturn(Optional.of(notification));

        systemeService.markNotificationAsRead(1L);

        assertTrue(notification.isVu());
        Mockito.verify(notificationRepository, Mockito.times(1)).save(notification);
    }

    @Test
    void testMarkNotificationAsRead_NotFound() {
        Mockito.when(notificationRepository.findById(anyLong())).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> systemeService.markNotificationAsRead(1L),
                "Expected markNotificationAsRead() to throw IllegalArgumentException, but it didn't"
        );

        assertEquals("Notification not found", thrown.getMessage());
    }
}