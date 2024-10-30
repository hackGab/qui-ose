package com.lacouf.rsbjwt.presentation;

import com.lacouf.rsbjwt.service.CandidatAccepterService;
import com.lacouf.rsbjwt.service.EmployeurService;
import com.lacouf.rsbjwt.service.GestionnaireService;
import com.lacouf.rsbjwt.service.dto.ContratDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/contrat")
@CrossOrigin(origins = "http://localhost:3000")
public class  ContratController {
    private final GestionnaireService gestionnaireService;

    private final EmployeurService employeurService;

    private final CandidatAccepterService candidatAccepterService;

    public ContratController(GestionnaireService gestionnaireService, CandidatAccepterService candidatAccepterService, EmployeurService employeurService) {
        this.gestionnaireService = gestionnaireService;
        this.candidatAccepterService = candidatAccepterService;
        this.employeurService = employeurService;
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


    @GetMapping("/getContrats-employeur/{employeurEmail}")
    public ResponseEntity<Iterable<ContratDTO>> getContratEmployeur(@PathVariable String employeurEmail) {
        ArrayList<ContratDTO> contrats = new ArrayList<>(employeurService.getContratEmployeur(employeurEmail));


        return ResponseEntity.ok().body(contrats);
    }

    // endpoint pour faire signe l employeur
    @PutMapping("/signer-employer/{uuid}")
    public ResponseEntity<ContratDTO> signerContrat(@PathVariable String uuid) {
        Optional<ContratDTO> contratSigne = employeurService.signerContrat(uuid);

        return contratSigne
                .map(contratDTO -> ResponseEntity.ok().body(contratDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @GetMapping("/all")
    public ResponseEntity<Iterable<ContratDTO>> getAllContrats() {
        Iterable<ContratDTO> contrats = gestionnaireService.getAllContrats();

        return ResponseEntity.ok().body(contrats);
    }
}
