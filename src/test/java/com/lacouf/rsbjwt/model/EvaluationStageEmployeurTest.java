package com.lacouf.rsbjwt.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EvaluationStageEmployeurTest {

    private EvaluationStageEmployeur evaluationStageEmployeur;

    @BeforeEach
    void setUp() {
        evaluationStageEmployeur = new EvaluationStageEmployeur(
                new Employeur(),
                new Etudiant(),
                "John Doe",
                "Computer Science",
                "TechCorp",
                "Jane Smith",
                "Developer",
                "1234567890",
                "Excellent",
                "Understands well",
                "Maintains good pace",
                "Prioritize well",
                "Meets deadlines",
                "Good performance",
                "Completes tasks",
                "Attention to detail",
                "Double-checked work",
                "Continual improvement",
                "Solves problems",
                "Quality work",
                "Communicates well",
                "Team player",
                "Adapts well",
                "Accepts feedback",
                "Respectful",
                "Active listener",
                "Works well with others",
                "Motivated",
                "Expresses ideas",
                "Takes initiative",
                "Follows safety protocols",
                "Responsible",
                "Punctual",
                "Diligent",
                "Excellent",
                "Great progress",
                true,
                10,
                "Desire next internship at TechCorp",
                "Well-trained staff",
                "Employer Signature",
                "2024-12-01"
        );
    }

    @Test
    void testGetters() {
        assertNull(evaluationStageEmployeur.getId());
        assertNotNull(evaluationStageEmployeur.getEmployeur());
        assertNotNull(evaluationStageEmployeur.getEtudiant());
        assertEquals("John Doe", evaluationStageEmployeur.getNomEleve());
        assertEquals("Computer Science", evaluationStageEmployeur.getProgrammeEtude());
        assertEquals("TechCorp", evaluationStageEmployeur.getNomEntreprise());
        assertEquals("Jane Smith", evaluationStageEmployeur.getNomSuperviseur());
        assertEquals("Developer", evaluationStageEmployeur.getFonction());
        assertEquals("1234567890", evaluationStageEmployeur.getTelephone());
        assertEquals("Excellent", evaluationStageEmployeur.getPlanifOrganiserTravail());
        assertEquals("Understands well", evaluationStageEmployeur.getComprendreDirectives());
        assertEquals("Maintains good pace", evaluationStageEmployeur.getMaintenirRythmeTravail());
        assertEquals("Prioritize well", evaluationStageEmployeur.getEtablirPriorites());
        assertEquals("Meets deadlines", evaluationStageEmployeur.getRespecterEcheanciers());
        assertEquals("Good performance", evaluationStageEmployeur.getCommentairesProductivite());
        assertEquals("Completes tasks", evaluationStageEmployeur.getRespecterMandats());
        assertEquals("Attention to detail", evaluationStageEmployeur.getAttentionAuxDetails());
        assertEquals("Double-checked work", evaluationStageEmployeur.getVerifierTravail());
        assertEquals("Continual improvement", evaluationStageEmployeur.getPerfectionnement());
        assertEquals("Solves problems", evaluationStageEmployeur.getAnalyseProblemes());
        assertEquals("Quality work", evaluationStageEmployeur.getCommentairesQualiteTravail());
        assertEquals("Communicates well", evaluationStageEmployeur.getEtablirContacts());
        assertEquals("Team player", evaluationStageEmployeur.getContribuerTravailEquipe());
        assertEquals("Adapts well", evaluationStageEmployeur.getAdapterCultureEntreprise());
        assertEquals("Accepts feedback", evaluationStageEmployeur.getAccepterCritiques());
        assertEquals("Respectful", evaluationStageEmployeur.getRespectueux());
        assertEquals("Active listener", evaluationStageEmployeur.getEcouteActive());
        assertEquals("Works well with others", evaluationStageEmployeur.getCommentairesRelationsInterpersonnelles());
        assertEquals("Motivated", evaluationStageEmployeur.getInteretMotivationTravail());
        assertEquals("Expresses ideas", evaluationStageEmployeur.getExprimerIdees());
        assertEquals("Takes initiative", evaluationStageEmployeur.getInitiative());
        assertEquals("Follows safety protocols", evaluationStageEmployeur.getTravailSecuritaire());
        assertEquals("Responsible", evaluationStageEmployeur.getSensResponsabilite());
        assertEquals("Punctual", evaluationStageEmployeur.getPonctualiteAssiduite());
        assertEquals("Diligent", evaluationStageEmployeur.getCommentairesPersonellesSkill());
        assertEquals("Excellent", evaluationStageEmployeur.getAppreciationGlobale());
        assertEquals("Great progress", evaluationStageEmployeur.getCommentairesAppreciation());
        assertTrue(evaluationStageEmployeur.isEvaluationDiscuteeAvecStagiaire());
        assertEquals(10, evaluationStageEmployeur.getHeuresEncadrementParSemaine());
        assertEquals("Desire next internship at TechCorp", evaluationStageEmployeur.getEntrepriseSouhaiteProchainStage());
        assertEquals("Well-trained staff", evaluationStageEmployeur.getCommentairesFormationTechnique());
        assertEquals("Employer Signature", evaluationStageEmployeur.getSignatureEmployeur());
        assertEquals("2024-12-01", evaluationStageEmployeur.getDateSignature());
    }

    @Test
    void testSetters() {
        evaluationStageEmployeur.setNomEleve("Jane Doe");
        assertEquals("Jane Doe", evaluationStageEmployeur.getNomEleve());

        evaluationStageEmployeur.setProgrammeEtude("Information Technology");
        assertEquals("Information Technology", evaluationStageEmployeur.getProgrammeEtude());

        evaluationStageEmployeur.setNomEntreprise("InnoTech");
        assertEquals("InnoTech", evaluationStageEmployeur.getNomEntreprise());

        evaluationStageEmployeur.setNomSuperviseur("John Smith");
        assertEquals("John Smith", evaluationStageEmployeur.getNomSuperviseur());

        evaluationStageEmployeur.setFonction("Manager");
        assertEquals("Manager", evaluationStageEmployeur.getFonction());

        evaluationStageEmployeur.setTelephone("0987654321");
        assertEquals("0987654321", evaluationStageEmployeur.getTelephone());

        evaluationStageEmployeur.setPlanifOrganiserTravail("Very Good");
        assertEquals("Very Good", evaluationStageEmployeur.getPlanifOrganiserTravail());

        evaluationStageEmployeur.setComprendreDirectives("Understands perfectly");
        assertEquals("Understands perfectly", evaluationStageEmployeur.getComprendreDirectives());

        evaluationStageEmployeur.setMaintenirRythmeTravail("Excellent pace");
        assertEquals("Excellent pace", evaluationStageEmployeur.getMaintenirRythmeTravail());

        evaluationStageEmployeur.setEtablirPriorites("Prioritizes excellently");
        assertEquals("Prioritizes excellently", evaluationStageEmployeur.getEtablirPriorites());

        evaluationStageEmployeur.setRespecterEcheanciers("Always meets deadlines");
        assertEquals("Always meets deadlines", evaluationStageEmployeur.getRespecterEcheanciers());

        evaluationStageEmployeur.setCommentairesProductivite("Outstanding performance");
        assertEquals("Outstanding performance", evaluationStageEmployeur.getCommentairesProductivite());

        evaluationStageEmployeur.setRespecterMandats("Always completes tasks");
        assertEquals("Always completes tasks", evaluationStageEmployeur.getRespecterMandats());

        evaluationStageEmployeur.setAttentionAuxDetails("Highly attentive to details");
        assertEquals("Highly attentive to details", evaluationStageEmployeur.getAttentionAuxDetails());

        evaluationStageEmployeur.setVerifierTravail("Always double-checks work");
        assertEquals("Always double-checks work", evaluationStageEmployeur.getVerifierTravail());

        evaluationStageEmployeur.setPerfectionnement("Constantly improving");
        assertEquals("Constantly improving", evaluationStageEmployeur.getPerfectionnement());

        evaluationStageEmployeur.setAnalyseProblemes("Excellent problem solver");
        assertEquals("Excellent problem solver", evaluationStageEmployeur.getAnalyseProblemes());

        evaluationStageEmployeur.setCommentairesQualiteTravail("High quality work");
        assertEquals("High quality work", evaluationStageEmployeur.getCommentairesQualiteTravail());

        evaluationStageEmployeur.setEtablirContacts("Great communicator");
        assertEquals("Great communicator", evaluationStageEmployeur.getEtablirContacts());

        evaluationStageEmployeur.setContribuerTravailEquipe("Excellent team player");
        assertEquals("Excellent team player", evaluationStageEmployeur.getContribuerTravailEquipe());

        evaluationStageEmployeur.setAdapterCultureEntreprise("Adapts very well");
        assertEquals("Adapts very well", evaluationStageEmployeur.getAdapterCultureEntreprise());

        evaluationStageEmployeur.setAccepterCritiques("Accepts feedback well");
        assertEquals("Accepts feedback well", evaluationStageEmployeur.getAccepterCritiques());

        evaluationStageEmployeur.setRespectueux("Very respectful");
        assertEquals("Very respectful", evaluationStageEmployeur.getRespectueux());

        evaluationStageEmployeur.setEcouteActive("Excellent listener");
        assertEquals("Excellent listener", evaluationStageEmployeur.getEcouteActive());

        evaluationStageEmployeur.setCommentairesRelationsInterpersonnelles("Works well with everyone");
        assertEquals("Works well with everyone", evaluationStageEmployeur.getCommentairesRelationsInterpersonnelles());

        evaluationStageEmployeur.setInteretMotivationTravail("Highly motivated");
        assertEquals("Highly motivated", evaluationStageEmployeur.getInteretMotivationTravail());

        evaluationStageEmployeur.setExprimerIdees("Expresses ideas clearly");
        assertEquals("Expresses ideas clearly", evaluationStageEmployeur.getExprimerIdees());

        evaluationStageEmployeur.setInitiative("Takes initiative often");
        assertEquals("Takes initiative often", evaluationStageEmployeur.getInitiative());

        evaluationStageEmployeur.setTravailSecuritaire("Follows all safety protocols");
        assertEquals("Follows all safety protocols", evaluationStageEmployeur.getTravailSecuritaire());

        evaluationStageEmployeur.setSensResponsabilite("Highly responsible");
        assertEquals("Highly responsible", evaluationStageEmployeur.getSensResponsabilite());

        evaluationStageEmployeur.setPonctualiteAssiduite("Always punctual");
        assertEquals("Always punctual", evaluationStageEmployeur.getPonctualiteAssiduite());

        evaluationStageEmployeur.setCommentairesPersonellesSkill("Very diligent");
        assertEquals("Very diligent", evaluationStageEmployeur.getCommentairesPersonellesSkill());

        evaluationStageEmployeur.setAppreciationGlobale("Outstanding");
        assertEquals("Outstanding", evaluationStageEmployeur.getAppreciationGlobale());

        evaluationStageEmployeur.setCommentairesAppreciation("Excellent progress");
        assertEquals("Excellent progress", evaluationStageEmployeur.getCommentairesAppreciation());

        evaluationStageEmployeur.setEvaluationDiscuteeAvecStagiaire(false);
        assertFalse(evaluationStageEmployeur.isEvaluationDiscuteeAvecStagiaire());

        evaluationStageEmployeur.setHeuresEncadrementParSemaine(5);
        assertEquals(5, evaluationStageEmployeur.getHeuresEncadrementParSemaine());

        evaluationStageEmployeur.setEntrepriseSouhaiteProchainStage("No desire for next internship");
        assertEquals("No desire for next internship", evaluationStageEmployeur.getEntrepriseSouhaiteProchainStage());

        evaluationStageEmployeur.setCommentairesFormationTechnique("Needs improvement");
        assertEquals("Needs improvement", evaluationStageEmployeur.getCommentairesFormationTechnique());

        evaluationStageEmployeur.setSignatureEmployeur("New Employer Signature");
        assertEquals("New Employer Signature", evaluationStageEmployeur.getSignatureEmployeur());

        evaluationStageEmployeur.setDateSignature("2025-01-01");
        assertEquals("2025-01-01", evaluationStageEmployeur.getDateSignature());
    }
}