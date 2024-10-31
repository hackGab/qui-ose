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

    @Column
    private String UUID;

    @Column(nullable = false)
    private boolean etudiantSigne;

    @Column(nullable = false)
    private boolean employeurSigne;

    @Column(nullable = false)
    private boolean gestionnaireSigne;

    @Column
    private LocalDate dateSignatureEtudiant;

    @Column
    private LocalDate dateSignatureEmployeur;

    @Column
    private LocalDate dateSignatureGestionnaire;

    @OneToOne
    @JoinColumn(nullable = false)
    private CandidatAccepter candidature;

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
                   LocalDate dateSignatureEmployeur, LocalDate dateSignatureGestionnaire, CandidatAccepter candidature,
                   String collegeEngagement, LocalDate dateDebut, LocalDate dateFin, String description, String entrepriseEngagement,
                   String etudiantEngagement, int heuresParSemaine, LocalTime heureHorraireDebut, LocalTime heureHorraireFin, String lieuStage,
                   int nbSemaines, float tauxHoraire) {
        this.etudiantSigne = etudiantSigne;
        this.employeurSigne = employeurSigne;
        this.gestionnaireSigne = gestionnaireSigne;
        this.dateSignatureEtudiant = dateSignatureEtudiant;
        this.dateSignatureEmployeur = dateSignatureEmployeur;
        this.dateSignatureGestionnaire = dateSignatureGestionnaire;
        this.candidature = candidature;
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

    public void signerContratEmployeur() {
        this.employeurSigne = true;
        this.dateSignatureEmployeur = LocalDate.now();
    }

    public void signerContratEtudiant() {
        this.etudiantSigne = true;
        this.dateSignatureEtudiant = LocalDate.now();
    }

    public void signerContratGestionnaire() {
        this.gestionnaireSigne = true;
        this.dateSignatureGestionnaire = LocalDate.now();
    }

    public void genererUUID() {
        this.UUID = java.util.UUID.randomUUID().toString();
    }
}
