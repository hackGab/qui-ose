package com.lacouf.rsbjwt.model;

import com.lacouf.rsbjwt.model.auth.Credentials;
import com.lacouf.rsbjwt.model.auth.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntrevueTest {
    private Entrevue entrevue;

    @BeforeEach
    void setUp() {
        initEntrevue();
    }

    private void initEntrevue() {
        entrevue = new Entrevue();
        entrevue.setId(1L);
        entrevue.setDateHeure(null);
        entrevue.setLocation("location");
        entrevue.setStatus("status");

        Etudiant etudiant = new Etudiant();
        etudiant.setCredentials(new Credentials("email", "password", Role.ETUDIANT));
        entrevue.setEtudiant(etudiant);

        OffreDeStage offreDeStage = new OffreDeStage();
        offreDeStage.setTitre("offreDeStage");
        entrevue.setOffreDeStage(offreDeStage);
    }


    @Test
    void testToString() {
        Entrevue entrevue = new Entrevue();
        entrevue.setId(1L);
        entrevue.setDateHeure(null);
        entrevue.setLocation("location");
        entrevue.setStatus("status");

        Etudiant etudiant = new Etudiant();
        etudiant.setCredentials(new Credentials("email", "password", Role.ETUDIANT));
        entrevue.setEtudiant(etudiant);

        OffreDeStage offreDeStage = new OffreDeStage();
        offreDeStage.setTitre("offreDeStage");
        entrevue.setOffreDeStage(offreDeStage);

        String expected = "Entrevue{id=1, dateHeure=null, location='location', status='status', " +
                "etudiant=email, offreDeStage=offreDeStage}";
        assertEquals(expected, entrevue.toString());
    }

    @Test
    void getId() {
        assertEquals(1L, entrevue.getId());
    }

    @Test
    void getDateHeure() {
        assertNull(entrevue.getDateHeure());
    }

    @Test
    void getLocation() {
        assertEquals("location", entrevue.getLocation());
    }

    @Test
    void getStatus() {
        assertEquals("status", entrevue.getStatus());
    }

    @Test
    void getEtudiant() {
        assertEquals("email", entrevue.getEtudiant().getCredentials().getEmail());
    }

    @Test
    void getOffreDeStage() {
        assertEquals("offreDeStage", entrevue.getOffreDeStage().getTitre());
    }

    @Test
    void setId() {
        entrevue.setId(2L);

        assertEquals(2L, entrevue.getId());
    }

    @Test
    void setDateHeure() {
        entrevue.setDateHeure(null);

        assertNull(entrevue.getDateHeure());
    }

    @Test
    void setLocation() {

        entrevue.setLocation("newLocation");

        assertEquals("newLocation", entrevue.getLocation());
    }

    @Test
    void setStatus() {
        entrevue.setStatus("newStatus");

        assertEquals("newStatus", entrevue.getStatus());
    }

    @Test
    void setEtudiant() {

        Etudiant etudiant = new Etudiant();
        etudiant.setCredentials(new Credentials("newEmail", "newPassword", Role.ETUDIANT));
        entrevue.setEtudiant(etudiant);

        assertEquals("newEmail", entrevue.getEtudiant().getCredentials().getEmail());
    }

    @Test
    void setOffreDeStage() {

        OffreDeStage offreDeStage = new OffreDeStage();
        offreDeStage.setTitre("newOffreDeStage");
        entrevue.setOffreDeStage(offreDeStage);

        assertEquals("newOffreDeStage", entrevue.getOffreDeStage().getTitre());

    }
}
