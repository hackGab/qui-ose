package com.lacouf.rsbjwt.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OffreDeStageTest {

    @Test
    public void test_create_offre_de_stage_with_all_fields() {
        // Créer un employeur pour associer à l'offre de stage
        Employeur employeur = new Employeur("John", "Doe", "john.doe@example.com", "password", "1234567890", "TechCorp");

        // Créer l'offre de stage avec tous les champs requis
        OffreDeStage offre = new OffreDeStage(
                "Software Engineer Intern",
                "Develop software",
                "6 months",
                "New York",
                "Experience with Java",
                LocalDate.of(2023, 11, 01), // dateDebutSouhaitee
                "Monthly", // typeRemuneration
                "3000$",
                "Full-Time", // disponibilite
                LocalDate.of(2023, 12, 31), // dateLimite
                "Qualification",
                "Contact Info"
        );
        offre.setEmployeur(employeur); // Associer l'employeur

        // Vérifications avec assertions
        assertNotNull(offre);
        assertEquals("Software Engineer Intern", offre.getTitre());
        assertEquals("Develop software", offre.getDescription());
        assertEquals("6 months", offre.getDuree());
        assertEquals("New York", offre.getLocalisation());
        assertEquals("Experience with Java", offre.getExigences()); // Vérification du champ exigences
        assertEquals(LocalDate.of(2023, 11, 01), offre.getDateDebutSouhaitee()); // Vérification du champ dateDebutSouhaitee
        assertEquals("Monthly", offre.getTypeRemuneration()); // Vérification du champ typeRemuneration
        assertEquals("Full-Time", offre.getDisponibilite()); // Vérification du champ disponibilite
        assertEquals(LocalDate.of(2023, 12, 31), offre.getDateLimite()); // Vérification du champ dateLimite
        assertEquals(employeur, offre.getEmployeur());
    }

    @Test
    public void testNoArgsConstructor() {
        OffreDeStage offre = new OffreDeStage(); // Vérification du constructeur sans arguments
        assertNotNull(offre);
    }

    @Test
    public void test_ToString() {
        // Créer un employeur pour associer à l'offre de stage
        Employeur employeur = new Employeur("John", "Doe", "john.doe@example.com", "password", "1234567890", "TechCorp");

        // Créer l'offre de stage avec tous les champs requis
        OffreDeStage offre = new OffreDeStage(
                "Software Engineer Intern",
                "Develop and maintain software",
                "6 months",
                "New York",
                "Experience with Java",
                LocalDate.of(2023, 11, 01), // dateDebutSouhaitee
                "Monthly", // typeRemuneration
                "3000$",
                "Full-Time", // disponibilite
                LocalDate.of(2023, 12, 31), // dateLimite
                "Qualification",
                "Contact Info"
        );
        offre.setEmployeur(employeur); // Associer l'employeur

        // Chaîne de caractères attendue pour toString
        String expected = "OffreDeStage{" +
                "id=null," +
                " titre='Software Engineer Intern'," +
                " description='Develop and maintain software'," +
                " duree='6 months'," +
                " localisation='New York'," +
                " exigences='Experience with Java'," +
                " dateDebutSouhaitee=2023-11-01," +
                " typeRemuneration='Monthly'," +
                " disponibilite='Full-Time'," +
                " dateLimite=2023-12-31," +
                " employeur=" + employeur +
                '}';

        // Vérification de l'égalité entre les chaînes
        assertEquals(expected, offre.toString());
    }

    @Test
    public void testIdCoverage() {
        OffreDeStage offre = new OffreDeStage(); // Test du constructeur sans arguments
        offre.setId(1L); // Définir un ID
        assertEquals(1L, offre.getId()); // Vérification de l'ID
    }
}
