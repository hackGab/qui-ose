package com.lacouf.rsbjwt.presentation;

import com.lacouf.rsbjwt.model.Etudiant;
import com.lacouf.rsbjwt.repository.UserAppRepository;
import com.lacouf.rsbjwt.service.EmployeurService;
import com.lacouf.rsbjwt.service.EtudiantService;
import com.lacouf.rsbjwt.service.dto.EntrevueDTO;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/entrevues")
public class EntrevueController {

    private final EmployeurService employeurService;
    private final EtudiantService etudiantService;
    private final UserAppRepository userAppRepository;

    public EntrevueController(EmployeurService employeurService, EtudiantService etudiantService,UserAppRepository userAppRepository) {
        this.employeurService = employeurService;
        this.etudiantService = etudiantService;
        this.userAppRepository = userAppRepository;
    }

    @PostMapping("/creerEntrevue/{email}/{offreId}")
    public ResponseEntity<EntrevueDTO> createEntrevue(@RequestBody EntrevueDTO entrevueDTO, @PathVariable String email, @PathVariable Long offreId) {
        Optional<EntrevueDTO> createdEntrevue = employeurService.createEntrevue(entrevueDTO, email, offreId);
        return createdEntrevue.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }


    @GetMapping("/{id}")
    public ResponseEntity<EntrevueDTO> getEntrevueById(@PathVariable Long id) {
        Optional<EntrevueDTO> entrevueOpt = employeurService.getEntrevueById(id);
        return entrevueOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/etudiant/{email}/session/{session}")
    public ResponseEntity<List<EntrevueDTO>> getEntrevuesByEtudiantAndSession(@PathVariable String email, @PathVariable String session) {
        List<EntrevueDTO> entrevues = etudiantService.getEntrevuesByEtudiantAndSession(email, session);
        return ResponseEntity.ok(entrevues);
    }

    @GetMapping("/enAttente/etudiant/{email}")
    public ResponseEntity<List<EntrevueDTO>> getEntrevuesEnAttenteByEtudiant(@PathVariable String email) {
        List<EntrevueDTO> entrevues = etudiantService.getEntrevuesEnAttenteByEtudiant(email);
        return ResponseEntity.ok(entrevues);
    }

    @GetMapping("/allEntrevue")
    public List<EntrevueDTO> getAllEntrevues() {
        return employeurService.getAllEntrevues();
    }

    @GetMapping("/offre/{offreId}")
    public ResponseEntity<List<EntrevueDTO>> getEntrevuesByOffre(@PathVariable Long offreId) {
        List<EntrevueDTO> entrevues = employeurService.getEntrevuesByOffre(offreId);
        return ResponseEntity.ok(entrevues);
    }

    @PutMapping("/changerStatus/{emailEtudiant}/{idOffreDeStage}")
    public ResponseEntity<EntrevueDTO> changerStatusEntrevue(@PathVariable String emailEtudiant, @PathVariable Long idOffreDeStage, @RequestBody String status) {
        Optional<EntrevueDTO> updatedEntrevue = etudiantService.changerStatusEntrevue(emailEtudiant, idOffreDeStage, status);
        return updatedEntrevue.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/entrevueAcceptee/offre/{offreId}")
    public ResponseEntity<List<EntrevueDTO>> getEntrevuesAccepteesByOffre(@PathVariable Long offreId) {
        List<EntrevueDTO> entrevues = employeurService.getEntrevuesAccepteesByOffre(offreId);
        System.out.println("entrevues = " + entrevues);
        return ResponseEntity.ok(entrevues);
    }

    @GetMapping("/acceptees/etudiant/{email}")
    public ResponseEntity<List<EntrevueDTO>> getEntrevuesAccepteesByEtudiant(@PathVariable String email) {
        try {
            List<EntrevueDTO> entrevues = etudiantService.getEntrevuesAccepteesByEtudiant(email);
            if (entrevues.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(entrevues);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/acceptees/employeur/{email}/session/{session}")
    public ResponseEntity<List<EntrevueDTO>> getEntrevuesAccepteesParEmployeur(@PathVariable String email, @PathVariable String session) {
        List<EntrevueDTO> entrevues = employeurService.getEntrevuesAccepteesParEmployeur(email, session);

        if (entrevues.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(entrevues);
    }
}
