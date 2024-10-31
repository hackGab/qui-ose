package com.lacouf.rsbjwt.service.dto;

import com.lacouf.rsbjwt.model.Departement;
import com.lacouf.rsbjwt.model.Etudiant;
import com.lacouf.rsbjwt.model.auth.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
public class EtudiantDTO extends UserDTO {
    private CredentialDTO credentials;
    private Departement departement;
    private CVDTO cv;
    private List<OffreDeStageDTO> offresAppliquees;
    private ProfesseurDTO professeur;

    public EtudiantDTO(String firstName, String lastName, Role role, String phoneNumber, CredentialDTO credentials, Departement departement) {
        super(firstName, lastName, phoneNumber, role);
        this.credentials = credentials;
        this.departement = departement;
    }

    public EtudiantDTO(Etudiant etudiant) {
        super(etudiant);
        this.credentials = new CredentialDTO(etudiant.getEmail(), etudiant.getPassword());
        this.departement = etudiant.getDepartement();

        if (etudiant.getCv() != null) {
            this.cv = new CVDTO(etudiant.getCv());
        }
        if (etudiant.getOffresAppliquees() != null) {
            this.offresAppliquees = etudiant.getOffresAppliquees().stream()
                    .map(OffreDeStageDTO::new)
                    .collect(Collectors.toList());
        }

        if (etudiant.getProfesseur() != null) {
            this.professeur = new ProfesseurDTO(etudiant.getProfesseur());
        }
    }

    public EtudiantDTO() {}

    public static EtudiantDTO empty() {
        return new EtudiantDTO();
    }
}
