package com.lacouf.rsbjwt.service.dto;

import com.lacouf.rsbjwt.model.OffreDeStage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class OffreDeStageDTO {

    private Long id;
    private String titre;
    private String description;
    private String responsabilites;
    private String qualifications;
    private String duree;
    private String localisation;
    private String salaire;
    private LocalDate dateLimite;
    private EmployeurDTO employeur;
    private String status;
    private boolean isCheked;


    public OffreDeStageDTO(OffreDeStage offre) {
        this.id = offre.getId();
        this.titre = offre.getTitre();
        this.description = offre.getDescription();
        this.responsabilites = offre.getResponsabilites();
        this.qualifications = offre.getQualifications();
        this.duree = offre.getDuree();
        this.localisation = offre.getLocalisation();
        this.salaire = offre.getSalaire();
        this.dateLimite = offre.getDateLimite();
        this.employeur = new EmployeurDTO(offre.getEmployeur());
    }

    public static OffreDeStageDTO empty() {
        return new OffreDeStageDTO();
    }



}
