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

    @Column(nullable = false, length = Integer.MAX_VALUE)
    private String data;

    private int nbCandidats;
    private String status;


    @ManyToOne
    @JoinColumn(name = "EMPLOYEUR_ID")
    private Employeur employeur;


    public OffreDeStage(String titre, String localisation, LocalDate dateLimite, String data, int nbCandidats,String status) {
        this.titre = titre;
        this.localisation = localisation;
        this.dateLimite = dateLimite;
        this.datePublication = LocalDate.now();
        this.data = data;
        this.nbCandidats = nbCandidats;
        this.status = status;
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
                ", nbCandidats='" + nbCandidats + '\'' +
                '}';
    }
}
