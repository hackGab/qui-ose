package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.*;
import com.lacouf.rsbjwt.repository.*;
import com.lacouf.rsbjwt.service.dto.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EtudiantService {

    private final UserAppRepository userAppRepository;
    private final EtudiantRepository etudiantRepository;
    private final PasswordEncoder passwordEncoder;
    private final CVRepository cvRepository;
    private final ContratRepository contratRepository;
    private final OffreDeStageRepository offreDeStageRepository;

    private final EntrevueRepository entrevueRepository;

    public EtudiantService(UserAppRepository userAppRepository, EtudiantRepository etudiantRepository, PasswordEncoder passwordEncoder, CVRepository cvRepository, OffreDeStageRepository offreDeStageRepository, EntrevueRepository entrevueRepository, ContratRepository contratRepository) {
        this.userAppRepository = userAppRepository;
        this.etudiantRepository = etudiantRepository;
        this.passwordEncoder = passwordEncoder;
        this.cvRepository = cvRepository;
        this.offreDeStageRepository = offreDeStageRepository;
        this.entrevueRepository = entrevueRepository;
        this.contratRepository = contratRepository;
    }

    public Optional<EtudiantDTO> creerEtudiant(EtudiantDTO etudiantDTO) {
        try {
            String encodedPassword = passwordEncoder.encode(etudiantDTO.getCredentials().getPassword());
            Etudiant etudiant = new Etudiant(
                    etudiantDTO.getFirstName(),
                    etudiantDTO.getLastName(),
                    etudiantDTO.getCredentials().getEmail(),
                    encodedPassword,
                    etudiantDTO.getPhoneNumber(),
                    etudiantDTO.getDepartement()
            );
            Etudiant savedEtudiant = etudiantRepository.save(etudiant);
            return Optional.of(new EtudiantDTO(savedEtudiant));
        } catch (Exception e) {
            return Optional.empty();
        }
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

    public Iterable<EtudiantDTO> getAllEtudiants() {
        return etudiantRepository.findAll().stream()
                .map(EtudiantDTO::new)
                .toList();
    }

    public List<OffreDeStageDTO> getOffresApprouvees() {
        return offreDeStageRepository.findAll().stream()
                .filter(offreDeStage -> offreDeStage.getStatus().equals("Validé"))
                .map(OffreDeStageDTO::new)
                .toList();
    }

    public Optional<EtudiantDTO> ajouterOffreDeStage(String etudiantEmail, Long offreId) {
        Optional<Etudiant> etudiantOpt = Optional.ofNullable(etudiantRepository.findByEmail(etudiantEmail));
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

    public Iterable<OffreDeStageDTO> getOffresDeStage(String etudiantEmail) {
        return etudiantRepository.findByEmail(etudiantEmail)
                .getOffresAppliquees().stream()
                .map(OffreDeStageDTO::new)
                .toList();
    }

    public Optional<EtudiantDTO> retirerOffreDeStage (String email, Long offreId) {
        Optional<Etudiant> etudiantOpt = Optional.ofNullable(etudiantRepository.findByEmail(email));
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
        Etudiant etudiant = etudiantRepository.findByEmail(email);
        Long etudiantId = etudiant.getId();
        return entrevueRepository.findAllByEtudiantId(etudiantId).stream()
                .map(EntrevueDTO::new)
                .toList();
    }

    public List<EntrevueDTO> getEntrevuesEnAttenteByEtudiant(String email) {
        Etudiant etudiant = etudiantRepository.findByEmail(email);
        System.out.println(etudiant);
        Long etudiantId = etudiant.getId();
        return entrevueRepository.findAllByEtudiantId(etudiantId).stream()
                .filter(entrevue -> entrevue.getStatus().equals("En attente"))
                .map(EntrevueDTO::new)
                .toList();
    }

    public Optional<EntrevueDTO> changerStatusEntrevue(String emailEtudiant, Long idOffreDeStage, String status) {
        Etudiant etudiant = etudiantRepository.findByEmail(emailEtudiant);
        Long etudiantId = etudiant.getId();
        Optional<Entrevue> entrevueOpt = entrevueRepository.findByEtudiantIdAndOffreDeStageId(etudiantId, idOffreDeStage);

        if (entrevueOpt.isPresent()) {
            Entrevue entrevue = entrevueOpt.get();
            entrevue.setStatus(status);
            entrevueRepository.save(entrevue);
            return Optional.of(new EntrevueDTO(entrevue));
        }

        return Optional.empty();
    }

    public List<EntrevueDTO> getEntrevuesAccepteesByEtudiant(String email) {
        Etudiant etudiant = etudiantRepository.findByEmail(email);
        Long etudiantId = etudiant.getId();
        return entrevueRepository.findAllByEtudiantId(etudiantId).stream()
                .filter(entrevue -> entrevue.getStatus().equals("Accepter"))
                .map(EntrevueDTO::new)
                .toList();
    }

    public List<ContratDTO> getContratsByEtudiant(String email) {
        Etudiant etudiant = etudiantRepository.findByEmail(email);
        Long etudiantId = etudiant.getId();
        return contratRepository.findContratsByEtudiantEmail(etudiant).stream()
                .map(ContratDTO::new)
                .toList();
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
        Etudiant etudiant = etudiantRepository.findByEmail(email);
        return contratRepository.findContratsByEtudiantEmail(etudiant).stream()
                .filter(contrat -> !contrat.isEtudiantSigne())
                .map(ContratDTO::new)
                .toList();
    }

}
