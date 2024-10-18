package com.lacouf.rsbjwt.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Entrevue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateHeure;

    @Column(nullable = false)
    private String location;

    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;

    public Entrevue(LocalDateTime dateHeure, String location, Etudiant etudiant) {
        this.dateHeure = dateHeure;
        this.location = location;
        this.etudiant = etudiant;
    }

    @Override
    public String toString() {
        return "Entrevue{" +
                "id=" + id +
                ", dateHeure=" + dateHeure +
                ", location='" + location + '\'' +
                ", etudiant=" + etudiant.getFirstName() + " " + etudiant.getLastName() +
                '}';
    }
}
