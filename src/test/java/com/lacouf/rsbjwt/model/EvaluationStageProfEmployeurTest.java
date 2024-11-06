package com.lacouf.rsbjwt.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EvaluationStageProfEmployeurTest {
    EvaluationStageProf evaluationStageProf;

    @BeforeEach
    void setUp() {
        evaluationStageProf = new EvaluationStageProf();
    }

    // Tests pour les attributs de type EvaluationConformite
    @Test
    void getAndSetTachesConformite() {
        evaluationStageProf.setTachesConformite(EvaluationStageProf.EvaluationConformite.TOTAL_EN_ACCORD);
        assertEquals(EvaluationStageProf.EvaluationConformite.TOTAL_EN_ACCORD, evaluationStageProf.getTachesConformite());
    }

    @Test
    void getAndSetAccueilIntegration() {
        evaluationStageProf.setAccueilIntegration(EvaluationStageProf.EvaluationConformite.PLUTOT_EN_DESACCORD);
        assertEquals(EvaluationStageProf.EvaluationConformite.PLUTOT_EN_DESACCORD, evaluationStageProf.getAccueilIntegration());
    }

    @Test
    void getAndSetEncadrementSuffisant() {
        evaluationStageProf.setEncadrementSuffisant(EvaluationStageProf.EvaluationConformite.IMPOSSIBLE_SE_PRONONCER);
        assertEquals(EvaluationStageProf.EvaluationConformite.IMPOSSIBLE_SE_PRONONCER, evaluationStageProf.getEncadrementSuffisant());
    }

    // Tests pour les attributs de type int et double
    @Test
    void getAndSetHeuresEncadrementPremierMois() {
        evaluationStageProf.setHeuresEncadrementPremierMois(10);
        assertEquals(10, evaluationStageProf.getHeuresEncadrementPremierMois());
    }

    @Test
    void getAndSetSalaireHoraire() {
        evaluationStageProf.setSalaireHoraire(15.75);
        assertEquals(15.75, evaluationStageProf.getSalaireHoraire());
    }

    // Tests pour les attributs de type boolean
    @Test
    void isAndSetPrivilegiePremierStage() {
        evaluationStageProf.setPrivilegiePremierStage(true);
        assertTrue(evaluationStageProf.isPrivilegiePremierStage());
    }

    @Test
    void isAndSetSouhaiteRevoirStagiaire() {
        evaluationStageProf.setSouhaiteRevoirStagiaire(false);
        assertFalse(evaluationStageProf.isSouhaiteRevoirStagiaire());
    }

    // Tests pour les attributs de type String
    @Test
    void getAndSetHorairesQuartsDeTravail() {
        evaluationStageProf.setHorairesQuartsDeTravail("8h-16h");
        assertEquals("8h-16h", evaluationStageProf.getHorairesQuartsDeTravail());
    }

    @Test
    void getAndSetCommentaires() {
        evaluationStageProf.setCommentaires("Bon stage dans l'ensemble.");
        assertEquals("Bon stage dans l'ensemble.", evaluationStageProf.getCommentaires());
    }

    @Test
    void getAndSetSignatureEnseignant() {
        evaluationStageProf.setSignatureEnseignant("M. Dupont");
        assertEquals("M. Dupont", evaluationStageProf.getSignatureEnseignant());
    }

    @Test
    void getAndSetDateSignature() {
        evaluationStageProf.setDateSignature("2023-05-10");
        assertEquals("2023-05-10", evaluationStageProf.getDateSignature());
    }
}
