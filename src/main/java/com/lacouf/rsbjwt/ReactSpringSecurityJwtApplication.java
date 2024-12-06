package com.lacouf.rsbjwt;

import com.lacouf.rsbjwt.model.CandidatAccepter;
import com.lacouf.rsbjwt.model.auth.Role;
import com.lacouf.rsbjwt.service.*;
import com.lacouf.rsbjwt.service.dto.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ReactSpringSecurityJwtApplication implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final EtudiantService etudiantService;
    private final ProfesseurService professeurService;
    private final EmployeurService employeurService;
    private final GestionnaireService gestionnaireService;
    private final UserAppService userService;

    public ReactSpringSecurityJwtApplication(PasswordEncoder passwordEncoder, EtudiantService etudiantService, ProfesseurService professeurService, EmployeurService employeurService, GestionnaireService gestionnaireService, UserAppService userService) {
        this.passwordEncoder = passwordEncoder;
        this.etudiantService = etudiantService;
        this.professeurService = professeurService;
        this.gestionnaireService = gestionnaireService;
        this.userService = userService;
        this.employeurService = employeurService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ReactSpringSecurityJwtApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        gestionnaireService.creerGestionnaire(
                new GestionnaireDTO(
                        "Thiraiyan",
                        "Moon",
                        "123-456-7890",
                        Role.GESTIONNAIRE,
                        new CredentialDTO("niseiyen@gmail.com", "nini123")
                )
        );

        userService.authenticateUser(new LoginDTO("niseiyen@gmail.com", "nini123"));
    }
}
