package com.lacouf.rsbjwt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contrat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private boolean etudiantSigne;

    @Column(nullable = false)
    private boolean employeurSigne;

    @Column(nullable = false)
    private boolean gestionnaireSigne;

    @Column(nullable = false)
    private LocalDate dateSignatureEtudiant;

    @Column(nullable = false)
    private LocalDate dateSignatureEmployeur;

    @Column(nullable = false)
    private LocalDate dateSignatureGestionnaire;

    @OneToOne
    private CandidatAccepter candidatAccepter;

    @Column(nullable = false)
    private String collegeEngagement;

    @Column(nullable = false)
    private LocalDate dateDebut;

    @Column(nullable = false)
    private LocalDate dateFin;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String entrepriseEngagement;

    @Column(nullable = false)
    private String etudiantEngagement;

    @Column(nullable = false)
    private int heuresParSemaine;

    @Column(nullable = false)
    private LocalTime heureHorraireDebut;

    @Column(nullable = false)
    private LocalTime heureHorraireFin;

    @Column(nullable = false)
    private String lieuStage;

    @Column(nullable = false)
    private int nbSemaines;

    @Column(nullable = false)
    private float tauxHoraire;

    public Contrat(boolean etudiantSigne, boolean employeurSigne, boolean gestionnaireSigne, LocalDate dateSignatureEtudiant,
                   LocalDate dateSignatureEmployeur, LocalDate dateSignatureGestionnaire, CandidatAccepter candidatAccepter,
                   String collegeEngagement, LocalDate dateDebut, LocalDate dateFin, String description, String entrepriseEngagement,
                   String etudiantEngagement, int heuresParSemaine, LocalTime heureHorraireDebut, LocalTime heureHorraireFin, String lieuStage,
                   int nbSemaines, float tauxHoraire) {
        this.etudiantSigne = etudiantSigne;
        this.employeurSigne = employeurSigne;
        this.gestionnaireSigne = gestionnaireSigne;
        this.dateSignatureEtudiant = dateSignatureEtudiant;
        this.dateSignatureEmployeur = dateSignatureEmployeur;
        this.dateSignatureGestionnaire = dateSignatureGestionnaire;
        this.candidatAccepter = candidatAccepter;
        this.collegeEngagement = collegeEngagement;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.description = description;
        this.entrepriseEngagement = entrepriseEngagement;
        this.etudiantEngagement = etudiantEngagement;
        this.heuresParSemaine = heuresParSemaine;
        this.heureHorraireDebut = heureHorraireDebut;
        this.heureHorraireFin = heureHorraireFin;
        this.lieuStage = lieuStage;
        this.nbSemaines = nbSemaines;
        this.tauxHoraire = tauxHoraire;
    }

    @Override
    public String toString() {
        return "Contrat{" +
                "id=" + id +
                ", etudiantSigne=" + etudiantSigne +
                ", employeurSigne=" + employeurSigne +
                ", gestionnaireSigne=" + gestionnaireSigne +
                ", dateSignatureEtudiant=" + dateSignatureEtudiant +
                ", dateSignatureEmployeur=" + dateSignatureEmployeur +
                ", dateSignatureGestionnaire=" + dateSignatureGestionnaire +
                ", etudiant=" + candidatAccepter.getEntrevue().getEtudiant().getEmail() +
                ", employeur=" + candidatAccepter.getEntrevue().getOffreDeStage().getEmployeur() +
                ", collegeEngagement='" + collegeEngagement + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", description='" + description + '\'' +
                ", entrepriseEngagement='" + entrepriseEngagement + '\'' +
                ", etudiantEngagement='" + etudiantEngagement + '\'' +
                ", heuresParSemaine=" + heuresParSemaine +
                ", heureHorraireDebut=" + heureHorraireDebut +
                ", heureHorraireFin=" + heureHorraireFin +
                ", lieuStage='" + lieuStage + '\'' +
                ", nbSemaines=" + nbSemaines +
                ", tauxHoraire=" + tauxHoraire +
                '}';
    }
}
