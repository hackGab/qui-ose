package com.lacouf.rsbjwt.presentation;

import com.lacouf.rsbjwt.model.Departement;
import com.lacouf.rsbjwt.service.EtudiantService;
import com.lacouf.rsbjwt.service.ProfesseurService;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import com.lacouf.rsbjwt.service.dto.ProfesseurDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/professeur")
@CrossOrigin(origins = "http://localhost:3000")
public class ProfesseurController {

    private final ProfesseurService professeurService;
    private final EtudiantService etudiantService;

    public ProfesseurController(ProfesseurService professeurService, EtudiantService etudiantService) {
        this.professeurService = professeurService;
        this.etudiantService = etudiantService;
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

    @GetMapping("/all")
    public ResponseEntity<Iterable<ProfesseurDTO>> getAllProfesseurs() {
        return ResponseEntity.ok(professeurService.getAllProfesseurs());
    }

    @PostMapping("/assignerEtudiants/{professeurEmail}")
    public ResponseEntity<ProfesseurDTO> assignerEtudiants(
            @PathVariable String professeurEmail,
            @RequestBody List<String> etudiantsEmails) {

        if (professeurEmail == null || etudiantsEmails == null || etudiantsEmails.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<ProfesseurDTO> professeurDTO = professeurService.assignerEtudiants(professeurEmail, etudiantsEmails);

        return professeurDTO.map(professeur -> ResponseEntity.ok().body(professeur))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/etudiants/{professeurEmail}")
    public ResponseEntity<List<EtudiantDTO>> getEtudiants(@PathVariable String professeurEmail) {
        if(professeurEmail == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<EtudiantDTO> etudiants = professeurService.getEtudiants(professeurEmail);

        return ResponseEntity.ok().body(etudiants);
    }

    @GetMapping("/etudiants/departement/{departement}")
    public ResponseEntity<List<EtudiantDTO>> getEtudiantsByDepartement(@PathVariable String departement) {
        System.out.println("Departement: " + departement);
//        if(departement == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }

        Departement departementEnum = Departement.valueOf(departement);
//
        List<EtudiantDTO> etudiants = etudiantService.getEtudiantsAvecContratByDepartement(departementEnum);
//
        return ResponseEntity.ok().body(etudiants);
    }


}
