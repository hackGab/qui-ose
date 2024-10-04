package com.lacouf.rsbjwt.service.dto;

import com.lacouf.rsbjwt.model.OffreDeStage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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

    public OffreDeStageDTO(Long id, String titre, String localisation, LocalDate dateLimite, LocalDate datePublication, String data, int nbCandidats, EmployeurDTO employeur) {
        this.id = id;
        this.titre = titre;
        this.localisation = localisation;
        this.dateLimite = dateLimite;
        this.datePublication = datePublication;
        this.data = data;
        this.nbCandidats = nbCandidats;
        this.employeur = employeur;
    }

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
