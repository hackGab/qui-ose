package com.lacouf.rsbjwt.model;

import com.lacouf.rsbjwt.model.auth.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Etudiant extends UserApp {

    @Column(nullable = false, name = "DEPARTEMENT")
    private String departement;

    @OneToOne
    private CV cv;

    public Etudiant(String firstName, String lastName, String email, String password, String phoneNumber, String departement) {
        super(firstName, lastName, email, password, phoneNumber, Role.ETUDIANT);
        this.departement = departement;
    }

    @Override
    public String toString() {
        return "Etudiant{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + getEmail() + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role='" + getRole() + '\'' +
                ", departement='" + departement + '\'' +
                '}';
    }
}
