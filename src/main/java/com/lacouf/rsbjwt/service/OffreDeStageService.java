package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.Employeur;
import com.lacouf.rsbjwt.model.OffreDeStage;
import com.lacouf.rsbjwt.repository.EmployeurRepository;
import com.lacouf.rsbjwt.repository.EntrevueRepository;
import com.lacouf.rsbjwt.repository.OffreDeStageRepository;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import com.lacouf.rsbjwt.service.dto.OffreDeStageDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public String deleteOffreDeStage(Long id) {
        try {
            offreDeStageRepository.deleteById(id);
            return "Offre de stage supprimée";
        } catch (Exception e) {
            return "Erreur lors de la suppression de l'offre de stage";
        }
    }

    public List<OffreDeStageDTO> getOffresEmployeurSession(Employeur employeur, String session) {
        List<OffreDeStage> offres = offreDeStageRepository.findByEmployeurAndSession(employeur, session);
        List<OffreDeStageDTO> offresDTO = offres.stream()
                .map(OffreDeStageDTO::new)
                .collect(Collectors.toList());
        return offresDTO;
    }

    public List<OffreDeStageDTO> getOffresBySession(String session) {
        List<OffreDeStageDTO> offres = offreDeStageRepository.findBySession(session)
                .stream()
                .map(OffreDeStageDTO::new)
                .collect(Collectors.toList());

        return offres;
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

    public int getNombreOffresEnAttente() {
        List<OffreDeStage> offres = offreDeStageRepository.findAll();
        List<OffreDeStage> offresEnAttente = offres.stream()
                .filter(offre -> offre.getStatus().equals("Attente"))
                .toList();
        return offresEnAttente.size();
    }
}

