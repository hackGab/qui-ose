package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.*;
import com.lacouf.rsbjwt.repository.*;
import com.lacouf.rsbjwt.service.dto.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EtudiantService {

    private final UserAppRepository userAppRepository;
    private final EtudiantRepository etudiantRepository;
    private final PasswordEncoder passwordEncoder;
    private final CVRepository cvRepository;
    private final ContratRepository contratRepository;
    private final OffreDeStageRepository offreDeStageRepository;

    private final EntrevueRepository entrevueRepository;
    private final CandidatAccepterRepository candidatAccepterRepository;

    public EtudiantService(UserAppRepository userAppRepository, EtudiantRepository etudiantRepository, PasswordEncoder passwordEncoder, CVRepository cvRepository, OffreDeStageRepository offreDeStageRepository, EntrevueRepository entrevueRepository, ContratRepository contratRepository, CandidatAccepterRepository candidatAccepterRepository) {
        this.userAppRepository = userAppRepository;
        this.etudiantRepository = etudiantRepository;
        this.passwordEncoder = passwordEncoder;
        this.cvRepository = cvRepository;
        this.offreDeStageRepository = offreDeStageRepository;
        this.entrevueRepository = entrevueRepository;
        this.contratRepository = contratRepository;
        this.candidatAccepterRepository = candidatAccepterRepository;
    }

    public Optional<EtudiantDTO> creerEtudiant(EtudiantDTO etudiantDTO) {
        try {
            CredentialDTO credentials = etudiantDTO.getCredentials();
            if (credentials == null) {
                return Optional.empty();
            }
            String encodedPassword = encodePassword(credentials.getPassword());

            Departement departementEnum = getDepartementEnum(etudiantDTO.getDepartement());

            Etudiant etudiant = new Etudiant(
                    etudiantDTO.getFirstName(),
                    etudiantDTO.getLastName(),
                    etudiantDTO.getCredentials().getEmail(),
                    encodedPassword,
                    etudiantDTO.getPhoneNumber(),
                    departementEnum
            );

            Etudiant savedEtudiant = etudiantRepository.save(etudiant);
            return Optional.of(new EtudiantDTO(savedEtudiant));
        } catch (Exception e) {
            System.out.println("Erreur lors de la création de l'étudiant : " + e.getMessage());
            return Optional.empty();
        }
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private Departement getDepartementEnum(Departement departement) {
        if (departement == null) {
            return null;
        }

        return Arrays.stream(Departement.values())
                .filter(dept -> dept.name().equalsIgnoreCase(departement.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Département invalide : " + departement));
    }

    public Optional<EtudiantDTO> getEtudiantById(Long id) {
        return etudiantRepository.findById(id)
                .map(EtudiantDTO::new);
    }

    public Optional<CVDTO> creerCV(CVDTO cvDTO, String email) {
        try {
            CV cv = new CV(
                    cvDTO.getName(),
                    cvDTO.getType(),
                    cvDTO.getData(),
                    cvDTO.getStatus()
            );

            CV savedCV = cvRepository.save(cv);
            Etudiant etudiant = userAppRepository.findUserAppByEmail(email)
                    .map(userApp -> (Etudiant) userApp)
                    .get();
            etudiant.setCv(savedCV);
            etudiantRepository.save(etudiant);

            return Optional.of(new CVDTO(savedCV));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void supprimerCV(Long id) {
        cvRepository.deleteById(id);
    }

    public Optional<EtudiantDTO> getEtudiantByEmail(String email) {
        Optional<UserApp> utilisateur = userAppRepository.findUserAppByEmail(email);

        if (utilisateur.isEmpty()) {
            return Optional.empty();
        }
        Etudiant etudiant = (Etudiant) utilisateur.get();

        System.out.println(etudiant);
        return Optional.of(new EtudiantDTO(etudiant));
    }

    public List<EtudiantDTO> getAllEtudiants() {
        return etudiantRepository.findAll().stream()
                .map(EtudiantDTO::new)
                .toList();
    }

    public List<OffreDeStageDTO> getOffresApprouveesParSession(String session) {
        return offreDeStageRepository.findBySession(session).stream()
                .filter(offreDeStage -> offreDeStage.getStatus().equals("Validé"))
                .map(OffreDeStageDTO::new)
                .toList();
    }

    public Optional<EtudiantDTO> ajouterOffreDeStage(String etudiantEmail, Long offreId) {
        Optional<Etudiant> etudiantOpt = etudiantRepository.findByEmail(etudiantEmail);
        Optional<OffreDeStage> offreOpt = offreDeStageRepository.findById(offreId);

        if (etudiantOpt.isPresent() && offreOpt.isPresent()) {
            Etudiant etudiant = etudiantOpt.get();
            OffreDeStage offre = offreOpt.get();

            etudiant.getOffresAppliquees().add(offre);

            etudiantRepository.save(etudiant);
            offreDeStageRepository.save(offre);

            return Optional.of(new EtudiantDTO(etudiant));
        }

        return Optional.empty();
    }

    public List<OffreDeStageDTO> getOffresDeStage(String etudiantEmail) {
        Optional<Etudiant> etudiantOptional = etudiantRepository.findByEmail(etudiantEmail);
        if (etudiantOptional.isPresent()) {
            Etudiant etudiant = etudiantOptional.get();
            return etudiant.getOffresAppliquees().stream()
                    .map(OffreDeStageDTO::new)
                    .toList();
        } else {
            return List.of();
        }
    }

    public Optional<EtudiantDTO> retirerOffreDeStage(String email, Long offreId) {
        Optional<Etudiant> etudiantOpt = etudiantRepository.findByEmail(email);
        Optional<OffreDeStage> offreOpt = offreDeStageRepository.findById(offreId);

        if (etudiantOpt.isPresent() && offreOpt.isPresent()) {
            Etudiant etudiant = etudiantOpt.get();
            OffreDeStage offre = offreOpt.get();

            etudiant.getOffresAppliquees().remove(offre);

            etudiantRepository.save(etudiant);

            return Optional.of(new EtudiantDTO(etudiant));
        }

        return Optional.empty();
    }

    public List<EntrevueDTO> getEntrevuesByEtudiant(String email) {
        Optional<Etudiant> etudiantOptional = etudiantRepository.findByEmail(email);
        if (etudiantOptional.isPresent()) {
            Etudiant etudiant = etudiantOptional.get();
            Long etudiantId = etudiant.getId();
            return entrevueRepository.findAllByEtudiantId(etudiantId).stream()
                    .map(EntrevueDTO::new)
                    .toList();
        } else {
            return List.of();
        }
    }

    public List<EntrevueDTO> getEntrevuesEnAttenteByEtudiant(String email) {
        Optional<Etudiant> etudiantOptional = etudiantRepository.findByEmail(email);
        if (etudiantOptional.isPresent()) {
            Etudiant etudiant = etudiantOptional.get();
            Long etudiantId = etudiant.getId();
            return entrevueRepository.findAllByEtudiantId(etudiantId).stream()
                    .filter(entrevue -> entrevue.getStatus().equals("En attente"))
                    .map(EntrevueDTO::new)
                    .toList();
        } else {
            return List.of();
        }
    }

    public Optional<EntrevueDTO> changerStatusEntrevue(String emailEtudiant, Long idOffreDeStage, String status) {
        Optional<Etudiant> etudiantOptional = etudiantRepository.findByEmail(emailEtudiant);
        if (etudiantOptional.isPresent()) {
            Etudiant etudiant = etudiantOptional.get();
            Long etudiantId = etudiant.getId();
            Optional<Entrevue> entrevueOpt = entrevueRepository.findByEtudiantIdAndOffreDeStageId(etudiantId, idOffreDeStage);

            if (entrevueOpt.isPresent()) {
                Entrevue entrevue = entrevueOpt.get();
                entrevue.setStatus(status);
                entrevueRepository.save(entrevue);
                return Optional.of(new EntrevueDTO(entrevue));
            }
        }
        return Optional.empty();
    }

    public List<EntrevueDTO> getEntrevuesAccepteesByEtudiant(String email) {
        Optional<Etudiant> etudiantOptional = etudiantRepository.findByEmail(email);
        if (etudiantOptional.isPresent()) {
            Etudiant etudiant = etudiantOptional.get();
            Long etudiantId = etudiant.getId();
            return entrevueRepository.findAllByEtudiantId(etudiantId).stream()
                    .filter(entrevue -> entrevue.getStatus().equals("Accepter"))
                    .map(EntrevueDTO::new)
                    .toList();
        } else {
            return List.of();
        }
    }

    public List<EntrevueDTO> getEntrevuesByEtudiantAndSession(String email, String session) {
        Optional<Etudiant> etudiantOptional = etudiantRepository.findByEmail(email);
        if (etudiantOptional.isPresent()) {
            Etudiant etudiant = etudiantOptional.get();
            Long etudiantId = etudiant.getId();
            return entrevueRepository.findByEtudiantEmailAndSession(etudiant.getCredentials().getEmail(), session).stream()
                    .map(EntrevueDTO::new)
                    .toList();
        } else {
            return List.of();
        }
    }

    public List<ContratDTO> getContratsByEtudiantAndSession(String email, String session) {
        Optional<Etudiant> etudiantOptional = etudiantRepository.findByEmail(email);
        if (etudiantOptional.isPresent()) {
            Etudiant etudiant = etudiantOptional.get();
            return contratRepository.findContratsByEtudiantEmailAndSession(etudiant, session).stream()
                    .map(ContratDTO::new)
                    .toList();
        } else {
            return List.of();
        }
    }

    public Optional<ContratDTO> signerContratParEtudiant(String uuid, String password) {
        Contrat contrat = contratRepository.findByUUID(uuid)
                .orElseThrow(() -> new RuntimeException("Contrat non trouvé"));

        Etudiant etudiant = getEtudiantFromContrat(contrat);

        if (passwordEncoder.matches(password, etudiant.getPassword())) {
            contrat.signerContratEtudiant();
            Contrat savedContrat = contratRepository.save(contrat);
            return Optional.of(new ContratDTO(savedContrat));
        } else {
            throw new IllegalArgumentException("Mot de passe incorrect");
        }
    }

    private Etudiant getEtudiantFromContrat(Contrat contrat) {
        return Optional.ofNullable(contrat.getCandidature())
                .map(CandidatAccepter::getEntrevue)
                .map(Entrevue::getEtudiant)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé pour le contrat donné"));
    }

    public List<ContratDTO> getContratsEnAttenteDeSignature(String email) {
        Optional<Etudiant> etudiantOptional = etudiantRepository.findByEmail(email);
        if (etudiantOptional.isPresent()) {
            Etudiant etudiant = etudiantOptional.get();
            return contratRepository.findContratsByEtudiantEmail(etudiant).stream()
                    .filter(contrat -> !contrat.isEtudiantSigne())
                    .map(ContratDTO::new)
                    .toList();
        } else {
            return List.of();
        }
    }


    public List<EtudiantDTO> getEtudiantsByDepartement(String departement) {
        System.out.println("Département : " + departement);

        Departement departementEnum = Arrays.stream(Departement.values())
                .filter(dept -> dept.getDisplayName().equalsIgnoreCase(departement))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Département invalide : " + departement));

        return etudiantRepository.findAllByDepartement(departementEnum).stream()
                .map(EtudiantDTO::new)
                .toList();
    }

    public List<EtudiantDTO> getEtudiantsAvecContratByDepartement(Departement departement) {
        return etudiantRepository.findEtudiantsAvecContratByDepartement(departement).stream()
                .map(EtudiantDTO::new)
                .toList();
    }

    public int getNombreCVEnAttente() {
        List<Etudiant> etudiants = etudiantRepository.findAll();
        List<CV> cvs = etudiants.stream()
                .map(Etudiant::getCv)
                .toList();
        List<CV> cvsEnAttente = cvs.stream()
                .filter(cv -> cv != null && "Attente".equals(cv.getStatus()))
                .toList();
        int nbCV = cvsEnAttente.size();
        return nbCV;
    }
}
