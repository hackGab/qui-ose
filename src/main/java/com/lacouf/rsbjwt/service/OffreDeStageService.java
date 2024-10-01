package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.OffreDeStage;
import com.lacouf.rsbjwt.repository.OffreDeStageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OffreDeStageService {

    private final OffreDeStageRepository offreDeStageRepository;

    public OffreDeStageService(OffreDeStageRepository offreDeStageRepository) {
        this.offreDeStageRepository = offreDeStageRepository;
    }

    public OffreDeStage creerOffreDeStage(OffreDeStage offre) {
        return offreDeStageRepository.save(offre);
    }

    public List<OffreDeStage> obtenirOffresParEmployeur(Long employeurId) {
        return offreDeStageRepository.findByEmployeurId(employeurId);
    }

    public List<OffreDeStage> obtenirToutesLesOffres() {
        return offreDeStageRepository.findAll();
    }

    public void supprimerOffre(Long id) {
        offreDeStageRepository.deleteById(id);
    }

    public OffreDeStage obtenirOffreParId(Long id) {
        return offreDeStageRepository.findById(id).orElse(null);
    }
}
