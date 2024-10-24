package com.lacouf.rsbjwt.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CandidatAccepterTest {

    @Test
    void testToString() {
        // Arrange
        Entrevue entrevue = new Entrevue();
        entrevue.setId(1L);
        CandidatAccepter candidatAccepter = new CandidatAccepter(entrevue, true);

        // Act
        String result = candidatAccepter.toString();

        // Assert
        String expected = "CandidatAccepter{" +
                "id=null" +
                ", entrevue=" + entrevue.getId() +
                ", accepte=" + candidatAccepter.isAccepte() +
                '}';
        assertEquals(expected, result);
    }

    @Test
    void testConstructorWithParameters() {
        // Arrange
        Entrevue entrevue = new Entrevue();
        entrevue.setId(1L);
        boolean accepte = true;

        // Act
        CandidatAccepter candidatAccepter = new CandidatAccepter(entrevue, accepte);

        // Assert
        assertEquals(entrevue, candidatAccepter.getEntrevue());
        assertTrue(candidatAccepter.isAccepte());
    }

    @Test
    void testGetAndSetId() {
        // Arrange
        CandidatAccepter candidatAccepter = new CandidatAccepter();

        // Act
        candidatAccepter.setId(1L);

        // Assert
        assertEquals(1L, candidatAccepter.getId());
    }

    @Test
    void testGetAndSetEntrevue() {
        // Arrange
        CandidatAccepter candidatAccepter = new CandidatAccepter();
        Entrevue entrevue = new Entrevue();
        entrevue.setId(1L);

        // Act
        candidatAccepter.setEntrevue(entrevue);

        // Assert
        assertEquals(entrevue, candidatAccepter.getEntrevue());
    }

    @Test
    void testGetAndSetAccepte() {
        // Arrange
        CandidatAccepter candidatAccepter = new CandidatAccepter();

        // Act
        candidatAccepter.setAccepte(true);

        // Assert
        assertTrue(candidatAccepter.isAccepte());
    }

    @Test
    void testNoArgsConstructor() {
        // Act
        CandidatAccepter candidatAccepter = new CandidatAccepter();

        // Assert
        assertNotNull(candidatAccepter);
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange
        Entrevue entrevue = new Entrevue();
        entrevue.setId(1L);
        boolean accepte = true;

        // Act
        CandidatAccepter candidatAccepter = new CandidatAccepter(1L, entrevue, accepte);

        // Assert
        assertEquals(1L, candidatAccepter.getId());
        assertEquals(entrevue, candidatAccepter.getEntrevue());
        assertTrue(candidatAccepter.isAccepte());
    }
}
