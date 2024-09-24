package com.lacouf.rsbjwt.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfesseurTest {

    @Test
    void testToString() {
        // Arrange
        Professeur professeur = new Professeur("John", "Doe", "john@gmail.com", "password", "123456789", "departement");

        // Act
        String result = professeur.toString();

        // Assert
        String expected = "Professeur{" +
                "id=null" +
                ", firstName='John'" +
                ", lastName='Doe'" +
                ", email='john@gmail.com'" +
                ", phoneNumber='123456789'" +
                ", role='ROLE_PROFESSEUR'" +
                '}';
        assertEquals(expected, result);
    }
}