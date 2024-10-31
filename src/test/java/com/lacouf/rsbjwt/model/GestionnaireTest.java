package com.lacouf.rsbjwt.model;

import com.lacouf.rsbjwt.model.auth.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GestionnaireTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        Gestionnaire gestionnaire = new Gestionnaire("John", "Doe", "gest@gmail.com", "password", "123456789");

        // Assert
        assertEquals("John", gestionnaire.getFirstName());
        assertEquals("Doe", gestionnaire.getLastName());
        assertEquals("gest@gmail.com", gestionnaire.getEmail());
        assertEquals("password", gestionnaire.getPassword());
        assertEquals("123456789", gestionnaire.getPhoneNumber());
        assertEquals(Role.GESTIONNAIRE, gestionnaire.getRole());
    }

    @Test
    void testNoArgsConstructor() {
        // Act
        Gestionnaire gestionnaire = new Gestionnaire();

        // Assert
        assertNotNull(gestionnaire);
    }

    @Test
    void testToString() {
        // Arrange
        Gestionnaire gestionnaire = new Gestionnaire("John", "Doe", "gest@gmail.com", "password", "123456789");

        // Act
        String result = gestionnaire.toString();

        // Assert
        String expected = "Gestionnaire{" +
                "id=null" +
                ", firstName='John'" +
                ", lastName='Doe'" +
                ", email='gest@gmail.com'" +
                ", phoneNumber='123456789'" +
                ", role='ROLE_GESTIONNAIRE'" +
                '}';
        assertEquals(expected, result);
    }
}