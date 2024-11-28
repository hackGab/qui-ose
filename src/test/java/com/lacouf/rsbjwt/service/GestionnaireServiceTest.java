package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.*;
import com.lacouf.rsbjwt.model.auth.Credentials;
import com.lacouf.rsbjwt.model.auth.Role;
import com.lacouf.rsbjwt.repository.*;
import com.lacouf.rsbjwt.service.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GestionnaireServiceTest {

    private GestionnaireService gestionnaireService;
    private GestionnaireRepository gestionnaireRepository;
    private CVRepository cvRepository;
    private EtudiantRepository etudiantRepository;
    private PasswordEncoder passwordEncoder;
    private ContratRepository contratRepository;
    private CandidatAccepterRepository candidatAccepterRepository;
    private OffreDeStageRepository offreDeStageRepository;
    private EntrevueRepository entrevueRepository;
    private ProfesseurRepository professeurRepository;
    private EvaluationStageProfRepository evaluationStageProfRepository;
    private NotificationRepository notificationRepository;
    private Etudiant etudiantEntity;
    private Employeur employeur;
    private OffreDeStage offreDeStage;
    private Professeur professeur;


    @BeforeEach
    void setUp() {
        gestionnaireRepository = Mockito.mock(GestionnaireRepository.class);
        cvRepository = Mockito.mock(CVRepository.class);
        etudiantRepository = Mockito.mock(EtudiantRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        contratRepository = Mockito.mock(ContratRepository.class);
        candidatAccepterRepository = Mockito.mock(CandidatAccepterRepository.class);
        offreDeStageRepository = Mockito.mock(OffreDeStageRepository.class);
        entrevueRepository = Mockito.mock(EntrevueRepository.class);
        professeurRepository = Mockito.mock(ProfesseurRepository.class);
        evaluationStageProfRepository = Mockito.mock(EvaluationStageProfRepository.class);
        notificationRepository = Mockito.mock(NotificationRepository.class);

        gestionnaireService = new GestionnaireService(
                gestionnaireRepository, cvRepository, etudiantRepository, offreDeStageRepository,
                passwordEncoder, contratRepository, entrevueRepository, professeurRepository,
                evaluationStageProfRepository, notificationRepository, candidatAccepterRepository
        );
        etudiantEntity = new Etudiant("John", "Doe", "email@gmail.com", "password", "123456789", Departement.TECHNIQUES_INFORMATIQUE);
        employeur = new Employeur();
        employeur.setId(1L);
        employeur.setEntreprise("Company Name");
        employeur.setCredentials(new Credentials("email@example.com", "password", Role.EMPLOYEUR));
        offreDeStage = new OffreDeStage("Stage Title", "Location", LocalDate.now(), "data", 1, "Attente", "session");
        offreDeStage.setEmployeur(employeur);
        professeur = new Professeur("Jane", "Smith", "jane.smith@example.com", "password", "987654321", Departement.TECHNIQUES_INFORMATIQUE);
    }

    @Test
    void testCreerGestionnaire() {
        GestionnaireDTO gestionnaireDTO = new GestionnaireDTO();
        gestionnaireDTO.setFirstName("John");
        gestionnaireDTO.setLastName("Doe");
        gestionnaireDTO.setPhoneNumber("1234567890");
        gestionnaireDTO.setCredentials(new CredentialDTO("john.doe@example.com", "password"));

        Gestionnaire gestionnaire = new Gestionnaire("John", "Doe", "john.doe@example.com", "encodedPassword", "1234567890");
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(gestionnaireRepository.save(any(Gestionnaire.class))).thenReturn(gestionnaire);

        Optional<GestionnaireDTO> result = gestionnaireService.creerGestionnaire(gestionnaireDTO);

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        assertEquals("Doe", result.get().getLastName());
        assertEquals("1234567890", result.get().getPhoneNumber());
    }

    @Test
    void testCreerGestionnaireWithException() {
        GestionnaireDTO gestionnaireDTO = new GestionnaireDTO();
        gestionnaireDTO.setFirstName("John");
        gestionnaireDTO.setLastName("Doe");
        gestionnaireDTO.setPhoneNumber("1234567890");
        gestionnaireDTO.setCredentials(new CredentialDTO("john.doe@example.com", "password"));

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(gestionnaireRepository.save(any(Gestionnaire.class))).thenThrow(new RuntimeException());

        Optional<GestionnaireDTO> result = gestionnaireService.creerGestionnaire(gestionnaireDTO);

        assertFalse(result.isPresent());
    }

    @Test
    void testValiderOuRejeterCV() {
        CV cv = new CV();
        cv.setId(1L);
        cv.setStatus("attend");

        when(cvRepository.findById(1L)).thenReturn(Optional.of(cv));
        when(cvRepository.save(any(CV.class))).thenReturn(cv);

        Optional<CVDTO> result = gestionnaireService.validerOuRejeterCV(1L, "accepté", "");

        assertTrue(result.isPresent());
        assertEquals("accepté", result.get().getStatus());
    }

    @Test
    void testValiderOuRejeterCVWithRejection() {
        CV cv = new CV();
        cv.setId(1L);
        cv.setStatus("attend");

        when(cvRepository.findById(1L)).thenReturn(Optional.of(cv));
        when(cvRepository.save(any(CV.class))).thenReturn(cv);

        Optional<CVDTO> result = gestionnaireService.validerOuRejeterCV(1L, "rejeté", "raison");

        assertTrue(result.isPresent());
        assertEquals("rejeté", result.get().getStatus());
        assertEquals("raison", result.get().getRejetMessage());
    }

    @Test
    void testValiderOuRejeterCVNonExistant() {
        when(cvRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<CVDTO> result = gestionnaireService.validerOuRejeterCV(1L, "accepté", "");

        assertFalse(result.isPresent());
    }

    @Test
    void testValiderOuRejeterOffre() {
        when(offreDeStageRepository.findById(1L)).thenReturn(Optional.of(offreDeStage));
        when(offreDeStageRepository.save(any(OffreDeStage.class))).thenReturn(offreDeStage);

        Optional<OffreDeStageDTO> result = gestionnaireService.validerOuRejeterOffre(1L, "accepté", "");

        assertTrue(result.isPresent());
        assertEquals("accepté", result.get().getStatus());
    }

    @Test
    void testValiderOuRejeterOffreWithRejection() {
        when(offreDeStageRepository.findById(1L)).thenReturn(Optional.of(offreDeStage));
        when(offreDeStageRepository.save(any(OffreDeStage.class))).thenReturn(offreDeStage);

        Optional<OffreDeStageDTO> result = gestionnaireService.validerOuRejeterOffre(1L, "rejeté", "raison");

        assertTrue(result.isPresent());
        assertEquals("rejeté", result.get().getStatus());
    }

    @Test
    void testValiderOuRejeterOffreNonExistant() {
        when(offreDeStageRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<OffreDeStageDTO> result = gestionnaireService.validerOuRejeterOffre(1L, "accepté", "");

        assertFalse(result.isPresent());
    }

    @Test
    void testCreateNotification() {
        Notification notification = new Notification("title", "message", "email", "url");
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        NotificationDTO result = gestionnaireService.createNotification(notification);

        assertEquals("title", result.getMessage());
        assertEquals("email", result.getEmail());
        assertEquals("url", result.getUrl());
    }



    @Test
    void testCreerContratWithException() {
        CandidatAccepterDTO candidatAccepterDTO = new CandidatAccepterDTO(1L, 1L, true);
        ContratDTO contratDTO = new ContratDTO();
        contratDTO.setCandidature(candidatAccepterDTO);

        when(entrevueRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<ContratDTO> result = gestionnaireService.creerContrat(contratDTO);

        assertFalse(result.isPresent());
    }

    @Test
    void getAllContrats() {

        Contrat contrat = new Contrat();
        contrat.setId(1L);
        contrat.setEtudiantSigne(true);
        contrat.setEmployeurSigne(true);
        contrat.setGestionnaireSigne(true);
        contrat.setDateSignatureEtudiant(LocalDate.now());
        contrat.setDateSignatureEmployeur(LocalDate.now());
        contrat.setDateSignatureGestionnaire(LocalDate.now());
        contrat.setCollegeEngagement("college");
        contrat.setDateDebut(LocalDate.now());
        contrat.setCandidature(new CandidatAccepter(new Entrevue(), true));

        Contrat contrat1 = new Contrat();
        contrat1.setId(2L);
        contrat1.setEtudiantSigne(true);
        contrat1.setEmployeurSigne(true);
        contrat1.setGestionnaireSigne(true);
        contrat1.setDateSignatureEtudiant(LocalDate.now());
        contrat1.setDateSignatureEmployeur(LocalDate.now());
        contrat1.setDateSignatureGestionnaire(LocalDate.now());
        contrat1.setCollegeEngagement("college");
        contrat1.setDateDebut(LocalDate.now());
        contrat1.setCandidature(new CandidatAccepter(new Entrevue(), true));

        when(contratRepository.findAll()).thenReturn(java.util.List.of(contrat, contrat1));
        Iterable<ContratDTO> result = gestionnaireService.getAllContrats();

        assertTrue(result.iterator().hasNext());
        assertEquals(2, result.spliterator().getExactSizeIfKnown());
    }

    @Test
    void signerContratGestionnaire() {
        String uuid = "uuid";
        String password = "password";
        String email = "email";

        Contrat contrat = new Contrat();
        contrat.setId(1L);
        contrat.setEtudiantSigne(true);
        contrat.setEmployeurSigne(true);
        contrat.setGestionnaireSigne(false);
        contrat.setDateSignatureEtudiant(LocalDate.now());
        contrat.setDateSignatureEmployeur(LocalDate.now());
        contrat.setDateSignatureGestionnaire(null);
        contrat.setCollegeEngagement("college");
        contrat.setDateDebut(LocalDate.now());
        contrat.setCandidature(new CandidatAccepter(new Entrevue(), true));

        Contrat contrat1 = new Contrat();
        contrat1.setId(1L);
        contrat1.setEtudiantSigne(true);
        contrat1.setEmployeurSigne(true);
        contrat1.setGestionnaireSigne(true);
        contrat1.setDateSignatureEtudiant(LocalDate.now());
        contrat1.setDateSignatureEmployeur(LocalDate.now());
        contrat1.setDateSignatureGestionnaire(LocalDate.now());
        contrat1.setCollegeEngagement("college");
        contrat1.setDateDebut(LocalDate.now());
        contrat1.setCandidature(new CandidatAccepter(new Entrevue(), true));

        Gestionnaire gestionnaire = new Gestionnaire();
        gestionnaire.setId(1L);
        gestionnaire.setCredentials(new Credentials("gestionnaire@gmail.com", "password", Role.GESTIONNAIRE));

        when(contratRepository.findByUUID(uuid)).thenReturn(Optional.of(contrat));
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat1);
        when(passwordEncoder.matches(password, "password")).thenReturn(true);
        when(gestionnaireRepository.findByEmail(email)).thenReturn(gestionnaire);

        Optional<ContratDTO> result = gestionnaireService.signerContratGestionnaire(uuid, password, email);

        assertTrue(result.isPresent());
        assertTrue(result.get().isGestionnaireSigne());
    }

    @Test
    void testSignerContratGestionnaireWithException() {
        String uuid = "uuid";
        String password = "password";
        String email = "email";

        when(contratRepository.findByUUID(uuid)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> gestionnaireService.signerContratGestionnaire(uuid, password, email));
    }

    @Test
    void testAssignerProfesseur() {
        Long etudiantId = 3L;
        Long professeurId = 4L;

        Etudiant etudiant1 = new Etudiant();
        etudiant1.setId(etudiantId);
        etudiant1.setCredentials(new Credentials("etudiant11@example.com", "password", Role.ETUDIANT));

        Professeur professeur1 = new Professeur();
        professeur1.setId(professeurId);
        professeur1.setCredentials(new Credentials("professeue1@gmail.com", "password", Role.PROFESSEUR));

        Contrat contrat = new Contrat();
        contrat.setCandidature(new CandidatAccepter(new Entrevue(), true));
        contrat.setGestionnaireSigne(true);

        when(etudiantRepository.findById(etudiantId)).thenReturn(Optional.of(etudiant1));
        when(professeurRepository.findById(professeurId)).thenReturn(Optional.of(professeur));
        when(contratRepository.findByCandidature_EtudiantAndGestionnaireSigneTrue(etudiant1)).thenReturn(Optional.of(contrat));
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant1);

        Optional<EtudiantDTO> result = gestionnaireService.assignerProfesseur(etudiantId, professeurId);

        assertTrue(result.isPresent());
    }

    @Test
    void testAssignerProfesseurWithException() {
        Long etudiantId = 1L;
        Long professeurId = 2L;

        Etudiant etudiant = new Etudiant();
        Professeur professeur = new Professeur();

        when(etudiantRepository.findById(etudiantId)).thenReturn(Optional.of(etudiant));
        when(professeurRepository.findById(professeurId)).thenReturn(Optional.of(professeur));
        when(contratRepository.findByCandidature_EtudiantAndGestionnaireSigneTrue(etudiant)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> gestionnaireService.assignerProfesseur(etudiantId, professeurId));
    }


    @Test
    void testGetOffresBySession() {
        when(offreDeStageRepository.findBySession("session")).thenReturn(List.of(offreDeStage));

        List<OffreDeStageDTO> result = gestionnaireService.getOffresBySession("session");

        assertEquals(1, result.size());
        assertEquals("Stage Title", result.get(0).getTitre());
    }

    @Test
    void testGetNombreOffresEnAttente() {
        when(offreDeStageRepository.findAll()).thenReturn(List.of(offreDeStage));

        int result = gestionnaireService.getNombreOffresEnAttente();

        assertEquals(1, result);
    }

    @Test
    void testGetAllCandidatures() {
        CandidatAccepter candidatAccepter = new CandidatAccepter();
        Entrevue entrevue = new Entrevue();
        candidatAccepter.setEntrevue(entrevue);
        when(candidatAccepterRepository.findAll()).thenReturn(List.of(candidatAccepter));

        List<CandidatAccepterDTO> result = gestionnaireService.getAllCandidatures();

        assertEquals(1, result.size());
    }

    @Test
    void testGetCandidaturesBySession() {
        CandidatAccepter candidatAccepter = new CandidatAccepter();
        Entrevue entrevue = new Entrevue();
        candidatAccepter.setEntrevue(entrevue);
        when(candidatAccepterRepository.findByEntrevueSession("session")).thenReturn(List.of(candidatAccepter));

        List<CandidatAccepterDTO> result = gestionnaireService.getCandidaturesBySession("session");

        assertEquals(1, result.size());
    }
}