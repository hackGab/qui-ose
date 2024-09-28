package com.lacouf.rsbjwt.service.dto;

import com.lacouf.rsbjwt.model.Etudiant;
import com.lacouf.rsbjwt.model.auth.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class EtudiantDTO extends UserDTO {
    private CredentialDTO credentials;
    private String departement;

    public EtudiantDTO(String firstName, String lastName, Role role, String phoneNumber, CredentialDTO credentials, String departement) {
        super(firstName, lastName, phoneNumber, role);
        this.credentials = credentials;
        this.departement = departement;
    }

    public EtudiantDTO(Etudiant etudiant) {
        super(etudiant);
        this.credentials = new CredentialDTO(etudiant.getEmail(), etudiant.getPassword());
        this.departement = etudiant.getDepartement();
    }

    public EtudiantDTO() {}

    public static EtudiantDTO empty() {
        return new EtudiantDTO();
    }
}
