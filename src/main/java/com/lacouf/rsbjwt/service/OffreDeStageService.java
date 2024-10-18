package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.Employeur;
import com.lacouf.rsbjwt.model.OffreDeStage;
import com.lacouf.rsbjwt.repository.EmployeurRepository;
import com.lacouf.rsbjwt.repository.OffreDeStageRepository;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import com.lacouf.rsbjwt.service.dto.OffreDeStageDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OffreDeStageService {

    private final OffreDeStageRepository offreDeStageRepository;
    private final EmployeurRepository employeurRepository;

    public OffreDeStageService(OffreDeStageRepository offreDeStageRepository, EmployeurRepository employeurRepository) {
        this.offreDeStageRepository = offreDeStageRepository;
        this.employeurRepository = employeurRepository;
    }

    public Optional<OffreDeStageDTO> creerOffreDeStage(OffreDeStageDTO offreDeStageDTO, Optional<Employeur> employeurOpt) {
        if (employeurOpt.isPresent()) {
            Employeur employeur = employeurOpt.get();
            try {
                OffreDeStage offreDeStage = new OffreDeStage(
                        offreDeStageDTO.getTitre(),
                        offreDeStageDTO.getLocalisation(),
                        offreDeStageDTO.getDateLimite(),
                        offreDeStageDTO.getData(),
                        offreDeStageDTO.getNbCandidats(),
                        offreDeStageDTO.getStatus()
                );
                offreDeStage.setEmployeur(employeur);
                OffreDeStage savedOffre = offreDeStageRepository.save(offreDeStage);
                return Optional.of(new OffreDeStageDTO(savedOffre));
            } catch (Exception e) {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    public Optional<OffreDeStageDTO> getOffreDeStageById(Long id) {
        return offreDeStageRepository.findById(id)
                .map(OffreDeStageDTO::new);
    }



    public String deleteOffreDeStage(Long id) {
        try {
            offreDeStageRepository.deleteById(id);
            return "Offre de stage supprimée";
        } catch (Exception e) {
            return "Erreur lors de la suppression de l'offre de stage";
        }
    }


    public Optional<OffreDeStageDTO> updateOffreDeStage(Long id, OffreDeStageDTO offreDeStageDTO) {
        return offreDeStageRepository.findById(id)
                .flatMap(offre -> {
                    offre.setTitre(offreDeStageDTO.getTitre());
                    offre.setLocalisation(offreDeStageDTO.getLocalisation());
                    offre.setDateLimite(offreDeStageDTO.getDateLimite());
                    offre.setData(offreDeStageDTO.getData());
                    offre.setNbCandidats(offreDeStageDTO.getNbCandidats());
                    OffreDeStage savedOffre = offreDeStageRepository.save(offre);
                    System.out.println("savedOffre = " + savedOffre);
                    return Optional.of(new OffreDeStageDTO(savedOffre));
                });
    }

    public Optional<List<OffreDeStageDTO>> getOffresEmployeur(Employeur employeur) {
        if (employeur.getEmail() == null || employeur.getEmail().isEmpty() || employeur.getEmail().isBlank()) {
            throw  new IllegalArgumentException("Employeur email is required");
        }
        List<OffreDeStage> offres = offreDeStageRepository.findByEmployeur(employeur);
        List<OffreDeStageDTO> offresDTO = offres.stream()
                .map(OffreDeStageDTO::new)
                .collect(Collectors.toList());
        return offresDTO.isEmpty() ? Optional.empty() : Optional.of(offresDTO);
    }

    public Iterable<OffreDeStageDTO> getAllOffresDeStage() {
        return offreDeStageRepository.findAll().stream()
                .map(OffreDeStageDTO::new)
                .toList();
    }

    public Optional<List<EtudiantDTO>> getEtudiantsByOffre(Long offreId) {

        Optional<OffreDeStage> offreOpt = offreDeStageRepository.findById(offreId);
        if (offreOpt.isEmpty()) {
            throw new IllegalArgumentException("Offre de stage introuvable");
        }


        List<EtudiantDTO> etudiants = offreOpt.get().getEtudiants().stream()
                .map(EtudiantDTO::new)
                .distinct()
                .collect(Collectors.toList());

        return etudiants.isEmpty() ? Optional.empty() : Optional.of(etudiants);
    }

}

