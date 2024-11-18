package com.lacouf.rsbjwt.presentation;

import com.lacouf.rsbjwt.model.Employeur;
import com.lacouf.rsbjwt.service.EmployeurService;
import com.lacouf.rsbjwt.service.EtudiantService;
import com.lacouf.rsbjwt.service.OffreDeStageService;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
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
    private final EtudiantService etudiantService;

    public OffreDeStageController(OffreDeStageService offreDeStageService, EmployeurService employeurService, EtudiantService etudiantService) {
        this.offreDeStageService = offreDeStageService;
        this.employeurService = employeurService;
        this.etudiantService = etudiantService;
    }

    @PostMapping("/creerOffreDeStage/{email}")
    public ResponseEntity<OffreDeStageDTO> creerOffreDeStage(
            @PathVariable String email,
            @RequestBody OffreDeStageDTO newOffreDeStageDTO) {
        if (newOffreDeStageDTO == null || email == null || email.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<Employeur> employeurOpt = employeurService.findByCredentials_Email(email);
        Optional<OffreDeStageDTO> offreDeStageDTO = offreDeStageService.creerOffreDeStage(newOffreDeStageDTO, employeurOpt);

        return offreDeStageDTO
                .map(offreDeStage -> ResponseEntity.status(HttpStatus.CREATED).body(offreDeStage))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @GetMapping("/offresEmployeur/{email}/session/{session}")
    public ResponseEntity<List<OffreDeStageDTO>> getOffresEmployeur(@PathVariable String email, @PathVariable String session) {
        if (email == null || email.isEmpty() || session == null || session.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<Employeur> employeurOpt = employeurService.findByCredentials_Email(email);
        if (employeurOpt.isPresent()) {
            List<OffreDeStageDTO> offreDeStageDTOList = offreDeStageService.getOffresEmployeurSession(employeurOpt.get(), session);

            return ResponseEntity.ok().body(offreDeStageDTOList);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffreDeStage(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String responseMessage = offreDeStageService.deleteOffreDeStage(id);


        if ("Offre de stage supprimée".equals(responseMessage)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Handle deletion error
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<OffreDeStageDTO> updateOffreDeStage(@PathVariable Long id, @RequestBody OffreDeStageDTO offreDeStageDTO) {
        System.out.println("id = " + id);


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

    @GetMapping("/all")
    public ResponseEntity<Iterable<OffreDeStageDTO>> getAllOffresDeStage() {
        return ResponseEntity.ok(offreDeStageService.getAllOffresDeStage());
    }

    @GetMapping("/session/{session}")
    public ResponseEntity<List<OffreDeStageDTO>> getOffresBySession(@PathVariable String session) {
        if (session == null || session.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


        List<OffreDeStageDTO> offresBySession = offreDeStageService.getOffresBySession(session);

        return ResponseEntity.ok().body(offresBySession);
    }

    @GetMapping("/annee/{annee}")
    public ResponseEntity<List<OffreDeStageDTO>> getOffresByAnnee(@PathVariable String annee) {
        if (annee == null || annee.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<OffreDeStageDTO> offresByAnnee = offreDeStageService.getOffresByAnnee(annee);

        return ResponseEntity.ok().body(offresByAnnee);
    }

    @GetMapping("/offresValidees")
    public ResponseEntity<List<OffreDeStageDTO>> getOffresValidees() {
        List<OffreDeStageDTO> offresValidees = etudiantService.getOffresApprouvees();

        System.out.println("offresValidees = " + offresValidees.toString());

        return ResponseEntity.ok().body(offresValidees);
    }

    @GetMapping("/{offreId}/etudiants")
    public ResponseEntity<List<EtudiantDTO>> getEtudiantsByOffre(@PathVariable Long offreId) {
        if (offreId == null) {
            return ResponseEntity.badRequest().build();
        }

        System.out.println("offreId = " + offreId);

        List<EtudiantDTO> etudiants = offreDeStageService.getEtudiantsByOffre(offreId).orElseGet(() -> List.of());

        return ResponseEntity.ok().body(etudiants);
    }



}