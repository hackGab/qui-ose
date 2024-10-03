package com.lacouf.rsbjwt.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OffreDeStageTest {

    private static final String TITRE = "Software Engineer Intern";
    private static final String DESCRIPTION = "Develop software";
    private static final String RESPONSABILITES = "Coding";
    private static final String QUALIFICATIONS = "Java";
    private static final String DUREE = "6 months";
    private static final String LOCALISATION = "New York";
    private static final String SALAIRE = "$3000";
    private static final LocalDate DATE_LIMITE = LocalDate.of(2023, 12, 31);
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
                DESCRIPTION,
                RESPONSABILITES,
                QUALIFICATIONS,
                DUREE,
                LOCALISATION,
                SALAIRE,
                DATE_LIMITE
        );
        offre.setEmployeur(EMPLOYEUR);
    }


    @Test
    public void test_create_offre_de_stage_with_all_fields() {
        assertNotNull(offre);
        assertEquals(TITRE, offre.getTitre());
        assertEquals(DESCRIPTION, offre.getDescription());
        assertEquals(RESPONSABILITES, offre.getResponsabilites());
        assertEquals(QUALIFICATIONS, offre.getQualifications());
        assertEquals(DUREE, offre.getDuree());
        assertEquals(LOCALISATION, offre.getLocalisation());
        assertEquals(SALAIRE, offre.getSalaire());
        assertEquals(DATE_LIMITE, offre.getDateLimite());
        assertEquals(EMPLOYEUR, offre.getEmployeur());
    }


    @Test
    public void testNoArgsConstructor() {
        OffreDeStage offre = new OffreDeStage();
        assertNotNull(offre);
    }

    @Test
    public void test_ToString() {
        String expected = "OffreDeStage{" +
                "id=null," +
                " titre='" + TITRE + "'," +
                " description='" + DESCRIPTION + "'," +
                " responsabilites='" + RESPONSABILITES + "'," +
                " qualifications='" + QUALIFICATIONS + "'," +
                " duree='" + DUREE + "'," +
                " localisation='" + LOCALISATION + "'," +
                " salaire='" + SALAIRE + "'," +
                " dateLimite=" + DATE_LIMITE + "," +
                " employeur='" + EMPLOYEUR +
                "}";
        assertEquals(expected, offre.toString());
    }

    @Test
    public void testIdCoverage() {
        OffreDeStage offre = new OffreDeStage();
        offre.setId(1L);
        assertEquals(1L, offre.getId());
    }
}
