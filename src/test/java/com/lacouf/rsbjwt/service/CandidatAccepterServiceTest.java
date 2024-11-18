package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.*;
import com.lacouf.rsbjwt.repository.CandidatAccepterRepository;
import com.lacouf.rsbjwt.repository.EntrevueRepository;
import com.lacouf.rsbjwt.repository.NotificationRepository;
import com.lacouf.rsbjwt.service.dto.CandidatAccepterDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.lacouf.rsbjwt.model.Departement;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

class CandidatAccepterServiceTest {

    private CandidatAccepterService candidatAccepterService;
    private CandidatAccepterRepository candidatAccepterRepository;
    private EntrevueRepository entrevueRepository;
    private NotificationRepository notificationRepository;

    private Etudiant etudiant;


    @BeforeEach
    void setUp() {
        candidatAccepterRepository = Mockito.mock(CandidatAccepterRepository.class);
        entrevueRepository = Mockito.mock(EntrevueRepository.class);
        notificationRepository = Mockito.mock(NotificationRepository.class);
        candidatAccepterService = new CandidatAccepterService(candidatAccepterRepository, entrevueRepository, notificationRepository);
        etudiant = new Etudiant("John", "Doe", "john.doe@example.com", "password123", "1234567890", Departement.TECHNIQUES_INFORMATIQUE);

    }

    @Test
    void shouldAcceptCandidatureWhenEntrevueExists() {
        // Arrange
        Entrevue entrevue = new Entrevue();
        entrevue.setId(1L);
        OffreDeStage offreDeStage = new OffreDeStage();
        offreDeStage.setTitre("Offre1");
        entrevue.setOffreDeStage(offreDeStage);
        entrevue.setEtudiant(etudiant);
        CandidatAccepter savedCandidat = new CandidatAccepter(entrevue, true);
        Notification notification = new Notification("Félicitations, vous avez été accepté pour le post", "Offre1", "test@example.com", "/stagesAppliquees");

        Mockito.when(entrevueRepository.findById(anyLong())).thenReturn(Optional.of(entrevue));
        Mockito.when(candidatAccepterRepository.save(any(CandidatAccepter.class))).thenReturn(savedCandidat);
        Mockito.when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        // Act
        Optional<CandidatAccepterDTO> result = candidatAccepterService.accepterCandidature(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getEntrevueId());
        assertTrue(result.get().isAccepte());
        Mockito.verify(notificationRepository, Mockito.times(1)).save(any(Notification.class));

    }

    @Test
    void shouldReturnEmptyWhenEntrevueDoesNotExist() {
        // Arrange
        Mockito.when(entrevueRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<CandidatAccepterDTO> result = candidatAccepterService.accepterCandidature(1L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void shouldRefuseCandidatureWhenEntrevueExists() {
        // Arrange
        Entrevue entrevue = new Entrevue();
        entrevue.setId(1L);
        OffreDeStage offreDeStage = new OffreDeStage();
        offreDeStage.setTitre("Offre1");
        entrevue.setOffreDeStage(offreDeStage);
        entrevue.setEtudiant(etudiant);
        CandidatAccepter savedCandidat = new CandidatAccepter(entrevue, false);
        Notification notification = new Notification("Votre candidature a été refusée", "Offre1", "test@example.com", "/stagesAppliquees");

        Mockito.when(entrevueRepository.findById(anyLong())).thenReturn(Optional.of(entrevue));
        Mockito.when(candidatAccepterRepository.save(any(CandidatAccepter.class))).thenReturn(savedCandidat);
        Mockito.when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        // Act
        Optional<CandidatAccepterDTO> result = candidatAccepterService.refuserCandidature(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getEntrevueId());
        assertFalse(result.get().isAccepte());
        Mockito.verify(notificationRepository, Mockito.times(1)).save(any(Notification.class));
    }

    @Test
    void shouldReturnEmptyWhenRefusingCandidatureAndEntrevueDoesNotExist() {
        // Arrange
        Mockito.when(entrevueRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<CandidatAccepterDTO> result = candidatAccepterService.refuserCandidature(1L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void shouldReturnCandidatureDecisionWhenCandidatureExists() {
        // Arrange
        Entrevue entrevue = new Entrevue();
        entrevue.setId(1L);
        CandidatAccepter candidatAccepter = new CandidatAccepter(entrevue, true);

        Mockito.when(candidatAccepterRepository.findByEntrevueId(anyLong())).thenReturn(Optional.of(candidatAccepter));

        // Act
        Optional<CandidatAccepterDTO> result = candidatAccepterService.getCandidatureDecision(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getEntrevueId());
        assertTrue(result.get().isAccepte());
    }

    @Test
    void shouldReturnEmptyWhenCandidatureDecisionDoesNotExist() {
        // Arrange
        Mockito.when(candidatAccepterRepository.findByEntrevueId(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<CandidatAccepterDTO> result = candidatAccepterService.getCandidatureDecision(1L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void getAllCandidatures() {
        //Arrange
        Entrevue entrevue = new Entrevue();
        entrevue.setId(1L);
        CandidatAccepter candidatAccepter = new CandidatAccepter(entrevue, true);

        Entrevue entrevue1 = new Entrevue();
        entrevue1.setId(2L);
        CandidatAccepter candidatAccepter1 = new CandidatAccepter(entrevue1, false);

        Entrevue entrevue2 = new Entrevue();
        entrevue2.setId(3L);
        CandidatAccepter candidatAccepter2 = new CandidatAccepter(entrevue2, true);

        Mockito.when(candidatAccepterRepository.findAll()).thenReturn(java.util.List.of(candidatAccepter, candidatAccepter1, candidatAccepter2));

        // Act

        Iterable<CandidatAccepterDTO> result = candidatAccepterService.getAllCandidatures();

        // Assert

        assertEquals(3, result.spliterator().getExactSizeIfKnown());
        assertTrue(result.iterator().hasNext());
        assertEquals(1L, result.iterator().next().getEntrevueId());

    }
}
