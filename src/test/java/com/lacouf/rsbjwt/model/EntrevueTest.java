package com.lacouf.rsbjwt.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class EntrevueTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        LocalDateTime dateHeure = LocalDateTime.now();
        Etudiant etudiant = new Etudiant();
        OffreDeStage offreDeStage = new OffreDeStage();
        Entrevue entrevue = new Entrevue(dateHeure, "Room 101", "Scheduled", etudiant, offreDeStage);

        // Assert
        assertEquals(dateHeure, entrevue.getDateHeure());
        assertEquals("Room 101", entrevue.getLocation());
        assertEquals("Scheduled", entrevue.getStatus());
        assertEquals(etudiant, entrevue.getEtudiant());
        assertEquals(offreDeStage, entrevue.getOffreDeStage());
    }

    @Test
    void testNoArgsConstructor() {
        // Act
        Entrevue entrevue = new Entrevue();

        // Assert
        assertNotNull(entrevue);
    }

    @Test
    void testSetters() {
        // Arrange
        Entrevue entrevue = new Entrevue();
        LocalDateTime dateHeure = LocalDateTime.now();
        Etudiant etudiant = new Etudiant();
        OffreDeStage offreDeStage = new OffreDeStage();

        // Act
        entrevue.setDateHeure(dateHeure);
        entrevue.setLocation("Room 101");
        entrevue.setStatus("Scheduled");
        entrevue.setEtudiant(etudiant);
        entrevue.setOffreDeStage(offreDeStage);

        // Assert
        assertEquals(dateHeure, entrevue.getDateHeure());
        assertEquals("Room 101", entrevue.getLocation());
        assertEquals("Scheduled", entrevue.getStatus());
        assertEquals(etudiant, entrevue.getEtudiant());
        assertEquals(offreDeStage, entrevue.getOffreDeStage());
    }

    @Test
    void testToString() {
        // Arrange
        LocalDateTime dateHeure = LocalDateTime.now();
        Etudiant etudiant = new Etudiant("joe", "doe", "joe@gmail.com", "123", "123456789", Departement.TECHNIQUES_INFORMATIQUE);
        OffreDeStage offreDeStage = new OffreDeStage();
        offreDeStage.setTitre("Software Internship");
        Entrevue entrevue = new Entrevue(dateHeure, "Room 101", "Scheduled", etudiant, offreDeStage);

        // Act
        String result = entrevue.toString();

        // Assert
        String expected = "Entrevue{" +
                "id=null" +
                ", dateHeure=" + dateHeure +
                ", location='Room 101'" +
                ", status='Scheduled'" +
                ", etudiant=joe@gmail.com" +
                ", offreDeStage=Software Internship" +
                '}';
        assertEquals(expected, result);
    }
}