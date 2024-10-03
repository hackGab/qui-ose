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
    private final EmployeurService employeurService;

    public OffreDeStageService(OffreDeStageRepository offreDeStageRepository, EmployeurService employeurService) {
        this.offreDeStageRepository = offreDeStageRepository;
        this.employeurService = employeurService;
    }

    public Optional<OffreDeStageDTO> creerOffreDeStage(OffreDeStageDTO offreDeStageDTO, String email) {
        Optional<Employeur> employeurOpt = employeurService.findByCredentials_Email(email);

        if (employeurOpt.isPresent()) {
            Employeur employeur = employeurOpt.get();
            try {
                OffreDeStage offreDeStage = new OffreDeStage(
                        offreDeStageDTO.getTitre(),
                        offreDeStageDTO.getDescription(),
                        offreDeStageDTO.getDuree(),
                        offreDeStageDTO.getLocalisation(),
                        offreDeStageDTO.getExigences(),
                        offreDeStageDTO.getDateDebutSouhaitee(),
                        offreDeStageDTO.getTypeRemuneration(),
                        offreDeStageDTO.getSalaire(),
                        offreDeStageDTO.getDisponibilite(),
                        offreDeStageDTO.getDateLimite(),
                        offreDeStageDTO.getQualification(),
                        offreDeStageDTO.getContactInfo()

                );

                // Associer l'employeur à l'offre de stage
                offreDeStage.setEmployeur(employeur);
                OffreDeStage savedOffre = offreDeStageRepository.save(offreDeStage);
                return Optional.of(new OffreDeStageDTO(savedOffre));

            } catch (Exception e) {
                return Optional.empty();
            }
        } else {
            return Optional.empty(); // Employeur non trouvé
        }
    }

    public Optional<OffreDeStageDTO> getOffreDeStageById(Long id) {
        return offreDeStageRepository.findById(id)
                .map(OffreDeStageDTO::new);  // Convertir l'entité en DTO si trouvée
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
                    offre.setDescription(offreDeStageDTO.getDescription());
                    offre.setDuree(offreDeStageDTO.getDuree());
                    offre.setLocalisation(offreDeStageDTO.getLocalisation());
                    offre.setExigences(offreDeStageDTO.getExigences());
                    offre.setDateDebutSouhaitee(offreDeStageDTO.getDateDebutSouhaitee());
                    offre.setTypeRemuneration(offreDeStageDTO.getTypeRemuneration());
                    offre.setSalaire(offreDeStageDTO.getSalaire());
                    offre.setDisponibilite(offreDeStageDTO.getDisponibilite());
                    offre.setDateLimite(offreDeStageDTO.getDateLimite());
                    offre.setQualification(offreDeStageDTO.getQualification());
                    OffreDeStage savedOffre = offreDeStageRepository.save(offre);
                    return Optional.of(new OffreDeStageDTO(savedOffre));
                });
    }
}
