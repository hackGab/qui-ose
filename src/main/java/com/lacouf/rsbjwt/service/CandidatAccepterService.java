package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.CandidatAccepter;
import com.lacouf.rsbjwt.model.Entrevue;
import com.lacouf.rsbjwt.model.Notification;
import com.lacouf.rsbjwt.repository.CandidatAccepterRepository;
import com.lacouf.rsbjwt.repository.EntrevueRepository;
import com.lacouf.rsbjwt.repository.NotificationRepository;
import com.lacouf.rsbjwt.service.dto.CandidatAccepterDTO;
import com.lacouf.rsbjwt.service.dto.NotificationDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CandidatAccepterService {

    private final CandidatAccepterRepository candidatAccepterRepository;
    private final EntrevueRepository entrevueRepository;

    private final NotificationRepository notificationRepository;

    public CandidatAccepterService(CandidatAccepterRepository candidatAccepterRepository, EntrevueRepository entrevueRepository, NotificationRepository notificationRepository) {
        this.candidatAccepterRepository = candidatAccepterRepository;
        this.entrevueRepository = entrevueRepository;
        this.notificationRepository = notificationRepository;
    }

    public NotificationDTO createNotification(Notification notification) {
        Notification savedNotification = notificationRepository.save(notification);
        return new NotificationDTO(savedNotification);
    }

    // Accepter une candidature
    public Optional<CandidatAccepterDTO> accepterCandidature(Long entrevueId) {
        Optional<Entrevue> entrevueOptional = entrevueRepository.findById(entrevueId);

        if (entrevueOptional.isPresent()) {
            Entrevue entrevue = entrevueOptional.get();
            CandidatAccepter candidatAccepter = new CandidatAccepter(entrevue, true);
            CandidatAccepter savedCandidatAccepter = candidatAccepterRepository.save(candidatAccepter);

           createNotification(new Notification("Félicitations, vous avez été accepté pour le post", candidatAccepter.getEmailEtudiant(), "/stagesAppliquees"));

            return Optional.of(new CandidatAccepterDTO(savedCandidatAccepter.getId(),savedCandidatAccepter.getEntrevue().getId(), savedCandidatAccepter.isAccepte()));

        }

        return Optional.empty();
    }


    // Refuser une candidature
    public Optional<CandidatAccepterDTO> refuserCandidature(Long entrevueId) {
        Optional<Entrevue> entrevueOptional = entrevueRepository.findById(entrevueId);

        if (entrevueOptional.isPresent()) {
            Entrevue entrevue = entrevueOptional.get();
            CandidatAccepter candidatAccepter = new CandidatAccepter(entrevue, false);
            CandidatAccepter savedCandidatAccepter = candidatAccepterRepository.save(candidatAccepter);
            createNotification(new Notification("Désoler, vous n'ètes pas accepté pour le post", candidatAccepter.getEmailEtudiant(), "/stagesAppliquees"));

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
