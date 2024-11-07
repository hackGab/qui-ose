package com.lacouf.rsbjwt.service.dto;

import com.lacouf.rsbjwt.model.EvaluationStageProf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationStageProfDTO {
    private Long professeurId;
    private Long etudiantId;

    // Identification de l'entreprise
    private String nomEntreprise;
    private String personneContact;
    private String adresse;
    private String telephone;


    // Identification du stagiaire
    private String nomStagiaire;
    private LocalDate dateStage;


    // Évaluation des tâches
    private EvaluationStageProf.EvaluationConformite tachesConformite;
    private EvaluationStageProf.EvaluationConformite accueilIntegration;
    private EvaluationStageProf.EvaluationConformite encadrementSuffisant;
    private int heuresEncadrementPremierMois;
    private int heuresEncadrementDeuxiemeMois;
    private int heuresEncadrementTroisiemeMois;

    private EvaluationStageProf.EvaluationConformite respectNormesHygiene;
    private EvaluationStageProf.EvaluationConformite climatDeTravail;
    private EvaluationStageProf.EvaluationConformite accesTransportCommun;
    private EvaluationStageProf.EvaluationConformite salaireInteressant;
    private double salaireHoraire;
    private EvaluationStageProf.EvaluationConformite communicationSuperviseur;
    private EvaluationStageProf.EvaluationConformite equipementAdequat;
    private EvaluationStageProf.EvaluationConformite volumeTravailAcceptable;

    // Observations générales
    private boolean privilegiePremierStage;
    private boolean privilegieDeuxiemeStage;
    private int nombreStagiairesAccueillis;
    private boolean souhaiteRevoirStagiaire;
    private boolean offreQuartsVariables;
    private String horairesQuartsDeTravail;

    // Commentaires et date
    private String commentaires;
    private String signatureEnseignant;
    private String dateSignature;

    public EvaluationStageProfDTO(EvaluationStageProf evaluationStageProf) {
        this.professeurId = evaluationStageProf.getProfesseur().getId();
        this.etudiantId = evaluationStageProf.getEtudiant().getId();

        this.nomEntreprise = evaluationStageProf.getNomEntreprise();
        this.personneContact = evaluationStageProf.getPersonneContact();
        this.adresse = evaluationStageProf.getAdresse();
        this.telephone = evaluationStageProf.getTelephone();
        this.nomStagiaire = evaluationStageProf.getNomStagiaire();
        this.dateStage = evaluationStageProf.getDateStage();


        this.tachesConformite = evaluationStageProf.getTachesConformite();
        this.accueilIntegration = evaluationStageProf.getAccueilIntegration();
        this.encadrementSuffisant = evaluationStageProf.getEncadrementSuffisant();
        this.heuresEncadrementPremierMois = evaluationStageProf.getHeuresEncadrementPremierMois();
        this.heuresEncadrementDeuxiemeMois = evaluationStageProf.getHeuresEncadrementDeuxiemeMois();
        this.heuresEncadrementTroisiemeMois = evaluationStageProf.getHeuresEncadrementTroisiemeMois();

        this.respectNormesHygiene = evaluationStageProf.getRespectNormesHygiene();
        this.climatDeTravail = evaluationStageProf.getClimatDeTravail();
        this.accesTransportCommun = evaluationStageProf.getAccesTransportCommun();
        this.salaireInteressant = evaluationStageProf.getSalaireInteressant();
        this.salaireHoraire = evaluationStageProf.getSalaireHoraire();
        this.communicationSuperviseur = evaluationStageProf.getCommunicationSuperviseur();
        this.equipementAdequat = evaluationStageProf.getEquipementAdequat();
        this.volumeTravailAcceptable = evaluationStageProf.getVolumeTravailAcceptable();

        this.privilegiePremierStage = evaluationStageProf.isPrivilegiePremierStage();
        this.privilegieDeuxiemeStage = evaluationStageProf.isPrivilegieDeuxiemeStage();
        this.nombreStagiairesAccueillis = evaluationStageProf.getNombreStagiairesAccueillis();

        this.souhaiteRevoirStagiaire = evaluationStageProf.isSouhaiteRevoirStagiaire();
        this.offreQuartsVariables = evaluationStageProf.isOffreQuartsVariables();
        this.horairesQuartsDeTravail = evaluationStageProf.getHorairesQuartsDeTravail();

        this.commentaires = evaluationStageProf.getCommentaires();
        this.signatureEnseignant = evaluationStageProf.getSignatureEnseignant();
        this.dateSignature = evaluationStageProf.getDateSignature();
    }

    public enum EvaluationConformite {
        TOTAL_EN_ACCORD,
        PLUTOT_EN_ACCORD,
        PLUTOT_EN_DESACCORD,
        TOTAL_EN_DESACCORD,
        IMPOSSIBLE_SE_PRONONCER
    }
}
