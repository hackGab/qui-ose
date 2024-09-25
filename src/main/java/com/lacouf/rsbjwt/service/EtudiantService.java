package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.Etudiant;
import com.lacouf.rsbjwt.repository.EtudiantRepository;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EtudiantService {

    private final EtudiantRepository etudiantRepository;
    private final PasswordEncoder passwordEncoder;

    public EtudiantService(EtudiantRepository etudiantRepository, PasswordEncoder passwordEncoder) {
        this.etudiantRepository = etudiantRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<EtudiantDTO> creerEtudiant(EtudiantDTO etudiantDTO) {
        try {
            String encodedPassword = passwordEncoder.encode(etudiantDTO.getCredentials().getPassword());
            Etudiant etudiant = new Etudiant(
                    etudiantDTO.getFirstName(),
                    etudiantDTO.getLastName(),
                    etudiantDTO.getCredentials().getEmail(),
                    encodedPassword,
                    etudiantDTO.getPhoneNumber(),
                    etudiantDTO.getDepartement()
            );
            Etudiant savedEtudiant = etudiantRepository.save(etudiant);
            return Optional.of(new EtudiantDTO(savedEtudiant));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<EtudiantDTO> getEtudiantById(Long id) {
        return etudiantRepository.findById(id)
                .map(EtudiantDTO::new);
    }
}
