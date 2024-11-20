package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.*;
import com.lacouf.rsbjwt.repository.EtudiantRepository;
import com.lacouf.rsbjwt.repository.EvaluationStageProfRepository;
import com.lacouf.rsbjwt.repository.ProfesseurRepository;
import com.lacouf.rsbjwt.service.dto.CredentialDTO;
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
            CredentialDTO credentialDTO = professeurDTO.getCredentials();
            String encodedPassword = encodePassword(credentialDTO.getPassword());
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

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
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

    public Optional<ProfesseurDTO> deassignerEtudiants(String professeurEmail, List<String> etudiantsEmails) {
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
            etudiant.setProfesseur(null);
        }

        professeur.getEtudiants().removeAll(etudiants);

        etudiantRepository.saveAll(etudiants);
        professeurRepository.save(professeur);

        return Optional.of(new ProfesseurDTO(professeur));
    }


    public List<EtudiantDTO> getEtudiants(String professeurEmail) {
        List<EtudiantDTO> etudiantsRecu = new ArrayList<>();
        Optional<List<String>> listeEmailsEtudiants = professeurRepository.findByEmail(professeurEmail)
                .map(Professeur::getEtudiants)
                .map(etudiants -> etudiants.stream()
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

    public void supprimerEvaluationStage(String professeurEmail, List<String> etudiantsEmails) {
        Professeur professeur = professeurRepository.findByEmail(professeurEmail).get();
        try {
            for (String email : etudiantsEmails) {
                Etudiant etudiant = etudiantRepository.findByEmail(email).get();
                EvaluationStageProf evaluationStageProf = evaluationStageProfRepository.findByEtudiantID(etudiant.getId());
                evaluationStageProfRepository.delete(evaluationStageProf);
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
        evaluationStageProf.setPersonneContact(offreDeStage.getEmployeur().getFirstName() + " " + offreDeStage.getEmployeur().getLastName());
        evaluationStageProf.setSalaireHoraire(contrat.getTauxHoraire());
        return evaluationStageProf;
    }

    public Optional<EvaluationStageProfDTO> evaluerStage(EvaluationStageProfDTO evaluationStageProfDTO) {


        EvaluationStageProf evaluationStageProf = evaluationStageProfRepository.findByEtudiantID(evaluationStageProfDTO.getEtudiantId());
        System.out.println("EvaluationStageProf: " + evaluationStageProf.getId());


        EvaluationStageProf evaluationStageProfDTOaSave = updateEvaluationStageProf(evaluationStageProf, evaluationStageProfDTO);

        evaluationStageProfRepository.save(evaluationStageProfDTOaSave);

        EvaluationStageProfDTO evaluationStageProfDTOaReturn = new EvaluationStageProfDTO(evaluationStageProfDTOaSave);

        return Optional.of(evaluationStageProfDTOaReturn);
    }

    private EvaluationStageProf updateEvaluationStageProf(EvaluationStageProf evaluationStageProf, EvaluationStageProfDTO evaluationStageProfDTO) {
        evaluationStageProf.setCommentaires(evaluationStageProfDTO.getCommentaires());
        evaluationStageProf.setTachesConformite(evaluationStageProfDTO.getTachesConformite());
        evaluationStageProf.setAccueilIntegration(evaluationStageProfDTO.getAccueilIntegration());
        evaluationStageProf.setEncadrementSuffisant(evaluationStageProfDTO.getEncadrementSuffisant());
        evaluationStageProf.setHeuresEncadrementPremierMois(evaluationStageProfDTO.getHeuresEncadrementPremierMois());
        evaluationStageProf.setHeuresEncadrementDeuxiemeMois(evaluationStageProfDTO.getHeuresEncadrementDeuxiemeMois());
        evaluationStageProf.setHeuresEncadrementTroisiemeMois(evaluationStageProfDTO.getHeuresEncadrementTroisiemeMois());
        evaluationStageProf.setRespectNormesHygiene(evaluationStageProfDTO.getRespectNormesHygiene());
        evaluationStageProf.setClimatDeTravail(evaluationStageProfDTO.getClimatDeTravail());
        evaluationStageProf.setAccesTransportCommun(evaluationStageProfDTO.getAccesTransportCommun());
        evaluationStageProf.setSalaireInteressant(evaluationStageProfDTO.getSalaireInteressant());
        evaluationStageProf.setSalaireHoraire(evaluationStageProfDTO.getSalaireHoraire());
        evaluationStageProf.setCommunicationSuperviseur(evaluationStageProfDTO.getCommunicationSuperviseur());
        evaluationStageProf.setEquipementAdequat(evaluationStageProfDTO.getEquipementAdequat());
        evaluationStageProf.setVolumeTravailAcceptable(evaluationStageProfDTO.getVolumeTravailAcceptable());
        evaluationStageProf.setPrivilegiePremierStage(evaluationStageProfDTO.isPrivilegiePremierStage());
        evaluationStageProf.setPrivilegieDeuxiemeStage(evaluationStageProfDTO.isPrivilegieDeuxiemeStage());
        evaluationStageProf.setNombreStagiairesAccueillis(evaluationStageProfDTO.getNombreStagiairesAccueillis());
        evaluationStageProf.setSouhaiteRevoirStagiaire(evaluationStageProfDTO.isSouhaiteRevoirStagiaire());
        evaluationStageProf.setOffreQuartsVariables(evaluationStageProfDTO.isOffreQuartsVariables());
        evaluationStageProf.setHorairesQuartsDeTravail(evaluationStageProfDTO.getHorairesQuartsDeTravail());
        evaluationStageProf.setSignatureEnseignant(evaluationStageProfDTO.getSignatureEnseignant());
        evaluationStageProf.setDateSignature(evaluationStageProfDTO.getDateSignature());
        return evaluationStageProf;
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
