package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.Etudiant;
import com.lacouf.rsbjwt.repository.EtudiantRepository;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EtudiantService {
    private final EtudiantRepository etudiantRepository;

    public EtudiantService(EtudiantRepository etudiantRepository) {
        this.etudiantRepository = etudiantRepository;
    }

    public Optional<EtudiantDTO> creerEtudiant(EtudiantDTO etudiantDTO) {
        if (etudiantDTO == null ||
                etudiantDTO.getCredentials() == null ||
                etudiantDTO.getCredentials().getPassword() == null ||
                !etudiantDTO.getCredentials().getPassword().equals(etudiantDTO.getCredentials().getPasswordConfirm())) {
            return Optional.empty();
        }

        Etudiant etudiant = new Etudiant(
                etudiantDTO.getFirstName(),
                etudiantDTO.getLastName(),
                etudiantDTO.getCredentials().getEmail(),
                etudiantDTO.getCredentials().getPassword(),
                etudiantDTO.getPhoneNumber()
        );

        return Optional.of(toEtudiantDTO(etudiantRepository.save(etudiant)));
    }

    public EtudiantDTO toEtudiantDTO(Etudiant etudiant) {
        return new EtudiantDTO(etudiant);
    }
}

