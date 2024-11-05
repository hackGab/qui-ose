package com.lacouf.rsbjwt.presentation;

import com.lacouf.rsbjwt.service.GestionnaireService;
import com.lacouf.rsbjwt.service.dto.CVDTO;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import com.lacouf.rsbjwt.service.dto.OffreDeStageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/gestionnaire")
@CrossOrigin(origins = "http://localhost:3000")
public class GestionnaireController {

    private final GestionnaireService gestionnaireService;

    public GestionnaireController(GestionnaireService gestionnaireService) {
        this.gestionnaireService = gestionnaireService;
    }

    @PutMapping("/validerOuRejeterOffre/{offreId}")
    public ResponseEntity<OffreDeStageDTO> validerOuRejeterOffre(
            @PathVariable Long offreId,
            @RequestBody Map<String, Object> body) {

        String status = (String) body.get("status");
        String rejectionReason = (String) body.get("rejectionReason");

        Optional<OffreDeStageDTO> offreDeStageDTO = gestionnaireService.validerOuRejeterOffre(offreId, status, rejectionReason);

        return offreDeStageDTO.map(offre -> new ResponseEntity<>(offre, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/validerOuRejeterCV/{cvId}")
    public ResponseEntity<CVDTO> validerOuRejeterCV(
            @PathVariable Long cvId,
            @RequestBody Map<String, Object> body) {

        String status = (String) body.get("status");
        String rejectionReason = (String) body.get("rejectionReason");

        Optional<CVDTO> cvDTO = gestionnaireService.validerOuRejeterCV(cvId, status, rejectionReason);

        return cvDTO.map(cv -> new ResponseEntity<>(cv, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/assignerProfesseur/{etudiantId}/{professeurId}")
    public ResponseEntity<EtudiantDTO> assignerProfesseur(
            @PathVariable Long etudiantId,
            @PathVariable Long professeurId) {

        try {
            Optional<EtudiantDTO> etudiantDTO = gestionnaireService.assignerProfesseur(etudiantId, professeurId);
            return etudiantDTO.map(etudiant -> new ResponseEntity<>(etudiant, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @PutMapping("/etudiants/deassignerProfesseur/{email}")
    public ResponseEntity<EtudiantDTO> deassignerProfesseur(@PathVariable String email) {
        Optional<EtudiantDTO> etudiantDTO = gestionnaireService.deassignerProfesseur(email);
        if (etudiantDTO.isPresent()) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(etudiantDTO.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
