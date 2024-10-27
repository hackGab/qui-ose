package com.lacouf.rsbjwt.presentation;

import com.lacouf.rsbjwt.service.CandidatAccepterService;
import com.lacouf.rsbjwt.service.GestionnaireService;
import com.lacouf.rsbjwt.service.dto.ContratDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/contrat")
@CrossOrigin(origins = "http://localhost:3000")
public class  ContratController {
    private final GestionnaireService gestionnaireService;

    private final CandidatAccepterService candidatAccepterService;

    public ContratController(GestionnaireService gestionnaireService, CandidatAccepterService candidatAccepterService) {
        this.gestionnaireService = gestionnaireService;
        this.candidatAccepterService = candidatAccepterService;
    }

    @PostMapping("/creerContrat")
    public ResponseEntity<ContratDTO> creerContrat(@RequestBody ContratDTO newContrat) {
        if (newContrat == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<ContratDTO> contratCree = gestionnaireService.creerContrat(newContrat);

        // Pour déboguer
        System.out.println("Contrat créé : " + contratCree);

        return contratCree
                .map(contratDTO -> ResponseEntity.status(HttpStatus.CREATED).body(contratDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<ContratDTO>> getAllContrats() {
        Iterable<ContratDTO> contrats = gestionnaireService.getAllContrats();

        return ResponseEntity.ok().body(contrats);
    }
}
