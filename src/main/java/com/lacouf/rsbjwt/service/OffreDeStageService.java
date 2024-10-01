package com.lacouf.rsbjwt.service;

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

    public Optional<OffreDeStageDTO> creerOffreDeStage(OffreDeStageDTO offreDeStageDTO) {
        try {
            OffreDeStage offreDeStage = new OffreDeStage(
               offreDeStageDTO.getTitre(),
               offreDeStageDTO.getDescription(),
               offreDeStageDTO.getResponsabilites(),
               offreDeStageDTO.getQualifications(),
               offreDeStageDTO.getDuree(),
               offreDeStageDTO.getLocalisation(),
               offreDeStageDTO.getSalaire(),
               offreDeStageDTO.getDateLimite()
            );
            OffreDeStage savedOffre = offreDeStageRepository.save(offreDeStage);
            return Optional.of(new OffreDeStageDTO(savedOffre));

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<OffreDeStageDTO> getOffreDeStageById(Long id) {
        return offreDeStageRepository.findById(id)
                .map(OffreDeStageDTO::new);  // Convertir l'entité en DTO si trouvée
    }

    public List<OffreDeStageDTO> trierParEmployeur(Long employeurId) {
        return offreDeStageRepository.findByEmployeurId(employeurId)
                .stream()
                .map(OffreDeStageDTO::new)  // Convertir chaque entité en DTO
                .collect(Collectors.toList());
    }

    public List<OffreDeStageDTO> getOffreDeStages() {
        return offreDeStageRepository.findAll()
                .stream()
                .map(OffreDeStageDTO::new)  // Convertir chaque entité en DTO
                .collect(Collectors.toList());
    }

    public void deleteOffreDeStage(Long id) {
        offreDeStageRepository.deleteById(id);
    }

    public Optional<OffreDeStageDTO> updateOffreDeStage(Long id, OffreDeStageDTO offreDeStageDTO) {
        return offreDeStageRepository.findById(id)
                .map(offre -> {
                    offre.setTitre(offreDeStageDTO.getTitre());
                    offre.setDescription(offreDeStageDTO.getDescription());
                    offre.setResponsabilites(offreDeStageDTO.getResponsabilites());
                    offre.setQualifications(offreDeStageDTO.getQualifications());
                    offre.setDuree(offreDeStageDTO.getDuree());
                    offre.setLocalisation(offreDeStageDTO.getLocalisation());
                    offre.setSalaire(offreDeStageDTO.getSalaire());
                    offre.setDateLimite(offreDeStageDTO.getDateLimite());
                    OffreDeStage savedOffre = offreDeStageRepository.save(offre);
                    return new OffreDeStageDTO(savedOffre);
                });
    }



}
