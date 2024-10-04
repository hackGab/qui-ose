package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.Employeur;
import com.lacouf.rsbjwt.model.OffreDeStage;
import com.lacouf.rsbjwt.repository.OffreDeStageRepository;
import com.lacouf.rsbjwt.service.dto.OffreDeStageDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OffreDeStageService {

    private final OffreDeStageRepository offreDeStageRepository;

    public OffreDeStageService(OffreDeStageRepository offreDeStageRepository) {
        this.offreDeStageRepository = offreDeStageRepository;
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


    public Optional<List<OffreDeStageDTO>> getOffreDeStages() {
        List<OffreDeStageDTO> result = offreDeStageRepository.findAll()
                .stream()
                .map(OffreDeStageDTO::new)
                .collect(Collectors.toList());
        return result.isEmpty() ? Optional.empty() : Optional.of(result);
    }

    public void deleteOffreDeStage(Long id) {
        offreDeStageRepository.deleteById(id);
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
                    return Optional.of(new OffreDeStageDTO(savedOffre));
                });
    }
}
