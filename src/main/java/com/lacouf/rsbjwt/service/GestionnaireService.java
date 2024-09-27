package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.Gestionnaire;
import com.lacouf.rsbjwt.repository.GestionnaireRepository;
import com.lacouf.rsbjwt.service.dto.GestionnaireDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GestionnaireService {
    private final GestionnaireRepository gestionnaireRepository;

    private final PasswordEncoder passwordEncoder;

    public GestionnaireService(GestionnaireRepository gestionnaireRepository, PasswordEncoder passwordEncoder) {
        this.gestionnaireRepository = gestionnaireRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<GestionnaireDTO> creerGestionnaire(GestionnaireDTO gestionnaireDTO) {
        try {
            String encodedPassword = passwordEncoder.encode(gestionnaireDTO.getCredentials().getPassword());
            Gestionnaire gestionnaire = new Gestionnaire(
                    gestionnaireDTO.getFirstName(),
                    gestionnaireDTO.getLastName(),
                    gestionnaireDTO.getCredentials().getEmail(),
                    encodedPassword,
                    gestionnaireDTO.getPhoneNumber()
            );
            Gestionnaire saveGestionnaire = gestionnaireRepository.save(gestionnaire);
            return Optional.of(new GestionnaireDTO(saveGestionnaire));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
