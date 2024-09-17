package com.lacouf.rsbjwt.presentation;

import com.lacouf.rsbjwt.service.EtudiantService;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/etudiant")
public class EtudiantController {
    Logger logger = Logger.getLogger(EtudiantController.class.getName());

    private final EtudiantService etudiantService;

    public  EtudiantController(EtudiantService etudiantService) {
        this.etudiantService = etudiantService;
    }

    @PostMapping("/creerEtudiant")
    public ResponseEntity<EtudiantDTO> creerEtudiant(@RequestBody EtudiantDTO newEtudiant) {
        logger.info("post - creerEtudiant " + newEtudiant);
        Optional<EtudiantDTO> etudiantDTO = etudiantService.creerEtudiant(newEtudiant);
        return etudiantDTO.map(etudiant -> ResponseEntity.status(HttpStatus.CREATED).body(etudiant))
                .orElse(ResponseEntity.status(HttpStatus.CONFLICT).build());
    }
}
