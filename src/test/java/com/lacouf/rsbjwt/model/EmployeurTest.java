package com.lacouf.rsbjwt.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeurTest {

    @Test
    void testToString() {
        // Arrange
        Employeur employeur = new Employeur("John", "Doe", "john.doe@example.com", "password", "123456789", "TechCorp");

        // Act
        String result = employeur.toString();

        // Assert
        String expected = "Employeur{" +
                "id=null" +
                ", firstName='John'" +
                ", lastName='Doe'" +
                ", email='john.doe@example.com'" +
                ", phoneNumber='123456789'" +
                ", role='ROLE_EMPLOYEUR'" +
                ", entreprise='TechCorp'" +
                '}';
        assertEquals(expected, result);
    }


    @Test
    void getEntreprise() {
        // Arrange
        Employeur employeur = new Employeur("John", "Doe", "john.doe@example.com", "password", "123456789", "TechCorp");

        // Act
        String entreprise = employeur.getEntreprise();

        // Assert
        assertEquals("TechCorp", entreprise);
    }


    @Test
    void setEntreprise() {
        // Arrange
        Employeur employeur = new Employeur("John", "Doe", "john.doe@example.com", "password", "123456789", "OldCorp");

        // Act
        employeur.setEntreprise("NewCorp");

        // Assert
        assertEquals("NewCorp", employeur.getEntreprise());
    }

}