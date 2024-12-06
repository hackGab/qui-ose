package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.*;
import com.lacouf.rsbjwt.model.auth.Role;
import com.lacouf.rsbjwt.repository.*;
import com.lacouf.rsbjwt.presentation.EtudiantController;
import com.lacouf.rsbjwt.service.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EtudiantServiceTest {

    private EtudiantRepository etudiantRepository;
    private EtudiantService etudiantService;
    private EtudiantController etudiantController;

    private UserAppRepository userAppRepository;
    private CVRepository cvRepository;
    private OffreDeStageRepository offreDeStageRepository;

    private EtudiantDTO newEtudiant;
    private Etudiant etudiantEntity;
    private CV cvEntity;
    private PasswordEncoder passwordEncoder;

    private EntrevueRepository entrevueRepository;

    private Employeur employeur;

    private ContratRepository contratRepository;
    private CandidatAccepterRepository candidatAccepterRepository;
    @BeforeEach
    void setUp() {
        userAppRepository = Mockito.mock(UserAppRepository.class);
        cvRepository = Mockito.mock(CVRepository.class);
        etudiantRepository = Mockito.mock(EtudiantRepository.class);
        offreDeStageRepository = Mockito.mock(OffreDeStageRepository.class);
        entrevueRepository = Mockito.mock(EntrevueRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        contratRepository = Mockito.mock(ContratRepository.class);
        candidatAccepterRepository = Mockito.mock(CandidatAccepterRepository.class);
        etudiantService = new EtudiantService(userAppRepository, etudiantRepository, passwordEncoder, cvRepository, offreDeStageRepository, entrevueRepository, contratRepository, candidatAccepterRepository );
        etudiantController = new EtudiantController(etudiantService);

        CredentialDTO credentials = new CredentialDTO("email@gmail.com", "password");
        newEtudiant = new EtudiantDTO("John", "Doe", Role.ETUDIANT, "123456789", credentials, Departement.TECHNIQUES_INFORMATIQUE);
        etudiantEntity = new Etudiant("John", "Doe", "email@gmail.com", "password", "123456789", Departement.TECHNIQUES_INFORMATIQUE);
        cvEntity = new CV("cvName", "cvType", "cvData", "cvStatus");
        employeur = new Employeur("John", "Doe", "john@gmail.com", "1234", "1231231234", "entreprise");

    }

    @Test
    void shouldCreateEtudiant() {
        // Arrange
        when(etudiantRepository.save(any(Etudiant.class)))
                .thenReturn(etudiantEntity);

        // Act
        ResponseEntity<EtudiantDTO> response = etudiantController.creerEtudiant(newEtudiant);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(newEtudiant.getFirstName(), response.getBody().getFirstName());
        assertEquals(newEtudiant.getLastName(), response.getBody().getLastName());
    }

    @Test
    void shouldReturnEmptyWhenExceptionIsThrown() {
        // Arrange
        when(etudiantRepository.save(any(Etudiant.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        Optional<EtudiantDTO> response = etudiantService.creerEtudiant(newEtudiant);

        // Assert
        assertTrue(response.isEmpty());
    }

    @Test
    void shouldReturnEtudiantWhenFound() {
        // Arrange
        Long etudiantId = 1L;
        when(etudiantRepository.findById(etudiantId))
                .thenReturn(Optional.of(etudiantEntity));

        // Act
        Optional<EtudiantDTO> response = etudiantService.getEtudiantById(etudiantId);

        // Assert
        assertTrue(response.isPresent());
        assertEquals(etudiantEntity.getFirstName(), response.get().getFirstName());
        assertEquals(etudiantEntity.getLastName(), response.get().getLastName());
        assertEquals(etudiantEntity.getCredentials().getEmail(), response.get().getCredentials().getEmail());
    }

    @Test
    void shouldReturnNotFoundWhenEtudiantNotFound() {
        // Arrange
        Long etudiantId = 1L;

        when(etudiantService.getEtudiantById(etudiantId))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<EtudiantDTO> response = etudiantController.getEtudiantById(etudiantId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldReturnEtudiantWhenFoundByEmail() {
        // Arrange
        String email = "email@gmail.com";
        when(userAppRepository.findUserAppByEmail(email))
                .thenReturn(Optional.of(etudiantEntity));

        // Act
        Optional<EtudiantDTO> response = etudiantService.getEtudiantByEmail(email);

        // Assert
        assertTrue(response.isPresent());
        assertEquals(etudiantEntity.getFirstName(), response.get().getFirstName());
        assertEquals(etudiantEntity.getLastName(), response.get().getLastName());
        assertEquals(etudiantEntity.getCredentials().getEmail(), response.get().getCredentials().getEmail());
    }

    @Test
    void shouldReturnEmptyWhenEtudiantNotFoundByEmail() {
        // Arrange
        String email = "null";
        when(userAppRepository.findUserAppByEmail(email))
                .thenReturn(Optional.empty());

        // Act
        Optional<EtudiantDTO> response = etudiantService.getEtudiantByEmail(email);

        // Assert
        assertTrue(response.isEmpty());
    }

    @Test
    void shouldCreerUnCV() {
        // Arrange
        String email = "email@gmail.com";
        CVDTO cvDTO = new CVDTO("cvName", "cvType", "cvData", "cvStatus");
        when(cvRepository.save(any(CV.class)))
                .thenReturn(cvEntity);
        when(userAppRepository.findUserAppByEmail(email))
                .thenReturn(Optional.of(etudiantEntity));

        // Act
        Optional<CVDTO> response = etudiantService.creerCV(cvDTO, email);

        // Assert
        assertTrue(response.isPresent());
        assertEquals(cvEntity.getName(), response.get().getName());
        assertEquals(cvEntity.getType(), response.get().getType());
        assertEquals(cvEntity.getData(), response.get().getData());
        assertEquals(cvEntity.getStatus(), response.get().getStatus());
    }

    @Test
    void shouldReturnEmptyWhenExceptionIsThrownWhileCreatingCV() {
        // Arrange
        String email = "null@gmail.com";
        CVDTO cvDTO = new CVDTO("cvName", "cvType", "cvData", "cvStatus");
        when(cvRepository.save(any(CV.class)))
                .thenThrow(new RuntimeException("Database error"));
        when(userAppRepository.findUserAppByEmail(email))
                .thenReturn(Optional.of(etudiantEntity));

        // Act
        Optional<CVDTO> response = etudiantService.creerCV(cvDTO, email);

        // Assert
        assertTrue(response.isEmpty());
    }

    @Test
    void shouldDeleteCV() {
        // Arrange
        Long cvId = 1L;

        // Act
        etudiantService.supprimerCV(cvId);

        // Assert
        verify(cvRepository, times(1)).deleteById(cvId);
    }

    @Test
    void shouldReturnEmptyIfEtudiantNotFound() {
        // Arrange
        String etudiantEmail = "email@gmail.com";
        Long offreId = 1L;
        when(etudiantRepository.findByEmail(etudiantEmail))
                .thenReturn(Optional.empty());

        // Act
        Optional<EtudiantDTO> response = etudiantService.ajouterOffreDeStage(etudiantEmail, offreId);

        // Assert
        assertTrue(response.isEmpty());
    }

    @Test
    void shouldReturnEmptyIfOffreNotFound() {
        // Arrange
        String etudiantEmail = "email@gmail.com";
        Long offreId = 1L;
        when(etudiantRepository.findByEmail(etudiantEmail))
                .thenReturn(Optional.of(etudiantEntity));
        when(offreDeStageRepository.findById(offreId))
                .thenReturn(Optional.empty());

        // Act
        Optional<EtudiantDTO> response = etudiantService.ajouterOffreDeStage(etudiantEmail, offreId);

        // Assert
        assertTrue(response.isEmpty());
    }

    @Test
    void shouldReturnAllEtudiants() {
        // Arrange
        List<Etudiant> etudiants = new ArrayList<>();
        etudiants.add(etudiantEntity);
        when(etudiantRepository.findAll()).thenReturn(etudiants);

        // Act
        Iterable<EtudiantDTO> response = etudiantService.getAllEtudiants();

        // Assert
        assertNotNull(response);
        assertEquals(1, ((List<EtudiantDTO>) response).size());
        assertEquals(etudiantEntity.getFirstName(), ((List<EtudiantDTO>) response).get(0).getFirstName());
    }

    @Test
    void shouldGetOffresApprouvees() {
        // Arrange
        String session = "HIVER25";
        OffreDeStage offreDeStage = new OffreDeStage();
        offreDeStage.setStatus("Validé");
        offreDeStage.setEmployeur(employeur);
        List<OffreDeStage> offres = List.of(offreDeStage);


        when(offreDeStageRepository.findBySession(session)).thenReturn(offres);


        // Act
        List<OffreDeStageDTO> response = etudiantService.getOffresApprouveesParSession(session);

        // Assert
        assertEquals(1, response.size());
        assertEquals("Validé", response.get(0).getStatus());
    }

    @Test
    void shouldGetOffresDeStage() {
        // Arrange
        String email = "email@gmail.com";
        OffreDeStage offreDeStage = new OffreDeStage();
        offreDeStage.setEmployeur(employeur);
        List<OffreDeStage> offres = List.of(offreDeStage);
        etudiantEntity.setOffresAppliquees(offres);

        when(etudiantRepository.findByEmail(email)).thenReturn(Optional.of(etudiantEntity));

        // Act
        Iterable<OffreDeStageDTO> response = etudiantService.getOffresDeStage(email);

        // Assert
        assertNotNull(response);
        assertEquals(1, ((List<OffreDeStageDTO>) response).size());
    }

    @Test
    void shouldRetirerOffreDeStage() {
        // Arrange
        String email = "email@gmail.com";
        Long offreId = 1L;
        OffreDeStage offreDeStage = new OffreDeStage();
        offreDeStage.setEmployeur(employeur);
        List<Etudiant> etudiants = new ArrayList<>();
        etudiants.add(etudiantEntity);
        offreDeStage.setEtudiants(etudiants);

        List<OffreDeStage> offresAppliquees = new ArrayList<>();
        offresAppliquees.add(offreDeStage);
        etudiantEntity.setOffresAppliquees(offresAppliquees);

        when(etudiantRepository.findByEmail(email)).thenReturn(Optional.of(etudiantEntity));
        when(offreDeStageRepository.findById(offreId)).thenReturn(Optional.of(offreDeStage));

        // Act
        Optional<EtudiantDTO> response = etudiantService.retirerOffreDeStage(email, offreId);

        // Assert
        assertTrue(response.isPresent());
    }

    @Test
    void shouldGetEntrevuesByEtudiant() {
        // Arrange
        String email = "email@gmail.com";
        OffreDeStage offreDeStage = new OffreDeStage();
        offreDeStage.setEmployeur(employeur);

        Entrevue entrevue = new Entrevue();
        entrevue.setEtudiant(etudiantEntity);
        entrevue.setOffreDeStage(offreDeStage);

        List<Entrevue> entrevues = List.of(entrevue);

        when(etudiantRepository.findByEmail(email)).thenReturn(Optional.of(etudiantEntity));
        when(entrevueRepository.findAllByEtudiantId(etudiantEntity.getId())).thenReturn(entrevues);

        // Act
        List<EntrevueDTO> response = etudiantService.getEntrevuesByEtudiant(email);

        // Assert
        assertEquals(1, response.size());
    }

    @Test
    void shouldGetEntrevuesEnAttenteByEtudiant() {
        // Arrange
        String email = "email@gmail.com";

        OffreDeStage offreDeStage = new OffreDeStage();
        offreDeStage.setEmployeur(employeur);

        Entrevue entrevue = new Entrevue();
        entrevue.setEtudiant(etudiantEntity);
        entrevue.setOffreDeStage(offreDeStage);
        entrevue.setStatus("En attente");
        List<Entrevue> entrevues = List.of(entrevue);
        when(etudiantRepository.findByEmail(email)).thenReturn(Optional.of(etudiantEntity));
        when(entrevueRepository.findAllByEtudiantId(etudiantEntity.getId())).thenReturn(entrevues);

        // Act
        List<EntrevueDTO> response = etudiantService.getEntrevuesEnAttenteByEtudiant(email);

        // Assert
        assertEquals(1, response.size());
        assertEquals("En attente", response.getFirst().getStatus());
    }

    @Test
    void shouldChangerStatusEntrevue() {
        // Arrange
        String email = "email@gmail.com";
        Long offreId = 1L;
        OffreDeStage offreDeStage = new OffreDeStage();
        offreDeStage.setEmployeur(employeur);

        Entrevue entrevue = new Entrevue();
        entrevue.setEtudiant(etudiantEntity);
        entrevue.setOffreDeStage(offreDeStage);
        String status = "Accepter";
        when(etudiantRepository.findByEmail(email)).thenReturn(Optional.of(etudiantEntity));
        when(entrevueRepository.findByEtudiantIdAndOffreDeStageId(etudiantEntity.getId(), offreId)).thenReturn(Optional.of(entrevue));

        // Act
        Optional<EntrevueDTO> response = etudiantService.changerStatusEntrevue(email, offreId, status);

        // Assert
        assertTrue(response.isPresent());
        assertEquals(status, response.get().getStatus());
    }

    @Test
    void shouldGetEntrevuesAccepteesByEtudiant() {
        // Arrange
        String email = "email@gmail.com";
        OffreDeStage offreDeStage = new OffreDeStage();
        offreDeStage.setEmployeur(employeur);

        Entrevue entrevue = new Entrevue();
        entrevue.setEtudiant(etudiantEntity);
        entrevue.setOffreDeStage(offreDeStage);

        entrevue.setStatus("Accepter");
        List<Entrevue> entrevues = List.of(entrevue);
        when(etudiantRepository.findByEmail(email)).thenReturn(Optional.of(etudiantEntity));
        when(entrevueRepository.findAllByEtudiantId(etudiantEntity.getId())).thenReturn(entrevues);

        // Act
        List<EntrevueDTO> response = etudiantService.getEntrevuesAccepteesByEtudiant(email);

        // Assert
        assertEquals(1, response.size());
        assertEquals("Accepter", response.get(0).getStatus());
    }

    @Test
    void shouldSignContratSuccessfully() {
        // Arrange
        String uuid = "unique-uuid";
        String password = "password";


        etudiantEntity.getPassword();


        OffreDeStage offreDeStage = new OffreDeStage();
        offreDeStage.setEmployeur(employeur);

        Entrevue entrevue = new Entrevue();
        entrevue.setEtudiant(etudiantEntity);
        entrevue.setOffreDeStage(offreDeStage);

        CandidatAccepter candidature = new CandidatAccepter();
        candidature.setEntrevue(entrevue);


        Contrat contrat = new Contrat();
        contrat.setUUID(uuid);
        contrat.setCandidature(candidature);
        contrat.setEtudiantSigne(false);


        when(contratRepository.findByUUID(uuid)).thenReturn(Optional.of(contrat));

        when(passwordEncoder.matches(password, etudiantEntity.getPassword())).thenReturn(true);

        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);

        // Act
        Optional<ContratDTO> response = etudiantService.signerContratParEtudiant(uuid, password);

        // Assert
        assertTrue(response.isPresent());
        assertTrue(contrat.isEtudiantSigne());
    }


    @Test
    void shouldThrowExceptionForIncorrectPassword() {
        // Arrange
        String uuid = "unique-uuid";
        String incorrectPassword = "wrongPassword";
        String encodedPassword = "encodedPassword";

        // Crée l'entité de l'étudiant et définit son mot de passe encodé
        etudiantEntity.getPassword();

        // Préparation de l'offre de stage, de l'entrevue et de la candidature
        OffreDeStage offreDeStage = new OffreDeStage();
        offreDeStage.setEmployeur(employeur);

        Entrevue entrevue = new Entrevue();
        entrevue.setEtudiant(etudiantEntity);
        entrevue.setOffreDeStage(offreDeStage);

        CandidatAccepter candidature = new CandidatAccepter();
        candidature.setEntrevue(entrevue);

        // Configuration du contrat
        Contrat contrat = new Contrat();
        contrat.setUUID(uuid);
        contrat.setCandidature(candidature);
        contrat.setEtudiantSigne(false);  // Initialement non signé

        // Mock du repository pour retourner le contrat
        when(contratRepository.findByUUID(uuid)).thenReturn(Optional.of(contrat));
        // Mock du passwordEncoder pour valider le mot de passe
        when(passwordEncoder.matches(incorrectPassword, encodedPassword)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> etudiantService.signerContratParEtudiant(uuid, incorrectPassword));
    }

    @Test
    void shouldReturnEmptyListWhenNoEtudiantsFound() {
        // Arrange
        when(etudiantRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        Iterable<EtudiantDTO> response = etudiantService.getAllEtudiants();

        // Assert
        assertNotNull(response);
        assertEquals(0, ((List<EtudiantDTO>) response).size());
    }


    @Test
    void shouldReturnEmptyListWhenNoApprovedOffres() {
        // Arrange
        String session = "HIVER25";
        OffreDeStage offreDeStage = new OffreDeStage();
        offreDeStage.setStatus("Non validé");
        when(offreDeStageRepository.findAll()).thenReturn(List.of(offreDeStage));

        // Act
        List<OffreDeStageDTO> response = etudiantService.getOffresApprouveesParSession(session);

        // Assert
        assertTrue(response.isEmpty());
    }

    @Test
    void shouldReturnEmptyWhenOffreNotFoundForRetirerOffreDeStage() {
        // Arrange
        String email = "email@gmail.com";
        Long offreId = 1L;
        when(etudiantRepository.findByEmail(email)).thenReturn(Optional.of(etudiantEntity));
        when(offreDeStageRepository.findById(offreId)).thenReturn(Optional.empty());

        // Act
        Optional<EtudiantDTO> response = etudiantService.retirerOffreDeStage(email, offreId);

        // Assert
        assertTrue(response.isEmpty());
    }

    @Test
    void getEtudiantsAvecContratByDepartement() {

        Departement departement = Departement.TECHNIQUES_INFORMATIQUE;

        when(etudiantRepository.findEtudiantsAvecContratByDepartement(departement)).thenReturn(List.of(etudiantEntity));

        List<EtudiantDTO> response = etudiantService.getEtudiantsAvecContratByDepartement(departement);

        assertEquals(1, response.size());
        assertEquals(Departement.TECHNIQUES_INFORMATIQUE, response.get(0).getDepartement());
    }

    @Test
    void shouldReturnNombreCVEnAttente() {
        // Arrange
        Etudiant etudiant1 = new Etudiant();
        Etudiant etudiant2 = new Etudiant();
        CV cv1 = new CV("cvName1", "cvType1", "cvData1", "Attente");
        CV cv2 = new CV("cvName2", "cvType2", "cvData2", "Validé");
        etudiant1.setCv(cv1);
        etudiant2.setCv(cv2);
        List<Etudiant> etudiants = List.of(etudiant1, etudiant2);

        when(etudiantRepository.findAll()).thenReturn(etudiants);

        // Act
        int result = etudiantService.getNombreCVEnAttente();

        // Assert
        assertEquals(1, result);
    }
}
