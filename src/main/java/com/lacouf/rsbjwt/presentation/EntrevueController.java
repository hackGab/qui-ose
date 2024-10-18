package com.lacouf.rsbjwt.presentation;

import com.lacouf.rsbjwt.model.Etudiant;
import com.lacouf.rsbjwt.service.EmployeurService;
import com.lacouf.rsbjwt.service.EtudiantService;
import com.lacouf.rsbjwt.service.dto.EntrevueDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/entrevues")
public class EntrevueController {

    private final EmployeurService employeurService;
    private final EtudiantService etudiantService;

    public EntrevueController(EmployeurService employeurService, EtudiantService etudiantService) {
        this.employeurService = employeurService;
        this.etudiantService = etudiantService;
    }

    @PostMapping("/creerEntrevue/{email}")
    public ResponseEntity<EntrevueDTO> createEntrevue(@RequestBody EntrevueDTO entrevueDTO, @PathVariable String email) {
        Optional<EntrevueDTO> createdEntrevue = employeurService.createEntrevue(entrevueDTO, email);
        return createdEntrevue.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntrevueDTO> getEntrevueById(@PathVariable Long id) {
        Optional<EntrevueDTO> entrevueOpt = employeurService.getEntrevueById(id);
        return entrevueOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/allEntrevue")
    public List<EntrevueDTO> getAllEntrevues() {
        return employeurService.getAllEntrevues();
    }
}
