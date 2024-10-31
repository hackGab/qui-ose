package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.*;
import com.lacouf.rsbjwt.repository.*;
import com.lacouf.rsbjwt.service.dto.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GestionnaireService {
    private final GestionnaireRepository gestionnaireRepository;
    private final CVRepository cvRepository;
    private final EtudiantRepository etudiantRepository;
    private final PasswordEncoder passwordEncoder;
    private final ContratRepository contratRepository;

    private final OffreDeStageRepository offreDeStageRepository;
    private final EntrevueRepository entrevueRepository;

    public GestionnaireService(GestionnaireRepository gestionnaireRepository, CVRepository cvRepository, EtudiantRepository etudiantRepository , OffreDeStageRepository offreDeStageRepository, PasswordEncoder passwordEncoder, ContratRepository contratRepository, EntrevueRepository entrevueRepository) {
        this.gestionnaireRepository = gestionnaireRepository;
        this.cvRepository = cvRepository;
        this.etudiantRepository = etudiantRepository;
 	    this.offreDeStageRepository = offreDeStageRepository;
        this.passwordEncoder = passwordEncoder;
        this.contratRepository = contratRepository;
        this.entrevueRepository = entrevueRepository;
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

    public Optional<OffreDeStageDTO> validerOuRejeterOffre(Long offreId, String status, String rejectionReason) {        
    Optional<OffreDeStage> offreOptional = offreDeStageRepository.findById(offreId);

        if (offreOptional.isPresent()) {
            OffreDeStage offreDeStage = offreOptional.get();
            offreDeStage.setStatus(status);

            if ("Rejeté".equals(status)) {
                offreDeStage.setRejetMessage(rejectionReason);
            } else {
                offreDeStage.setRejetMessage("");
            }

            offreDeStageRepository.save(offreDeStage);
            return Optional.of(new OffreDeStageDTO(offreDeStage));
        }

        return Optional.empty();
    }

    public Optional<ContratDTO> creerContrat(ContratDTO contratDTO) {
        try {
            CandidatAccepter candidat = contratDTO.getCandidature().toEntity();
            Optional<Entrevue> entrevueDTO = entrevueRepository.findById(contratDTO.getCandidature().getEntrevueId());
            candidat.setEntrevue(entrevueDTO.get());

            Contrat contrat = new Contrat(
                    contratDTO.isEtudiantSigne(),
                    contratDTO.isEmployeurSigne(),
                    contratDTO.isGestionnaireSigne(),
                    contratDTO.getDateSignatureEtudiant(),
                    contratDTO.getDateSignatureEmployeur(),
                    contratDTO.getDateSignatureGestionnaire(),
                    candidat,
                    contratDTO.getCollegeEngagement(),
                    contratDTO.getDateDebut(),
                    contratDTO.getDateFin(),
                    contratDTO.getDescription(),
                    contratDTO.getEntrepriseEngagement(),
                    contratDTO.getEtudiantEngagement(),
                    contratDTO.getHeuresParSemaine(),
                    contratDTO.getHeureHorraireDebut(),
                    contratDTO.getHeureHorraireFin(),
                    contratDTO.getLieuStage(),
                    contratDTO.getNbSemaines(),
                    contratDTO.getTauxHoraire()
            );

            contrat.genererUUID();

            Contrat saveContrat = contratRepository.save(contrat);

            return Optional.of(new ContratDTO(saveContrat));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Iterable<ContratDTO> getAllContrats() {
        return contratRepository.findAll().stream()
                .map(ContratDTO::new)
                .toList();
    }

    public Optional<Contrat> getContratByUUID(String uuid) {
        return contratRepository.findByUUID(uuid);
    }
}
