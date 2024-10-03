package com.lacouf.rsbjwt.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OffreDeStageTest {
    private static final String TITRE = "Software Engineer Intern";
    private static final String DESCRIPTION = "Develop software";
    private static final String EXIGENCES = "Coding";
    private static final String QUALIFICATIONS = "Java";
    private static final String DUREE = "6 months";
    private static final String LOCALISATION = "New York";
    private static final String SALAIRE = "$3000";
    private static final LocalDate DATE_LIMITE = LocalDate.of(2023, 12, 31);

    private static final LocalDate DATE_DEBUT_SOUHAITEE = LocalDate.of(2023, 11, 01);

    private static final String DISPONIBILITE = "Full-Time";

    private static final String TYPE_REMUNERATION = "Monthly";

    private static final String CONTACT_INFO = "Contact Info";

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
                DUREE,
                LOCALISATION,
                EXIGENCES,
                DATE_DEBUT_SOUHAITEE,
                TYPE_REMUNERATION,
                SALAIRE,
                DISPONIBILITE,
                DATE_LIMITE,
                QUALIFICATIONS,
                CONTACT_INFO
        );
        offre.setEmployeur(EMPLOYEUR);
    }


    @Test
    public void test_create_offre_de_stage_with_all_fields() {
        assertNotNull(offre);
        assertEquals(TITRE, offre.getTitre());
        assertEquals(DESCRIPTION, offre.getDescription());
        assertEquals(EXIGENCES, offre.getExigences());
        assertEquals(QUALIFICATIONS, offre.getQualification());
        assertEquals(DUREE, offre.getDuree());
        assertEquals(LOCALISATION, offre.getLocalisation());
        assertEquals(SALAIRE, offre.getSalaire());
        assertEquals(DATE_LIMITE, offre.getDateLimite());
        assertEquals(EMPLOYEUR, offre.getEmployeur());
        assertEquals(DATE_DEBUT_SOUHAITEE, offre.getDateDebutSouhaitee());
        assertEquals(TYPE_REMUNERATION, offre.getTypeRemuneration());
        assertEquals(DISPONIBILITE, offre.getDisponibilite());
        assertEquals(CONTACT_INFO, offre.getContactInfo());
    }

    @Test
    public void testNoArgsConstructor() {
        OffreDeStage offre = new OffreDeStage(); // Vérification du constructeur sans arguments
        assertNotNull(offre);
    }

    @Test
    public void test_ToString() {

//        "OffreDeStage{" +
//                "id=" + id +
//                ", titre='" + titre + '\'' +
//                ", description='" + description + '\'' +
//                ", duree='" + duree + '\'' +
//                ", localisation='" + localisation + '\'' +
//                ", exigences='" + exigences + '\'' +
//                ", qualification='" + qualification + '\'' +
//                ", dateDebutSouhaitee='" + dateDebutSouhaitee + '\'' +
//                ", typeRemuneration='" + typeRemuneration + '\'' +
//                ", disponibilite='" + disponibilite + '\'' +
//                ", dateLimite='" + dateLimite + '\'' +
//                ", contactInfo='" + contactInfo + '\'' +
//                ", employeur='" + employeur + '\'' +
//                '}';


        String expected = "OffreDeStage{" +
                "id=null" +
                ", titre='" + TITRE + '\'' +
                ", description='" + DESCRIPTION + '\'' +
                ", duree='" + DUREE + '\'' +
                ", localisation='" + LOCALISATION + '\'' +
                ", exigences='" + EXIGENCES + '\'' +
                ", qualifications='" + QUALIFICATIONS + '\'' +
                ", dateDebutSouhaitee='" + DATE_DEBUT_SOUHAITEE + '\'' +
                ", typeRemuneration='" + TYPE_REMUNERATION + '\'' +
                ", salaire='" + SALAIRE + '\'' +
                ", disponibilite='" + DISPONIBILITE + '\'' +
                ", dateLimite='" + DATE_LIMITE + '\'' +
                ", contactInfo='" + CONTACT_INFO + '\'' +
                ", employeur='" + EMPLOYEUR + '\'' +
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