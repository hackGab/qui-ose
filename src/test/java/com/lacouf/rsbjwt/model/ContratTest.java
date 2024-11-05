package com.lacouf.rsbjwt.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContratTest {
    @Test
    void testToString() {
        // Arrange
        CandidatAccepter candidatAccepter = new CandidatAccepter(1L, null, false);
        Contrat contrat = new Contrat();
        contrat.setId(1L);
        contrat.setEtudiantSigne(false);
        contrat.setEmployeurSigne(false);
        contrat.setGestionnaireSigne(false);
        contrat.setDateSignatureEtudiant(null);
        contrat.setDateSignatureEmployeur(null);
        contrat.setDateSignatureGestionnaire(null);
        contrat.setCollegeEngagement("collegeEngagement");
        contrat.setDateDebut(null);
        contrat.setDateFin(null);
        contrat.setDescription("description");
        contrat.setEntrepriseEngagement("entrepriseEngagement");
        contrat.setEtudiantEngagement("etudiantEngagement");
        contrat.setHeuresParSemaine(40);
        contrat.setHeureHorraireDebut(null);
        contrat.setHeureHorraireFin(null);
        contrat.setLieuStage("lieuStage");
        contrat.setNbSemaines(12);
        contrat.setTauxHoraire(15.0f);

        // Act
        String result = contrat.toString();

        // Assert
        String expected = "Contrat{" +
                "id=1" +
                ", etudiantSigne=false" +
                ", employeurSigne=false" +
                ", gestionnaireSigne=false" +
                ", dateSignatureEtudiant=null" +
                ", dateSignatureEmployeur=null" +
                ", dateSignatureGestionnaire=null" +
                ", collegeEngagement='collegeEngagement'" +
                ", dateDebut=null" +
                ", dateFin=null" +
                ", description='description'" +
                ", entrepriseEngagement='entrepriseEngagement'" +
                ", etudiantEngagement='etudiantEngagement'" +
                ", heuresParSemaine=40" +
                ", heureHorraireDebut=null" +
                ", heureHorraireFin=null" +
                ", lieuStage='lieuStage'" +
                ", nbSemaines=12" +
                ", tauxHoraire=15.0" +
                '}';
        assert expected.equals(result);
    }

    @Test
    void testGetId() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setId(1L);

        // Act
        Long id = contrat.getId();

        // Assert
        assert id.equals(1L);
    }

    @Test
    void testSetId() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setId(1L);

        // Act
        contrat.setId(2L);

        // Assert
        assert contrat.getId().equals(2L);
    }

    @Test
    void testGetEtudiantSigne() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setEtudiantSigne(true);

        // Act
        boolean etudiantSigne = contrat.isEtudiantSigne();

        // Assert
        assert etudiantSigne;
    }

    @Test
    void testSetEtudiantSigne() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setEtudiantSigne(true);

        // Act
        contrat.setEtudiantSigne(false);

        // Assert
        assert !contrat.isEtudiantSigne();
    }

    @Test
    void testGetEmployeurSigne() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setEmployeurSigne(true);

        // Act
        boolean employeurSigne = contrat.isEmployeurSigne();

        // Assert
        assert employeurSigne;
    }

    @Test
    void testSetEmployeurSigne() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setEmployeurSigne(true);

        // Act
        contrat.setEmployeurSigne(false);

        // Assert
        assert !contrat.isEmployeurSigne();
    }

    @Test
    void testGetGestionnaireSigne() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setGestionnaireSigne(true);

        // Act
        boolean gestionnaireSigne = contrat.isGestionnaireSigne();

        // Assert
        assert gestionnaireSigne;
    }

    @Test
    void testSetGestionnaireSigne() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setGestionnaireSigne(true);

        // Act
        contrat.setGestionnaireSigne(false);

        // Assert
        assert !contrat.isGestionnaireSigne();
    }

    @Test
    void testGetDateSignatureEtudiant() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setDateSignatureEtudiant(LocalDate.now());

        // Act
        LocalDate dateSignatureEtudiant = contrat.getDateSignatureEtudiant();

        // Assert
        assert dateSignatureEtudiant.equals(LocalDate.now());
    }

    @Test
    void testSetDateSignatureEtudiant() {
        // Arrange
        Contrat contrat = new Contrat();
        LocalDate date = LocalDate.now();
        contrat.setDateSignatureEtudiant(date);

        // Act
        contrat.setDateSignatureEtudiant(date.plusDays(1));

        // Assert
        assert contrat.getDateSignatureEtudiant().equals(date.plusDays(1));
    }

    @Test
    void testGetDateSignatureEmployeur() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setDateSignatureEmployeur(LocalDate.now());

        // Act
        LocalDate dateSignatureEmployeur = contrat.getDateSignatureEmployeur();

        // Assert
        assert dateSignatureEmployeur.equals(LocalDate.now());
    }

    @Test
    void testSetDateSignatureEmployeur() {
        // Arrange
        Contrat contrat = new Contrat();
        LocalDate date = LocalDate.now();
        contrat.setDateSignatureEmployeur(date);

        // Act
        contrat.setDateSignatureEmployeur(date.plusDays(1));

        // Assert
        assert contrat.getDateSignatureEmployeur().equals(date.plusDays(1));
    }

    @Test
    void testGetDateSignatureGestionnaire() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setDateSignatureGestionnaire(LocalDate.now());

        // Act
        LocalDate dateSignatureGestionnaire = contrat.getDateSignatureGestionnaire();

        // Assert
        assert dateSignatureGestionnaire.equals(LocalDate.now());
    }

    @Test
    void testSetDateSignatureGestionnaire() {
        // Arrange
        Contrat contrat = new Contrat();
        LocalDate date = LocalDate.now();
        contrat.setDateSignatureGestionnaire(date);

        // Act
        contrat.setDateSignatureGestionnaire(date.plusDays(1));

        // Assert
        assert contrat.getDateSignatureGestionnaire().equals(date.plusDays(1));
    }

    @Test
    void testGetCandidature() {
        // Arrange
        CandidatAccepter candidatAccepter = new CandidatAccepter(1L, null, false);
        Contrat contrat = new Contrat();
        contrat.setCandidature(candidatAccepter);

        // Act
        CandidatAccepter candidature = contrat.getCandidature();

        // Assert
        assert candidature.equals(candidatAccepter);
    }

    @Test
    void testSetCandidature() {
        // Arrange
        CandidatAccepter candidatAccepter = new CandidatAccepter(1L, null, false);
        Contrat contrat = new Contrat();
        contrat.setCandidature(candidatAccepter);

        // Act
        CandidatAccepter candidature = new CandidatAccepter(2L, null, false);
        contrat.setCandidature(candidature);

        // Assert
        assert contrat.getCandidature().equals(candidature);
    }

    @Test
    void testGetCollegeEngagement() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setCollegeEngagement("collegeEngagement");

        // Act
        String collegeEngagement = contrat.getCollegeEngagement();

        // Assert
        assert collegeEngagement.equals("collegeEngagement");
    }

    @Test
    void testSetCollegeEngagement() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setCollegeEngagement("collegeEngagement");

        // Act
        contrat.setCollegeEngagement("newCollegeEngagement");

        // Assert
        assert contrat.getCollegeEngagement().equals("newCollegeEngagement");
    }

    @Test
    void testGetDateDebut() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setDateDebut(LocalDate.now());

        // Act
        LocalDate dateDebut = contrat.getDateDebut();

        // Assert
        assert dateDebut.equals(LocalDate.now());
    }

    @Test
    void testSetDateDebut() {
        // Arrange
        Contrat contrat = new Contrat();
        LocalDate date = LocalDate.now();
        contrat.setDateDebut(date);

        // Act
        contrat.setDateDebut(date.plusDays(1));

        // Assert
        assert contrat.getDateDebut().equals(date.plusDays(1));
    }

    @Test
    void testGetDateFin() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setDateFin(LocalDate.now());

        // Act
        LocalDate dateFin = contrat.getDateFin();

        // Assert
        assert dateFin.equals(LocalDate.now());
    }

    @Test
    void testSetDateFin() {
        // Arrange
        Contrat contrat = new Contrat();
        LocalDate date = LocalDate.now();
        contrat.setDateFin(date);

        // Act
        contrat.setDateFin(date.plusDays(1));

        // Assert
        assert contrat.getDateFin().equals(date.plusDays(1));
    }

    @Test
    void testGetDescription() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setDescription("description");

        // Act
        String description = contrat.getDescription();

        // Assert
        assert description.equals("description");
    }

    @Test
    void testSetDescription() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setDescription("description");

        // Act
        contrat.setDescription("newDescription");

        // Assert
        assert contrat.getDescription().equals("newDescription");
    }

    @Test
    void testGetEntrepriseEngagement() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setEntrepriseEngagement("entrepriseEngagement");

        // Act
        String entrepriseEngagement = contrat.getEntrepriseEngagement();

        // Assert
        assert entrepriseEngagement.equals("entrepriseEngagement");
    }

    @Test
    void testSetEntrepriseEngagement() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setEntrepriseEngagement("entrepriseEngagement");

        // Act
        contrat.setEntrepriseEngagement("newEntrepriseEngagement");

        // Assert
        assert contrat.getEntrepriseEngagement().equals("newEntrepriseEngagement");
    }

    @Test
    void testGetEtudiantEngagement() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setEtudiantEngagement("etudiantEngagement");

        // Act
        String etudiantEngagement = contrat.getEtudiantEngagement();

        // Assert
        assert etudiantEngagement.equals("etudiantEngagement");
    }

    @Test
    void testSetEtudiantEngagement() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setEtudiantEngagement("etudiantEngagement");

        // Act
        contrat.setEtudiantEngagement("newEtudiantEngagement");

        // Assert
        assert contrat.getEtudiantEngagement().equals("newEtudiantEngagement");
    }

    @Test
    void testGetHeuresParSemaine() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setHeuresParSemaine(40);

        // Act
        int heuresParSemaine = contrat.getHeuresParSemaine();

        // Assert
        assert heuresParSemaine == 40;
    }

    @Test
    void testSetHeuresParSemaine() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setHeuresParSemaine(40);

        // Act
        contrat.setHeuresParSemaine(35);

        // Assert
        assert contrat.getHeuresParSemaine() == 35;
    }

    @Test
    void testGetHeureHorraireDebut() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setHeureHorraireDebut(null);

        // Act
        LocalTime heureHorraireDebut = contrat.getHeureHorraireDebut();

        // Assert
        assert heureHorraireDebut == null;
    }

    @Test
    void testSetHeureHorraireDebut() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setHeureHorraireDebut(null);

        // Act
        contrat.setHeureHorraireDebut(LocalTime.of(8, 0));

        // Assert
        assert contrat.getHeureHorraireDebut().equals(LocalTime.of(8, 0));
    }

    @Test
    void testGetHeureHorraireFin() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setHeureHorraireFin(null);

        // Act
        LocalTime heureHorraireFin = contrat.getHeureHorraireFin();

        // Assert
        assert heureHorraireFin == null;
    }

    @Test
    void testSetHeureHorraireFin() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setHeureHorraireFin(null);

        // Act
        contrat.setHeureHorraireFin(LocalTime.of(16, 0));

        // Assert
        assert contrat.getHeureHorraireFin().equals(LocalTime.of(16, 0));
    }

    @Test
    void testGetLieuStage() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setLieuStage("lieuStage");

        // Act
        String lieuStage = contrat.getLieuStage();

        // Assert
        assert lieuStage.equals("lieuStage");
    }

    @Test
    void testSetLieuStage() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setLieuStage("lieuStage");

        // Act
        contrat.setLieuStage("newLieuStage");

        // Assert
        assert contrat.getLieuStage().equals("newLieuStage");
    }

    @Test
    void testGetNbSemaines() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setNbSemaines(12);

        // Act
        int nbSemaines = contrat.getNbSemaines();

        // Assert
        assert nbSemaines == 12;
    }

    @Test
    void testSetNbSemaines() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setNbSemaines(12);

        // Act
        contrat.setNbSemaines(10);

        // Assert
        assert contrat.getNbSemaines() == 10;
    }

    @Test
    void testGetTauxHoraire() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setTauxHoraire(15.0f);

        // Act
        float tauxHoraire = contrat.getTauxHoraire();

        // Assert
        assert tauxHoraire == 15.0f;
    }

    @Test
    void testSetTauxHoraire() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setTauxHoraire(15.0f);

        // Act
        contrat.setTauxHoraire(20.0f);

        // Assert
        assert contrat.getTauxHoraire() == 20.0f;
    }

    @Test
    void testContrat() {
        // Arrange
        CandidatAccepter candidatAccepter = new CandidatAccepter(1L, null, false);
        Contrat contrat = new Contrat(false, false, false, null, null, null, candidatAccepter, "collegeEngagement", null, null, "description", "entrepriseEngagement", "etudiantEngagement", 40, null, null, "lieuStage", 12, 15.0f);

        // Act
        boolean etudiantSigne = contrat.isEtudiantSigne();
        boolean employeurSigne = contrat.isEmployeurSigne();
        boolean gestionnaireSigne = contrat.isGestionnaireSigne();
        LocalDate dateSignatureEtudiant = contrat.getDateSignatureEtudiant();
        LocalDate dateSignatureEmployeur = contrat.getDateSignatureEmployeur();
        LocalDate dateSignatureGestionnaire = contrat.getDateSignatureGestionnaire();
        CandidatAccepter candidature = contrat.getCandidature();
        String collegeEngagement = contrat.getCollegeEngagement();
        LocalDate dateDebut = contrat.getDateDebut();
        LocalDate dateFin = contrat.getDateFin();
        String description = contrat.getDescription();
        String entrepriseEngagement = contrat.getEntrepriseEngagement();
        String etudiantEngagement = contrat.getEtudiantEngagement();
        int heuresParSemaine = contrat.getHeuresParSemaine();
        LocalTime heureHorraireDebut = contrat.getHeureHorraireDebut();
        LocalTime heureHorraireFin = contrat.getHeureHorraireFin();
        String lieuStage = contrat.getLieuStage();
        int nbSemaines = contrat.getNbSemaines();
        float tauxHoraire = contrat.getTauxHoraire();

        // Assert
        assert !etudiantSigne;
        assert !employeurSigne;
        assert !gestionnaireSigne;
        assert dateSignatureEtudiant == null;
        assert dateSignatureEmployeur == null;
        assert dateSignatureGestionnaire == null;
        assert candidature.equals(candidatAccepter);
        assert collegeEngagement.equals("collegeEngagement");
        assert dateDebut == null;
        assert dateFin == null;
        assert description.equals("description");
        assert entrepriseEngagement.equals("entrepriseEngagement");
        assert etudiantEngagement.equals("etudiantEngagement");
        assert heuresParSemaine == 40;
        assert heureHorraireDebut == null;
        assert heureHorraireFin == null;
        assert lieuStage.equals("lieuStage");
        assert nbSemaines == 12;
        assert tauxHoraire == 15.0f;
    }


    @Test
    void signerContratEmployeur() {
        Contrat contrat = new Contrat();
        contrat.signerContratEmployeur();

        assert contrat.isEmployeurSigne();
        assert contrat.getDateSignatureEmployeur().equals(LocalDate.now());

    }

    @Test
    void genererUUID() {
        Contrat contrat = new Contrat();
        contrat.genererUUID();

        System.out.println(contrat.getUUID());

        assert contrat.getUUID() != null;
    }
}
