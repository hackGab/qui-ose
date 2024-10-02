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
        Optional<Employeur> employeurOpt = employeurService.findByEmail(email);

        if (employeurOpt.isPresent()) {
            Employeur employeur = employeurOpt.get();
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



                // Associer l'employeur à l'offre de stage
                offreDeStage.setEmployeur(employeur);// Assurez-vous que cette méthode existe dans votre entité OffreDeStage
                offreDeStage.setStatus("En attente");
                offreDeStage.setCheked(false);
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

    public List<OffreDeStageDTO> trierParEmployeur(Long employeurId) {
        List<OffreDeStageDTO> result = offreDeStageRepository.findByEmployeurId(employeurId)
                .stream()
                .map(OffreDeStageDTO::new)  // Convertir chaque entité en DTO
                .collect(Collectors.toList());
        return result;  // Ne pas envelopper dans Optional
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
