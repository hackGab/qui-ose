package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.CV;
import com.lacouf.rsbjwt.model.Etudiant;
import com.lacouf.rsbjwt.model.UserApp;
import com.lacouf.rsbjwt.repository.CVRepository;
import com.lacouf.rsbjwt.repository.EtudiantRepository;
import com.lacouf.rsbjwt.repository.UserAppRepository;
import com.lacouf.rsbjwt.service.dto.CVDTO;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EtudiantService {

    private final UserAppRepository userAppRepository;
    private final EtudiantRepository etudiantRepository;
    private final PasswordEncoder passwordEncoder;
    private final CVRepository cvRepository;

    public EtudiantService(UserAppRepository userAppRepository, EtudiantRepository etudiantRepository, PasswordEncoder passwordEncoder, CVRepository cvRepository) {
        this.userAppRepository = userAppRepository;
        this.etudiantRepository = etudiantRepository;
        this.passwordEncoder = passwordEncoder;
        this.cvRepository = cvRepository;
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

    public Optional<CVDTO> creerCV(CVDTO cvDTO, Long idEtudiant) {
        try {
            CV cv = new CV(
                    cvDTO.getName(),
                    cvDTO.getType(),
                    cvDTO.getUploadDate(),
                    cvDTO.getData(),
                    cvDTO.getStatus()
            );

            CV savedCV = cvRepository.save(cv);
            Etudiant etudiant = etudiantRepository.findById(idEtudiant).get();
            etudiant.setCv(savedCV);
            etudiantRepository.save(etudiant);

            return Optional.of(new CVDTO(savedCV));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void supprimerCV(Long id) {
        cvRepository.deleteById(id);
    }
}
