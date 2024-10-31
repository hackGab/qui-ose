package com.lacouf.rsbjwt.presentation;

import com.lacouf.rsbjwt.service.CandidatAccepterService;
import com.lacouf.rsbjwt.service.EmployeurService;
import com.lacouf.rsbjwt.service.EtudiantService;
import com.lacouf.rsbjwt.service.GestionnaireService;
import com.lacouf.rsbjwt.service.dto.ContratDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/contrat")
@CrossOrigin(origins = "http://localhost:3000")
public class  ContratController {
    private final GestionnaireService gestionnaireService;

    private final EmployeurService employeurService;
    private final EtudiantService etudiantService;

    private final CandidatAccepterService candidatAccepterService;

    public ContratController(GestionnaireService gestionnaireService, CandidatAccepterService candidatAccepterService, EmployeurService employeurService, EtudiantService etudiantService) {
        this.gestionnaireService = gestionnaireService;
        this.candidatAccepterService = candidatAccepterService;
        this.employeurService = employeurService;
        this.etudiantService = etudiantService;
    }

    @PostMapping("/creerContrat")
    public ResponseEntity<ContratDTO> creerContrat(@RequestBody ContratDTO newContrat) {
        if (newContrat == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<ContratDTO> contratCree = gestionnaireService.creerContrat(newContrat);

        // Pour déboguer
        System.out.println("Contrat créé : " + contratCree);

        return contratCree
                .map(contratDTO -> ResponseEntity.status(HttpStatus.CREATED).body(contratDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }


    @GetMapping("/getContrats-employeur/{employeurEmail}")
    public ResponseEntity<Iterable<ContratDTO>> getContratEmployeur(@PathVariable String employeurEmail) {
        ArrayList<ContratDTO> contrats = new ArrayList<>(employeurService.getContratEmployeur(employeurEmail));


        return ResponseEntity.ok().body(contrats);
    }

    @GetMapping("/getContrats-etudiant/{etudiantEmail}")
    public ResponseEntity<Iterable<ContratDTO>> getContratEtudiant(@PathVariable String etudiantEmail) {
        ArrayList<ContratDTO> contrats = new ArrayList<>(etudiantService.getContratsByEtudiant(etudiantEmail));

        return ResponseEntity.ok().body(contrats);
    }

    @PutMapping("/signer-employeur/{uuid}")
    public ResponseEntity<ContratDTO> signerContrat(@PathVariable String uuid, @RequestBody Map<String,String> request) {
        String password = request.get("password");
        Optional<ContratDTO> contratSigne = employeurService.signerContratEmployeur(uuid, password);

        return contratSigne
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PutMapping("/signer-etudiant/{uuid}")
    public ResponseEntity<ContratDTO> signerContratParEtudiant(@PathVariable String uuid, @RequestBody Map<String, String> request) {
        String password = request.get("password");
        System.out.println("UUID : " + uuid);

        try {
            Optional<ContratDTO> contratSigne = etudiantService.signerContratParEtudiant(uuid, password);
            return contratSigne
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/signer-gestionnaire/{uuid}")
    public ResponseEntity<ContratDTO> signerContratParGestionnaire(@PathVariable String uuid, @RequestBody Map<String, String> request, @PathVariable String email) {
        String password = request.get("password");
        Optional<ContratDTO> contratSigne = gestionnaireService.signerContratGestionnaire(uuid, password,email);

        return contratSigne
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<ContratDTO>> getAllContrats() {
        Iterable<ContratDTO> contrats = gestionnaireService.getAllContrats();

        return ResponseEntity.ok().body(contrats);
    }

    @GetMapping("/en-attente-signature/{etudiantEmail}")
    public ResponseEntity<List<ContratDTO>> getContratsEnAttenteDeSignature(@PathVariable String etudiantEmail) {
        List<ContratDTO> contrats = etudiantService.getContratsEnAttenteDeSignature(etudiantEmail);
        return ResponseEntity.ok(contrats);
    }
}
