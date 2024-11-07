package com.lacouf.rsbjwt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class EvaluationStageEmployeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Employeur employeur;

    @ManyToOne
    private Etudiant etudiant;

    // Basic Information
    private String nomEleve;
    private String programmeEtude;
    private String nomEntreprise;
    private String nomSuperviseur;
    private String fonction;
    private String telephone;

    // Productivity Section (Changement des types de EvaluationConformite à String)
    private String planifOrganiserTravail;
    private String comprendreDirectives;
    private String maintenirRythmeTravail;
    private String etablirPriorites;
    private String respecterEcheanciers;
    private String commentairesProductivite;

    // Quality of Work Section
    private String respecterMandats;
    private String attentionAuxDetails;
    private String verifierTravail;
    private String perfectionnement;
    private String analyseProblemes;
    private String commentairesQualiteTravail;

    // Interpersonal Relations Section
    private String etablirContacts;
    private String contribuerTravailEquipe;
    private String adapterCultureEntreprise;
    private String accepterCritiques;
    private String respectueux;
    private String ecouteActive;
    private String commentairesRelationsInterpersonnelles;

    // Personal Skills Section
    private String interetMotivationTravail;
    private String exprimerIdees;
    private String initiative;
    private String travailSecuritaire;
    private String sensResponsabilite;
    private String ponctualiteAssiduite;
    private String commentairesPersonellesSkill;

    private String appreciationGlobale;
    private String commentairesAppreciation;

    // Additional Fields
    private boolean evaluationDiscuteeAvecStagiaire;
    private double heuresEncadrementParSemaine;
    private String entrepriseSouhaiteProchainStage;
    private String commentairesFormationTechnique;

    private String signatureEmployeur;
    private String dateSignature;


    public EvaluationStageEmployeur(
                                    Employeur employeurEntity,
                                    Etudiant etudiantEntity,
                                    String nomEleve,
                                    String programmeEtude,
                                    String nomEntreprise,
                                    String nomSuperviseur,
                                    String fonction,
                                    String telephone,
                                    String planifOrganiserTravail,
                                    String comprendreDirectives,
                                    String maintenirRythmeTravail,
                                    String etablirPriorites,
                                    String respecterEcheanciers,
                                    String commentairesProductivite,
                                    String respecterMandats,
                                    String attentionAuxDetails,
                                    String verifierTravail,
                                    String perfectionnement,
                                    String analyseProblemes,
                                    String commentairesQualiteTravail,
                                    String etablirContacts,
                                    String contribuerTravailEquipe,
                                    String adapterCultureEntreprise,
                                    String accepterCritiques,
                                    String respectueux,
                                    String ecouteActive,
                                    String commentairesRelationsInterpersonnelles,
                                    String interetMotivationTravail,
                                    String exprimerIdees,
                                    String initiative)
    {
        this.etudiant = etudiantEntity;
        this.employeur = employeurEntity;
        this.nomEleve = nomEleve;
        this.programmeEtude = programmeEtude;
        this.nomEntreprise = nomEntreprise;
        this.nomSuperviseur = nomSuperviseur;
        this.fonction = fonction;
        this.telephone = telephone;
        this.planifOrganiserTravail = planifOrganiserTravail;
        this.comprendreDirectives = comprendreDirectives;
        this.maintenirRythmeTravail = maintenirRythmeTravail;
        this.etablirPriorites = etablirPriorites;
        this.respecterEcheanciers = respecterEcheanciers;
        this.commentairesProductivite = commentairesProductivite;
        this.respecterMandats = respecterMandats;
        this.attentionAuxDetails = attentionAuxDetails;
        this.verifierTravail = verifierTravail;
        this.perfectionnement = perfectionnement;
        this.analyseProblemes = analyseProblemes;
        this.commentairesQualiteTravail = commentairesQualiteTravail;
        this.etablirContacts = etablirContacts;
        this.contribuerTravailEquipe = contribuerTravailEquipe;
        this.adapterCultureEntreprise = adapterCultureEntreprise;
        this.accepterCritiques = accepterCritiques;
        this.respectueux = respectueux;
        this.ecouteActive = ecouteActive;
        this.commentairesRelationsInterpersonnelles = commentairesRelationsInterpersonnelles;
        this.interetMotivationTravail = interetMotivationTravail;
        this.exprimerIdees = exprimerIdees;
        this.initiative = initiative;
    }
}
