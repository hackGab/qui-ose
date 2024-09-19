package com.lacouf.rsbjwt.presentation;

import com.lacouf.rsbjwt.service.ProfesseurService;
import com.lacouf.rsbjwt.service.dto.ProfesseurDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/professeur")
@CrossOrigin(origins = "http://localhost:3000")
public class ProfesseurController {

    private final ProfesseurService professeurService;

    public ProfesseurController(ProfesseurService professeurService) {
        this.professeurService = professeurService;
    }

    @PostMapping("/creerProfesseur")
    public ResponseEntity<ProfesseurDTO> creerProfesseur(@RequestBody ProfesseurDTO newProfesseur) {
        if (newProfesseur == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<ProfesseurDTO> professeurDTO = professeurService.creerProfesseur(newProfesseur);

        return professeurDTO.map(professeur -> ResponseEntity.status(HttpStatus.CREATED).body(professeur))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfesseurDTO> getProfesseurById(@PathVariable Long id) {
        if(id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<ProfesseurDTO> professeurDTO = professeurService.getProfesseurById(id);

        return professeurDTO.map(professeur -> ResponseEntity.ok().body(professeur))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
