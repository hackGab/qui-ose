package com.lacouf.rsbjwt.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CVTest {

    @Test
    void testToString() {
        // Arrange
        CV cv = new CV("cv.pdf", "application/pdf", "data", "pending");

        // Act
        String result = cv.toString();

        // Assert
        String expected = "CV{" +
                "id=null" +
                ", name='cv.pdf'" +
                ", type='application/pdf'" +
                ", uploadDate=" + cv.getUploadDate() +
                ", status='pending'" +
                ", rejetMessage=''" +
                '}';
        assertEquals(expected, result);
    }
}
