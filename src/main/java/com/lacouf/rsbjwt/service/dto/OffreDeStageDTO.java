package com.lacouf.rsbjwt.service.dto;

import com.lacouf.rsbjwt.model.Employeur;
import com.lacouf.rsbjwt.model.OffreDeStage;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    private String localisation;
    private LocalDate dateLimite;
    private LocalDate datePublication;
    private String data;
    private int nbCandidats;
    private EmployeurDTO employeur;


    // Constructeur qui prend en paramètre un objet OffreDeStage
    public OffreDeStageDTO(OffreDeStage offre) {
        this.id = offre.getId();
        this.titre = offre.getTitre();
        this.localisation = offre.getLocalisation();
        this.dateLimite = offre.getDateLimite();
        this.datePublication = offre.getDatePublication();
        this.data = offre.getData();
        this.nbCandidats = offre.getNbCandidats();
        this.employeur = new EmployeurDTO(offre.getEmployeur());
    }

    public static OffreDeStageDTO empty() {
        return new OffreDeStageDTO();
    }



}
