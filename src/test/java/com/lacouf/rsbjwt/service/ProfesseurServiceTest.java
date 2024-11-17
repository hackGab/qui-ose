package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.*;
import com.lacouf.rsbjwt.model.auth.Role;
import com.lacouf.rsbjwt.presentation.ProfesseurController;
import com.lacouf.rsbjwt.repository.*;
import com.lacouf.rsbjwt.service.dto.CredentialDTO;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import com.lacouf.rsbjwt.service.dto.EvaluationStageProfDTO;
import com.lacouf.rsbjwt.service.dto.ProfesseurDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ProfesseurServiceTest {

    private ProfesseurRepository professeurRepository;
    private EtudiantRepository etudiantRepository;

    private EvaluationStageProfRepository evaluationStageProfRepository;
    private ProfesseurService professeurService;

    private EtudiantService etudiantService;
    private ProfesseurController professeurController;

    private UserAppRepository userAppRepository;

    private CVRepository cvRepository;

    private OffreDeStageRepository offreDeStageRepository;

    private EntrevueRepository entrevueRepository;

    private ContratRepository contratRepository;


    private ProfesseurDTO newProfesseur;
    private Professeur professeurEntity;
    private Etudiant etudiantEntity;
    private PasswordEncoder passwordEncoder;
    private CandidatAccepterRepository candidatAccepterRepository;

    @BeforeEach
    void setUp() {
        professeurRepository = Mockito.mock(ProfesseurRepository.class);
        etudiantRepository = Mockito.mock(EtudiantRepository.class);
        evaluationStageProfRepository = Mockito.mock(EvaluationStageProfRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        candidatAccepterRepository = Mockito.mock(CandidatAccepterRepository.class);
        professeurService = new ProfesseurService(professeurRepository, etudiantRepository, passwordEncoder, evaluationStageProfRepository);
        etudiantService = new EtudiantService(userAppRepository, etudiantRepository, passwordEncoder, cvRepository, offreDeStageRepository, entrevueRepository, contratRepository, candidatAccepterRepository);
        professeurController = new ProfesseurController(professeurService, etudiantService);

        CredentialDTO credentials = new CredentialDTO("email@gmail.com", "password");
        newProfesseur = new ProfesseurDTO("John", "Doe", Role.PROFESSEUR, "23456789", credentials, Departement.TECHNIQUES_INFORMATIQUE);
        professeurEntity = new Professeur("John", "Doe", "email@gmail.com", "password", "23456789", Departement.TECHNIQUES_INFORMATIQUE);
        professeurEntity.setEtudiants(new ArrayList<>());
        etudiantEntity = new Etudiant("John", "Doe", "email@gmail.com", "password", "123456789", Departement.TECHNIQUES_INFORMATIQUE);
    }

    @Test
    void shouldCreateProfesseur() {
        // Arrange
        when(professeurRepository.save(any(Professeur.class)))
                .thenReturn(professeurEntity);
        // Act
        ResponseEntity<ProfesseurDTO> response = professeurController.creerProfesseur(newProfesseur);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(newProfesseur.getFirstName(), response.getBody().getFirstName());
        assertEquals(newProfesseur.getLastName(), response.getBody().getLastName());
    }

    @Test
    void shouldReturnEmptyWhenExceptionIsThrown() {
        // Arrange
        when(professeurRepository.save(any(Professeur.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        Optional<ProfesseurDTO> response = professeurService.creerProfesseur(newProfesseur);

        // Assert
        assertTrue(response.isEmpty());
    }

    @Test
    void shouldReturnProfesseurById() {
        // Arrange
        Long professeurId = 1L;

        when(professeurRepository.findById(professeurId))
                .thenReturn(Optional.of(professeurEntity));

        // Act
        Optional<ProfesseurDTO> response = professeurService.getProfesseurById(professeurId);

        // Assert
        assertTrue(response.isPresent());
        assertEquals(professeurEntity.getFirstName(), response.get().getFirstName());
        assertEquals(professeurEntity.getLastName(), response.get().getLastName());
        assertEquals(professeurEntity.getEmail(), response.get().getCredentials().getEmail());
    }

    @Test
    void shouldReturnNotFoundWhenProfesseurNotFound() {
        // Arrange
        Long professeurId = 1L;
        when(professeurService.getProfesseurById(professeurId))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<ProfesseurDTO> response = professeurController.getProfesseurById(professeurId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAllProfesseurs_shouldReturnListOfProfesseurDTOs() {
        // Arrange
        List<Professeur> professeurs = List.of(professeurEntity);
        when(professeurRepository.findAll()).thenReturn(professeurs);

        // Act
        List<ProfesseurDTO> result = professeurService.getAllProfesseurs();

        // Assert
        assertEquals(1, result.size());
        assertEquals(professeurEntity.getEmail(), result.get(0).getCredentials().getEmail());
    }

    @Test
    void assignerEtudiants_shouldAssignEtudiantsToProfesseur() {
        // Arrange
        String professeurEmail = "email@gmail.com";
        List<String> etudiantsEmails = List.of("student1@gmail.com");

        when(professeurRepository.findByEmail(professeurEmail)).thenReturn(Optional.of(professeurEntity));
        when(etudiantRepository.findAllByEmailIn(etudiantsEmails)).thenReturn(List.of(etudiantEntity));

        // Act
        Optional<ProfesseurDTO> result = professeurService.assignerEtudiants(professeurEmail, etudiantsEmails);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1, professeurEntity.getEtudiants().size());
        assertEquals(etudiantEntity.getEmail(), professeurEntity.getEtudiants().get(0).getEmail());
    }

    @Test
    void assignerEtudiants_shouldReturnEmptyWhenProfesseurNotFound() {
        // Arrange
        String professeurEmail = "email@gmail.com";
        List<String> etudiantsEmails = List.of("student1@gmail.com");

        when(professeurRepository.findByEmail(professeurEmail)).thenReturn(Optional.empty());

        // Act
        Optional<ProfesseurDTO> result = professeurService.assignerEtudiants(professeurEmail, etudiantsEmails);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void assignerEtudiants_shouldReturnEmptyWhenNoEtudiantsFound() {
        // Arrange
        String professeurEmail = "email@gmail.com";
        List<String> etudiantsEmails = List.of("nonexistentstudent@gmail.com");

        when(professeurRepository.findByEmail(professeurEmail)).thenReturn(Optional.of(professeurEntity));
        when(etudiantRepository.findAllByEmailIn(etudiantsEmails)).thenReturn(new ArrayList<>());

        // Act
        Optional<ProfesseurDTO> result = professeurService.assignerEtudiants(professeurEmail, etudiantsEmails);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void getEtudiants_shouldReturnListOfEtudiantDTOs() {
        // Arrange
        String professeurEmail = "email@gmail.com";
        List<Etudiant> etudiants = List.of(etudiantEntity);
        professeurEntity.setEtudiants(etudiants);

        when(professeurRepository.findByEmail(professeurEmail)).thenReturn(Optional.of(professeurEntity));
        when(etudiantRepository.findByEmail(etudiantEntity.getEmail())).thenReturn(Optional.of(etudiantEntity));

        // Act
        List<EtudiantDTO> result = professeurService.getEtudiants(professeurEmail);

        // Assert
        assertEquals(1, result.size());
        assertEquals(etudiantEntity.getEmail(), result.get(0).getEmail());
    }

    @Test
    void getEtudiants_shouldReturnEmptyListWhenNoEtudiantsFound() {
        // Arrange
        String professeurEmail = "email@gmail.com";
        professeurEntity.setEtudiants(new ArrayList<>());

        when(professeurRepository.findByEmail(professeurEmail)).thenReturn(Optional.of(professeurEntity));

        // Act
        List<EtudiantDTO> result = professeurService.getEtudiants(professeurEmail);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getEtudiants_shouldReturnEmptyListWhenProfesseurNotFound() {
        // Arrange
        String professeurEmail = "email@gmail.com";

        when(professeurRepository.findByEmail(professeurEmail)).thenReturn(Optional.empty());

        // Act
        List<EtudiantDTO> result = professeurService.getEtudiants(professeurEmail);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void creerEvaluationStage() {

        Professeur professeur = new Professeur("A", "B", "C", "D", "E", Departement.TECHNIQUES_INFORMATIQUE);
        List<Etudiant> etudiants = List.of(new Etudiant("B", "C", "D", "E", "F", Departement.TECHNIQUES_INFORMATIQUE));

        professeur.setEtudiants(etudiants);

        Employeur employeur = new Employeur();

        OffreDeStage offreDeStage = new OffreDeStage();

        offreDeStage.setEmployeur(employeur);
        offreDeStage.setEtudiants(etudiants);

        Entrevue entrevue = new Entrevue();

        entrevue.setEtudiant(new Etudiant("B", "C", "D", "E", "F", Departement.TECHNIQUES_INFORMATIQUE));
        entrevue.setOffreDeStage(offreDeStage);

        Contrat contrat = new Contrat();

        CandidatAccepter candidatAccepter = new CandidatAccepter();

        contrat.setCandidature(candidatAccepter);

        candidatAccepter.setEntrevue(entrevue);

        when(professeurRepository.findByEmail(any(String.class))).thenReturn(Optional.of(professeur));
        when(etudiantRepository.findByEmail(any(String.class))).thenReturn(Optional.of(etudiantEntity));
        when(etudiantRepository.findOffreDeStageByEntrevue(etudiants.get(0).getId())).thenReturn((offreDeStage));
        when(etudiantRepository.findContratByEntrevue(etudiants.get(0).getId())).thenReturn(contrat);
        when(evaluationStageProfRepository.findByProsseurID(any(Long.class))).thenReturn(Optional.of(new EvaluationStageProf()));
        when(evaluationStageProfRepository.save(any())).thenReturn(null);


        professeurService.creerEvaluationStage("A", List.of("B"));

        Mockito.verify(evaluationStageProfRepository, Mockito.times(1)).save(any());
    }

    @Test
    void evaluerStage() {

        EvaluationStageProf evaluationStageProf = getEvaluationStageProf();


        when(evaluationStageProfRepository.findByEtudiantID(any(Long.class))).thenReturn(evaluationStageProf);
        when(evaluationStageProfRepository.save(any())).thenReturn(null);

        EvaluationStageProfDTO evaluationStageProfDTO = new EvaluationStageProfDTO(evaluationStageProf);

        professeurService.evaluerStage(evaluationStageProfDTO);

        Mockito.verify(evaluationStageProfRepository, Mockito.times(1)).save(any());


    }

    private static EvaluationStageProf getEvaluationStageProf() {
        Etudiant etudiant = new Etudiant("B", "C", "D", "E", "F", Departement.TECHNIQUES_INFORMATIQUE);
        etudiant.setId(1L);
        Professeur professeur = new Professeur("A", "B", "C", "D", "E", Departement.TECHNIQUES_INFORMATIQUE);
        professeur.setId(1L);

        EvaluationStageProf evaluationStageProf = new EvaluationStageProf();
        evaluationStageProf.setId(1L);

        evaluationStageProf.setEtudiant(etudiant);
        evaluationStageProf.setProfesseur(professeur);
        evaluationStageProf.setNomEntreprise("A");
        evaluationStageProf.setPersonneContact("B");
        evaluationStageProf.setAdresse("C");

        evaluationStageProf.setTelephone("F");
        evaluationStageProf.setNomStagiaire("H");
        evaluationStageProf.setDateStage(LocalDate.now());

        evaluationStageProf.setTachesConformite(EvaluationStageProf.EvaluationConformite.TOTAL_EN_ACCORD);
        evaluationStageProf.setAccueilIntegration(EvaluationStageProf.EvaluationConformite.TOTAL_EN_ACCORD);
        evaluationStageProf.setEncadrementSuffisant(EvaluationStageProf.EvaluationConformite.TOTAL_EN_ACCORD);

        evaluationStageProf.setHeuresEncadrementPremierMois(1);
        evaluationStageProf.setHeuresEncadrementDeuxiemeMois(1);
        evaluationStageProf.setHeuresEncadrementTroisiemeMois(1);
        evaluationStageProf.setRespectNormesHygiene(EvaluationStageProf.EvaluationConformite.TOTAL_EN_ACCORD);

        evaluationStageProf.setSalaireHoraire(1);
        evaluationStageProf.setClimatDeTravail(EvaluationStageProf.EvaluationConformite.TOTAL_EN_ACCORD);
        evaluationStageProf.setAccesTransportCommun(EvaluationStageProf.EvaluationConformite.TOTAL_EN_ACCORD);
        evaluationStageProf.setSalaireInteressant(EvaluationStageProf.EvaluationConformite.TOTAL_EN_ACCORD);
        evaluationStageProf.setPrivilegiePremierStage(true);
        evaluationStageProf.setPrivilegieDeuxiemeStage(true);
        evaluationStageProf.setNombreStagiairesAccueillis(1);
        evaluationStageProf.setSouhaiteRevoirStagiaire(true);
        evaluationStageProf.setOffreQuartsVariables(true);
        evaluationStageProf.setHorairesQuartsDeTravail("A");
        evaluationStageProf.setCommentaires("B");
        evaluationStageProf.setSignatureEnseignant("C");
        evaluationStageProf.setDateSignature("D");

        return evaluationStageProf;
    }

    @Test
    void getEvaluationsStageProf() {
        Professeur professeur = new Professeur("A", "B", "C@b.vom", "D", "E", Departement.TECHNIQUES_INFORMATIQUE);
        professeur.setId(1L);

        EvaluationStageProf evaluationStageProf = getEvaluationStageProf();

        List<EvaluationStageProf> evaluationStageProfs = List.of(evaluationStageProf);


        when(professeurRepository.findByEmail(any(String.class))).thenReturn(Optional.of(professeur));
        when(evaluationStageProfRepository.findAllByProfesseur(professeur)).thenReturn(evaluationStageProfs);

        List<EvaluationStageProfDTO> result = professeurService.getEvaluationsStageProf("C@b.vom");

        assertEquals(1, result.size());

    }
}
