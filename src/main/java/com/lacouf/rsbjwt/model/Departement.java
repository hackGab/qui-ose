package com.lacouf.rsbjwt.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Getter
public enum Departement {
    BACCALAUREAT_INTERNATIONAL_SCIENCES_NATURE_OPTION_SANTE("Baccalauréat international en Sciences de la nature Option Sciences de la santé"),
    CINEMA("Cinéma"),
    GESTION_DE_COMMERCES("Gestion de commerces"),
    GESTION_DES_OPERATIONS_ET_CHAINE_LOGISTIQUE("Gestion des opérations et de la chaîne logistique"),
    JOURNALISME_MULTIMEDIA("Journalisme multimédia"),
    LANGUES_TRILINGUISME_CULTURES("Langues – profil Trilinguisme et cultures"),
    PHOTOGRAPHIE_ET_DESIGN_GRAPHIQUE("Photographie et design graphique"),
    SCIENCES_DE_LA_NATURE("Sciences de la nature"),
    SCIENCES_HUMAINES_ADMINISTRATION_ECONOMIE("Sciences humaines – profil Administration et économie"),
    SCIENCES_HUMAINES_INDIVIDU_RELATIONS_HUMAINES("Sciences humaines – profil Individu et relations humaines"),
    SCIENCES_HUMAINES_MONDE_EN_ACTION("Sciences humaines – profil Monde en action"),
    SCIENCES_HUMAINES_MATH("Sciences humaines – profil Sciences humaines avec mathématiques"),
    SOINS_INFIRMIERS("Soins infirmiers"),
    SOINS_INFIRMIERS_POUR_AUXILIAIRES("Soins infirmiers pour auxiliaires"),
    TECHNIQUES_EDUCATION_ENFANCE("Techniques d'éducation à l'enfance"),
    TECHNIQUES_DE_BUREAUTIQUE("Techniques de bureautique"),
    TECHNIQUES_COMPTABILITE_GESTION("Techniques de comptabilité et de gestion"),
    TECHNIQUES_INFORMATIQUE("Techniques de l'informatique"),
    TECHNIQUES_TRAVAIL_SOCIAL("Techniques de travail social"),
    TECHNOLOGIE_ARCHITECTURE("Technologie de l'architecture"),
    TECHNOLOGIE_ESTIMATION_EVALUATION_BATIMENT("Technologie de l'estimation et de l'évaluation en bâtiment"),
    TECHNOLOGIE_GENIE_CIVIL("Technologie du génie civil"),
    TECHNOLOGIE_GENIE_ELECTRIQUE_AUTOMATISATION_CONTROLE("Technologie du génie électrique: automatisation et contrôle"),
    TECHNOLOGIE_GENIE_PHYSIQUE("Technologie du génie physique"),
    TREMPOLIN_DEC("Tremplin DEC");

    private final String displayName;
    private final Set<Departement> relatedDepartments = new HashSet<>();

    Departement(String displayName) {
        this.displayName = displayName;
    }


    @Override
    public String toString() {
        return name();
    }
}
