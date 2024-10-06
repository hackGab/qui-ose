package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.CV;
import com.lacouf.rsbjwt.model.Gestionnaire;
import com.lacouf.rsbjwt.repository.CVRepository;
import com.lacouf.rsbjwt.repository.EtudiantRepository;
import com.lacouf.rsbjwt.repository.GestionnaireRepository;
import com.lacouf.rsbjwt.service.dto.CVDTO;
import com.lacouf.rsbjwt.service.dto.GestionnaireDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GestionnaireService {
    private final GestionnaireRepository gestionnaireRepository;
    private final CVRepository cvRepository;
    private final EtudiantRepository etudiantRepository;
    private final PasswordEncoder passwordEncoder;


    public GestionnaireService(GestionnaireRepository gestionnaireRepository, CVRepository cvRepository, EtudiantRepository etudiantRepository ,  PasswordEncoder passwordEncoder) {
        this.gestionnaireRepository = gestionnaireRepository;
        this.cvRepository = cvRepository;
        this.etudiantRepository = etudiantRepository;
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

    public Optional<CVDTO> validerOuRejeterCV(Long cvId, String status, String rejectionReason) {
        Optional<CV> cvOptional = cvRepository.findById(cvId);

        if (cvOptional.isPresent()) {
            CV cv = cvOptional.get();
            cv.setStatus(status);

            if ("rejeté".equals(status)) {
                cv.setRejetMessage(rejectionReason);
            } else {
                cv.setRejetMessage("");
            }

            cvRepository.save(cv);
            return Optional.of(new CVDTO(cv));
        }

        return Optional.empty();
    }

}
