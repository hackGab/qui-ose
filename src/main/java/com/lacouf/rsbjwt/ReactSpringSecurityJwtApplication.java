package com.lacouf.rsbjwt;

import com.lacouf.rsbjwt.service.EtudiantService;
import com.lacouf.rsbjwt.service.ProfesseurService;
import com.lacouf.rsbjwt.service.dto.CredentialDTO;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import com.lacouf.rsbjwt.service.dto.ProfesseurDTO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication
public class ReactSpringSecurityJwtApplication implements CommandLineRunner {

//    private final PasswordEncoder passwordEncoder;
//    private final EtudiantService etudiantService;
//    private final ProfesseurService professeurService;
//
//    public ReactSpringSecurityJwtApplication(PasswordEncoder passwordEncoder, EtudiantService etudiantService, ProfesseurService professeurService) {
//        this.passwordEncoder = passwordEncoder;
//        this.etudiantService = etudiantService;
//        this.professeurService = professeurService;
//    }

    public static void main(String[] args) {
        SpringApplication.run(ReactSpringSecurityJwtApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Création d'un étudiant
//        EtudiantDTO etudiantDTO = new EtudiantDTO();
//        etudiantDTO.setFirstName("John");
//        etudiantDTO.setLastName("Doe");
//        CredentialDTO credentialDTO = new CredentialDTO();
//        credentialDTO.setEmail("john.doe@test.com");
//        credentialDTO.setPassword(passwordEncoder.encode("password123"));
//        credentialDTO.setPasswordConfirm("password123");
//        etudiantDTO.setCredentials(credentialDTO);
//
//        Optional<EtudiantDTO> etudiantCree = etudiantService.creerEtudiant(etudiantDTO);
//
//        if (etudiantCree.isPresent()) {
//            System.out.println("Étudiant créé avec succès : " + etudiantCree.get());
//
//            // Récupération de l'étudiant par ID
//            Long etudiantId = etudiantCree.get().getId(); // Assurez-vous que vous avez un getter pour l'ID dans EtudiantDTO
//            Optional<EtudiantDTO> etudiantRecupere = etudiantService.getEtudiantById(etudiantId);
//
//            if (etudiantRecupere.isPresent()) {
//                System.out.println("Étudiant récupéré avec succès : ");
//                System.out.println("ID: " + etudiantRecupere.get().getId());
//                System.out.println("Prénom: " + etudiantRecupere.get().getFirstName());
//                System.out.println("Nom: " + etudiantRecupere.get().getLastName());
//                System.out.println("Email: " + (etudiantRecupere.get().getCredentials() != null ? etudiantRecupere.get().getCredentials().getEmail() : "N/A"));
//            } else {
//                System.out.println("Étudiant non trouvé avec ID : " + etudiantId);
//            }
//        } else {
//            System.out.println("Échec de la création de l'étudiant.");
//        }
//
//        // Création d'un professeur
//        ProfesseurDTO professeurDTO = new ProfesseurDTO();
//        professeurDTO.setFirstName("Jane");
//        professeurDTO.setLastName("Doe");
//        CredentialDTO credentialProfDTO = new CredentialDTO();
//        credentialProfDTO.setEmail("jane.doe@test.com");
//        credentialProfDTO.setPassword(passwordEncoder.encode("password123"));
//        credentialProfDTO.setPasswordConfirm("password123");
//        professeurDTO.setCredentials(credentialProfDTO);
//
//        Optional<ProfesseurDTO> professeurCree = professeurService.creerProfesseur(professeurDTO);
//
//        if (professeurCree.isPresent()) {
//            System.out.println("Professeur créé avec succès : " + professeurCree.get());
//
//            // Récupération du professeur par ID
//            Long professeurId = professeurCree.get().getId(); // Assurez-vous que vous avez un getter pour l'ID dans ProfesseurDTO
//            Optional<ProfesseurDTO> professeurRecupere = professeurService.getProfesseurById(professeurId);
//
//            if (professeurRecupere.isPresent()) {
//                System.out.println("Professeur récupéré avec succès : ");
//                System.out.println("ID: " + professeurRecupere.get().getId());
//                System.out.println("Prénom: " + professeurRecupere.get().getFirstName());
//                System.out.println("Nom: " + professeurRecupere.get().getLastName());
//                System.out.println("Email: " + (professeurRecupere.get().getCredentials() != null ? professeurRecupere.get().getCredentials().getEmail() : "N/A"));
//            } else {
//                System.out.println("Professeur non trouvé avec ID : " + professeurId);
//            }
//        } else {
//            System.out.println("Échec de la création du professeur.");
//        }
    }
}
