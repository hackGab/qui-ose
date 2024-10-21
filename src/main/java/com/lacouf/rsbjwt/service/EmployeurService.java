package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.Employeur;
import com.lacouf.rsbjwt.model.Entrevue;
import com.lacouf.rsbjwt.model.Etudiant;
import com.lacouf.rsbjwt.repository.EmployeurRepository;
import com.lacouf.rsbjwt.repository.EntrevueRepository;
import com.lacouf.rsbjwt.repository.UserAppRepository;
import com.lacouf.rsbjwt.service.dto.EmployeurDTO;
import com.lacouf.rsbjwt.service.dto.EntrevueDTO;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeurService {

    private final EmployeurRepository employeurRepository;
    private final EntrevueRepository entrevueRepository;
    private final UserAppRepository userAppRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeurService(EmployeurRepository employeurRepository, PasswordEncoder passwordEncoder, EntrevueRepository entrevueRepository,UserAppRepository userAppRepository) {
        this.employeurRepository = employeurRepository;
        this.passwordEncoder = passwordEncoder;
        this.userAppRepository = userAppRepository;
        this.entrevueRepository = entrevueRepository;
    }

    public Optional<EmployeurDTO> creerEmployeur(EmployeurDTO employeurDTO) {
        try {
            String encodedPassword = passwordEncoder.encode(employeurDTO.getCredentials().getPassword());
            Employeur employeur = new Employeur(
                    employeurDTO.getFirstName(),
                    employeurDTO.getLastName(),
                    employeurDTO.getCredentials().getEmail(),
                    encodedPassword,
                    employeurDTO.getPhoneNumber(),
                    employeurDTO.getEntreprise()
            );
            Employeur savedEmployeur = employeurRepository.save(employeur);
            return Optional.of(new EmployeurDTO(savedEmployeur));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<EmployeurDTO> getEmployeurById(Long id) {
        return employeurRepository.findById(id)
                .map(EmployeurDTO::new);
    }


    public Optional<Employeur> findByCredentials_Email(String email) {
        return employeurRepository.findByCredentials_email(email);
    }

    public Optional<EntrevueDTO> createEntrevue(EntrevueDTO entrevueDTO, String email) {
        try {
            Etudiant etudiant = userAppRepository.findUserAppByEmail(email)
                    .map(userApp -> (Etudiant) userApp)
                    .get();
            Entrevue entrevue = new Entrevue(
                    entrevueDTO.getDateHeure(),
                    entrevueDTO.getLocation(),
                    etudiant
            );
            Entrevue savedEntrevue = entrevueRepository.save(entrevue);
            return Optional.of(new EntrevueDTO(savedEntrevue));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    public Optional<EntrevueDTO> getEntrevueById(Long id) {
        return entrevueRepository.findById(id)
                .map(EntrevueDTO::new);
    }

    public List<EntrevueDTO> getAllEntrevues() {
        return entrevueRepository.findAll().stream()
                .map(EntrevueDTO::new)
                .toList();
    }
}
