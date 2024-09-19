package com.lacouf.rsbjwt;

import com.lacouf.rsbjwt.service.EtudiantService;
import com.lacouf.rsbjwt.service.dto.CredentialDTO;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication
public class ReactSpringSecurityJwtApplication implements CommandLineRunner {

//    private final PasswordEncoder passwordEncoder;
//    private final EtudiantService etudiantService;
//
//    public ReactSpringSecurityJwtApplication(PasswordEncoder passwordEncoder, EtudiantService etudiantService) {
//        this.passwordEncoder = passwordEncoder;
//        this.etudiantService = etudiantService;
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
    }
}
