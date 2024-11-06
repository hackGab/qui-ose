package com.lacouf.rsbjwt.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EtudiantTest {

    @Test
    void testToString() {
        // Arrange
        Etudiant etudiant = new Etudiant("John", "Doe", "john@gmail.com", "password", "123456789", Departement.TECHNIQUES_INFORMATIQUE);

        // Act
        String result = etudiant.toString();

        // Assert
        String expected = "Etudiant{" +
                "id=null" +
                ", firstName='John'" +
                ", lastName='Doe'" +
                ", email='john@gmail.com'" +
                ", phoneNumber='123456789'" +
                ", role='ROLE_ETUDIANT'" +
                ", departement='TECHNIQUES_INFORMATIQUE'" +
                ", cv=null" +
                ", professeur=null" +
                ", offresAppliquees=null" +
                '}';

        assertEquals(expected, result);
    }
}