package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.CandidatAccepter;
import com.lacouf.rsbjwt.model.Entrevue;
import com.lacouf.rsbjwt.repository.CandidatAccepterRepository;
import com.lacouf.rsbjwt.repository.EntrevueRepository;
import com.lacouf.rsbjwt.service.dto.CandidatAccepterDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

class CandidatAccepterServiceTest {

    private CandidatAccepterService candidatAccepterService;
    private CandidatAccepterRepository candidatAccepterRepository;
    private EntrevueRepository entrevueRepository;

    @BeforeEach
    void setUp() {
        candidatAccepterRepository = Mockito.mock(CandidatAccepterRepository.class);
        entrevueRepository = Mockito.mock(EntrevueRepository.class);
        candidatAccepterService = new CandidatAccepterService(candidatAccepterRepository, entrevueRepository);
    }

    @Test
    void shouldAcceptCandidatureWhenEntrevueExists() {
        // Arrange
        Entrevue entrevue = new Entrevue();
        entrevue.setId(1L);
        CandidatAccepter savedCandidat = new CandidatAccepter(entrevue, true);

        Mockito.when(entrevueRepository.findById(anyLong())).thenReturn(Optional.of(entrevue));
        Mockito.when(candidatAccepterRepository.save(any(CandidatAccepter.class))).thenReturn(savedCandidat);

        // Act
        Optional<CandidatAccepterDTO> result = candidatAccepterService.accepterCandidature(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getEntrevueId());
        assertTrue(result.get().isAccepte());
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
        CandidatAccepter savedCandidat = new CandidatAccepter(entrevue, false);

        Mockito.when(entrevueRepository.findById(anyLong())).thenReturn(Optional.of(entrevue));
        Mockito.when(candidatAccepterRepository.save(any(CandidatAccepter.class))).thenReturn(savedCandidat);

        // Act
        Optional<CandidatAccepterDTO> result = candidatAccepterService.refuserCandidature(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getEntrevueId());
        assertFalse(result.get().isAccepte());
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
}
