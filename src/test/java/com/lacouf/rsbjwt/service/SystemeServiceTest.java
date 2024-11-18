package com.lacouf.rsbjwt.service;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.lacouf.rsbjwt.model.*;
import com.lacouf.rsbjwt.model.auth.Credentials;
import com.lacouf.rsbjwt.model.auth.Role;
import com.lacouf.rsbjwt.repository.ContratRepository;
import com.lacouf.rsbjwt.repository.NotificationRepository;
import com.lacouf.rsbjwt.service.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
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
    void shouldGenerateEvaluationProfPDF() throws Exception {
        // Arrange
        EvaluationStageProfDTO evaluation = new EvaluationStageProfDTO();
        evaluation.setNomEntreprise("Entreprise Test");
        evaluation.setPersonneContact("Contact Test");
        evaluation.setAdresse("Adresse Test");
        evaluation.setTelephone("1234567890");
        evaluation.setNomStagiaire("Stagiaire Test");
        evaluation.setDateStage(LocalDate.now());
        evaluation.setTachesConformite(EvaluationStageProf.EvaluationConformite.TOTAL_EN_ACCORD);
        evaluation.setAccueilIntegration(EvaluationStageProf.EvaluationConformite.TOTAL_EN_ACCORD);
        evaluation.setEncadrementSuffisant(EvaluationStageProf.EvaluationConformite.TOTAL_EN_ACCORD);
        evaluation.setRespectNormesHygiene(EvaluationStageProf.EvaluationConformite.TOTAL_EN_ACCORD);
        evaluation.setClimatDeTravail(EvaluationStageProf.EvaluationConformite.TOTAL_EN_ACCORD);
        evaluation.setAccesTransportCommun(EvaluationStageProf.EvaluationConformite.TOTAL_EN_ACCORD);
        evaluation.setSalaireHoraire(15.0);
        evaluation.setSalaireInteressant(EvaluationStageProf.EvaluationConformite.TOTAL_EN_ACCORD);
        evaluation.setCommunicationSuperviseur(EvaluationStageProf.EvaluationConformite.TOTAL_EN_ACCORD);
        evaluation.setEquipementAdequat(EvaluationStageProf.EvaluationConformite.TOTAL_EN_ACCORD);
        evaluation.setVolumeTravailAcceptable(EvaluationStageProf.EvaluationConformite.TOTAL_EN_ACCORD);
        evaluation.setHeuresEncadrementPremierMois(40);
        evaluation.setHeuresEncadrementDeuxiemeMois(40);
        evaluation.setHeuresEncadrementTroisiemeMois(40);
        evaluation.setCommentaires("Commentaires Test");
        evaluation.setPrivilegiePremierStage(true);
        evaluation.setNombreStagiairesAccueillis(5);
        evaluation.setSouhaiteRevoirStagiaire(true);
        evaluation.setSignatureEnseignant("Enseignant Test");
        evaluation.setDateSignature("2023-10-10");

        // Act
        byte[] pdfBytes = systemeService.generateEvaluationProfPDF(evaluation);

        // Assert
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
    }

    @Test
    void shouldGenerateEvaluationEmployeurPDF() throws Exception {
        // Arrange
        EvaluationStageEmployeurDTO evaluation = new EvaluationStageEmployeurDTO();
        evaluation.setNomEleve("Élève Test");
        evaluation.setEtudiant(new EtudiantDTO(new Etudiant("Nom", "Prenom", "Courriel", "MotDePasse", "Matricule", Departement.TECHNIQUES_INFORMATIQUE)));
        evaluation.setNomEntreprise("Entreprise Test");
        evaluation.setNomSuperviseur("Superviseur Test");
        evaluation.setFonction("Fonction Test");
        evaluation.setTelephone("1234567890");
        evaluation.setPlanifOrganiserTravail("TOTAL_EN_ACCORD");
        evaluation.setComprendreDirectives("TOTAL_EN_ACCORD");
        evaluation.setMaintenirRythmeTravail("TOTAL_EN_ACCORD");
        evaluation.setEtablirPriorites("TOTAL_EN_ACCORD");
        evaluation.setRespecterEcheanciers("TOTAL_EN_ACCORD");
        evaluation.setCommentairesProductivite("Commentaires Productivité Test");
        evaluation.setRespecterMandats("TOTAL_EN_ACCORD");
        evaluation.setAttentionAuxDetails("TOTAL_EN_ACCORD");
        evaluation.setVerifierTravail("TOTAL_EN_ACCORD");
        evaluation.setPerfectionnement("TOTAL_EN_ACCORD");
        evaluation.setAnalyseProblemes("TOTAL_EN_ACCORD");
        evaluation.setCommentairesQualiteTravail("Commentaires Qualité Travail Test");
        evaluation.setEtablirContacts("TOTAL_EN_ACCORD");
        evaluation.setContribuerTravailEquipe("TOTAL_EN_ACCORD");
        evaluation.setAdapterCultureEntreprise("TOTAL_EN_ACCORD");
        evaluation.setAccepterCritiques("TOTAL_EN_ACCORD");
        evaluation.setRespectueux("TOTAL_EN_ACCORD");
        evaluation.setEcouteActive("TOTAL_EN_ACCORD");
        evaluation.setCommentairesRelationsInterpersonnelles("Commentaires Relations Interpersonnelles Test");
        evaluation.setInteretMotivationTravail("TOTAL_EN_ACCORD");
        evaluation.setExprimerIdees("TOTAL_EN_ACCORD");
        evaluation.setInitiative("TOTAL_EN_ACCORD");
        evaluation.setTravailSecuritaire("TOTAL_EN_ACCORD");
        evaluation.setSensResponsabilite("TOTAL_EN_ACCORD");
        evaluation.setPonctualiteAssiduite("TOTAL_EN_ACCORD");
        evaluation.setHabiletePersonnelles("Commentaires Habiletés Personnelles Test");
        evaluation.setAppreciationGlobale("Appreciation Globale Test");
        evaluation.setCommentairesAppreciation("Commentaires Appreciation Test");
        evaluation.setEvaluationDiscuteeAvecStagiaire(true);
        evaluation.setHeuresEncadrementParSemaine(40);
        evaluation.setEntrepriseSouhaiteProchainStage("Oui");
        evaluation.setCommentairesFormationTechnique("Commentaires Formation Technique Test");
        evaluation.setEmployeur(new EmployeurDTO("Nom", "Prenom", "Courriel", Role.EMPLOYEUR, new CredentialDTO("Courriel", "MotDePasse"), "Entreprise Test"));
        evaluation.setSignatureEmployeur("Signature Employeur Test");
        evaluation.setDateSignature("2023-10-10");

        // Act
        byte[] pdfBytes = systemeService.generateEvaluationEmployeurPDF(evaluation);

        // Assert
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
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
