package com.lacouf.rsbjwt.presentation;

import com.lacouf.rsbjwt.service.GestionnaireService;
import com.lacouf.rsbjwt.service.dto.CVDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/gestionnaire")
@CrossOrigin(origins = "http://localhost:3000")
public class GestionnaireController {

    private final GestionnaireService gestionnaireService;

    public GestionnaireController(GestionnaireService gestionnaireService) {
        this.gestionnaireService = gestionnaireService;
    }

    @PutMapping("/validerOuRejeterCV/{cvId}")
    public ResponseEntity<CVDTO> validerOuRejeterCV(@PathVariable Long cvId, @RequestParam String status) {
        Optional<CVDTO> cvDTO = gestionnaireService.validerOuRejeterCV(cvId, status);

        return cvDTO.map(cv -> new ResponseEntity<>(cv, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
