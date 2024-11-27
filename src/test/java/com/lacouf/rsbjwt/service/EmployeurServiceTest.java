package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.*;
import com.lacouf.rsbjwt.model.auth.Credentials;
import com.lacouf.rsbjwt.model.auth.Role;
import com.lacouf.rsbjwt.presentation.EmployeurController;
import com.lacouf.rsbjwt.repository.*;
import com.lacouf.rsbjwt.service.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EmployeurServiceTest {
    private EmployeurRepository employeurRepository;
    private EntrevueRepository entrevueRepository;
    private UserAppRepository userAppRepository;
    private OffreDeStageRepository offreDeStageRepository;
    private EtudiantRepository etudiantRepository;
    private EmployeurController employeurController;
    private EvaluationStageEmployeurRepository evaluationStageEmployeurRepository;
    private CandidatAccepterRepository candidatAccepterRepository;
    private NotificationRepository notificationRepository;

    private ContratRepository contratRepository;
    private EmployeurService employeurService;

    private EmployeurDTO newEmployeur;
    private Employeur employeurEntity;
    private Etudiant etudiantEntity;
    private EvaluationStageEmployeur evaluationStageEmployeur;
    private EvaluationStageEmployeurDTO evaluationStageEmployeurDTO;
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    void setUp() {
        employeurRepository = Mockito.mock(EmployeurRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        entrevueRepository = Mockito.mock(EntrevueRepository.class);
        userAppRepository = Mockito.mock(UserAppRepository.class);
        offreDeStageRepository = Mockito.mock(OffreDeStageRepository.class);
        etudiantRepository = Mockito.mock(EtudiantRepository.class);
        contratRepository = Mockito.mock(ContratRepository.class);
        evaluationStageEmployeurRepository = Mockito.mock(EvaluationStageEmployeurRepository.class);
        candidatAccepterRepository = Mockito.mock(CandidatAccepterRepository.class);
        notificationRepository = Mockito.mock(NotificationRepository.class);
        employeurService = new EmployeurService(employeurRepository, passwordEncoder, entrevueRepository, userAppRepository, offreDeStageRepository, etudiantRepository, contratRepository, evaluationStageEmployeurRepository, candidatAccepterRepository, notificationRepository);

        CredentialDTO credentials = new CredentialDTO("email@gmail.com", "password");
        newEmployeur = new EmployeurDTO("John", "Doe", "123456789", Role.EMPLOYEUR, credentials, "Entreprise");

        employeurEntity = new Employeur("John", "Doe", "email@gmail.com", "password", "123456789", "Entreprise");
        etudiantEntity = new Etudiant("John", "Doe", "email2gmail.com", "password", "123456789", Departement.TECHNIQUES_INFORMATIQUE);
        evaluationStageEmployeur = new EvaluationStageEmployeur();
        evaluationStageEmployeurDTO = new EvaluationStageEmployeurDTO();
    }

    @Test
    void shouldCreateEmployeur() {
        // Arrange
        when(employeurRepository.save(any(Employeur.class)))
                .thenReturn(employeurEntity);

        // Act
        employeurService.creerEmployeur(newEmployeur);

        // Assert
        Mockito.verify(employeurRepository).save(any(Employeur.class));
        Mockito.verify(passwordEncoder).encode(newEmployeur.getCredentials().getPassword());
    }


    @Test
    void shouldReturnEmptyWhenExceptionIsThrown() {
        // Arrange
        when(employeurRepository.save(any(Employeur.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        Optional<EmployeurDTO> response = employeurService.creerEmployeur(newEmployeur);

        // Assert
        assertTrue(response.isEmpty());
    }

    @Test
    void shouldReturnEmployeurById() {
        // Arrange
        Long employeurId = 1L;

        when(employeurRepository.findById(employeurId))
                .thenReturn(Optional.of(employeurEntity));

        // Act
        Optional<EmployeurDTO> response = employeurService.getEmployeurById(employeurId);

        // Assert
        assertTrue(response.isPresent());
        assertEquals(employeurEntity.getFirstName(), response.get().getFirstName());
        assertEquals(employeurEntity.getLastName(), response.get().getLastName());
        assertEquals(employeurEntity.getEntreprise(), response.get().getEntreprise());
    }

    @Test
    void shouldReturnNotFoundWhenEmployeurNotFound() {
        // Arrange
        Long employeurId = 1L;

        when(employeurService.getEmployeurById(employeurId))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<EmployeurDTO> response = employeurService.getEmployeurById(employeurId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldFindEmployeurByEmail() {
        // Arrange
        String email = "email@gmail.com";
        when(employeurRepository.findByCredentials_email(email))
                .thenReturn(Optional.of(employeurEntity));

        // Act
        Optional<Employeur> response = employeurService.findByCredentials_Email(email);

        // Assert
        assertTrue(response.isPresent());
        assertEquals(employeurEntity.getEmail(), response.get().getEmail());
    }

    @Test
    void shouldNotFindEmployeurByEmail() {
        // Arrange
        String email = "nonexistent@gmail.com";
        when(employeurRepository.findByCredentials_email(email))
                .thenReturn(Optional.empty());

        // Act
        Optional<Employeur> response = employeurService.findByCredentials_Email(email);

        // Assert
        assertTrue(response.isEmpty());
    }

    @Test
    void shouldCreateEntrevue() {
        // Arrange
        String email = "etudiant@gmail.com";

        Employeur employeur = new Employeur();
        employeur.setId(1L);
        employeur.setFirstName("John");
        employeur.setLastName("Doe");
        employeur.setCredentials(new Credentials("", "", Role.EMPLOYEUR));
        employeur.setEntreprise("Entreprise");


        List<OffreDeStage> offres = new ArrayList<>();
        OffreDeStage offreDeStage = new OffreDeStage();
        offreDeStage.setId(1L);
        offreDeStage.setTitre("Stage en développement");
        offreDeStage.setLocalisation("Paris");
        offreDeStage.setDateLimite(LocalDate.now().plusDays(30));
        offreDeStage.setData("Description du stage");
        offreDeStage.setNbCandidats(10);
        offreDeStage.setEmployeur(employeur);

        offres.add(offreDeStage);

        EntrevueDTO entrevueDTO = new EntrevueDTO();
        entrevueDTO.setLocation("Lachine");
        entrevueDTO.setStatus("validé");


        Etudiant etudiant = new Etudiant("John", "Doe", email, "password", "123456789", Departement.TECHNIQUES_INFORMATIQUE);
        etudiant.setId(1L);
        etudiant.setOffresAppliquees(offres);

        when(userAppRepository.findUserAppByEmail(email))
                .thenReturn(Optional.of(etudiant));


        when(offreDeStageRepository.findById(1L))
                .thenReturn(Optional.of(offreDeStage));

        when(entrevueRepository.existsByEtudiantAndOffreDeStage(etudiant, offreDeStage))
                .thenReturn(false);

        Entrevue savedEntrevue = new Entrevue(LocalDateTime.now(), "Lachine", "validé", etudiant, offreDeStage);
        when(entrevueRepository.save(any(Entrevue.class)))
                .thenReturn(savedEntrevue);

        // Act
        Optional<EntrevueDTO> response = employeurService.createEntrevue(entrevueDTO, email, 1L);

        // Assert
        assertTrue(response.isPresent());
        assertEquals("Lachine", response.get().getLocation());
        assertEquals("validé", response.get().getStatus());
    }


    @Test
    void shouldReturnEmptyWhenCreatingEntrevueFails() {
        // Arrange
        String email = "etudiant@gmail.com";
        EntrevueDTO entrevueDTO = new EntrevueDTO();

        when(userAppRepository.findUserAppByEmail(email))
                .thenReturn(Optional.empty());

        // Act
        Optional<EntrevueDTO> response = employeurService.createEntrevue(entrevueDTO, email, 1L);

        // Assert
        assertTrue(response.isEmpty());
    }

    @Test
    void shouldGetEntrevueById() {
        // Arrange
        Long entrevueId = 1L;
        Etudiant etudiant = new Etudiant("Lol","Lala", "email","12334","", Departement.TECHNIQUES_INFORMATIQUE);
        Entrevue entrevue = new Entrevue(LocalDateTime.now(), "Lachine", "En attente", etudiant, new OffreDeStage());

        OffreDeStage offreDeStage = new OffreDeStage();
        offreDeStage.setTitre("Stage en développement");
        offreDeStage.setLocalisation("Paris");
        offreDeStage.setDateLimite(LocalDate.now().plusDays(30));
        offreDeStage.setData("Description du stage");
        offreDeStage.setNbCandidats(10);
        offreDeStage.setStatus("Ouvert");
        offreDeStage.setEmployeur(employeurEntity);

        entrevue.setEtudiant(etudiant);
        entrevue.setOffreDeStage(offreDeStage);



        entrevue.setId(entrevueId);
        when(entrevueRepository.findById(entrevueId))
                .thenReturn(Optional.of(entrevue));

        // Act
        Optional<EntrevueDTO> response = employeurService.getEntrevueById(entrevueId);

        // Assert
        assertTrue(response.isPresent());
    }

    @Test
    void shouldReturnAllEntrevues() {
        // Arrange
        Etudiant etudiant1 = new Etudiant("John", "Doe", "email1@gmail.com", "password1", "123456789", Departement.TECHNIQUES_INFORMATIQUE);
        Etudiant etudiant2 = new Etudiant("Jane", "Doe", "email2@gmail.com", "password2", "987654321", Departement.TECHNIQUES_INFORMATIQUE);

        OffreDeStage offreDeStage1 = new OffreDeStage();
        offreDeStage1.setTitre("Stage en développement");
        offreDeStage1.setLocalisation("Paris");
        offreDeStage1.setDateLimite(LocalDate.now().plusDays(30));
        offreDeStage1.setData("Description du stage");
        offreDeStage1.setNbCandidats(10);
        offreDeStage1.setStatus("Ouvert");
        offreDeStage1.setEmployeur(employeurEntity);

        OffreDeStage offreDeStage2 = new OffreDeStage();
        offreDeStage2.setTitre("Stage en marketing");
        offreDeStage2.setLocalisation("Lyon");
        offreDeStage2.setDateLimite(LocalDate.now().plusDays(30));
        offreDeStage2.setData("Description du stage");
        offreDeStage2.setNbCandidats(5);
        offreDeStage2.setStatus("Ouvert");
        offreDeStage2.setEmployeur(employeurEntity);

        Entrevue entrevue1 = new Entrevue(LocalDateTime.now(), "Lachine", "En attente", etudiant1, offreDeStage1);
        Entrevue entrevue2 = new Entrevue(LocalDateTime.now(), "Lachine", "En attente", etudiant2, offreDeStage2);

        when(entrevueRepository.findAll()).thenReturn(List.of(entrevue1, entrevue2));

        // Act
        List<EntrevueDTO> response = employeurService.getAllEntrevues();

        // Assert
        assertEquals(2, response.size());
        assertEquals("Lachine", response.get(0).getLocation());
        assertEquals("Lachine", response.get(1).getLocation());
    }

    @Test
    void shouldEncodePasswordWhenCreatingEmployeur() {
        // Arrange
        when(passwordEncoder.encode(newEmployeur.getCredentials().getPassword()))
                .thenReturn("encodedPassword");

        when(employeurRepository.save(any(Employeur.class)))
                .thenAnswer(invocation -> {
                    Employeur savedEmployeur = invocation.getArgument(0);
                    assertEquals("encodedPassword", savedEmployeur.getPassword());
                    return savedEmployeur;
                });

        // Act
        employeurService.creerEmployeur(newEmployeur);

        // Assert
        Mockito.verify(passwordEncoder).encode(newEmployeur.getCredentials().getPassword());
    }




    @Test
    void shouldReturnEmptyWhenOfferNotFound() {
        // Arrange
        String email = "email@gmail.com";
        EntrevueDTO entrevueDTO = new EntrevueDTO();

        // Simuler qu'aucune offre de stage n'est trouvée
        when(offreDeStageRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<EntrevueDTO> response = employeurService.createEntrevue(entrevueDTO, email, 1L);

        // Assert
        assertTrue(response.isEmpty()); // S'assurer que la réponse est vide
    }

    @Test
    void shouldSignContratEmployeurSuccessfully() {

        // Arrange
        String uuid = "unique-uuid";
        String password = "password";

        employeurEntity.getPassword();

        OffreDeStage offreDeStage = new OffreDeStage();
        offreDeStage.setEmployeur(employeurEntity);

        Entrevue entrevue = new Entrevue();
        entrevue.setOffreDeStage(offreDeStage);


        CandidatAccepter candidature = new CandidatAccepter();
        candidature.setEntrevue(entrevue);


        Contrat contrat = new Contrat();
        contrat.setUUID(uuid);
        contrat.setCandidature(candidature);
        contrat.setEmployeurSigne(false);

        // Act

        when(contratRepository.findByUUID(uuid)).thenReturn(Optional.of(contrat));
        when(passwordEncoder.matches(password, employeurEntity.getPassword())).thenReturn(true);
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);

        Optional<ContratDTO> response = employeurService.signerContratEmployeur(uuid, password);

        // Assert the results
        assertTrue(response.isPresent());
        assertTrue(contrat.isEmployeurSigne());
    }

    @Test
    void shouldFailToSignContratWithIncorrectPassword() {
        // Arrange
        String uuid = "unique-uuid";
        String incorrectPassword = "wrongPassword";
        OffreDeStage offreDeStage = new OffreDeStage();
        offreDeStage.setEmployeur(employeurEntity);

        Entrevue entrevue = new Entrevue();
        entrevue.setOffreDeStage(offreDeStage);

        CandidatAccepter candidature = new CandidatAccepter();
        candidature.setEntrevue(entrevue);

        Contrat contrat = new Contrat();
        contrat.setUUID(uuid);
        contrat.setCandidature(candidature);

        when(contratRepository.findByUUID(uuid)).thenReturn(Optional.of(contrat));
        when(passwordEncoder.matches(incorrectPassword, employeurEntity.getPassword())).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> employeurService.signerContratEmployeur(uuid, incorrectPassword));
    }

    @Test
    void shouldReturnEmptyWhenContratNotFoundForSignature() {
        // Arrange
        String uuid = "non-existent-uuid";
        when(contratRepository.findByUUID(uuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> employeurService.signerContratEmployeur(uuid, "password"));
    }

    @Test
    void shouldReturnContratsForEmployeur() {
        // Arrange
        String emailEmployeur = "email@gmail.com";
        String session = "HIVER25";
        Employeur employeur = new Employeur("John", "Doe", emailEmployeur, "password", "123456789", "Entreprise");

        OffreDeStage offreDeStage = new OffreDeStage();
        offreDeStage.setEmployeur(employeur);

        Entrevue entrevue = new Entrevue();
        entrevue.setOffreDeStage(offreDeStage);

        CandidatAccepter candidature = new CandidatAccepter();
        candidature.setEntrevue(entrevue);

        Contrat contrat = new Contrat();
        contrat.setCandidature(candidature);

        List<Contrat> contrats = List.of(contrat);

        when(employeurRepository.findByCredentials_email(emailEmployeur)).thenReturn(Optional.of(employeur));
        when(contratRepository.findByEmployeurAndSession(employeur, session)).thenReturn(contrats);

        // Act
        List<ContratDTO> response = employeurService.getContratEmployeur(emailEmployeur, session);

        // Assert
        assertEquals(1, response.size());
    }

    @Test
    void shouldReturnEntrevuesByOffreDeStage() {
        // Arrange
        OffreDeStage offreDeStage = new OffreDeStage();
        offreDeStage.setId(1L);
        offreDeStage.setTitre("Stage en développement");
        offreDeStage.setLocalisation("Paris");
        offreDeStage.setDateLimite(LocalDate.now().plusDays(30));
        offreDeStage.setData("Description du stage");
        offreDeStage.setNbCandidats(10);
        offreDeStage.setStatus("Ouvert");
        offreDeStage.setEmployeur(employeurEntity);

        Entrevue entrevue = new Entrevue(LocalDateTime.now(), "Lachine", "En attente", etudiantEntity, offreDeStage);
        entrevue.setId(1L);

        when(entrevueRepository.findByOffreDeStageId(1L)).thenReturn(List.of(entrevue));

        // Act
        List<EntrevueDTO> response = employeurService.getEntrevuesByOffre(1L);

        // Assert
        assertEquals(1, response.size());
    }

    @Test
    void creerEvaluationEtudiant_shouldThrowExceptionWhenEmployeurNotFound() {
        // Arrange
        String employeurEmail = "invalid@example.com";
        String etudiantEmail = "etudiant@example.com";
        EvaluationStageEmployeurDTO evaluationDTO = new EvaluationStageEmployeurDTO();

        when(employeurRepository.findByCredentials_email(employeurEmail)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () ->
                employeurService.creerEvaluationEtudiant(employeurEmail, etudiantEmail, evaluationDTO));

        assertEquals("java.lang.Exception: Employeur non trouvé", exception.getMessage());
    }

    @Test
    void creerEvaluationEtudiant_shouldThrowExceptionWhenEtudiantNotFound() {
        // Arrange
        String employeurEmail = "employeur@example.com";
        String etudiantEmail = "invalid@example.com";
        EvaluationStageEmployeurDTO evaluationDTO = new EvaluationStageEmployeurDTO();
        Employeur employeur = new Employeur();

        when(employeurRepository.findByCredentials_email(employeurEmail)).thenReturn(Optional.of(employeur));
        when(etudiantRepository.findByEmail(etudiantEmail)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () ->
                employeurService.creerEvaluationEtudiant(employeurEmail, etudiantEmail, evaluationDTO));

        assertEquals("java.lang.Exception: Etudiant non trouvé", exception.getMessage());
    }

    @Test
    void getEntrevuesAccepteesParEmployeur_ShouldReturnEmptyListIfEmployeurNotFound() {
        String emailEmployeur = "nonexistent@example.com";
        String session = "HIVER25";

        when(employeurRepository.findByCredentials_email(emailEmployeur)).thenReturn(Optional.empty());

        List<EntrevueDTO> result = employeurService.getEntrevuesAccepteesParEmployeur(emailEmployeur,session);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldCreatEvaluation() {
        // Arrange
        when(evaluationStageEmployeurRepository.save(any(EvaluationStageEmployeur.class)))
                .thenReturn(evaluationStageEmployeur);

        when(employeurRepository.findByCredentials_email(employeurEntity.getEmail()))
                .thenReturn(Optional.of(employeurEntity));

        when(etudiantRepository.findByEmail(etudiantEntity.getEmail()))
                .thenReturn(Optional.of(etudiantEntity));

        // Act
        Optional<EvaluationStageEmployeurDTO> response = employeurService.creerEvaluationEtudiant(employeurEntity.getEmail(),etudiantEntity.getEmail(), evaluationStageEmployeurDTO);

        // Assert
        assertTrue(response.isPresent());
    }
}


