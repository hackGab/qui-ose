package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.*;
import com.lacouf.rsbjwt.model.auth.Credentials;
import com.lacouf.rsbjwt.model.auth.Role;
import com.lacouf.rsbjwt.repository.ContratRepository;
import com.lacouf.rsbjwt.service.dto.CandidatAccepterDTO;
import com.lacouf.rsbjwt.service.dto.ContratDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SystemeServiceTest {

    private ContratRepository contratRepository;

    private SystemeService systemeService;

    @BeforeEach
    void setUp() {
        contratRepository = Mockito.mock(ContratRepository.class);
        systemeService = new SystemeService(contratRepository);
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
}