package com.lacouf.rsbjwt.presentation;

import com.lacouf.rsbjwt.model.CV;
import com.lacouf.rsbjwt.service.EtudiantService;
import com.lacouf.rsbjwt.service.dto.CVDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Optional;

@RestController
@RequestMapping("/cv")
@CrossOrigin(origins = "http://localhost:3000")
public class CVController {


    private final EtudiantService etudiantService;

    public CVController(EtudiantService etudiantService) {
        this.etudiantService = etudiantService;
    }

    @PostMapping("/creerCV/{email}")
    public ResponseEntity<CVDTO> creerCV(@RequestBody CVDTO newCV, @PathVariable String email) {
        if (newCV == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<CVDTO> cvDTO = etudiantService.creerCV(newCV, email);

        return cvDTO.map(cv -> new ResponseEntity<>(cv, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.CONFLICT));
    }

    @DeleteMapping("/supprimerCV/{id}")
    public ResponseEntity<Void> supprimerCV(@PathVariable Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        etudiantService.supprimerCV(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/attentes")
    public ResponseEntity<Integer> getNombreCVEnAttente() {
        int nbCV = etudiantService.getNombreCVEnAttente();
        return ResponseEntity.ok(nbCV);
    }
}
