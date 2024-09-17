package com.lacouf.rsbjwt;

import com.lacouf.rsbjwt.model.*;
import com.lacouf.rsbjwt.repository.UserAppRepository;
import com.lacouf.rsbjwt.service.EtudiantService;
import com.lacouf.rsbjwt.service.dto.CredentialDTO;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootApplication
public class ReactSpringSecurityJwtApplication implements CommandLineRunner {
    private final UserAppRepository userAppRepository;

    private final PasswordEncoder passwordEncoder;

    private final EtudiantService etudiantService;

    public ReactSpringSecurityJwtApplication(UserAppRepository userAppRepository, PasswordEncoder passwordEncoder, EtudiantService etudiantService) {
        this.userAppRepository = userAppRepository;
        this.passwordEncoder = passwordEncoder;
        this.etudiantService = etudiantService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ReactSpringSecurityJwtApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Créer un nouvel étudiant DTO avec des informations de test
        EtudiantDTO etudiantDTO = new EtudiantDTO();
        etudiantDTO.setFirstName("John");
        etudiantDTO.setLastName("Doe");
        CredentialDTO credentialDTO = new CredentialDTO();
        credentialDTO.setEmail("john.doe@test.com");
        credentialDTO.setPassword(passwordEncoder.encode("password123"));
        credentialDTO.setPasswordConfirm("password123");
        etudiantDTO.setCredentials(credentialDTO);

        Optional<EtudiantDTO> etudiantCree = etudiantService.creerEtudiant(etudiantDTO);

        // Vérifier si l'étudiant a bien été créé et affiché dans la console
        if (etudiantCree.isPresent()) {
            System.out.println("Étudiant créé avec succès : " + etudiantCree.get());
        } else {
            System.out.println("Échec de la création de l'étudiant.");
        }
    }
}
