package com.lacouf.rsbjwt.presentation;

import com.lacouf.rsbjwt.service.CandidatAccepterService;
import com.lacouf.rsbjwt.service.dto.CandidatAccepterDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/candidatures")
@CrossOrigin(origins = "http://localhost:3000")
public class CandidatAccepterController {

    private final CandidatAccepterService candidatAccepterService;

    public CandidatAccepterController(CandidatAccepterService candidatAccepterService) {
        this.candidatAccepterService = candidatAccepterService;
    }

    // Endpoint pour accepter une candidature
    @PutMapping("/accepter/{entrevueId}")
    public ResponseEntity<CandidatAccepterDTO> accepterCandidature(@PathVariable Long entrevueId) {
        Optional<CandidatAccepterDTO> candidatAccepterOpt = candidatAccepterService.accepterCandidature(entrevueId);

        return candidatAccepterOpt
                .map(candidatAccepter -> ResponseEntity.ok().body(candidatAccepter))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Endpoint pour refuser une candidature
    @PutMapping("/refuser/{entrevueId}")
    public ResponseEntity<CandidatAccepterDTO> refuserCandidature(@PathVariable Long entrevueId) {
        Optional<CandidatAccepterDTO> candidatAccepterOpt = candidatAccepterService.refuserCandidature(entrevueId);

        return candidatAccepterOpt
                .map(candidatAccepter -> ResponseEntity.ok().body(candidatAccepter))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Endpoint pour récupérer la décision d'une entrevue
    @GetMapping("/{entrevueId}")
    public ResponseEntity<CandidatAccepterDTO> getCandidatureDecision(@PathVariable Long entrevueId) {
        Optional<CandidatAccepterDTO> decisionOpt = candidatAccepterService.getCandidatureDecision(entrevueId);

        if (decisionOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return decisionOpt
                .map(decision -> ResponseEntity.ok().body(decision))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
