package com.lacouf.rsbjwt.presentation;

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


    public ContratController(GestionnaireService gestionnaireService, EmployeurService employeurService, EtudiantService etudiantService) {
        this.gestionnaireService = gestionnaireService;
        this.employeurService = employeurService;
        this.etudiantService = etudiantService;
    }

    @PostMapping("/creerContrat")
    public ResponseEntity<ContratDTO> creerContrat(@RequestBody ContratDTO newContrat) {
        if (newContrat == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<ContratDTO> contratCree = gestionnaireService.creerContrat(newContrat);

        return contratCree
                .map(contratDTO -> ResponseEntity.status(HttpStatus.CREATED).body(contratDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }


    @GetMapping("/getContrats-employeur/{employeurEmail}/session/{session}")
    public ResponseEntity<List<ContratDTO>> getContratEmployeur(@PathVariable String employeurEmail, @PathVariable String session) {
        List<ContratDTO> contrats = new ArrayList<>(employeurService.getContratEmployeur(employeurEmail, session));

        return ResponseEntity.ok().body(contrats);
    }

    @GetMapping("/getContrats-etudiant/{etudiantEmail}/session/{session}")
    public ResponseEntity<List<ContratDTO>> getContratEtudiant(@PathVariable String etudiantEmail, @PathVariable String session) {
        List<ContratDTO> contrats = new ArrayList<>(etudiantService.getContratsByEtudiantAndSession(etudiantEmail, session));

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

    @PutMapping("/signer-gestionnaire/{uuid}/{email}")
    public ResponseEntity<ContratDTO> signerContratParGestionnaire(
            @PathVariable String uuid,
            @PathVariable String email,
            @RequestBody Map<String, String> request) {
        String password = request.get("password");
        Optional<ContratDTO> contratSigne = gestionnaireService.signerContratGestionnaire(uuid, password, email);

        return contratSigne
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }


    @GetMapping("/all")
    public ResponseEntity<List<ContratDTO>> getAllContrats() {
        List<ContratDTO> contrats = gestionnaireService.getAllContrats();

        return ResponseEntity.ok().body(contrats);
    }

    @GetMapping("/session/{session}")
    public ResponseEntity<List<ContratDTO>> getContratsBySession(@PathVariable String session) {
        List<ContratDTO> contrats = gestionnaireService.getContratsBySession(session);

        return ResponseEntity.ok().body(contrats);
    }

    @GetMapping("/en-attente-signature/{etudiantEmail}")
    public ResponseEntity<List<ContratDTO>> getContratsEnAttenteDeSignature(@PathVariable String etudiantEmail) {
        List<ContratDTO> contrats = etudiantService.getContratsEnAttenteDeSignature(etudiantEmail);
        return ResponseEntity.ok(contrats);
    }
}
