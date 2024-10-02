package com.lacouf.rsbjwt.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OffreDeStageTest {

    /*@Test
    void testToString() {
        // Arrange
        OffreDeStage offreDeStage = new OffreDeStage("Java Developer", "TechCorp", "Paris", "Stage de 6 mois", "2021-06-01", "2021-12-01", "Java, Spring, Hibernate", "Bac+2", "Développement d'une application web");

        // Act
        String result = offreDeStage.toString();

        // Assert
        String expected = "OffreDeStage{" +
                "id=null" +
                ", titre='Java Developer'" +
                ", entreprise='TechCorp'" +
                ", localisation='Paris'" +
                ", description='Stage de 6 mois'" +
                ", dateDebut='2021-06-01'" +
                ", dateFin='2021-12-01'" +
                ", competences='Java, Spring, Hibernate'" +
                ", niveauEtude='Bac+2'" +
                ", mission='Développement d'une application web'" +
                '}';
        assertEquals(expected, result);
    }*/

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
}
