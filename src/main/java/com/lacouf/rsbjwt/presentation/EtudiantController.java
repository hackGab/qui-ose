package com.lacouf.rsbjwt.presentation;

import com.lacouf.rsbjwt.service.EtudiantService;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import com.lacouf.rsbjwt.service.dto.OffreDeStageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/etudiant")
@CrossOrigin(origins = "http://localhost:3000")
public class EtudiantController {

    private final EtudiantService etudiantService;

    public EtudiantController(EtudiantService etudiantService) {
        this.etudiantService = etudiantService;
    }

    @PostMapping("/creerEtudiant")
    public ResponseEntity<EtudiantDTO> creerEtudiant(@RequestBody EtudiantDTO newEtudiant) {
        if (newEtudiant == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<EtudiantDTO> etudiantDTO = etudiantService.creerEtudiant(newEtudiant);

        return etudiantDTO.map(etudiant -> ResponseEntity.status(HttpStatus.CREATED).body(etudiant))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EtudiantDTO> getEtudiantById(@PathVariable Long id) {
        if(id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<EtudiantDTO> etudiantDTO = etudiantService.getEtudiantById(id);

        return etudiantDTO.map(etudiant -> ResponseEntity.ok().body(etudiant))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/credentials/{email}")
    public ResponseEntity<EtudiantDTO> getEtudiantByEmail(@PathVariable String email) {
        if(email == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<EtudiantDTO> etudiantDTO = etudiantService.getEtudiantByEmail(email);

        return etudiantDTO.map(etudiant -> ResponseEntity.ok().body(etudiant))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<EtudiantDTO>> getAllEtudiants() {
        return ResponseEntity.ok(etudiantService.getAllEtudiants());
    }

    @PostMapping("/{etudiantEmail}/offre/{offreId}")
    public ResponseEntity<EtudiantDTO> ajouterOffreDeStage(@PathVariable String etudiantEmail, @PathVariable Long offreId) {
        Optional<EtudiantDTO> etudiantDTO = etudiantService.ajouterOffreDeStage(etudiantEmail, offreId);

        return etudiantDTO.map(etudiant -> ResponseEntity.ok().body(etudiant))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @GetMapping("/{etudiantEmail}/offres")
    public ResponseEntity<Iterable<OffreDeStageDTO>> getOffresDeStage(@PathVariable String etudiantEmail) {
        return ResponseEntity.ok(etudiantService.getOffresDeStage(etudiantEmail));
    }
}
