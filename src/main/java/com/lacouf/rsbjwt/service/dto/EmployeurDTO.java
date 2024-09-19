package com.lacouf.rsbjwt.service.dto;

import com.lacouf.rsbjwt.model.Employeur;
import com.lacouf.rsbjwt.model.auth.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeurDTO extends UserDTO {
    private CredentialDTO credentials;
    private String entreprise;

    public EmployeurDTO(String firstName, String lastName, String phoneNumber, Role role, CredentialDTO credentials, String entreprise) {
        super(firstName, lastName, phoneNumber, role);
        this.credentials = credentials;
        this.entreprise = entreprise;
    }

    public EmployeurDTO(Employeur employeur) {
        super(employeur);
        this.credentials = new CredentialDTO(employeur.getEmail(), employeur.getPassword(), null);
        this.entreprise = employeur.getEntreprise();
    }
}
