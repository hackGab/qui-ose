package com.lacouf.rsbjwt.model;

import com.lacouf.rsbjwt.model.auth.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Employeur extends UserApp{
    @Column(nullable = false, name = "ENTREPRISE")
    private String entreprise;

    public Employeur(String firstName, String lastName, String email, String password, String phoneNumber, String entreprise) {
        super(firstName, lastName, email, password, phoneNumber, Role.EMPLOYEUR);
        this.entreprise = entreprise;
    }



    @Override
    public String toString() {
        return "Employeur{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + getEmail() + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role='" + getRole() + '\'' +
                ", entreprise='" + entreprise + '\'' +
                '}';
    }
}
