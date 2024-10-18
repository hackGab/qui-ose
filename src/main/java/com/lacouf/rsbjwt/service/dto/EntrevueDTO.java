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
    private EtudiantDTO etudiantDTO;

    public EntrevueDTO(LocalDateTime dateHeure, String location, EtudiantDTO etudiantDTO){
        this.dateHeure = dateHeure;
        this.location = location;
        this.etudiantDTO = etudiantDTO;
    }

    public EntrevueDTO(Entrevue entrevue){
        this.dateHeure = entrevue.getDateHeure();
        this.location = entrevue.getLocation();
        this.etudiantDTO = new EtudiantDTO(entrevue.getEtudiant());
    }

    public EntrevueDTO() {}

    public static EntrevueDTO empty() {return new EntrevueDTO();}
}
