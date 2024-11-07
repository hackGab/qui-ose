package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.*;
import com.lacouf.rsbjwt.repository.EtudiantRepository;
import com.lacouf.rsbjwt.repository.EvaluationStageProfRepository;
import com.lacouf.rsbjwt.repository.ProfesseurRepository;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import com.lacouf.rsbjwt.service.dto.EvaluationStageProfDTO;
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
                EvaluationStageProf evaluationStageProfRemplie =  remplireEvaluationStage(evaluationStageProf,etudiant);
                evaluationStageProfRepository.save(evaluationStageProfRemplie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private EvaluationStageProf remplireEvaluationStage(EvaluationStageProf evaluationStageProf, Etudiant etudiant) {
        evaluationStageProf.setNomStagiaire(etudiant.getFirstName() + " " + etudiant.getLastName());
        OffreDeStage offreDeStage = etudiantRepository.findOffreDeStageByEntrevue(etudiant.getId());
        Contrat contrat = etudiantRepository.findContratByEntrevue(etudiant.getId());
        evaluationStageProf.setDateStage(contrat.getDateDebut());
        evaluationStageProf.setNomEntreprise(offreDeStage.getEmployeur().getEntreprise());
        evaluationStageProf.setAdresse(offreDeStage.getLocalisation());
        evaluationStageProf.setTelephone(offreDeStage.getEmployeur().getPhoneNumber());
        return evaluationStageProf;
    }

    public Optional<EvaluationStageProfDTO> evaluerStage(EvaluationStageProfDTO evaluationStageProfDTO) {

        Optional<EvaluationStageProf> evaluationStageProf = evaluationStageProfRepository.findByProsseurID(evaluationStageProfDTO.getProfesseurId());
        if (evaluationStageProf.isEmpty()) {
            return Optional.empty();
        }

        EvaluationStageProfDTO evaluationStageProCopie = new EvaluationStageProfDTO(evaluationStageProf.get());
        EvaluationStageProfDTO evaluationStageProfDTOaSave = updateEvaluationStageProf(evaluationStageProfDTO, evaluationStageProCopie);

        EvaluationStageProf evaluationStageProfUpdated = new EvaluationStageProf(evaluationStageProfDTOaSave);
        evaluationStageProfRepository.save(evaluationStageProfUpdated);

        return Optional.of(evaluationStageProfDTOaSave);
    }

    private EvaluationStageProfDTO updateEvaluationStageProf(EvaluationStageProfDTO evaluationStageProfDTO, EvaluationStageProfDTO evaluationStageProfDTOUpdated) {

        evaluationStageProfDTOUpdated.setNomEntreprise(evaluationStageProfDTO.getNomEntreprise());
        evaluationStageProfDTOUpdated.setPersonneContact(evaluationStageProfDTO.getPersonneContact());
        evaluationStageProfDTOUpdated.setAdresse(evaluationStageProfDTO.getAdresse());
        evaluationStageProfDTOUpdated.setVille(evaluationStageProfDTO.getVille());
        evaluationStageProfDTOUpdated.setCodePostal(evaluationStageProfDTO.getCodePostal());
        evaluationStageProfDTOUpdated.setTelephone(evaluationStageProfDTO.getTelephone());
        evaluationStageProfDTOUpdated.setTelecopieur(evaluationStageProfDTO.getTelecopieur());

        evaluationStageProfDTOUpdated.setNomStagiaire(evaluationStageProfDTO.getNomStagiaire());
        evaluationStageProfDTOUpdated.setDateStage(evaluationStageProfDTO.getDateStage());
        evaluationStageProfDTOUpdated.setNumeroStage(evaluationStageProfDTO.getNumeroStage());

        // Évaluation des tâches
        evaluationStageProfDTOUpdated.setTachesConformite(evaluationStageProfDTO.getTachesConformite());
        evaluationStageProfDTOUpdated.setAccueilIntegration(evaluationStageProfDTO.getAccueilIntegration());
        evaluationStageProfDTOUpdated.setEncadrementSuffisant(evaluationStageProfDTO.getEncadrementSuffisant());
        evaluationStageProfDTOUpdated.setHeuresEncadrementPremierMois(evaluationStageProfDTO.getHeuresEncadrementPremierMois());
        evaluationStageProfDTOUpdated.setHeuresEncadrementDeuxiemeMois(evaluationStageProfDTO.getHeuresEncadrementDeuxiemeMois());
        evaluationStageProfDTOUpdated.setHeuresEncadrementTroisiemeMois(evaluationStageProfDTO.getHeuresEncadrementTroisiemeMois());

        evaluationStageProfDTOUpdated.setRespectNormesHygiene(evaluationStageProfDTO.getRespectNormesHygiene());
        evaluationStageProfDTOUpdated.setClimatDeTravail(evaluationStageProfDTO.getClimatDeTravail());
        evaluationStageProfDTOUpdated.setAccesTransportCommun(evaluationStageProfDTO.getAccesTransportCommun());
        evaluationStageProfDTOUpdated.setSalaireInteressant(evaluationStageProfDTO.getSalaireInteressant());
        evaluationStageProfDTOUpdated.setSalaireHoraire(evaluationStageProfDTO.getSalaireHoraire());
        evaluationStageProfDTOUpdated.setCommunicationSuperviseur(evaluationStageProfDTO.getCommunicationSuperviseur());
        evaluationStageProfDTOUpdated.setEquipementAdequat(evaluationStageProfDTO.getEquipementAdequat());
        evaluationStageProfDTOUpdated.setVolumeTravailAcceptable(evaluationStageProfDTO.getVolumeTravailAcceptable());

        // Observations générales
        evaluationStageProfDTOUpdated.setPrivilegiePremierStage(evaluationStageProfDTO.isPrivilegiePremierStage());
        evaluationStageProfDTOUpdated.setPrivilegieDeuxiemeStage(evaluationStageProfDTO.isPrivilegieDeuxiemeStage());
        evaluationStageProfDTOUpdated.setNombreStagiairesAccueillis(evaluationStageProfDTO.getNombreStagiairesAccueillis());
        evaluationStageProfDTOUpdated.setSouhaiteRevoirStagiaire(evaluationStageProfDTO.isSouhaiteRevoirStagiaire());
        evaluationStageProfDTOUpdated.setOffreQuartsVariables(evaluationStageProfDTO.isOffreQuartsVariables());
        evaluationStageProfDTOUpdated.setHorairesQuartsDeTravail(evaluationStageProfDTO.getHorairesQuartsDeTravail());

        // Commentaires et date
        evaluationStageProfDTOUpdated.setCommentaires(evaluationStageProfDTO.getCommentaires());
        evaluationStageProfDTOUpdated.setSignatureEnseignant(evaluationStageProfDTO.getSignatureEnseignant());
        evaluationStageProfDTOUpdated.setDateSignature(evaluationStageProfDTO.getDateSignature());

        return evaluationStageProfDTOUpdated;
    }

    public List<EvaluationStageProfDTO> getEvaluationsStageProf(String professeurEmail) {
        Optional<Professeur> professeurOpt = professeurRepository.findByEmail(professeurEmail);
        System.out.println("Professeur: " + professeurOpt);
        if (professeurOpt.isEmpty()) {
            return new ArrayList<>();
        }

        List<EvaluationStageProfDTO> evaluationsStageProf = evaluationStageProfRepository.findAllByProfesseur(professeurOpt.get()).stream().map(EvaluationStageProfDTO::new).collect(Collectors.toList());
        System.out.println("Evaluations: " + evaluationsStageProf.get(0).getDateStage());
        return evaluationsStageProf;
    }
}
