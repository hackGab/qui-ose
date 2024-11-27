package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.*;
import com.lacouf.rsbjwt.repository.*;
import com.lacouf.rsbjwt.service.dto.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GestionnaireService {
    private final GestionnaireRepository gestionnaireRepository;
    private final CVRepository cvRepository;
    private final EtudiantRepository etudiantRepository;
    private final PasswordEncoder passwordEncoder;
    private final ContratRepository contratRepository;
    private final CandidatAccepterRepository candidatAccepterRepository;

    private final OffreDeStageRepository offreDeStageRepository;
    private final EntrevueRepository entrevueRepository;
    private final ProfesseurRepository professeurRepository;
    private final EvaluationStageProfRepository evaluationStageProfRepository;
    private final NotificationRepository notificationRepository;

    public GestionnaireService(GestionnaireRepository gestionnaireRepository, CVRepository cvRepository, EtudiantRepository etudiantRepository , OffreDeStageRepository offreDeStageRepository, PasswordEncoder passwordEncoder, ContratRepository contratRepository, EntrevueRepository entrevueRepository, ProfesseurRepository professeurRepository, EvaluationStageProfRepository evaluationStageProfRepository, NotificationRepository notificationRepository, CandidatAccepterRepository candidatAccepterRepository) {
        this.gestionnaireRepository = gestionnaireRepository;
        this.cvRepository = cvRepository;
        this.etudiantRepository = etudiantRepository;
 	    this.offreDeStageRepository = offreDeStageRepository;
        this.passwordEncoder = passwordEncoder;
        this.contratRepository = contratRepository;
        this.entrevueRepository = entrevueRepository;
        this.professeurRepository = professeurRepository;
        this.evaluationStageProfRepository = evaluationStageProfRepository;
        this.notificationRepository = notificationRepository;
        this.candidatAccepterRepository = candidatAccepterRepository;
    }

    public Optional<GestionnaireDTO> creerGestionnaire(GestionnaireDTO gestionnaireDTO) {
        try {
            CredentialDTO credentialDTO = gestionnaireDTO.getCredentials();
            if (credentialDTO == null) {
                return Optional.empty();
            }
            String encodedPassword = encodePassword(credentialDTO.getPassword());

            Gestionnaire gestionnaire = new Gestionnaire(
                    gestionnaireDTO.getFirstName(),
                    gestionnaireDTO.getLastName(),
                    gestionnaireDTO.getCredentials().getEmail(),
                    encodedPassword,
                    gestionnaireDTO.getPhoneNumber()
            );
            Gestionnaire saveGestionnaire = gestionnaireRepository.save(gestionnaire);
            return Optional.of(new GestionnaireDTO(saveGestionnaire));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public Optional<CVDTO> validerOuRejeterCV(Long cvId, String status, String rejectionReason) {
        Optional<CV> cvOptional = cvRepository.findById(cvId);

        if (cvOptional.isPresent()) {
            CV cv = cvOptional.get();
            cv.setStatus(status);

            if ("rejeté".equals(status)) {
                cv.setRejetMessage(rejectionReason);
            } else {
                cv.setRejetMessage("");
            }

            cvRepository.save(cv);
            return Optional.of(new CVDTO(cv));
        }

        return Optional.empty();
    }

    public Optional<OffreDeStageDTO> validerOuRejeterOffre(Long offreId, String status, String rejectionReason) {        
    Optional<OffreDeStage> offreOptional = offreDeStageRepository.findById(offreId);

        if (offreOptional.isPresent()) {
            OffreDeStage offreDeStage = offreOptional.get();
            offreDeStage.setStatus(status);

            if ("Rejeté".equals(status)) {
                offreDeStage.setRejetMessage(rejectionReason);
            } else {
                offreDeStage.setRejetMessage("");
            }

            offreDeStageRepository.save(offreDeStage);
            return Optional.of(new OffreDeStageDTO(offreDeStage));
        }

        return Optional.empty();
    }

    public NotificationDTO createNotification(Notification notification) {
        Notification savedNotification = notificationRepository.save(notification);
        return new NotificationDTO(savedNotification);
    }

    public Optional<ContratDTO> creerContrat(ContratDTO contratDTO) {
        try {
            CandidatAccepter candidat = contratDTO.getCandidature().toEntity();
            Optional<Entrevue> entrevueDTO = entrevueRepository.findById(contratDTO.getCandidature().getEntrevueId());
            candidat.setEntrevue(entrevueDTO.get());

            Contrat contrat = new Contrat(
                    contratDTO.isEtudiantSigne(),
                    contratDTO.isEmployeurSigne(),
                    contratDTO.isGestionnaireSigne(),
                    contratDTO.getDateSignatureEtudiant(),
                    contratDTO.getDateSignatureEmployeur(),
                    contratDTO.getDateSignatureGestionnaire(),
                    candidat,
                    contratDTO.getCollegeEngagement(),
                    contratDTO.getDateDebut(),
                    contratDTO.getDateFin(),
                    contratDTO.getDescription(),
                    contratDTO.getEntrepriseEngagement(),
                    contratDTO.getEtudiantEngagement(),
                    contratDTO.getHeuresParSemaine(),
                    contratDTO.getHeureHorraireDebut(),
                    contratDTO.getHeureHorraireFin(),
                    contratDTO.getLieuStage(),
                    contratDTO.getNbSemaines(),
                    contratDTO.getTauxHoraire()
            );

            contrat.genererUUID();

            Contrat saveContrat = contratRepository.save(contrat);
            createNotification(new Notification("Nouveau Contrat, veuiller le signer ", entrevueDTO.get().getOffreDeStage().getTitre() , candidat.getEmailEtudiant() , "/signerContrat"));
            createNotification(new Notification("Nouveau Contrat, veuiller le signer ", entrevueDTO.get().getOffreDeStage().getTitre() , entrevueDTO.get().getOffreDeStage().getEmployeur().getEmail() , "/signerContrat"));


            return Optional.of(new ContratDTO(saveContrat));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<ContratDTO> getAllContrats() {
        return contratRepository.findAll().stream()
                .map(ContratDTO::new)
                .toList();
    }

    public Optional<ContratDTO> signerContratGestionnaire(String uuid, String password, String email) {
        Contrat contrat = contratRepository.findByUUID(uuid)
                .orElseThrow(() -> new RuntimeException("Contrat non trouvé"));

        Gestionnaire gestionnaire = gestionnaireRepository.findByEmail(email);

        if (passwordEncoder.matches(password, gestionnaire.getPassword())) {
            contrat.signerContratGestionnaire();
            Contrat savedContrat = contratRepository.save(contrat);
            return Optional.of(new ContratDTO(savedContrat));
        } else {
            throw new IllegalArgumentException("Mot de passe incorrect");
        }
    }

    public Optional<EtudiantDTO> assignerProfesseur(Long etudiantId, Long professeurId) {
        Optional<Etudiant> etudiantOpt = etudiantRepository.findById(etudiantId);
        Optional<Professeur> professeurOpt = professeurRepository.findById(professeurId);

        if (etudiantOpt.isPresent() && professeurOpt.isPresent()) {
            Etudiant etudiant = etudiantOpt.get();

            Optional<Contrat> contratOpt = contratRepository.findByCandidature_EtudiantAndGestionnaireSigneTrue(etudiant);
            if (contratOpt.isEmpty()) {
                throw new IllegalStateException("Un contrat signé par le gestionnaire est requis pour assigner un professeur à cet étudiant.");
            }

            etudiant.setProfesseur(professeurOpt.get());
            etudiantRepository.save(etudiant);

            return Optional.of(new EtudiantDTO(etudiant));
        }
        return Optional.empty();
    }

    public Optional<EtudiantDTO> deassignerProfesseur(String email) {
        Optional<Etudiant> etudiantOpt = etudiantRepository.findByEmail(email);

        if (etudiantOpt.isPresent()) {
            Etudiant etudiant = etudiantOpt.get();
            etudiant.setProfesseur(null);
            etudiantRepository.save(etudiant);

            evaluationStageProfRepository.deleteByEtudiant(etudiant);

            return Optional.of(new EtudiantDTO(etudiant));
        }
        return Optional.empty();
    }

    public List<ContratDTO> getContratsBySession(String session) {
        return contratRepository.findBySession(session).stream()
                .map(ContratDTO::new)
                .toList();
    }

    public List<OffreDeStageDTO> getOffresBySession(String session) {
        List<OffreDeStageDTO> offres = offreDeStageRepository.findBySession(session)
                .stream()
                .map(OffreDeStageDTO::new)
                .collect(Collectors.toList());

        return offres;
    }

    public int getNombreOffresEnAttente() {
        List<OffreDeStage> offres = offreDeStageRepository.findAll();
        List<OffreDeStage> offresEnAttente = offres.stream()
                .filter(offre -> offre.getStatus().equals("Attente"))
                .toList();
        return offresEnAttente.size();
    }

    public List<CandidatAccepterDTO> getAllCandidatures() {
        return candidatAccepterRepository.findAll().stream()
                .map(candidatAccepter -> new CandidatAccepterDTO(
                        candidatAccepter.getId(),
                        candidatAccepter.getEntrevue().getId(),
                        candidatAccepter.isAccepte()
                ))
                .toList();
    }

    public List<CandidatAccepterDTO> getCandidaturesBySession(String session) {
        return candidatAccepterRepository.findByEntrevueSession(session).stream()
                .map(candidatAccepter -> new CandidatAccepterDTO(
                        candidatAccepter.getId(),
                        candidatAccepter.getEntrevue().getId(),
                        candidatAccepter.isAccepte()
                ))
                .toList();
    }
}
