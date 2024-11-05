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
public class Etudiant extends UserApp {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "DEPARTEMENT")
    private Departement departement;

    @OneToOne
    private CV cv;

    @ManyToMany
    @JoinTable(
            name = "etudiant_offres_appliquees",
            joinColumns = @JoinColumn(name = "etudiant_id"),
            inverseJoinColumns = @JoinColumn(name = "offre_de_stage_id")
    )
    private List<OffreDeStage> offresAppliquees;

    @ManyToOne
    @JoinColumn(name = "professeur_id")
    private Professeur professeur;


    public Etudiant(String firstName, String lastName, String email, String password, String phoneNumber, Departement departement) {
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
                ", cv=" + (cv != null ? cv.getId() : "null") +
                ", professeur=" + (professeur != null ? professeur.getId() : "null") +
                ", offresAppliquees=" + offresAppliquees +
                '}';
    }

}
