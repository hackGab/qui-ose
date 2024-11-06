package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.Etudiant;
import com.lacouf.rsbjwt.model.EvaluationStageProf;
import com.lacouf.rsbjwt.model.Professeur;
import com.lacouf.rsbjwt.repository.EtudiantRepository;
import com.lacouf.rsbjwt.repository.EvaluationStageProfRepository;
import com.lacouf.rsbjwt.repository.ProfesseurRepository;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import com.lacouf.rsbjwt.service.dto.ProfesseurDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfesseurService {
    private final ProfesseurRepository professeurRepository;
    private final EtudiantRepository etudiantRepository;

    private final EvaluationStageProfRepository evaluationStageProfRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfesseurService(ProfesseurRepository professeurRepository, EtudiantRepository etudiantRepository, PasswordEncoder passwordEncoder, EvaluationStageProfRepository evaluationStageProfRepository) {
        this.professeurRepository = professeurRepository;
        this.etudiantRepository = etudiantRepository;
        this.passwordEncoder = passwordEncoder;
        this.evaluationStageProfRepository = evaluationStageProfRepository;
    }

    public Optional<ProfesseurDTO> creerProfesseur(ProfesseurDTO professeurDTO) {
        try {
            String encodedPassword = passwordEncoder.encode(professeurDTO.getCredentials().getPassword());
            Professeur professeur = new Professeur(
                    professeurDTO.getFirstName(),
                    professeurDTO.getLastName(),
                    professeurDTO.getCredentials().getEmail(),
                    encodedPassword,
                    professeurDTO.getPhoneNumber(),
                    professeurDTO.getDepartement()
            );
            Professeur savedProfesseur = professeurRepository.save(professeur);
            return Optional.of(new ProfesseurDTO(savedProfesseur));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<ProfesseurDTO> getProfesseurById(Long id) {
        return professeurRepository.findById(id)
                .map(ProfesseurDTO::new);
    }


    public List<ProfesseurDTO> getAllProfesseurs() {
        return professeurRepository.findAll()
                .stream()
                .map(ProfesseurDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<ProfesseurDTO> assignerEtudiants(String professeurEmail, List<String> etudiantsEmails) {
        Optional<Professeur> professeurOpt = professeurRepository.findByEmail(professeurEmail);
        if (professeurOpt.isEmpty()) {
            return Optional.empty();
        }

        Professeur professeur = professeurOpt.get();
        List<Etudiant> etudiants = etudiantRepository.findAllByEmailIn(etudiantsEmails);
        if (etudiants.isEmpty()) {
            return Optional.empty();
        }

        for (Etudiant etudiant : etudiants) {
            etudiant.setProfesseur(professeur);
        }

        professeur.getEtudiants().addAll(etudiants);

        etudiantRepository.saveAll(etudiants);
        professeurRepository.save(professeur);

        return Optional.of(new ProfesseurDTO(professeur));
    }

    public List<EtudiantDTO> getEtudiants(String professeurEmail) {
        List<EtudiantDTO> etudiantsRecu = new ArrayList<>();
        Optional<List<String>> listeEmailsEtudiants = professeurRepository.findByEmail(professeurEmail).map(Professeur::getEtudiants).map(etudiants -> etudiants.stream()
                .map(Etudiant::getEmail)
                        .collect(Collectors.toList()));

        if (listeEmailsEtudiants.isEmpty()) {
            return etudiantsRecu;
        }

        for (String email : listeEmailsEtudiants.get()) {
            Etudiant etudiant = etudiantRepository.findByEmail(email).get();

            etudiantsRecu.add(new EtudiantDTO(etudiant));
        }

        return etudiantsRecu;
    }

    public void creerEvaluationStage(String professeurEmail, List<String> etudiantsEmails) {
        Professeur professeur = professeurRepository.findByEmail(professeurEmail).get();
        try {
            for (String email : etudiantsEmails) {
                Etudiant etudiant = etudiantRepository.findByEmail(email).get();
                EvaluationStageProf evaluationStageProf = new EvaluationStageProf();
                evaluationStageProf.setEtudiant(etudiant);
                evaluationStageProf.setProfesseur(professeur);
                evaluationStageProfRepository.save(evaluationStageProf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
