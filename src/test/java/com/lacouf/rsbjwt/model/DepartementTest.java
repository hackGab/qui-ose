package com.lacouf.rsbjwt.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class DepartementTest {

    @Test
    void testGetDisplayName() {
        // Arrange & Act
        String displayName = Departement.TECHNIQUES_INFORMATIQUE.getDisplayName();

        // Assert
        assertEquals("Techniques de l'informatique", displayName);
    }

//    @Test
//    void testGetAllDepartementDisplayNames() {
//        // Act
//        List<String> displayNames = Departement.getAllDepartementDisplayNames();
//
//        // Assert
//        assertTrue(displayNames.contains("Techniques de l'informatique"));
//        assertEquals(25, displayNames.size());
//    }

    @Test
    void testGetRelatedDepartments() {
        // Arrange & Act
        Set<Departement> relatedDepartments = Departement.TECHNIQUES_INFORMATIQUE.getRelatedDepartments();

        // Assert
        assertNotNull(relatedDepartments);
        assertTrue(relatedDepartments.isEmpty());
    }

    @Test
    void testToString() {
        // Arrange & Act
        String result = Departement.TECHNIQUES_INFORMATIQUE.toString();

        // Assert
        assertEquals("TECHNIQUES_INFORMATIQUE", result);
    }
}