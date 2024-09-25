package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.Employeur;
import com.lacouf.rsbjwt.repository.EmployeurRepository;
import com.lacouf.rsbjwt.service.dto.EmployeurDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeurService {

    private final EmployeurRepository employeurRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeurService(EmployeurRepository employeurRepository, PasswordEncoder passwordEncoder) {
        this.employeurRepository = employeurRepository;
        this.passwordEncoder = passwordEncoder;
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
}
