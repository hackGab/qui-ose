package com.lacouf.rsbjwt.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EvaluationStageTest {
    EvaluationStage evaluationStage;

    @BeforeEach
    void setUp() {
        evaluationStage = new EvaluationStage();
    }

    // Tests pour les attributs de type EvaluationConformite
    @Test
    void getAndSetTachesConformite() {
        evaluationStage.setTachesConformite(EvaluationStage.EvaluationConformite.TOTAL_EN_ACCORD);
        assertEquals(EvaluationStage.EvaluationConformite.TOTAL_EN_ACCORD, evaluationStage.getTachesConformite());
    }

    @Test
    void getAndSetAccueilIntegration() {
        evaluationStage.setAccueilIntegration(EvaluationStage.EvaluationConformite.PLUTOT_EN_DESACCORD);
        assertEquals(EvaluationStage.EvaluationConformite.PLUTOT_EN_DESACCORD, evaluationStage.getAccueilIntegration());
    }

    @Test
    void getAndSetEncadrementSuffisant() {
        evaluationStage.setEncadrementSuffisant(EvaluationStage.EvaluationConformite.IMPOSSIBLE_SE_PRONONCER);
        assertEquals(EvaluationStage.EvaluationConformite.IMPOSSIBLE_SE_PRONONCER, evaluationStage.getEncadrementSuffisant());
    }

    // Tests pour les attributs de type int et double
    @Test
    void getAndSetHeuresEncadrementPremierMois() {
        evaluationStage.setHeuresEncadrementPremierMois(10);
        assertEquals(10, evaluationStage.getHeuresEncadrementPremierMois());
    }

    @Test
    void getAndSetSalaireHoraire() {
        evaluationStage.setSalaireHoraire(15.75);
        assertEquals(15.75, evaluationStage.getSalaireHoraire());
    }

    // Tests pour les attributs de type boolean
    @Test
    void isAndSetPrivilegiePremierStage() {
        evaluationStage.setPrivilegiePremierStage(true);
        assertTrue(evaluationStage.isPrivilegiePremierStage());
    }

    @Test
    void isAndSetSouhaiteRevoirStagiaire() {
        evaluationStage.setSouhaiteRevoirStagiaire(false);
        assertFalse(evaluationStage.isSouhaiteRevoirStagiaire());
    }

    // Tests pour les attributs de type String
    @Test
    void getAndSetHorairesQuartsDeTravail() {
        evaluationStage.setHorairesQuartsDeTravail("8h-16h");
        assertEquals("8h-16h", evaluationStage.getHorairesQuartsDeTravail());
    }

    @Test
    void getAndSetCommentaires() {
        evaluationStage.setCommentaires("Bon stage dans l'ensemble.");
        assertEquals("Bon stage dans l'ensemble.", evaluationStage.getCommentaires());
    }

    @Test
    void getAndSetSignatureEnseignant() {
        evaluationStage.setSignatureEnseignant("M. Dupont");
        assertEquals("M. Dupont", evaluationStage.getSignatureEnseignant());
    }

    @Test
    void getAndSetDateSignature() {
        evaluationStage.setDateSignature("2023-05-10");
        assertEquals("2023-05-10", evaluationStage.getDateSignature());
    }
}
