package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.*;
import com.lacouf.rsbjwt.model.auth.Role;
import com.lacouf.rsbjwt.repository.*;
import com.lacouf.rsbjwt.service.dto.*;
import com.lacouf.rsbjwt.security.JwtTokenProvider;
import com.lacouf.rsbjwt.security.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserAppService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserAppRepository userAppRepository;
    private final EtudiantRepository etudiantRepository;
    private final ProfesseurRepository professeurRepository;
    private final EmployeurRepository employeurRepository;
    private final GestionnaireRepository gestionnaireRepository;

    public UserAppService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserAppRepository userAppRepository, EtudiantRepository etudiantRepository, ProfesseurRepository professeurRepository, EmployeurRepository employeurRepository, GestionnaireRepository gestionnaireRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userAppRepository = userAppRepository;
        this.etudiantRepository = etudiantRepository;
        this.professeurRepository = professeurRepository;
        this.employeurRepository = employeurRepository;
        this.gestionnaireRepository = gestionnaireRepository;
    }

    public String authenticateUser(LoginDTO loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        return jwtTokenProvider.generateToken(authentication);
    }

    public UserDTO getMe(String token) {
        token = token.startsWith("Bearer") ? token.substring(7) : token;
        String email = jwtTokenProvider.getEmailFromJWT(token);
        UserApp user = userAppRepository.findUserAppByEmail(email).orElseThrow(UserNotFoundException::new);
        return switch(user.getRole()){
            case GESTIONNAIRE -> getGestionnaireDTO(user.getId());
            case PROFESSEUR -> getProfesseurDTO(user.getId());
            case EMPLOYEUR -> getEmployeurDTO(user.getId());
            case ETUDIANT -> getEtudiantDTO(user.getId());
        };
    }

    public EtudiantDTO getEtudiantDTO(Long id) {
        final Optional<Etudiant> etudiantOptional = etudiantRepository.findById(id);

        return etudiantOptional.map(etudiant -> new EtudiantDTO(
                etudiant.getFirstName(),
                etudiant.getLastName(),
                etudiant.getRole(),
                etudiant.getPhoneNumber(),
                new CredentialDTO(etudiant.getCredentials().getEmail(), etudiant.getCredentials().getPassword()),
                etudiant.getDepartement()
                )).orElseGet(EtudiantDTO::empty);
    }

    public ProfesseurDTO getProfesseurDTO(Long id) {
        final Optional<Professeur> professeurOptional = professeurRepository.findById(id);

        return professeurOptional.map(professeur -> new ProfesseurDTO(
                professeur.getFirstName(),
                professeur.getLastName(),
                professeur.getRole(),
                professeur.getPhoneNumber(),
                new CredentialDTO(professeur.getCredentials().getEmail(), professeur.getCredentials().getPassword()),
                professeur.getDepartement()
                )).orElseGet(ProfesseurDTO::empty);
    }

    public EmployeurDTO getEmployeurDTO(Long id) {
        final Optional<Employeur> employeurOptional = employeurRepository.findById(id);

        return employeurOptional.map(employeur -> new EmployeurDTO(
                employeur.getFirstName(),
                employeur.getLastName(),
                employeur.getPhoneNumber(),
                employeur.getRole(),
                new CredentialDTO(employeur.getCredentials().getEmail(), employeur.getCredentials().getPassword()),
                employeur.getEntreprise()
                )).orElseGet(EmployeurDTO::empty);
    }

    public GestionnaireDTO getGestionnaireDTO(Long id) {
        final Optional<Gestionnaire> gestionnaireOptional = gestionnaireRepository.findById(id);

        return gestionnaireOptional.map(gestionnaire -> new GestionnaireDTO(
                gestionnaire.getFirstName(),
                gestionnaire.getLastName(),
                gestionnaire.getPhoneNumber(),
                gestionnaire.getRole(),
                new CredentialDTO(gestionnaire.getCredentials().getEmail(), gestionnaire.getCredentials().getPassword())
                )).orElseGet(GestionnaireDTO::empty);
    }

    public Optional<String> getDepartementByEmail(String email) {
        return userAppRepository.findUserAppByEmail(email)
                .flatMap(user -> {
                    switch (user.getRole()) {
                        case ETUDIANT:
                            return etudiantRepository.findByEmail(email)
                                    .map(Etudiant::getDepartement)
                                    .map(Departement::getDisplayName);
                        case PROFESSEUR:
                            return professeurRepository.findByEmail(email)
                                    .map(Professeur::getDepartement)
                                    .map(Departement::getDisplayName);
                        default:
                            return Optional.of("This user role does not have a department.");
                    }
                });
    }

    public List<Departement> getAllDepartements() {
        return Departement.getAllDepartements();
    }

}
