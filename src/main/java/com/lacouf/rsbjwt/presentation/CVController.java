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

    @PostMapping("/creerCV")
    public ResponseEntity<CVDTO> creerCV(@RequestBody CVDTO newCV) {
        if (newCV == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        System.out.println("CV: " + newCV);

        Optional<CVDTO> cvDTO = etudiantService.creerCV(newCV, 3853L);

        return cvDTO.map(cv -> new ResponseEntity<>(cv, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.CONFLICT));
    }
}
