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
    public void run(String... args) throws Exception {}
}
