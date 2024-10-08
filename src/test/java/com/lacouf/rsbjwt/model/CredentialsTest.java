package com.lacouf.rsbjwt.model;

import com.lacouf.rsbjwt.model.auth.Credentials;
import com.lacouf.rsbjwt.model.auth.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CredentialsTest {
    @Test
    void testToString() {
        // Arrange
        Role role = Role.GESTIONNAIRE;
        Credentials credentials = new Credentials("email@gmail.com", "mdp", role);

        // Act
        String result = credentials.toString();

        // Assert
        String expected = "Credentials(" +
                "email=email@gmail.com" +
                ", password=mdp" +
                ", role=ROLE_GESTIONNAIRE" +
                ')';

        assertEquals(expected, result);
    }

    @Test
    void testGetAuthorities() {
        // Arrange
        Role role = Role.GESTIONNAIRE;
        Credentials credentials = new Credentials("email@gmail.com", "mdp", role);

        // Act
        var result = credentials.getAuthorities();

        // Assert
        var expected = "[GESTIONNAIRE]";
        assertEquals(expected, result.toString());
    }

    @Test
    void testGetPassword() {
        // Arrange
        Role role = Role.GESTIONNAIRE;
        Credentials credentials = new Credentials("email@gmail.com", "mdp", role);

        // Act
        var result = credentials.getPassword();

        // Assert
        var expected = "mdp";
        assertEquals(expected, result);
    }

    @Test
    void testGetUsername() {
        // Arrange
        Role role = Role.GESTIONNAIRE;
        Credentials credentials = new Credentials("email@gmail.com", "mdp", role);

        // Act
        var result = credentials.getUsername();

        // Assert
        var expected = "email@gmail.com";
        assertEquals(expected, result);
    }

    @Test
    void testIsAccountNonExpired() {
        // Arrange
        Role role = Role.GESTIONNAIRE;
        Credentials credentials = new Credentials("email@gmail.com", "mdp", role);

        // Act
        var result = credentials.isAccountNonExpired();

        // Assert
        var expected = true;
        assertEquals(expected, result);
    }

    @Test
    void testIsAccountNonLocked() {
        // Arrange
        Role role = Role.GESTIONNAIRE;
        Credentials credentials = new Credentials("email@gmail.com", "mdp", role);

        // Act
        var result = credentials.isAccountNonLocked();

        // Assert
        var expected = true;
        assertEquals(expected, result);
    }

    @Test
    void testIsCredentialsNonExpired() {
        // Arrange
        Role role = Role.GESTIONNAIRE;
        Credentials credentials = new Credentials("email@gmail.com", "mdp", role);

        // Act
        var result = credentials.isCredentialsNonExpired();

        // Assert
        var expected = true;
        assertEquals(expected, result);
    }

    @Test
    void testIsEnabled() {
        // Arrange
        Role role = Role.GESTIONNAIRE;
        Credentials credentials = new Credentials("email@gmail.com", "mdp", role);

        // Act
        var result = credentials.isEnabled();

        // Assert
        var expected = true;
        assertEquals(expected, result);
    }
}
