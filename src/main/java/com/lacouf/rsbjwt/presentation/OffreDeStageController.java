package com.lacouf.rsbjwt.presentation;

import com.lacouf.rsbjwt.service.OffreDeStageService;
import com.lacouf.rsbjwt.service.dto.OffreDeStageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/offreDeStage")
@CrossOrigin(origins = "http://localhost:3000")
public class OffreDeStageController {

    private final OffreDeStageService offreDeStageService;

    public OffreDeStageController(OffreDeStageService offreDeStageService) {
        this.offreDeStageService = offreDeStageService;
    }

    @PostMapping("/creerOffreDeStage")
    public ResponseEntity<OffreDeStageDTO> creerEmployeur(@RequestBody OffreDeStageDTO newOffreDeStageDTO) {
        if (newOffreDeStageDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<OffreDeStageDTO> offreDeStageDTO = offreDeStageService.creerOffreDeStage(newOffreDeStageDTO);

        return offreDeStageDTO.map(offreDeStage -> ResponseEntity.status(HttpStatus.CREATED).body(offreDeStage))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<OffreDeStageDTO> deleteOffreDeStage(@PathVariable Long id) {
        if(id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        offreDeStageService.deleteOffreDeStage(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<OffreDeStageDTO> updateOffreDeStage(@PathVariable Long id, @RequestBody OffreDeStageDTO offreDeStageDTO) {
        if(id == null || offreDeStageDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<OffreDeStageDTO> offreDeStageDTOUpdated = offreDeStageService.updateOffreDeStage(id, offreDeStageDTO);

        return offreDeStageDTOUpdated.map(offreDeStage -> ResponseEntity.ok().body(offreDeStage))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @GetMapping("/{id}")
    public ResponseEntity<OffreDeStageDTO> getOffreDeStageById(@PathVariable Long id) {
        // TODO: Check if id is null... Faut véfirifier si le id existe bien dans la bd..
        if(id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<OffreDeStageDTO> offreDeStageDTO = offreDeStageService.getOffreDeStageById(id);

        return offreDeStageDTO.map(offreDeStage -> ResponseEntity.ok().body(offreDeStage))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /*@GetMapping("/tous")
    public ResponseEntity<OffreDeStageDTO> getOffreDeStages() {
        Optional<OffreDeStageDTO> offreDeStageDTO = offreDeStageService.getOffreDeStages();

        return offreDeStageDTO.map(offreDeStage -> ResponseEntity.ok().body(offreDeStage))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    // Liste des fonctions de tries
    @GetMapping("/trierParEmployeur/{id}")
    public ResponseEntity<OffreDeStageDTO> trierParEmployeur(@PathVariable Long id) {
        if(id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<OffreDeStageDTO> offreDeStageDTO = offreDeStageService.trierParEmployeur(id);

        return offreDeStageDTO.map(offreDeStage -> ResponseEntity.ok().body(offreDeStage))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/trierParDuréeStage/{nbSemaines}")
    public ResponseEntity<OffreDeStageDTO> trierParDureeStage(@PathVariable Long nbSemaines) {
        Optional<OffreDeStageDTO> offreDeStageDTO = offreDeStageService.trierParDureeStage(nbSemaines);

        return offreDeStageDTO.map(offreDeStage -> ResponseEntity.ok().body(offreDeStage))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @GetMapping("/trierParSalaire/{salaire}")
    public ResponseEntity<OffreDeStageDTO> trierParSalaire(@PathVariable Long salaire) {
        Optional<OffreDeStageDTO> offreDeStageDTO = offreDeStageService.trierParSalaire(salaire);

        return offreDeStageDTO.map(offreDeStage -> ResponseEntity.ok().body(offreDeStage))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @GetMapping("/trierParDispo/{dispo}")
    public ResponseEntity<OffreDeStageDTO> trierParDispo(@PathVariable String dispo) {
        if (dispo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<OffreDeStageDTO> offreDeStageDTO = offreDeStageService.trierParDispo(dispo);

        return offreDeStageDTO.map(offreDeStage -> ResponseEntity.ok().body(offreDeStage))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @GetMapping("/trierParLocalisation/{localisation}")
    public ResponseEntity<OffreDeStageDTO> trierParLocalisation(@PathVariable String localisation) {
        if (localisation == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<OffreDeStageDTO> offreDeStageDTO = offreDeStageService.trierParLocalisation(localisation);

        return offreDeStageDTO.map(offreDeStage -> ResponseEntity.ok().body(offreDeStage))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }*/
}
