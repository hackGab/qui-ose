package com.lacouf.rsbjwt.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GestionnaireTest {

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
