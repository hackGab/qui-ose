package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.CandidatAccepter;
import com.lacouf.rsbjwt.model.Entrevue;
import com.lacouf.rsbjwt.repository.CandidatAccepterRepository;
import com.lacouf.rsbjwt.repository.EntrevueRepository;
import com.lacouf.rsbjwt.service.dto.CandidatAccepterDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidatAccepterService {

    private final CandidatAccepterRepository candidatAccepterRepository;
    private final EntrevueRepository entrevueRepository;

    public CandidatAccepterService(CandidatAccepterRepository candidatAccepterRepository, EntrevueRepository entrevueRepository) {
        this.candidatAccepterRepository = candidatAccepterRepository;
        this.entrevueRepository = entrevueRepository;
    }


    public Optional<CandidatAccepterDTO> accepterCandidature(Long entrevueId) {
        return handleCandidature(entrevueId, true);
    }

    public Optional<CandidatAccepterDTO> refuserCandidature(Long entrevueId) {
        return handleCandidature(entrevueId, false);
    }

    private Optional<CandidatAccepterDTO> handleCandidature(Long entrevueId, boolean accepte) {
        Optional<CandidatAccepter> existingCandidatAccepter = candidatAccepterRepository.findByEntrevueId(entrevueId);

        if (existingCandidatAccepter.isPresent()) {
            return saveCandidatAccepter(existingCandidatAccepter.get(), accepte);
        }

        Optional<Entrevue> entrevueOptional = entrevueRepository.findById(entrevueId);
        if (entrevueOptional.isPresent()) {
            CandidatAccepter newCandidatAccepter = new CandidatAccepter(entrevueOptional.get(), accepte);
            return saveCandidatAccepter(newCandidatAccepter, accepte);
        }

        return Optional.empty();
    }

    private Optional<CandidatAccepterDTO> saveCandidatAccepter(CandidatAccepter candidatAccepter, boolean accepte) {
        candidatAccepter.setAccepte(accepte);
        CandidatAccepter savedCandidatAccepter = candidatAccepterRepository.save(candidatAccepter);
        System.out.println("savedCandidatAccepter = " + savedCandidatAccepter);
        return Optional.of(new CandidatAccepterDTO(savedCandidatAccepter.getEntrevue().getId(), savedCandidatAccepter.isAccepte()));
    }


    public Optional<CandidatAccepterDTO> getCandidatureDecision(Long entrevueId) {
        Optional<CandidatAccepter> candidatAccepterOptional = candidatAccepterRepository.findByEntrevueId(entrevueId);

        Optional<CandidatAccepterDTO> decision = candidatAccepterOptional.map(candidatAccepter -> new CandidatAccepterDTO(
                candidatAccepter.getEntrevue().getId(),
                candidatAccepter.isAccepte()
        ));

        System.out.println("Decision pour entrevueId " + entrevueId + ": " + decision);
        return decision;
    }
}
