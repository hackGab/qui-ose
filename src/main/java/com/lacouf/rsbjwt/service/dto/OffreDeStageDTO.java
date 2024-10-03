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
    private String duree;  // Modifié pour refléter les changements
    private String localisation;  // Modifié pour refléter les changements
    private String exigences;  // Modifié pour refléter les changements
    private LocalDate dateDebutSouhaitee;  // Modifié pour refléter les changements
    private String typeRemuneration;  // Modifié pour refléter les changements
    private String salaire;  // Modifié pour refléter les changements
    private String disponibilite;  // Modifié pour refléter les changements
    private LocalDate dateLimite;
    private String qualification;
    private String contactInfo;
    private EmployeurDTO employeur;

    // Constructeur qui prend en paramètre un objet OffreDeStage
    public OffreDeStageDTO(OffreDeStage offre) {
        this.id = offre.getId();
        this.titre = offre.getTitre();
        this.description = offre.getDescription();
        this.duree = offre.getDuree();
        this.localisation = offre.getLocalisation();
        this.exigences = offre.getExigences();  // Modifié pour refléter les changements
        this.dateDebutSouhaitee = offre.getDateDebutSouhaitee();  // Modifié pour refléter les changements
        this.typeRemuneration = offre.getTypeRemuneration();  // Modifié pour refléter les changements
        this.salaire = offre.getSalaire();  // Modifié pour refléter les changements
        this.disponibilite = offre.getDisponibilite();  // Modifié pour refléter les changements
        this.dateLimite = offre.getDateLimite();
        this.employeur = new EmployeurDTO(offre.getEmployeur());
        this.qualification = offre.getQualification();
        this.contactInfo = offre.getContactInfo();
    }

    public static OffreDeStageDTO empty() {
        return new OffreDeStageDTO();
    }
}
