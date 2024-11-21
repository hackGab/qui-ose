package com.lacouf.rsbjwt.presentation;

import com.lacouf.rsbjwt.service.EmployeurService;
import com.lacouf.rsbjwt.service.dto.EmployeurDTO;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import com.lacouf.rsbjwt.service.dto.EvaluationStageEmployeurDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employeur")
@CrossOrigin(origins = "http://localhost:3000")
public class EmployeurController {

    private final EmployeurService employeurService;

    public EmployeurController(EmployeurService employeurService) {
        this.employeurService = employeurService;
    }

    @PostMapping("/creerEmployeur")
    public ResponseEntity<EmployeurDTO> creerEmployeur(@RequestBody EmployeurDTO newEmployeur) {
        if (newEmployeur == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<EmployeurDTO> employeurDTO = employeurService.creerEmployeur(newEmployeur);

        return employeurDTO.map(employeur -> ResponseEntity.status(HttpStatus.CREATED).body(employeur))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeurDTO> getEmployeurById(@PathVariable Long id) {
        if(id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<EmployeurDTO> employeurDTO = employeurService.getEmployeurById(id);

        return employeurDTO.map(employeur -> ResponseEntity.ok().body(employeur))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/creerEvaluationEtudiant/{emailEmployeur}/{emailEtudiant}")
    public ResponseEntity<EvaluationStageEmployeurDTO> creerEvaluationEtudiant(
            @PathVariable String emailEmployeur,
            @PathVariable String emailEtudiant,
            @RequestBody EvaluationStageEmployeurDTO evaluationStageEmployeur) {

        if (emailEmployeur == null || emailEmployeur.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (emailEtudiant == null || emailEtudiant.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (evaluationStageEmployeur == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Optional<EvaluationStageEmployeurDTO> evaluationStageEmployeurDTO = employeurService.creerEvaluationEtudiant(
                emailEmployeur, emailEtudiant, evaluationStageEmployeur);

        return evaluationStageEmployeurDTO.map(evaluation -> ResponseEntity.status(HttpStatus.CREATED).body(evaluation))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @GetMapping("/evaluationEmployeur/{emailEmployeur}/{emailEtudiant}")
    public ResponseEntity<EvaluationStageEmployeurDTO> getEvaluationEtudiant(
            @PathVariable String emailEmployeur,
            @PathVariable String emailEtudiant) {

        if (emailEmployeur == null || emailEmployeur.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (emailEtudiant == null || emailEtudiant.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Optional<EvaluationStageEmployeurDTO> evaluationStageEmployeurDTO = employeurService.getEvaluationEtudiant(
                emailEmployeur, emailEtudiant);

        return evaluationStageEmployeurDTO.map(evaluation -> ResponseEntity.ok().body(evaluation))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }



    @GetMapping("/evaluationEmployeur/all")
    public ResponseEntity<Iterable<EvaluationStageEmployeurDTO>> getAllEvaluations() {
        List<EvaluationStageEmployeurDTO> evaluations = employeurService.getAllEvaluations();
        return ResponseEntity.ok().body(evaluations);
    }
}
