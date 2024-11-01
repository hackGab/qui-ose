package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.*;
import com.lacouf.rsbjwt.model.auth.Credentials;
import com.lacouf.rsbjwt.model.auth.Role;
import com.lacouf.rsbjwt.repository.*;
import com.lacouf.rsbjwt.service.dto.*;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;


import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private OffreDeStageRepository offreDeStageRepository;
    private ContratRepository contratRepository;
    private EntrevueRepository entrevueRepository;

    private ProfesseurRepository professeurRepository;
    private Gestionnaire gestionnaire;

    private CV cvEntity;
    @BeforeEach
    void setUp() {
        gestionnaireRepository = Mockito.mock(GestionnaireRepository.class);
        cvRepository = Mockito.mock(CVRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        offreDeStageRepository = Mockito.mock(OffreDeStageRepository.class);
        contratRepository = Mockito.mock(ContratRepository.class);
        entrevueRepository = Mockito.mock(EntrevueRepository.class);
        professeurRepository = Mockito.mock(ProfesseurRepository.class);
        etudiantRepository = Mockito.mock(EtudiantRepository.class);
        gestionnaireService = new GestionnaireService(gestionnaireRepository,  cvRepository, etudiantRepository,  offreDeStageRepository, passwordEncoder, contratRepository, entrevueRepository, professeurRepository);
        gestionnaireEntity = new Gestionnaire("Thiraiyan", "Moon", "titi@gmail.com", "password", "123-456-7890");
        gestionnaireDTO = new GestionnaireDTO(gestionnaireEntity);

	cvEntity = new CV();
        cvEntity.setId(1L);
        cvEntity.setStatus("attend");

         gestionnaire = new Gestionnaire();

        gestionnaire.setId(1L);
        gestionnaire.setCredentials(new Credentials("email", "password", Role.GESTIONNAIRE));
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


    @Test
    void validerOuRejeterOffre() {

        OffreDeStage offreDeStage = new OffreDeStage();
        offreDeStage.setId(1L);
        offreDeStage.setStatus("attend");

        OffreDeStage offreDeStage1 = new OffreDeStage();
        offreDeStage1.setId(1L);
        offreDeStage1.setStatus("accepté");


        Employeur employeur = new Employeur();
        employeur.setId(1L);
        employeur.setCredentials(new Credentials("email", "password", Role.EMPLOYEUR));
        offreDeStage.setEmployeur(employeur);

        when(offreDeStageRepository.findById(1L)).thenReturn(Optional.of(offreDeStage));
        when(offreDeStageRepository.save(any(OffreDeStage.class))).thenReturn(offreDeStage1);

        Optional<OffreDeStageDTO> result = gestionnaireService.validerOuRejeterOffre(1L, "accepté", "");

        assertTrue(result.isPresent());
        assertEquals("accepté", result.get().getStatus());
    }

    @Test
    void validerOuRejeterOffreWithRejection() {

        OffreDeStage offreDeStage = new OffreDeStage();
        offreDeStage.setId(1L);
        offreDeStage.setStatus("attend");

        OffreDeStage offreDeStage1 = new OffreDeStage();
        offreDeStage1.setId(1L);
        offreDeStage1.setStatus("rejeté");

        Employeur employeur = new Employeur();
        employeur.setId(1L);
        employeur.setCredentials(new Credentials("email", "password", Role.EMPLOYEUR));

        offreDeStage.setEmployeur(employeur);

        when(offreDeStageRepository.findById(1L)).thenReturn(Optional.of(offreDeStage));
        when(offreDeStageRepository.save(any(OffreDeStage.class))).thenReturn(offreDeStage1);

        Optional<OffreDeStageDTO> result = gestionnaireService.validerOuRejeterOffre(1L, "rejeté", "raison");

        assertTrue(result.isPresent());
        assertEquals("rejeté", result.get().getStatus());
    }

    @Test
    void creerContrat() {
        // Arrange
        ContratDTO contratDTO = new ContratDTO();
        contratDTO.setEtudiantSigne(true);
        contratDTO.setEmployeurSigne(true);
        contratDTO.setGestionnaireSigne(true);
        contratDTO.setDateSignatureEtudiant(LocalDate.now());
        contratDTO.setDateSignatureEmployeur(LocalDate.now());
        contratDTO.setDateSignatureGestionnaire(LocalDate.now());
        contratDTO.setCollegeEngagement("college");
        contratDTO.setDateDebut(LocalDate.now());
        contratDTO.setCandidature(new CandidatAccepterDTO(1L, 1L, true));

        Entrevue entrevue = new Entrevue();
        entrevue.setId(1L);

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
        contrat.setCandidature(new CandidatAccepter(entrevue, true));

        when(entrevueRepository.findById(1L)).thenReturn(Optional.of(entrevue));
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);

        // Act
        Optional<ContratDTO> result = gestionnaireService.creerContrat(contratDTO);

        // Assert
        assertTrue(result.isPresent());
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

        when(contratRepository.findByUUID(uuid)).thenReturn(Optional.of(contrat));
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat1);
        when(passwordEncoder.matches(password, "password")).thenReturn(true);
        when(gestionnaireRepository.findByEmail(email)).thenReturn(gestionnaire);

        Optional<ContratDTO> result = gestionnaireService.signerContratGestionnaire(uuid, password, email);

        assertTrue(result.isPresent());
        assertTrue(result.get().isGestionnaireSigne());
    }

    @Test
    void testAssignerProfesseur_ContratNonSigne() {
        // Arrange
        Long etudiantId = 1L;
        Long professeurId = 2L;

        Etudiant etudiant = new Etudiant();
        etudiant.setId(etudiantId);
        etudiant.setCredentials(new Credentials("etudiant@example.com", "password", Role.ETUDIANT));

        Professeur professeur = new Professeur();
        professeur.setId(professeurId);
        professeur.setCredentials(new Credentials("professeur@example.com", "password", Role.PROFESSEUR));

        Contrat contrat = new Contrat();
        contrat.setCandidature(new CandidatAccepter(new Entrevue(), true));
        contrat.setGestionnaireSigne(false); // Contract not signed by the Gestionnaire

        when(etudiantRepository.findById(etudiantId)).thenReturn(Optional.of(etudiant));
        when(professeurRepository.findById(professeurId)).thenReturn(Optional.of(professeur));
        when(contratRepository.findByCandidature_EtudiantAndGestionnaireSigneTrue(etudiant)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            gestionnaireService.assignerProfesseur(etudiantId, professeurId);
        });
    }

     @Test
    void testAssignerProfesseur() {
        // Arrange
        Long etudiantId = 1L;
        Long professeurId = 2L;

        Etudiant etudiant = new Etudiant();
        etudiant.setId(etudiantId);
        etudiant.setCredentials(new Credentials("etudiant@example.com", "password", Role.ETUDIANT));

        Professeur professeur = new Professeur();
        professeur.setId(professeurId);
        professeur.setCredentials(new Credentials("professeur@example.com", "password", Role.PROFESSEUR));

        Contrat contrat = new Contrat();
        contrat.setCandidature(new CandidatAccepter(new Entrevue(), true));
        contrat.setGestionnaireSigne(true);

        when(etudiantRepository.findById(etudiantId)).thenReturn(Optional.of(etudiant));
        when(professeurRepository.findById(professeurId)).thenReturn(Optional.of(professeur));
        when(contratRepository.findByCandidature_EtudiantAndGestionnaireSigneTrue(etudiant)).thenReturn(Optional.of(contrat));
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        // Act
        Optional<EtudiantDTO> result = gestionnaireService.assignerProfesseur(etudiantId, professeurId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(professeurId, result.get().getProfesseur().getId());
    }


    @Test
    void testAssignerProfesseur_EtudiantOuProfesseurNonExistant() {
        // Arrange
        Long etudiantId = 1L;
        Long professeurId = 2L;


        when(etudiantRepository.findById(etudiantId)).thenReturn(Optional.empty());
        when(professeurRepository.findById(professeurId)).thenReturn(Optional.of(new Professeur()));

        // Act
        Optional<EtudiantDTO> resultEtudiantNonExistant = gestionnaireService.assignerProfesseur(etudiantId, professeurId);

        // Assert
        assertFalse(resultEtudiantNonExistant.isPresent());


        when(etudiantRepository.findById(etudiantId)).thenReturn(Optional.of(new Etudiant()));
        when(professeurRepository.findById(professeurId)).thenReturn(Optional.empty());

        // Act
        Optional<EtudiantDTO> resultProfesseurNonExistant = gestionnaireService.assignerProfesseur(etudiantId, professeurId);

        // Assert
        assertFalse(resultProfesseurNonExistant.isPresent());
    }
}
