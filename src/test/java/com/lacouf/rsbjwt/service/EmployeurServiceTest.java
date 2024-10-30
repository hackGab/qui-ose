package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.Employeur;
import com.lacouf.rsbjwt.model.Entrevue;
import com.lacouf.rsbjwt.model.Etudiant;
import com.lacouf.rsbjwt.model.OffreDeStage;
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
import static org.mockito.Mockito.when;

public class EmployeurServiceTest {
    private EmployeurRepository employeurRepository;
    private EntrevueRepository entrevueRepository;
    private UserAppRepository userAppRepository;
    private OffreDeStageRepository offreDeStageRepository;
    private EtudiantRepository etudiantRepository;

    private ContratRepository contratRepository;
    private EmployeurService employeurService;

    private EmployeurDTO newEmployeur;
    private Employeur employeurEntity;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        employeurRepository = Mockito.mock(EmployeurRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        entrevueRepository = Mockito.mock(EntrevueRepository.class);
        userAppRepository = Mockito.mock(UserAppRepository.class);
        offreDeStageRepository = Mockito.mock(OffreDeStageRepository.class); // Mocking offreDeStageRepository
        etudiantRepository = Mockito.mock(EtudiantRepository.class);
        employeurService = new EmployeurService(employeurRepository, passwordEncoder, entrevueRepository, userAppRepository, offreDeStageRepository, etudiantRepository, contratRepository);

        CredentialDTO credentials = new CredentialDTO("email@gmail.com", "password");
        newEmployeur = new EmployeurDTO("John", "Doe", "123456789", Role.EMPLOYEUR, credentials, "Entreprise");

        employeurEntity = new Employeur("John", "Doe", "email@gmail.com", "password", "123456789", "Entreprise");
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


        Etudiant etudiant = new Etudiant("John", "Doe", email, "password", "123456789", "Département");
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
        Etudiant etudiant = new Etudiant("Lol","Lala", "email","12334","", "");
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
        Etudiant etudiant1 = new Etudiant("John", "Doe", "email1@gmail.com", "password1", "123456789", "departement1");
        Etudiant etudiant2 = new Etudiant("Jane", "Doe", "email2@gmail.com", "password2", "987654321", "departement2");

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


}
