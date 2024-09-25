package com.lacouf.rsbjwt;

import com.lacouf.rsbjwt.service.EmployeurService;
import com.lacouf.rsbjwt.service.EtudiantService;
import com.lacouf.rsbjwt.service.ProfesseurService;
import com.lacouf.rsbjwt.service.UserAppService;
import com.lacouf.rsbjwt.service.dto.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication
public class ReactSpringSecurityJwtApplication implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final EtudiantService etudiantService;
    private final ProfesseurService professeurService;
    private final EmployeurService employeurService;
    private final UserAppService userService;

    public ReactSpringSecurityJwtApplication(PasswordEncoder passwordEncoder, EtudiantService etudiantService, ProfesseurService professeurService, EmployeurService employeurService, UserAppService userService) {
        this.passwordEncoder = passwordEncoder;
        this.etudiantService = etudiantService;
        this.professeurService = professeurService;
        this.userService = userService;
        this.employeurService = employeurService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ReactSpringSecurityJwtApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Création d'un étudiant
        EtudiantDTO etudiantDTO = new EtudiantDTO();
        etudiantDTO.setFirstName("John");
        etudiantDTO.setLastName("Doe");
        etudiantDTO.setDepartement("Informatique");
        CredentialDTO credentialDTO = new CredentialDTO();
        credentialDTO.setEmail("john.doe@test.com");
        credentialDTO.setPassword(passwordEncoder.encode("password123"));
        credentialDTO.setPasswordConfirm("password123");
        etudiantDTO.setCredentials(credentialDTO);

        Optional<EtudiantDTO> etudiantCree = etudiantService.creerEtudiant(etudiantDTO);

        if (etudiantCree.isPresent()) {
            System.out.println("Étudiant créé avec succès : " + etudiantCree.get());
        } else {
            System.out.println("Échec de la création de l'étudiant.");
        }

        // Création d'un professeur
        ProfesseurDTO professeurDTO = new ProfesseurDTO();
        professeurDTO.setFirstName("Jane");
        professeurDTO.setLastName("Doe");
        professeurDTO.setDepartement("Informatique");
        CredentialDTO credentialProfDTO = new CredentialDTO();
        credentialProfDTO.setEmail("jane.doe@test.com");
        credentialProfDTO.setPassword(passwordEncoder.encode("password123"));
        credentialProfDTO.setPasswordConfirm("password123");
        professeurDTO.setCredentials(credentialProfDTO);

        Optional<ProfesseurDTO> professeurCree = professeurService.creerProfesseur(professeurDTO);

        if (professeurCree.isPresent()) {
            System.out.println("Professeur créé avec succès : " + professeurCree.get());
        } else {
            System.out.println("Échec de la création du professeur.");
        }

        EmployeurDTO employeurDTO = new EmployeurDTO();
        employeurDTO.setFirstName("Jack");
        employeurDTO.setLastName("Doe");
        employeurDTO.setEntreprise("Doe Inc.");
        CredentialDTO credentialEmpDTO = new CredentialDTO();
        credentialEmpDTO.setEmail("Jack.doe@test.com");
        credentialEmpDTO.setPassword(passwordEncoder.encode("password123"));
        credentialEmpDTO.setPasswordConfirm("password123");
        employeurDTO.setCredentials(credentialEmpDTO);

        Optional<EmployeurDTO> employeurCreer = employeurService.creerEmployeur(employeurDTO);

        if (employeurCreer.isPresent()) {
            System.out.println("Employeur créé avec succès : " + employeurCreer.get());
        } else {
            System.out.println("Échec de la création de l'employeur.");
        }

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("john.doe@test.com");
        loginDTO.setPassword("password123");

        try {
            String accessToken = userService.authenticateUser(loginDTO);
            System.out.println("Authentification réussie, token: " + accessToken);
        } catch (Exception e) {
            System.out.println("Échec de l'authentification : " + e.getMessage());
        }
    }
}
