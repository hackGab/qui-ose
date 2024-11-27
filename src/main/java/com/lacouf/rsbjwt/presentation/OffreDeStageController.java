package com.lacouf.rsbjwt.presentation;

import com.lacouf.rsbjwt.model.Employeur;
import com.lacouf.rsbjwt.service.EmployeurService;
import com.lacouf.rsbjwt.service.EtudiantService;
import com.lacouf.rsbjwt.service.GestionnaireService;
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

    private final EmployeurService employeurService;
    private final EtudiantService etudiantService;
    private final GestionnaireService gestionnaireService;

    public OffreDeStageController(EmployeurService employeurService, EtudiantService etudiantService, GestionnaireService gestionnaireService) {
        this.employeurService = employeurService;
        this.etudiantService = etudiantService;
        this.gestionnaireService = gestionnaireService;
    }

    @PostMapping("/creerOffreDeStage/{email}")
    public ResponseEntity<OffreDeStageDTO> creerOffreDeStage(
            @PathVariable String email,
            @RequestBody OffreDeStageDTO newOffreDeStageDTO) {
        if (newOffreDeStageDTO == null || email == null || email.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<Employeur> employeurOpt = employeurService.findByCredentials_Email(email);
        Optional<OffreDeStageDTO> offreDeStageDTO = employeurService.creerOffreDeStage(newOffreDeStageDTO, employeurOpt);

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
            List<OffreDeStageDTO> offreDeStageDTOList = employeurService.getOffresEmployeurSession(employeurOpt.get(), session);

            return ResponseEntity.ok().body(offreDeStageDTOList);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OffreDeStageDTO> updateOffreDeStage(@PathVariable Long id, @RequestBody OffreDeStageDTO offreDeStageDTO) {
        System.out.println("id = " + id);


        if (id == null || offreDeStageDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<OffreDeStageDTO> offreDeStageDTOUpdated = employeurService.updateOffreDeStage(id, offreDeStageDTO);

        return offreDeStageDTOUpdated.map(offreDeStage -> ResponseEntity.ok().body(offreDeStage))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OffreDeStageDTO> getOffreDeStageById(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<OffreDeStageDTO> offreDeStageDTO = employeurService.getOffreDeStageById(id);

        return offreDeStageDTO.map(offreDeStage -> ResponseEntity.ok().body(offreDeStage))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/session/{session}")
    public ResponseEntity<List<OffreDeStageDTO>> getOffresBySession(@PathVariable String session) {
        if (session == null || session.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<OffreDeStageDTO> offresBySession = gestionnaireService.getOffresBySession(session);

        return ResponseEntity.ok().body(offresBySession);
    }

    @GetMapping("/offresValidees/session/{session}")
    public ResponseEntity<List<OffreDeStageDTO>> getOffresValidees(@PathVariable String session) {
        List<OffreDeStageDTO> offresValidees = etudiantService.getOffresApprouveesParSession(session);

        System.out.println("offresValidees = " + offresValidees.toString());

        return ResponseEntity.ok().body(offresValidees);
    }

    @GetMapping("/{offreId}/etudiants")
    public ResponseEntity<List<EtudiantDTO>> getEtudiantsByOffre(@PathVariable Long offreId) {
        if (offreId == null) {
            return ResponseEntity.badRequest().build();
        }

        System.out.println("offreId = " + offreId);

        List<EtudiantDTO> etudiants = employeurService.getEtudiantsByOffre(offreId).orElseGet(() -> List.of());

        return ResponseEntity.ok().body(etudiants);
    }

    @GetMapping("/attentes")
    public ResponseEntity<Integer> getOffresAttentes() {
        int nbOffres = gestionnaireService.getNombreOffresEnAttente();
        return ResponseEntity.ok(nbOffres);
    }
}
