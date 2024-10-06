package com.lacouf.rsbjwt.presentation;

import com.lacouf.rsbjwt.model.Employeur;
import com.lacouf.rsbjwt.service.EmployeurService;
import com.lacouf.rsbjwt.service.OffreDeStageService;
import com.lacouf.rsbjwt.service.dto.OffreDeStageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/offreDeStage")
@CrossOrigin(origins = "http://localhost:3000")
public class OffreDeStageController {

    private final OffreDeStageService offreDeStageService;
    private final EmployeurService employeurService;

    public OffreDeStageController(OffreDeStageService offreDeStageService, EmployeurService employeurService) {
        this.offreDeStageService = offreDeStageService;
        this.employeurService = employeurService;
    }

    @PostMapping("/creerOffreDeStage/{email}")
    public ResponseEntity<OffreDeStageDTO> creerOffreDeStage(
            @PathVariable String email,
            @RequestBody OffreDeStageDTO newOffreDeStageDTO) {
        if (newOffreDeStageDTO == null || email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<Employeur> employeurOpt = employeurService.findByCredentials_Email(email);
        Optional<OffreDeStageDTO> offreDeStageDTO = offreDeStageService.creerOffreDeStage(newOffreDeStageDTO, employeurOpt);

        return offreDeStageDTO
                .map(offreDeStage -> ResponseEntity.status(HttpStatus.CREATED).body(offreDeStage))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @GetMapping("/offresEmployeur/{email}")
    public ResponseEntity<List<OffreDeStageDTO>> getOffresEmployeur(@PathVariable String email) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<Employeur> employeurOpt = employeurService.findByCredentials_Email(email);
        if (employeurOpt.isPresent()) {
            Optional<List<OffreDeStageDTO>> offreDeStageDTOList = offreDeStageService.getOffresEmployeur(employeurOpt.get());

            return offreDeStageDTOList.map(offreDeStageList -> ResponseEntity.ok().body(offreDeStageList))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffreDeStage(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        offreDeStageService.deleteOffreDeStage(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<OffreDeStageDTO> updateOffreDeStage(@PathVariable Long id, @RequestBody OffreDeStageDTO offreDeStageDTO) {
        if (id == null || offreDeStageDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<OffreDeStageDTO> offreDeStageDTOUpdated = offreDeStageService.updateOffreDeStage(id, offreDeStageDTO);

        return offreDeStageDTOUpdated.map(offreDeStage -> ResponseEntity.ok().body(offreDeStage))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OffreDeStageDTO> getOffreDeStageById(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<OffreDeStageDTO> offreDeStageDTO = offreDeStageService.getOffreDeStageById(id);

        return offreDeStageDTO.map(offreDeStage -> ResponseEntity.ok().body(offreDeStage))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/tous")
    public ResponseEntity<List<OffreDeStageDTO>> getOffreDeStages() {
        Optional<List<OffreDeStageDTO>> offreDeStageDTOList = offreDeStageService.getOffreDeStages();

        return offreDeStageDTOList.map(offreDeStageList -> ResponseEntity.ok().body(offreDeStageList))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}

