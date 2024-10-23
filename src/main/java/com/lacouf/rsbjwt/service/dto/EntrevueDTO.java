package com.lacouf.rsbjwt.service.dto;

import com.lacouf.rsbjwt.model.Entrevue;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class EntrevueDTO {
    private LocalDateTime dateHeure;
    private String location;
    private String status;
    private EtudiantDTO etudiantDTO;
    private OffreDeStageDTO offreDeStageDTO;

    public EntrevueDTO(LocalDateTime dateHeure, String location, String status, EtudiantDTO etudiantDTO){
        this.dateHeure = dateHeure;
        this.location = location;
        this.status = status;
        this.etudiantDTO = etudiantDTO;
    }

    public EntrevueDTO(Entrevue entrevue){
        this.dateHeure = entrevue.getDateHeure();
        this.location = entrevue.getLocation();
        this.status = entrevue.getStatus();
        this.etudiantDTO = new EtudiantDTO(entrevue.getEtudiant());
        this.offreDeStageDTO = new OffreDeStageDTO(entrevue.getOffreDeStage());
    }

    public EntrevueDTO() {}

    public static EntrevueDTO empty() {return new EntrevueDTO();}
}