package com.lacouf.rsbjwt.model;

import com.lacouf.rsbjwt.model.auth.Credentials;
import com.lacouf.rsbjwt.model.auth.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfesseurTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        Professeur professeur = new Professeur("John", "Doe", "john@gmail.com", "password", "123456789", Departement.TECHNIQUES_INFORMATIQUE);

        // Assert
        assertEquals("John", professeur.getFirstName());
        assertEquals("Doe", professeur.getLastName());
        assertEquals("john@gmail.com", professeur.getEmail());
        assertEquals("password", professeur.getPassword());
        assertEquals("123456789", professeur.getPhoneNumber());
        assertEquals(Role.PROFESSEUR, professeur.getRole());
        assertEquals(Departement.TECHNIQUES_INFORMATIQUE, professeur.getDepartement());
    }

    @Test
    void testNoArgsConstructor() {
        // Act
        Professeur professeur = new Professeur();

        // Assert
        assertNotNull(professeur);
    }


    @Test
    void testToString() {
        // Arrange
        Professeur professeur = new Professeur("John", "Doe", "john@gmail.com", "password", "123456789", Departement.TECHNIQUES_INFORMATIQUE);

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
                ", departement='TECHNIQUES_INFORMATIQUE'" +
                ", etudiants='null'" +
                '}';
        assertEquals(expected, result);
    }


    @Test
    void testSetters() {
        // Arrange
        Professeur professeur = new Professeur();

        // Act
        professeur.setFirstName("John");
        professeur.setLastName("Doe");
        professeur.setCredentials(new Credentials("john@gmail.com", "password", Role.PROFESSEUR));
        professeur.setPhoneNumber("123456789");
        professeur.setDepartement(Departement.TECHNIQUES_INFORMATIQUE);

        // Assert
        assertEquals("John", professeur.getFirstName());
        assertEquals("Doe", professeur.getLastName());
        assertEquals("john@gmail.com", professeur.getEmail());
        assertEquals("password", professeur.getPassword());
        assertEquals("123456789", professeur.getPhoneNumber());
        assertEquals(Departement.TECHNIQUES_INFORMATIQUE, professeur.getDepartement());
        assertEquals(Role.PROFESSEUR, professeur.getRole());
    }
}