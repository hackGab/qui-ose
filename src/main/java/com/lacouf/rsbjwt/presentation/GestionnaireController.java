package com.lacouf.rsbjwt.presentation;

import com.lacouf.rsbjwt.service.GestionnaireService;
import com.lacouf.rsbjwt.service.dto.CVDTO;
import com.lacouf.rsbjwt.service.dto.OffreDeStageDTO;
import org.springframework.http.HttpStatus;
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
}