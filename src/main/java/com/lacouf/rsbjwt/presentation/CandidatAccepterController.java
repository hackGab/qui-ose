package com.lacouf.rsbjwt.presentation;

import com.lacouf.rsbjwt.repository.GestionnaireRepository;
import com.lacouf.rsbjwt.service.EmployeurService;
import com.lacouf.rsbjwt.service.GestionnaireService;
import com.lacouf.rsbjwt.service.dto.CandidatAccepterDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/candidatures")
@CrossOrigin(origins = "http://localhost:3000")
public class CandidatAccepterController {

    private final EmployeurService employeurService;
    private final GestionnaireRepository gestionnaireRepository;
    private final GestionnaireService gestionnaireService;

    public CandidatAccepterController(EmployeurService employeurService, GestionnaireRepository gestionnaireRepository, GestionnaireService gestionnaireService) {this.employeurService = employeurService;
        this.gestionnaireRepository = gestionnaireRepository;
        this.gestionnaireService = gestionnaireService;
    }

    // Endpoint pour accepter une candidature
    @PutMapping("/accepter/{entrevueId}")
    public ResponseEntity<CandidatAccepterDTO> accepterCandidature(@PathVariable Long entrevueId) {
        Optional<CandidatAccepterDTO> candidatAccepterOpt = employeurService.accepterCandidature(entrevueId);

        return candidatAccepterOpt
                .map(candidatAccepter -> ResponseEntity.ok().body(candidatAccepter))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Endpoint pour refuser une candidature
    @PutMapping("/refuser/{entrevueId}")
    public ResponseEntity<CandidatAccepterDTO> refuserCandidature(@PathVariable Long entrevueId) {
        Optional<CandidatAccepterDTO> candidatAccepterOpt = employeurService.refuserCandidature(entrevueId);

        return candidatAccepterOpt
                .map(candidatAccepter -> ResponseEntity.ok().body(candidatAccepter))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Endpoint pour récupérer la décision d'une entrevue
    @GetMapping("/{entrevueId}")
    public ResponseEntity<CandidatAccepterDTO> getCandidatureDecision(@PathVariable Long entrevueId) {
        Optional<CandidatAccepterDTO> decisionOpt = employeurService.getCandidatureDecision(entrevueId);

        if (decisionOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return decisionOpt
                .map(decision -> ResponseEntity.ok().body(decision))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<CandidatAccepterDTO>> getAllCandidatures() {
        return ResponseEntity.ok().body(gestionnaireService.getAllCandidatures());
    }

    @GetMapping("/session/{session}")
    public ResponseEntity<List<CandidatAccepterDTO>> getCandidaturesBySession(@PathVariable String session) {
        return ResponseEntity.ok().body(gestionnaireService.getCandidaturesBySession(session));
    }
}
