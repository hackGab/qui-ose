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

    // Titre du stage
    private String titre;

    // Description détaillée
    private String description;

    // Durée du stage (ex: "3 mois", "6 semaines")
    private String duree;

    // Localisation du stage
    private String localisation;

    // Exigences du stage (compétences techniques, qualifications)
    private String exigences;

    private String qualification;

    // Date de début souhaitée
    private LocalDate dateDebutSouhaitee;

    // Type de rémunération
    private String typeRemuneration;

    private String salaire;

    // Disponibilité (temps plein, temps partiel)
    private String disponibilite;

    // Date limite de candidature
    private LocalDate dateLimite;

    private String contactInfo;

    // Relation avec l'employeur
    @ManyToOne
    @JoinColumn(name = "employeur_id")
    private Employeur employeur;

    // Constructeur pour initialiser tous les champs
    public OffreDeStage(String titre, String description, String duree, String localisation, String exigences,
                        LocalDate dateDebutSouhaitee, String typeRemuneration,String salaire, String disponibilite, LocalDate dateLimite,String qualification,String contactInfo) {
        this.titre = titre;
        this.description = description;
        this.duree = duree;
        this.localisation = localisation;
        this.exigences = exigences;
        this.dateDebutSouhaitee = dateDebutSouhaitee;
        this.typeRemuneration = typeRemuneration;
        this.salaire = salaire;
        this.disponibilite = disponibilite;
        this.dateLimite = dateLimite;
        this.qualification = qualification;
        this.contactInfo = contactInfo;
    }

    @Override
    public String toString() {
        return "OffreDeStage{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", duree='" + duree + '\'' +
                ", localisation='" + localisation + '\'' +
                ", exigences='" + exigences + '\'' +
                ", dateDebutSouhaitee=" + dateDebutSouhaitee +
                ", typeRemuneration='" + typeRemuneration + '\'' +
                ", disponibilite='" + disponibilite + '\'' +
                ", dateLimite=" + dateLimite +
                ", employeur=" + employeur +
                '}';
    }
}
