package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.OffreDeStage;
import com.lacouf.rsbjwt.repository.OffreDeStageRepository;
import org.springframework.stereotype.Service;

@Service
public class OffreDeStageService {

    private final OffreDeStageRepository offreDeStageRepository;

    public OffreDeStageService(OffreDeStageRepository offreDeStageRepository) {
        this.offreDeStageRepository = offreDeStageRepository;
    }

    public OffreDeStage creerOffreDeStage(OffreDeStage offre) {
        return offreDeStageRepository.save(offre);
    }
}
