package com.lacouf.rsbjwt.model;

import com.lacouf.rsbjwt.model.auth.Role;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Professeur extends UserApp {

    public Professeur(String firstName, String lastName, String email, String password, String phoneNumber) {
        super(firstName, lastName, email, password, phoneNumber, Role.ETUDIANT);
    }

    @Override
    public String toString() {
        return "Professeur{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + getEmail() + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role='" + getRole() + '\'' +
                '}';
    }
}