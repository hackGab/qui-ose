package com.lacouf.rsbjwt.service.dto;

import com.lacouf.rsbjwt.model.Professeur;
import com.lacouf.rsbjwt.model.auth.Role;

public class ProfesseurDTO extends UserDTO {
    private CredentialDTO credentials;

    public ProfesseurDTO(String firstName, String lastName, Role role, CredentialDTO credentials) {
        super(firstName, lastName, role);
        this.credentials = credentials;
    }

    public ProfesseurDTO(Professeur professeur) {
        super(professeur);
        this.credentials = new CredentialDTO(professeur.getEmail(), professeur.getPassword(), null);
    }
}
