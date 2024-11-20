package com.lacouf.rsbjwt.service.dto;

import com.lacouf.rsbjwt.model.Employeur;
import com.lacouf.rsbjwt.model.EvaluationStageEmployeur;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class EvaluationStageEmployeurDTO {
    // Associations
    private EmployeurDTO employeur;
    private EtudiantDTO etudiant;

    // Basic Information
    private String nomEleve;
    private String programmeEtude;
    private String nomEntreprise;
    private String nomSuperviseur;
    private String fonction;
    private String telephone;

    // Productivity Section
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
    private String habiletePersonnelles;

    // Overall Appreciation
    private String appreciationGlobale;
    private String commentairesAppreciation;

    // Additional Fields
    private boolean evaluationDiscuteeAvecStagiaire;
    private double heuresEncadrementParSemaine;
    private String entrepriseSouhaiteProchainStage;
    private String commentairesFormationTechnique;

    private String signatureEmployeur;
    private String dateSignature;

    public EvaluationStageEmployeurDTO(EmployeurDTO employeur,
                                       EtudiantDTO etudiant,
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
                                       String initiative,
                                       String travailSecuritaire,
                                       String sensResponsabilite,
                                       String ponctualiteAssiduite,
                                       String habiletePersonnelles,
                                       String appreciationGlobale,
                                       String commentairesAppreciation,
                                       boolean evaluationDiscuteeAvecStagiaire,
                                       double heuresEncadrementParSemaine,
                                       String entrepriseSouhaiteProchainStage,
                                       String commentairesFormationTechnique,
                                       String signatureEmployeur,
                                       String dateSignature) {
        this.employeur = employeur;
        this.etudiant = etudiant;
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
        this.travailSecuritaire = travailSecuritaire;
        this.sensResponsabilite = sensResponsabilite;
        this.ponctualiteAssiduite = ponctualiteAssiduite;
        this.habiletePersonnelles = habiletePersonnelles;
        this.appreciationGlobale = appreciationGlobale;
        this.commentairesAppreciation = commentairesAppreciation;
        this.evaluationDiscuteeAvecStagiaire = evaluationDiscuteeAvecStagiaire;
        this.heuresEncadrementParSemaine = heuresEncadrementParSemaine;
        this.entrepriseSouhaiteProchainStage = entrepriseSouhaiteProchainStage;
        this.commentairesFormationTechnique = commentairesFormationTechnique;
        this.signatureEmployeur = signatureEmployeur;
        this.dateSignature = dateSignature;
    }


    public EvaluationStageEmployeurDTO(EvaluationStageEmployeur savedEvaluationStageEmployeur) {
        this.employeur = new EmployeurDTO(savedEvaluationStageEmployeur.getEmployeur());
        this.etudiant = new EtudiantDTO(savedEvaluationStageEmployeur.getEtudiant());
        this.nomEleve = savedEvaluationStageEmployeur.getNomEleve();
        this.programmeEtude = savedEvaluationStageEmployeur.getProgrammeEtude();
        this.nomEntreprise = savedEvaluationStageEmployeur.getNomEntreprise();
        this.nomSuperviseur = savedEvaluationStageEmployeur.getNomSuperviseur();
        this.fonction = savedEvaluationStageEmployeur.getFonction();
        this.telephone = savedEvaluationStageEmployeur.getTelephone();
        this.planifOrganiserTravail = savedEvaluationStageEmployeur.getPlanifOrganiserTravail();
        this.comprendreDirectives = savedEvaluationStageEmployeur.getComprendreDirectives();
        this.maintenirRythmeTravail = savedEvaluationStageEmployeur.getMaintenirRythmeTravail();
        this.etablirPriorites = savedEvaluationStageEmployeur.getEtablirPriorites();
        this.respecterEcheanciers = savedEvaluationStageEmployeur.getRespecterEcheanciers();
        this.commentairesProductivite = savedEvaluationStageEmployeur.getCommentairesProductivite();
        this.respecterMandats = savedEvaluationStageEmployeur.getRespecterMandats();
        this.attentionAuxDetails = savedEvaluationStageEmployeur.getAttentionAuxDetails();
        this.verifierTravail = savedEvaluationStageEmployeur.getVerifierTravail();
        this.perfectionnement = savedEvaluationStageEmployeur.getPerfectionnement();
        this.analyseProblemes = savedEvaluationStageEmployeur.getAnalyseProblemes();
        this.commentairesQualiteTravail = savedEvaluationStageEmployeur.getCommentairesQualiteTravail();
        this.etablirContacts = savedEvaluationStageEmployeur.getEtablirContacts();
        this.contribuerTravailEquipe = savedEvaluationStageEmployeur.getContribuerTravailEquipe();
        this.adapterCultureEntreprise = savedEvaluationStageEmployeur.getAdapterCultureEntreprise();
        this.accepterCritiques = savedEvaluationStageEmployeur.getAccepterCritiques();
        this.respectueux = savedEvaluationStageEmployeur.getRespectueux();
        this.ecouteActive = savedEvaluationStageEmployeur.getEcouteActive();
        this.commentairesRelationsInterpersonnelles = savedEvaluationStageEmployeur.getCommentairesRelationsInterpersonnelles();
        this.interetMotivationTravail = savedEvaluationStageEmployeur.getInteretMotivationTravail();
        this.exprimerIdees = savedEvaluationStageEmployeur.getExprimerIdees();
        this.initiative = savedEvaluationStageEmployeur.getInitiative();
        this.travailSecuritaire = savedEvaluationStageEmployeur.getTravailSecuritaire();
        this.sensResponsabilite = savedEvaluationStageEmployeur.getSensResponsabilite();
        this.ponctualiteAssiduite = savedEvaluationStageEmployeur.getPonctualiteAssiduite();
        this.habiletePersonnelles = savedEvaluationStageEmployeur.getCommentairesPersonellesSkill();
        this.appreciationGlobale = savedEvaluationStageEmployeur.getAppreciationGlobale();
        this.commentairesAppreciation = savedEvaluationStageEmployeur.getCommentairesAppreciation();
        this.evaluationDiscuteeAvecStagiaire = savedEvaluationStageEmployeur.isEvaluationDiscuteeAvecStagiaire();
        this.heuresEncadrementParSemaine = savedEvaluationStageEmployeur.getHeuresEncadrementParSemaine();
        this.entrepriseSouhaiteProchainStage = savedEvaluationStageEmployeur.getEntrepriseSouhaiteProchainStage();
        this.commentairesFormationTechnique = savedEvaluationStageEmployeur.getCommentairesFormationTechnique();
        this.signatureEmployeur = savedEvaluationStageEmployeur.getSignatureEmployeur();
        this.dateSignature = savedEvaluationStageEmployeur.getDateSignature();
    }
}
