package com.lacouf.rsbjwt.service.dto;

import com.lacouf.rsbjwt.model.OffreDeStage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
    private String status;
    private String rejetMessage = "";
    private EmployeurDTO employeur;
    private List<EtudiantDTO> etudiants;

    public OffreDeStageDTO(Long id, String titre, String localisation, LocalDate dateLimite, LocalDate datePublication, String data, int nbCandidats, String status, EmployeurDTO employeur, List<EtudiantDTO> etudiants) {
        this.id = id;
        this.titre = titre;
        this.localisation = localisation;
        this.dateLimite = dateLimite;
        this.datePublication = datePublication;
        this.data = data;
        this.nbCandidats = nbCandidats;
        this.status = status;
        this.employeur = employeur;
        this.etudiants = etudiants;
    }

    public OffreDeStageDTO(OffreDeStage offre) {
        this.id = offre.getId();
        this.titre = offre.getTitre();
        this.localisation = offre.getLocalisation();
        this.dateLimite = offre.getDateLimite();
        this.datePublication = offre.getDatePublication();
        this.data = offre.getData();
        this.nbCandidats = offre.getNbCandidats();
        this.status = offre.getStatus();
        this.rejetMessage = offre.getRejetMessage();
        this.employeur = new EmployeurDTO(offre.getEmployeur());
        this.etudiants = offre.getEtudiants().stream()
                .map(EtudiantDTO::new)  // Conversion de chaque Etudiant en EtudiantDTO
                .collect(Collectors.toList());
    }

    public static OffreDeStageDTO empty() {
        return new OffreDeStageDTO();
    }
}
