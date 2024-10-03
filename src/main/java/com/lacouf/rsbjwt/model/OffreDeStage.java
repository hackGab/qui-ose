package com.lacouf.rsbjwt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String responsabilites;
    private String qualifications;
    private Long duree;
    private String localisation;
    private Double salaire;
    private LocalDate dateLimite;

    @ManyToOne
    @JoinColumn(name = "employeur_id")
    private Employeur employeur;


}
