package com.lacouf.rsbjwt.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    @Test
    void testNotificationConstructor() {
        String message = "Test message";
        String titreOffre = "Test offer";
        String email = "test@example.com";
        String url = "http://example.com";

        Notification notification = new Notification(message, titreOffre, email, url);

        assertEquals(message, notification.getMessage());
        assertEquals(titreOffre, notification.getTitreOffre());
        assertFalse(notification.isVu());
        assertEquals(email, notification.getEmail());
        assertEquals(url, notification.getUrl());
        assertNotNull(notification.getDateCreation());
        assertNotNull(notification.getTempsDepuisReception());
    }

    @Test
    void testCalculateTempsDepuisReception() {
        LocalDateTime now = LocalDateTime.now();
        Notification notification = new Notification();
        notification.setDateCreation(now.minusSeconds(30));
        assertEquals("il y a 30 secondes", notification.calculateTempsDepuisReception(notification.getDateCreation()));

        notification.setDateCreation(now.minusMinutes(5));
        assertEquals("il y a 5 minutes", notification.calculateTempsDepuisReception(notification.getDateCreation()));

        notification.setDateCreation(now.minusHours(2));
        assertEquals("il y a 2 heures", notification.calculateTempsDepuisReception(notification.getDateCreation()));

        notification.setDateCreation(now.minusDays(1));
        assertEquals("il y a 1 jours", notification.calculateTempsDepuisReception(notification.getDateCreation()));
    }

    @Test
    void testMarkAsRead() {
        Notification notification = new Notification();
        notification.setVu(false);
        notification.markAsRead();
        assertTrue(notification.isVu());
    }

    @Test
    void testToString() {
        Notification notification = new Notification();
        notification.setId(1L);
        notification.setMessage("Test message");
        notification.setTitreOffre("Test offer");
        notification.setVu(false);
        notification.setEmail("test@example.com");
        notification.setUrl("http://example.com");
        notification.setDateCreation(LocalDateTime.of(2023, 10, 1, 12, 0));

        String expected = "Notification{id=1, message='Test message', titreOffre='Test offer', vu=false, email='test@example.com', url='http://example.com', dateCreation=2023-10-01T12:00}";
        assertEquals(expected, notification.toString());
    }
}