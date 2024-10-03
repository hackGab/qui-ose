package com.lacouf.rsbjwt.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OffreDeStage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    private String description;
    private String duree;
    private String localisation;
    private String exigences;
    private LocalDate dateDebutSouhaitee;
    private String typeRemuneration;
    private String salaire;
    private String disponibilite;
    private LocalDate dateLimite;
    private String qualification;
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
                ", qualifications='" + qualification + '\'' +
                ", dateDebutSouhaitee='" + dateDebutSouhaitee + '\'' +
                ", typeRemuneration='" + typeRemuneration + '\'' +
                ", salaire='" + salaire + '\'' +
                ", disponibilite='" + disponibilite + '\'' +
                ", dateLimite='" + dateLimite + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                ", employeur='" + employeur + '\'' +
                '}';
    }
}
