package com.lacouf.rsbjwt.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OffreDeStage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    private String description;
    private String responsabilites;
    private String qualifications;
    private String duree;
    private String localisation;
    private String salaire;
    private LocalDate dateLimite;

    @ManyToOne
    @JoinColumn(name = "employeur_id")
    private Employeur employeur;

    public OffreDeStage(String titre, String description, String responsabilites, String qualifications, String duree, String localisation, String salaire, LocalDate dateLimite) {
        this.titre = titre;
        this.description = description;
        this.responsabilites = responsabilites;
        this.qualifications = qualifications;
        this.duree = duree;
        this.localisation = localisation;
        this.salaire = salaire;
        this.dateLimite = dateLimite;
    }

    
    @Override
    public String toString() {
        return "OffreDeStage{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", responsabilites='" + responsabilites + '\'' +
                ", qualifications='" + qualifications + '\'' +
                ", duree='" + duree + '\'' +
                ", localisation='" + localisation + '\'' +
                ", salaire='" + salaire + '\'' +
                ", dateLimite=" + dateLimite + '\'' +
                ", employeur='" + employeur +
                '}';
    }
}
