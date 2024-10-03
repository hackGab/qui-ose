package com.lacouf.rsbjwt.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OffreDeStage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    private String localisation;
    private LocalDate dateLimite;
    private LocalDate datePublication;
    private String data;
    private int nbCandidats;

    @ManyToOne
    @JoinColumn(name = "employeur_id")
    private Employeur employeur;


    public OffreDeStage(String titre, String localisation, LocalDate dateLimite, String data, int nbCandidats) {
        this.titre = titre;
        this.localisation = localisation;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.dateLimite = LocalDate.parse(dateLimite.format(formatter), formatter);
        this.datePublication = LocalDate.parse(LocalDate.now().format(formatter), formatter);
        this.data = data;
        this.nbCandidats = nbCandidats;
    }

    

    @Override
    public String toString() {
        return "OffreDeStage{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", localisation='" + localisation + '\'' +
                ", dateLimite='" + dateLimite + '\'' +
                ", employeur='" + employeur + '\'' +
                ", datePublication='" + datePublication + '\'' +
                ", data='" + data + '\'' +
                ", nbCandidats='" + nbCandidats + '\'' +
                '}';
    }
}
