package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.Professeur;
import com.lacouf.rsbjwt.repository.ProfesseurRepository;
import com.lacouf.rsbjwt.service.dto.ProfesseurDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfesseurService {
    private final ProfesseurRepository professeurRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfesseurService(ProfesseurRepository professeurRepository, PasswordEncoder passwordEncoder) {
        this.professeurRepository = professeurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<ProfesseurDTO> creerProfesseur(ProfesseurDTO professeurDTO) {
        try {
            String encodedPassword = passwordEncoder.encode(professeurDTO.getCredentials().getPassword());
            Professeur professeur = new Professeur(
                    professeurDTO.getFirstName(),
                    professeurDTO.getLastName(),
                    professeurDTO.getCredentials().getEmail(),
                    encodedPassword,
                    professeurDTO.getPhoneNumber(),
                    professeurDTO.getDepartement()
            );
            Professeur savedProfesseur = professeurRepository.save(professeur);
            return Optional.of(new ProfesseurDTO(savedProfesseur));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<ProfesseurDTO> getProfesseurById(Long id) {
        return professeurRepository.findById(id)
                .map(ProfesseurDTO::new);
    }
}
