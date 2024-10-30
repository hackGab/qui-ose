package com.lacouf.rsbjwt.service.dto;

import com.lacouf.rsbjwt.model.Contrat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@Getter
@Setter
public class ContratDTO {
    private boolean etudiantSigne;
    private boolean employeurSigne;
    private boolean gestionnaireSigne;
    private String UUID;
    private LocalDate dateSignatureEtudiant;
    private LocalDate dateSignatureEmployeur;
    private LocalDate dateSignatureGestionnaire;
    private CandidatAccepterDTO candidature;
    private String collegeEngagement;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String description;
    private String entrepriseEngagement;
    private String etudiantEngagement;
    private int heuresParSemaine;
    private LocalTime heureHorraireDebut;
    private LocalTime heureHorraireFin;
    private String lieuStage;
    private int nbSemaines;
    private float tauxHoraire;

    public ContratDTO(Contrat contrat) {
        this.etudiantSigne = contrat.isEtudiantSigne();
        this.employeurSigne = contrat.isEmployeurSigne();
        this.UUID = contrat.getUUID();
        this.gestionnaireSigne = contrat.isGestionnaireSigne();
        this.dateSignatureEtudiant = contrat.getDateSignatureEtudiant();
        this.dateSignatureEmployeur = contrat.getDateSignatureEmployeur();
        this.dateSignatureGestionnaire = contrat.getDateSignatureGestionnaire();
        this.candidature = new CandidatAccepterDTO(contrat.getCandidature());
        this.collegeEngagement = contrat.getCollegeEngagement();
        this.dateDebut = contrat.getDateDebut();
        this.dateFin = contrat.getDateFin();
        this.description = contrat.getDescription();
        this.entrepriseEngagement = contrat.getEntrepriseEngagement();
        this.etudiantEngagement = contrat.getEtudiantEngagement();
        this.heuresParSemaine = contrat.getHeuresParSemaine();
        this.heureHorraireDebut = contrat.getHeureHorraireDebut();
        this.heureHorraireFin = contrat.getHeureHorraireFin();
        this.lieuStage = contrat.getLieuStage();
        this.nbSemaines = contrat.getNbSemaines();
        this.tauxHoraire = contrat.getTauxHoraire();
    }

    public ContratDTO() {}

    public static ContratDTO empty() {return new ContratDTO();}
}
