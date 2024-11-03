package com.lacouf.rsbjwt.model;

import com.lacouf.rsbjwt.model.auth.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Professeur extends UserApp {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "DEPARTEMENT")
    private Departement departement;

    @OneToMany(mappedBy = "professeur")
    private List<Etudiant> etudiants;

    public Professeur(String firstName, String lastName, String email, String password, String phoneNumber, Departement departement) {
        super(firstName, lastName, email, password, phoneNumber, Role.PROFESSEUR);
        this.departement = departement;
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
                ", departement='" + departement + '\'' +
                ", etudiants='" + etudiants + '\'' +
                '}';
    }


}
