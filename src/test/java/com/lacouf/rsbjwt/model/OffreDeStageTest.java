package com.lacouf.rsbjwt.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OffreDeStageTest {


    @Test
    public void test_create_offre_de_stage_with_all_fields() {
        Employeur employeur = new Employeur("John", "Doe", "john.doe@example.com", "password", "1234567890", "TechCorp");
        OffreDeStage offre = new OffreDeStage("Software Engineer Intern", "Develop software", "Coding", "Java", "6 months", "New York", "$3000", LocalDate.of(2023, 12, 31));
        offre.setEmployeur(employeur);

        assertNotNull(offre);
        assertEquals("Software Engineer Intern", offre.getTitre());
        assertEquals("Develop software", offre.getDescription());
        assertEquals("Coding", offre.getResponsabilites());
        assertEquals("Java", offre.getQualifications());
        assertEquals("6 months", offre.getDuree());
        assertEquals("New York", offre.getLocalisation());
        assertEquals("$3000", offre.getSalaire());
        assertEquals(LocalDate.of(2023, 12, 31), offre.getDateLimite());
        assertEquals(employeur, offre.getEmployeur());
    }

    @Test
    public void testNoArgsConstructor() {
        OffreDeStage offre = new OffreDeStage();
        assertNotNull(offre);
    }


    @Test
    public void test_ToString() {
        OffreDeStage offre = new OffreDeStage(
                "Software Engineer Intern",
                "Develop and maintain software",
                "Coding, Testing",
                "Java, Spring",
                "6 months",
                "New York",
                "$3000/month",
                LocalDate.of(2023, 12, 31)
        );

        Employeur employeur = new Employeur("John", "Doe", "john.doe@example.com", "password", "1234567890", "TechCorp");
        offre.setEmployeur(employeur);

        String expected = "OffreDeStage{" +
                "id=null," +
                " titre='Software Engineer Intern'," +
                " description='Develop and maintain software'," +
                " responsabilites='Coding, Testing'," +
                " qualifications='Java, Spring'," +
                " duree='6 months'," +
                " localisation='New York'," +
                " salaire='$3000/month'," +
                " dateLimite=2023-12-31'," +
                " employeur='" + employeur +
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
