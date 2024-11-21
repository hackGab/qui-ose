package com.lacouf.rsbjwt.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OffreDeStageTest {
    private static final String TITRE = "Software Engineer Intern";
    private static final String LOCALISATION = "New York";
    private static final LocalDate DATE_LIMITE = LocalDate.of(2023, 12, 3);
    private static final String DATA = "dataOffreDeStage";
    private static final LocalDate DATE_JOUR = LocalDate.now();
    private static final int NB_CANDIDATS = 3;

    private static final String STATUS = "status";
    private static final String SESSION = "HIVER25";


    private static final Employeur EMPLOYEUR = new Employeur(
            "John",
            "Doe",
            "john.doe@example.com",
            "password",
            "1234567890",
            "TechCorp"
    );

    private OffreDeStage offre;

    @BeforeEach
    public void setUp() {
        offre = new OffreDeStage(
                TITRE,
                LOCALISATION,
                DATE_LIMITE,
                DATA,
                NB_CANDIDATS,
                STATUS,
                SESSION
        );
        offre.setEmployeur(EMPLOYEUR);
    }


    @Test
    public void test_create_offre_de_stage_with_all_fields() {
        assertNotNull(offre);
        assertEquals(TITRE, offre.getTitre());
        assertEquals(LOCALISATION, offre.getLocalisation());
        assertEquals(DATE_LIMITE, offre.getDateLimite());
        assertEquals(EMPLOYEUR, offre.getEmployeur());
        assertEquals(DATA, offre.getData());
        assertEquals(NB_CANDIDATS, offre.getNbCandidats());
        assertEquals(DATE_JOUR, offre.getDatePublication());
    }

    @Test
    public void testNoArgsConstructor() {
        OffreDeStage offre = new OffreDeStage(); // Vérification du constructeur sans arguments
        assertNotNull(offre);
    }

    @Test
    public void test_ToString() {
        String expected = "OffreDeStage{" +
                "id=null" +
                ", titre='" + TITRE + '\'' +
                ", localisation='" + LOCALISATION + '\'' +
                ", dateLimite='" + DATE_LIMITE + '\'' +
                ", employeur='" + EMPLOYEUR + '\'' +
                ", datePublication='" + DATE_JOUR + '\'' +
                ", nbCandidats='" + NB_CANDIDATS + '\'' +
                ", status='" + STATUS + '\'' +
                ", rejetMessage=''" +
                "}";
        assertEquals(expected, offre.toString());
    }

    @Test
    public void testIdCoverage() {
        OffreDeStage offre = new OffreDeStage(); // Test du constructeur sans arguments
        offre.setId(1L); // Définir un ID
        assertEquals(1L, offre.getId()); // Vérification de l'ID
    }
}