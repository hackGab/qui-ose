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

    @Column(nullable = false, name = "DEPARTEMENT")
    private String departement;

    @OneToOne
    private CV cv;

    @ManyToMany
    @JoinTable(
            name = "etudiant_offres_appliquees",
            joinColumns = @JoinColumn(name = "etudiant_id"),
            inverseJoinColumns = @JoinColumn(name = "offre_de_stage_id")
    )
    private List<OffreDeStage> offresAppliquees;

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
                ", cv=" + cv +
                ", offresAppliquees=" + offresAppliquees +
                '}';
    }
}
