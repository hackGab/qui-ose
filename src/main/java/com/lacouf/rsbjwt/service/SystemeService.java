package com.lacouf.rsbjwt.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lacouf.rsbjwt.model.Contrat;
import com.lacouf.rsbjwt.model.Employeur;
import com.lacouf.rsbjwt.model.Etudiant;
import com.lacouf.rsbjwt.model.EvaluationStageProf;
import com.lacouf.rsbjwt.repository.ContratRepository;
import com.lacouf.rsbjwt.service.dto.ContratDTO;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import com.lacouf.rsbjwt.service.dto.EvaluationStageProfDTO;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Optional;

@Service
public class SystemeService {

    private ContratRepository contratRepository;

    public SystemeService(ContratRepository contratRepository) {
        this.contratRepository = contratRepository;
    }

    public byte[] generateContratPDF(ContratDTO contrat) throws Exception {
        Contrat contratRecu = contratRepository.findByUUID(contrat.getUUID()).orElse(null);

        if (contratRecu == null) {
            throw new IllegalArgumentException("Contrat non trouvé");
        }

        Etudiant etudiant = contratRecu.getCandidature().getEntrevue().getEtudiant();
        Employeur employeur = contratRecu.getCandidature().getEntrevue().getOffreDeStage().getEmployeur();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();

        Paragraph titlePage = new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\n\nCONTRAT DE STAGE", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK));
        titlePage.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(titlePage);
        document.newPage();

        Paragraph ententeTitle = new Paragraph("ENTENTE DE STAGE INTERVENUE ENTRE LES PARTIES SUIVANTES\n\n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK));
        ententeTitle.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(ententeTitle);
        Paragraph intro = new Paragraph("Dans le cadre de la formule ATE, les parties citées ci-dessous :\n\n");
        intro.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(intro);

        Paragraph gestionnaire = new Paragraph("Le gestionnaire de stage, Thiraiyan Moon\n\net\n\n");
        gestionnaire.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(gestionnaire);

        Paragraph employeurNom = new Paragraph("L’employeur, " + employeur.getFirstName() + " " + employeur.getLastName() + "\n\net\n\n");
        employeurNom.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(employeurNom);

        Paragraph etudiantNom = new Paragraph("L’étudiant(e), " + etudiant.getFirstName() + " " + etudiant.getLastName() + ",\n\n");
        etudiantNom.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(etudiantNom);

        Paragraph conditions = new Paragraph("Conviennent des conditions de stage suivantes :\n\n");
        conditions.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(conditions);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Paragraph("ENDROIT DU STAGE"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        table.addCell("Adresse : " + contrat.getLieuStage());

        cell = new PdfPCell(new Paragraph("DUREE DU STAGE"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        table.addCell("Date de début : " + contrat.getDateDebut() + "\nDate de fin : " + contrat.getDateFin() + "\nNombre total de semaines : " + contrat.getNbSemaines());

        cell = new PdfPCell(new Paragraph("HORAIRE DE TRAVAIL"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        table.addCell("Horaire de travail: " + contrat.getHeureHorraireDebut() + " - " + contrat.getHeureHorraireFin() + "\nNombre total d’heures par semaine : " + contrat.getHeuresParSemaine() + "h");

        cell = new PdfPCell(new Paragraph("SALAIRE"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        table.addCell("Salaire horaire : " + contrat.getTauxHoraire());

        document.add(table);
        document.newPage();

        Paragraph tachesTitle = new Paragraph("TACHES ET RESPONSABILITES DU STAGIAIRE\n\n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK));
        tachesTitle.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(tachesTitle);
        document.add(new Paragraph(contrat.getDescription() + "\n\n"));

        Paragraph respTitle = new Paragraph("RESPONSABILITES\n\n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK));
        respTitle.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(respTitle);
        document.add(new Paragraph("Le Collège s’engage à : \n" + contrat.getCollegeEngagement() + "\n\nL’entreprise s’engage à : \n" + contrat.getEntrepriseEngagement() + "\n\nL’étudiant s’engage à : \n" + contrat.getEtudiantEngagement() + "\n\n"));

        Paragraph signaturesTitle = new Paragraph("SIGNATURES\n\n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK));
        signaturesTitle.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(signaturesTitle);
        Paragraph partiesEngagent = new Paragraph("Les parties s’engagent à respecter cette entente de stage\n\n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
        partiesEngagent.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(partiesEngagent);

        document.add(new Paragraph("En foi de quoi les parties ont signé,\n\n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));

        document.add(new Paragraph("L’étudiant(e) :", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));

        PdfPTable etudiantTable = new PdfPTable(2);
        etudiantTable.setWidthPercentage(100);
        etudiantTable.setWidths(new float[]{3, 1});

        PdfPCell etudiantEmailCell = new PdfPCell(new Paragraph(etudiant.getEmail(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
        etudiantEmailCell.setBorder(PdfPCell.NO_BORDER);
        etudiantTable.addCell(etudiantEmailCell);

        PdfPCell etudiantDateCell = new PdfPCell(new Paragraph("" + contrat.getDateSignatureEtudiant(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
        etudiantDateCell.setBorder(PdfPCell.NO_BORDER);
        etudiantDateCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        etudiantTable.addCell(etudiantDateCell);

        PdfPCell etudiantLineCell = new PdfPCell(new Paragraph("_____________________________________________________________________________"));
        etudiantLineCell.setColspan(2);
        etudiantLineCell.setBorder(PdfPCell.NO_BORDER);
        etudiantTable.addCell(etudiantLineCell);

        PdfPCell etudiantNameCell = new PdfPCell(new Paragraph(etudiant.getFirstName() + " " + etudiant.getLastName(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
        etudiantNameCell.setBorder(PdfPCell.NO_BORDER);
        etudiantTable.addCell(etudiantNameCell);

        PdfPCell etudiantDateLabelCell = new PdfPCell(new Paragraph("Date", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        etudiantDateLabelCell.setBorder(PdfPCell.NO_BORDER);
        etudiantDateLabelCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        etudiantTable.addCell(etudiantDateLabelCell);

        document.add(etudiantTable);

        document.add(new Paragraph("\nL’employeur :", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));

        PdfPTable employeurTable = new PdfPTable(2);
        employeurTable.setWidthPercentage(100);
        employeurTable.setWidths(new float[]{3, 1});

        PdfPCell employeurEmailCell = new PdfPCell(new Paragraph(employeur.getEmail(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
        employeurEmailCell.setBorder(PdfPCell.NO_BORDER);
        employeurTable.addCell(employeurEmailCell);

        PdfPCell employeurDateCell = new PdfPCell(new Paragraph("" + contrat.getDateSignatureEmployeur(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
        employeurDateCell.setBorder(PdfPCell.NO_BORDER);
        employeurDateCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        employeurTable.addCell(employeurDateCell);

        PdfPCell employeurLineCell = new PdfPCell(new Paragraph("_____________________________________________________________________________"));
        employeurLineCell.setColspan(2);
        employeurLineCell.setBorder(PdfPCell.NO_BORDER);
        employeurTable.addCell(employeurLineCell);

        PdfPCell employeurNameCell = new PdfPCell(new Paragraph(employeur.getFirstName() + " " + employeur.getLastName(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
        employeurNameCell.setBorder(PdfPCell.NO_BORDER);
        employeurTable.addCell(employeurNameCell);

        PdfPCell employeurDateLabelCell = new PdfPCell(new Paragraph("Date", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        employeurDateLabelCell.setBorder(PdfPCell.NO_BORDER);
        employeurDateLabelCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        employeurTable.addCell(employeurDateLabelCell);

        document.add(employeurTable);

        document.add(new Paragraph("\nLe gestionnaire de stage :", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));

        PdfPTable gestionnaireTable = new PdfPTable(2);
        gestionnaireTable.setWidthPercentage(100);
        gestionnaireTable.setWidths(new float[]{3, 1});

        PdfPCell gestionnaireEmailCell = new PdfPCell(new Paragraph("niseiyen@gmail.com", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        gestionnaireEmailCell.setBorder(PdfPCell.NO_BORDER);
        gestionnaireTable.addCell(gestionnaireEmailCell);

        PdfPCell gestionnaireDateCell = new PdfPCell(new Paragraph("" + contrat.getDateSignatureGestionnaire(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
        gestionnaireDateCell.setBorder(PdfPCell.NO_BORDER);
        gestionnaireDateCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        gestionnaireTable.addCell(gestionnaireDateCell);

        PdfPCell gestionnaireLineCell = new PdfPCell(new Paragraph("_____________________________________________________________________________"));
        gestionnaireLineCell.setColspan(2);
        gestionnaireLineCell.setBorder(PdfPCell.NO_BORDER);
        gestionnaireTable.addCell(gestionnaireLineCell);

        PdfPCell gestionnaireNameCell = new PdfPCell(new Paragraph("Thiraiyan Moon", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        gestionnaireNameCell.setBorder(PdfPCell.NO_BORDER);
        gestionnaireTable.addCell(gestionnaireNameCell);

        PdfPCell gestionnaireDateLabelCell = new PdfPCell(new Paragraph("Date", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        gestionnaireDateLabelCell.setBorder(PdfPCell.NO_BORDER);
        gestionnaireDateLabelCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        gestionnaireTable.addCell(gestionnaireDateLabelCell);

        document.add(gestionnaireTable);

        document.close();

        return byteArrayOutputStream.toByteArray();
    }

    public byte[] generateEvaluationProfPDF(EvaluationStageProfDTO evaluation) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        document.open();

        // Titre
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        document.add(new Paragraph("ÉVALUATION DU MILIEU DE STAGE", titleFont));
        document.add(Chunk.NEWLINE);

        // Identification de l'entreprise
        document.add(new Paragraph("IDENTIFICATION DE L'ENTREPRISE"));
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell("Nom de l'entreprise :");
        table.addCell(evaluation.getNomEntreprise());
        table.addCell("Personne contact :");
        table.addCell(evaluation.getPersonneContact());
        table.addCell("Adresse :");
        table.addCell(evaluation.getAdresse());
        table.addCell("Téléphone :");
        table.addCell(evaluation.getTelephone());

        document.add(table);
        document.add(Chunk.NEWLINE);

        // Identification du stagiaire
        document.add(new Paragraph("IDENTIFICATION DU STAGIAIRE"));
        PdfPTable table2 = new PdfPTable(2);
        table2.setWidthPercentage(100);

        table2.addCell("Nom du stagiaire :");
        table2.addCell(evaluation.getNomStagiaire());
        table2.addCell("Date du stage :");
        table2.addCell(evaluation.getDateStage().toString());

        document.add(table2);
        document.add(Chunk.NEWLINE);

        // Évaluation des tâches
        document.add(new Paragraph("ÉVALUATION DES TÂCHES"));
        PdfPTable evaluationTable = new PdfPTable(6);
        evaluationTable.setWidthPercentage(100);

        // Header de l'évaluation
        evaluationTable.addCell("Évaluation");
        evaluationTable.addCell("Totalement en accord");
        evaluationTable.addCell("Plutôt en accord");
        evaluationTable.addCell("Plutôt en désaccord");
        evaluationTable.addCell("Totalement en désaccord");
        evaluationTable.addCell("Impossible de se prononcer");

        // Evaluation cochées
        addEvaluationRow(evaluationTable, "Les tâches confiées au stagiaire sont conformes", convertToDtoConformite(evaluation.getTachesConformite()));
        addEvaluationRow(evaluationTable, "Accueil et intégration", convertToDtoConformite(evaluation.getAccueilIntegration()));
        addEvaluationRow(evaluationTable, "Encadrement suffisant", convertToDtoConformite(evaluation.getEncadrementSuffisant()));
        addEvaluationRow(evaluationTable, "Respect des normes d'hygiène", convertToDtoConformite(evaluation.getRespectNormesHygiene()));
        addEvaluationRow(evaluationTable, "Climat de travail", convertToDtoConformite(evaluation.getClimatDeTravail()));
        addEvaluationRow(evaluationTable, "Accès transport commun", convertToDtoConformite(evaluation.getAccesTransportCommun()));
        addEvaluationRow(evaluationTable,
                "Salaire intéressant: " + String.format("%.2f", evaluation.getSalaireHoraire()) + "$/l'heure",
                convertToDtoConformite(evaluation.getSalaireInteressant()));
        addEvaluationRow(evaluationTable, "Communication superviseur", convertToDtoConformite(evaluation.getCommunicationSuperviseur()));
        addEvaluationRow(evaluationTable, "Équipement adéquat", convertToDtoConformite(evaluation.getEquipementAdequat()));
        addEvaluationRow(evaluationTable, "Volume de travail acceptable", convertToDtoConformite(evaluation.getVolumeTravailAcceptable()));

        document.add(evaluationTable);
        document.add(Chunk.NEWLINE);

        // Préciser le nombre d'heures/semaine
        document.add(new Paragraph("Préciser le nombre d'heures/semaine :"));
        PdfPTable hoursTable = new PdfPTable(2);
        hoursTable.setWidthPercentage(100);
        hoursTable.setSpacingBefore(5f);

        // Premier mois
        hoursTable.addCell("Premier mois :");
        hoursTable.addCell(new Phrase(evaluation.getHeuresEncadrementPremierMois() + " heures"));

        // Deuxième mois
        hoursTable.addCell("Deuxième mois :");
        hoursTable.addCell(new Phrase(evaluation.getHeuresEncadrementDeuxiemeMois() + " heures"));

        // Troisième mois
        hoursTable.addCell("Troisième mois :");
        hoursTable.addCell(new Phrase(evaluation.getHeuresEncadrementTroisiemeMois() + " heures"));

        document.add(hoursTable);
        document.add(Chunk.NEWLINE);


        // Comments and signature
        document.add(new Paragraph("\nCOMMENTAIRES"));
        document.add(new Paragraph(evaluation.getCommentaires()));
        document.add(Chunk.NEWLINE);

        // Observations générales
        document.add(new Paragraph("OBSERVATIONS GÉNÉRALES"));
        PdfPTable observationsTable = new PdfPTable(2);
        observationsTable.setWidthPercentage(100);

        observationsTable.addCell("Ce milieu est à privilégier pour :");
        observationsTable.addCell(evaluation.isPrivilegiePremierStage() ? "premier stage" : "deuxième stage");
        observationsTable.addCell("Nombre de stagiaires accueillis :");
        observationsTable.addCell(String.valueOf(evaluation.getNombreStagiairesAccueillis()));
        observationsTable.addCell("Souhaite revoir le stagiaire :");
        observationsTable.addCell(evaluation.isSouhaiteRevoirStagiaire() ? "Oui" : "Non");

        document.add(observationsTable);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        // Signature et date
        PdfPTable signatureTable = new PdfPTable(2);
        signatureTable.setWidthPercentage(100);
        signatureTable.setSpacingBefore(10f);

        PdfPCell emailCell = new PdfPCell(new Phrase(evaluation.getSignatureEnseignant()));
        emailCell.setBorder(PdfPCell.NO_BORDER);
        emailCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell dateOfSignatureCell = new PdfPCell(new Phrase(evaluation.getDateSignature()));
        dateOfSignatureCell.setBorder(PdfPCell.NO_BORDER);
        dateOfSignatureCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        signatureTable.addCell(emailCell);
        signatureTable.addCell(dateOfSignatureCell);

        PdfPCell signatureCell = new PdfPCell(new Phrase("Signature de l'enseignant responsable du stagiaire"));
        signatureCell.setBorder(PdfPCell.TOP);
        signatureCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        signatureCell.setPaddingTop(10f);

        PdfPCell dateCell = new PdfPCell(new Phrase("Date"));
        dateCell.setBorder(PdfPCell.TOP);
        dateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        dateCell.setPaddingTop(10f);

        signatureTable.addCell(signatureCell);
        signatureTable.addCell(dateCell);

        document.add(signatureTable);

        document.close();
        return baos.toByteArray();
    }

    private void addEvaluationRow(PdfPTable table, String description, EvaluationStageProfDTO.EvaluationConformite conformite) {
        table.addCell(description);
        table.addCell(conformite == EvaluationStageProfDTO.EvaluationConformite.TOTAL_EN_ACCORD ? "X" : "");
        table.addCell(conformite == EvaluationStageProfDTO.EvaluationConformite.PLUTOT_EN_ACCORD ? "X" : "");
        table.addCell(conformite == EvaluationStageProfDTO.EvaluationConformite.PLUTOT_EN_DESACCORD ? "X" : "");
        table.addCell(conformite == EvaluationStageProfDTO.EvaluationConformite.TOTAL_EN_DESACCORD ? "X" : "");
        table.addCell(conformite == EvaluationStageProfDTO.EvaluationConformite.IMPOSSIBLE_SE_PRONONCER ? "X" : "");
    }

    private EvaluationStageProfDTO.EvaluationConformite convertToDtoConformite(EvaluationStageProf.EvaluationConformite conformite) {
        return EvaluationStageProfDTO.EvaluationConformite.valueOf(conformite.name());
    }
}
