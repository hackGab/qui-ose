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
public class EtudiantDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Role role;


    public EtudiantDTO(String firstName, String lastName, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public EtudiantDTO(Etudiant etudiant) {
        this.id = etudiant.getId();
        this.firstName = etudiant.getFirstName();
        this.lastName = etudiant.getLastName();
        this.email = etudiant.getEmail();
        this.phoneNumber = etudiant.getPhoneNumber();
        this.role = etudiant.getRole();
    }
}
