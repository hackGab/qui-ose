package com.lacouf.rsbjwt.service.dto;

import com.lacouf.rsbjwt.model.Gestionnaire;
import com.lacouf.rsbjwt.model.auth.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GestionnaireDTO extends UserDTO {
    private CredentialDTO credentials;

    public GestionnaireDTO() {}

    public GestionnaireDTO(String firstName, String lastName, String phoneNumber, Role role, CredentialDTO credentials) {
        super(firstName, lastName, phoneNumber, role);
        this.credentials = credentials;
    }

    public GestionnaireDTO(Gestionnaire gestionnaire) {
        super(gestionnaire);
        this.credentials = new CredentialDTO(gestionnaire.getEmail(), gestionnaire.getPassword());
    }

    public static GestionnaireDTO empty() {
        return new GestionnaireDTO();
    }

    @Override
    public String toString() {
        return "GestionnaireDTO{" +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", role='" + getRole() + '\'' +
                ", credentials='" + credentials + '\'' +
                '}';
    }
}
