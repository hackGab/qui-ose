package com.lacouf.rsbjwt.presentation;

import com.lacouf.rsbjwt.service.EtudiantService;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import com.lacouf.rsbjwt.service.dto.OffreDeStageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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
    public ResponseEntity<EtudiantDTO> creerEtudiant(@RequestBody EtudiantDTO etudiantDTO) {
        try {
            Optional<EtudiantDTO> etudiantCree = etudiantService.creerEtudiant(etudiantDTO);

            if (etudiantCree.isPresent()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(etudiantCree.get());
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            System.out.println("Erreur lors de la création de l'étudiant : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
    public ResponseEntity<List<EtudiantDTO>> getAllEtudiants() {
        return ResponseEntity.ok(etudiantService.getAllEtudiants());
    }

    @PostMapping("/{etudiantEmail}/offre/{offreId}")
    public ResponseEntity<EtudiantDTO> ajouterOffreDeStage(@PathVariable String etudiantEmail, @PathVariable Long offreId) {
        Optional<EtudiantDTO> etudiantDTO = etudiantService.ajouterOffreDeStage(etudiantEmail, offreId);

        return etudiantDTO.map(etudiant -> ResponseEntity.ok().body(etudiant))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{etudiantEmail}/offres")
    public ResponseEntity<List<OffreDeStageDTO>> getOffresDeStage(@PathVariable String etudiantEmail) {
        return ResponseEntity.ok(etudiantService.getOffresDeStage(etudiantEmail));
    }

    @PutMapping("/{email}/retirerOffre/{offreId}")
    public ResponseEntity<EtudiantDTO> retirerOffreDeStage(@PathVariable String email, @PathVariable Long offreId) {
        if (email == null || offreId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        System.out.println(email + " " + offreId);

        Optional<EtudiantDTO> etudiantDTO = etudiantService.retirerOffreDeStage(email, offreId);

        return etudiantDTO.map(etudiant -> ResponseEntity.ok().body(etudiant))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    } 

    @GetMapping("/departement/{departement}")
    public ResponseEntity<List<EtudiantDTO>> getEtudiantsByDepartement(@PathVariable String departement) {
        return ResponseEntity.ok(etudiantService.getEtudiantsByDepartement(departement));
    }


}
