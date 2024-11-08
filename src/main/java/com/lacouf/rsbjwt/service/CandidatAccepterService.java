package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.CandidatAccepter;
import com.lacouf.rsbjwt.model.Entrevue;
import com.lacouf.rsbjwt.repository.CandidatAccepterRepository;
import com.lacouf.rsbjwt.repository.EntrevueRepository;
import com.lacouf.rsbjwt.service.dto.CandidatAccepterDTO;
import org.springframework.stereotype.Service;

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
        Optional<Entrevue> entrevueOptional = entrevueRepository.findById(entrevueId);

        if (entrevueOptional.isPresent()) {
            Entrevue entrevue = entrevueOptional.get();
            CandidatAccepter candidatAccepter = new CandidatAccepter(entrevue, true);
            CandidatAccepter savedCandidatAccepter = candidatAccepterRepository.save(candidatAccepter);
            return Optional.of(new CandidatAccepterDTO(savedCandidatAccepter.getId(),savedCandidatAccepter.getEntrevue().getId(), savedCandidatAccepter.isAccepte()));
        }

        return Optional.empty();
    }

    public Optional<CandidatAccepterDTO> refuserCandidature(Long entrevueId) {
        Optional<Entrevue> entrevueOptional = entrevueRepository.findById(entrevueId);

        if (entrevueOptional.isPresent()) {
            Entrevue entrevue = entrevueOptional.get();
            CandidatAccepter candidatAccepter = new CandidatAccepter(entrevue, false);
            CandidatAccepter savedCandidatAccepter = candidatAccepterRepository.save(candidatAccepter);
            return Optional.of(new CandidatAccepterDTO(savedCandidatAccepter.getId(), savedCandidatAccepter.getEntrevue().getId(), savedCandidatAccepter.isAccepte()));
        }

        return Optional.empty();
    }

    public Optional<CandidatAccepterDTO> getCandidatureDecision(Long entrevueId) {
        Optional<CandidatAccepter> candidatAccepterOptional = candidatAccepterRepository.findByEntrevueId(entrevueId);

        return candidatAccepterOptional.map(candidatAccepter -> new CandidatAccepterDTO(
                candidatAccepter.getId(),
                candidatAccepter.getEntrevue().getId(),
                candidatAccepter.isAccepte()
        ));
    }

    public Iterable<CandidatAccepterDTO> getAllCandidatures() {
        return candidatAccepterRepository.findAll().stream()
                .map(candidatAccepter -> new CandidatAccepterDTO(
                        candidatAccepter.getId(),
                        candidatAccepter.getEntrevue().getId(),
                        candidatAccepter.isAccepte()
                ))
                .toList();
    }
}
