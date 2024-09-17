package com.lacouf.rsbjwt.service.dto;

import com.lacouf.rsbjwt.model.Etudiant;
import com.lacouf.rsbjwt.model.auth.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EtudiantDTO extends UserDTO {
    private CredentialDTO credentials; // Ajoutez cette ligne pour inclure les informations de connexion

    public EtudiantDTO(String firstName, String lastName, Role role, CredentialDTO credentials) {
        super(firstName, lastName, role);
        this.credentials = credentials;
    }

    public EtudiantDTO(Etudiant etudiant) {
        super(etudiant);
        this.credentials = new CredentialDTO(etudiant.getEmail(), etudiant.getPassword(), null); // Assurez-vous que vous n'exposez pas le mot de passe
    }
}
