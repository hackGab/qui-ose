import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import ProfesseurHeader from "./ProfesseurHeader";
import { useTranslation } from "react-i18next";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";

const EvaluationConformiteOptions = [
    { label: "Totalement en accord", value: "TOTAL_EN_ACCORD" },
    { label: "Plutôt en accord", value: "PLUTOT_EN_ACCORD" },
    { label: "Plutôt en désaccord", value: "PLUTOT_EN_DESACCORD" },
    { label: "Totalement en désaccord", value: "TOTAL_EN_DESACCORD" },
    { label: "Impossible de se prononcer", value: "IMPOSSIBLE_SE_PRONONCER" }
];

function AccueilProfesseur() {
    const { t } = useTranslation();
    const location = useLocation();
    const { userData } = location.state || {};
    const [listeEvaluations, setListeEvaluations] = useState([]);
    const [selectedEvaluation, setSelectedEvaluation] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [evaluation, setEvaluation] = useState({
        tachesConformite: "",
        accueilIntegration: "",
        encadrementSuffisant: "",
        respectNormesHygiene: "",
        climatDeTravail: "",
        accesTransportCommun: "",
        salaireInteressant: "",
        communicationSuperviseur: "",
        equipementAdequat: "",
        volumeTravailAcceptable: "",
        heuresEncadrementPremierMois: "",
        heuresEncadrementDeuxiemeMois: "",
        heuresEncadrementTroisiemeMois: "",
        commentaires: "",
        privilegiePremierStage: false,
        privilegieDeuxiemeStage: false,
        nombreStagiairesAccueillis: "",
        souhaiteRevoirStagiaire: null,
    });

    useEffect(() => {
         if (userData?.credentials?.email) {
             console.log("userData", userData);
             const fetchEvaluations = async () => {
                 try {
                     const response = await fetch(`http://localhost:8081/professeur/evaluations/${userData.credentials.email}`);

                     if (!response.ok) {
                         throw new Error("Erreur lors de la récupération des évaluations");
                     }

                     console.log("response", response);

                     const data = await response.json();
                     console.log("data", data);
                     setListeEvaluations(data);
                 } catch (error) {
                     console.error("Erreur lors de la récupération des données :", error);
                 }
             };

             fetchEvaluations();
         }
     }, [userData?.credentials?.email]);

    const handleShowModal = (evaluation) => {
        setSelectedEvaluation(evaluation);
        console.log("Selected Evaluation:", evaluation);
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setSelectedEvaluation(null);
    };

    const handleChange = (e, field) => {
        setEvaluation({
            ...evaluation,
            [field]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        selectedEvaluation.tachesConformite = evaluation.tachesConformite;
        selectedEvaluation.accueilIntegration = evaluation.accueilIntegration;
        selectedEvaluation.encadrementSuffisant = evaluation.encadrementSuffisant;
        selectedEvaluation.respectNormesHygiene = evaluation.respectNormesHygiene;
        selectedEvaluation.climatDeTravail = evaluation.climatDeTravail;
        selectedEvaluation.accesTransportCommun = evaluation.accesTransportCommun;
        selectedEvaluation.salaireInteressant = evaluation.salaireInteressant;
        selectedEvaluation.communicationSuperviseur = evaluation.communicationSuperviseur;
        selectedEvaluation.equipementAdequat = evaluation.equipementAdequat;
        selectedEvaluation.volumeTravailAcceptable = evaluation.volumeTravailAcceptable;
        selectedEvaluation.heuresEncadrementPremierMois = evaluation.heuresEncadrementPremierMois;
        selectedEvaluation.heuresEncadrementDeuxiemeMois = evaluation.heuresEncadrementDeuxiemeMois;
        selectedEvaluation.heuresEncadrementTroisiemeMois = evaluation.heuresEncadrementTroisiemeMois;
        selectedEvaluation.commentaires = evaluation.commentaires;
        selectedEvaluation.privilegiePremierStage = evaluation.privilegiePremierStage;
        selectedEvaluation.privilegieDeuxiemeStage = evaluation.privilegieDeuxiemeStage;
        selectedEvaluation.nombreStagiairesAccueillis = evaluation.nombreStagiairesAccueillis;
        selectedEvaluation.souhaiteRevoirStagiaire = evaluation.souhaiteRevoirStagiaire;
        const date = new Date();
        const formattedDate = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getFullYear()).padStart(2, '0')}`;
        selectedEvaluation.dateSignature = formattedDate;
        selectedEvaluation.signatureEnseignant = userData.credentials.email;

        setEvaluation({
            tachesConformite: "",
            accueilIntegration: "",
            encadrementSuffisant: "",
            respectNormesHygiene: "",
            climatDeTravail: "",
            accesTransportCommun: "",
            salaireInteressant: "",
            communicationSuperviseur: "",
            equipementAdequat: "",
            volumeTravailAcceptable: "",
            heuresEncadrementPremierMois: "",
            heuresEncadrementDeuxiemeMois: "",
            heuresEncadrementTroisiemeMois: "",
            commentaires: "",
            privilegiePremierStage: false,
            privilegieDeuxiemeStage: false,
            nombreStagiairesAccueillis: "",
            souhaiteRevoirStagiaire: null,
        });

        await fetch(`http://localhost:8081/professeur/evaluerStage`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(selectedEvaluation)
        });
        handleCloseModal();
    };

    const generationPDF = async (evaluation) => {
        try {
            const response = await fetch(`http://localhost:8081/generatePDF/evaluationProf`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(evaluation)
            });

            if (!response.ok) {
                throw new Error("Erreur lors de la génération du PDF");
            }

            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement("a");
            link.href = url;
            link.download = "Evaluation_Stage_Prof.pdf";
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);

            console.log("PDF généré et téléchargé avec succès");
        } catch (error) {
            console.error("Erreur lors de la génération du PDF :", error);
        }
    };


    return (
        <>
            <ProfesseurHeader userData={userData} />
            <div className="container-fluid p-4">
                <h2 className="text-center my-4" style={{ color: "#01579b" }}>
                    {t('Bienvenue')}, {userData ? userData.firstName + " " + userData.lastName : ""}!
                </h2>

                <div className="container">
                    <h1 className="mb-4 text-center">{t('studentListTitleForEvaluation')}</h1>

                    {listeEvaluations.length === 0 ? (
                        <p className="text-center">{t('AucunEtudiantTrouve')}</p>
                    ) : (
                        <div className="row">
                            {listeEvaluations.map((evaluation) => (
                                <div className="col-12 col-md-6 col-lg-4 mb-4" key={evaluation.id}>
                                    <div
                                        className="card shadow"
                                        onClick={() => {
                                            if (!evaluation.signatureEnseignant) {
                                                handleShowModal(evaluation);
                                            }
                                        }}
                                        style={{ cursor: "pointer" }}
                                    >
                                        <div className="card-body">
                                            <h5 className="card-title">{`${evaluation.nomStagiaire}`}</h5>
                                            {evaluation.signatureEnseignant ? (
                                                <Button onClick={() => generationPDF(evaluation)} className="btn-success">
                                                    Générer Évaluation en PDF
                                                </Button>
                                            ) : null}
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>

            <Modal show={showModal} onHide={handleCloseModal} size="lg">
                <form onSubmit={handleSubmit}>
                    <Modal.Header closeButton>
                        <Modal.Title>Évaluation de {selectedEvaluation ? selectedEvaluation.nomStagiaire : null}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <h5 style={{
                            fontSize: "1.25rem",
                            fontWeight: "bold",
                            backgroundColor: "#f8f9fa",
                            padding: "10px",
                            borderRadius: "5px",
                            border: "1px solid #dee2e6",
                            marginBottom: "15px",
                            textAlign: "center"
                        }}>
                            Informations sur le stage
                        </h5>
                        <div className="mb-3">
                            <p><strong>Location du stage: </strong>{selectedEvaluation ? selectedEvaluation.adresse : null}</p>
                            <p><strong>Date du stage: </strong>{selectedEvaluation ? selectedEvaluation.dateStage : null}</p>
                            <p><strong>Nom de l'entreprise: </strong>{selectedEvaluation ? selectedEvaluation.nomEntreprise : null}</p>
                            <p><strong>Téléphone de l'employeur: </strong>{selectedEvaluation ? selectedEvaluation.telephone : null}</p>
                        </div>


                            <h5 style={{
                                fontSize: "1.25rem",
                                fontWeight: "bold",
                                backgroundColor: "#f8f9fa",
                                padding: "10px",
                                borderRadius: "5px",
                                border: "1px solid #dee2e6",
                                marginBottom: "15px",
                                textAlign: "center"
                            }}>
                                Évaluation des tâches
                            </h5>
                            <table className="table table-bordered">
                                <thead>
                                <tr>
                                    <th>Critères</th>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <th key={option.value} className="text-center">{option.label}</th>
                                    ))}
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>Les tâches confiées au stagiaire sont conformes aux tâches annoncées dans
                                        l'entente
                                        de stage.
                                    </td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="tachesConformite"
                                                value={option.value}
                                                checked={evaluation.tachesConformite === option.value}
                                                onChange={(e) => handleChange(e, "tachesConformite")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                <tr>
                                    <td>Des mesures d’accueil facilitent l’intégration du nouveau stagiaire.</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="accueilIntegration"
                                                value={option.value}
                                                checked={evaluation.accueilIntegration === option.value}
                                                onChange={(e) => handleChange(e, "accueilIntegration")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                <tr>
                                    <td>Le temps réel consacré à l’encadrement du stagiaire est suffisant.</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="encadrementSuffisant"
                                                value={option.value}
                                                checked={evaluation.encadrementSuffisant === option.value}
                                                onChange={(e) => handleChange(e, "encadrementSuffisant")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                </tbody>
                            </table>

                            <h5>Nombre d'heures/semaine</h5>
                            <div className="mb-3">
                                <label>Premier mois:</label>
                                <input
                                    type="number"
                                    className="form-control"
                                    min={1}
                                    value={evaluation.heuresEncadrementPremierMois}
                                    onChange={(e) => handleChange(e, "heuresEncadrementPremierMois")}
                                    placeholder="Heures"
                                    required
                                />
                            </div>
                            <div className="mb-3">
                                <label>Deuxième mois:</label>
                                <input
                                    type="number"
                                    min={1}
                                    className="form-control"
                                    value={evaluation.heuresEncadrementDeuxiemeMois}
                                    onChange={(e) => handleChange(e, "heuresEncadrementDeuxiemeMois")}
                                    placeholder="Heures"
                                    required
                                />
                            </div>
                            <div className="mb-3">
                                <label>Troisième mois:</label>
                                <input
                                    type="number"
                                    min={1}
                                    className="form-control"
                                    value={evaluation.heuresEncadrementTroisiemeMois}
                                    onChange={(e) => handleChange(e, "heuresEncadrementTroisiemeMois")}
                                    placeholder="Heures"
                                    required
                                />
                            </div>

                            <h5>Autres critères d'évaluation</h5>
                            <table className="table table-bordered">
                                <thead>
                                <tr>
                                    <th>Critères</th>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <th key={option.value} className="text-center">{option.label}</th>
                                    ))}
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>L’environnement de travail respecte les normes d’hygiène et de sécurité au
                                        travail.
                                    </td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="respectNormesHygiene"
                                                value={option.value}
                                                checked={evaluation.respectNormesHygiene === option.value}
                                                onChange={(e) => handleChange(e, "respectNormesHygiene")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                <tr>
                                    <td>Le climat de travail est agréable.</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="climatDeTravail"
                                                value={option.value}
                                                checked={evaluation.climatDeTravail === option.value}
                                                onChange={(e) => handleChange(e, "climatDeTravail")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                <tr>
                                    <td>Le milieu de stage est accessible par transport en commun.</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="accesTransportCommun"
                                                value={option.value}
                                                checked={evaluation.accesTransportCommun === option.value}
                                                onChange={(e) => handleChange(e, "accesTransportCommun")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                <tr>
                                    <td>Le salaire offert est intéressant pour le stagiaire.</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="salaireInteressant"
                                                value={option.value}
                                                checked={evaluation.salaireInteressant === option.value}
                                                onChange={(e) => handleChange(e, "salaireInteressant")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                <tr>
                                    <td>La communication avec le superviseur de stage facilite le déroulement du
                                        stage.
                                    </td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="communicationSuperviseur"
                                                value={option.value}
                                                checked={evaluation.communicationSuperviseur === option.value}
                                                onChange={(e) => handleChange(e, "communicationSuperviseur")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                <tr>
                                    <td>L’équipement fourni est adéquat pour réaliser les tâches confiées.</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="equipementAdequat"
                                                value={option.value}
                                                checked={evaluation.equipementAdequat === option.value}
                                                onChange={(e) => handleChange(e, "equipementAdequat")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                <tr>
                                    <td>Le volume de travail est acceptable.</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="volumeTravailAcceptable"
                                                value={option.value}
                                                checked={evaluation.volumeTravailAcceptable === option.value}
                                                onChange={(e) => handleChange(e, "volumeTravailAcceptable")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                </tbody>
                            </table>
                            <h5 style={{
                                fontSize: "1.25rem",
                                fontWeight: "bold",
                                backgroundColor: "#f8f9fa",
                                padding: "10px",
                                borderRadius: "5px",
                                border: "1px solid #dee2e6",
                                marginBottom: "15px",
                                textAlign: "center"
                            }}>
                                Commentaires
                            </h5>

                            <div className="mb-3">
                            <textarea
                                className="form-control"
                                value={evaluation.commentaires || ""}
                                onChange={(e) => handleChange(e, "commentaires")}
                                placeholder="Ajoutez vos commentaires ici"
                                required
                            />
                            </div>
                            <h5 style={{
                                fontSize: "1.25rem",
                                fontWeight: "bold",
                                backgroundColor: "#f8f9fa",
                                padding: "10px",
                                borderRadius: "5px",
                                border: "1px solid #dee2e6",
                                marginBottom: "15px",
                                textAlign: "center"
                            }}>
                                Observations Générales
                            </h5>

                            <div className="mb-3">
                                <label>Ce milieu est à privilégier pour le stage :</label>
                                <div className="form-check">
                                    <input
                                        type="checkbox"
                                        className="form-check-input"
                                        checked={evaluation.privilegiePremierStage}
                                        onChange={(e) => {
                                            setEvaluation({
                                                ...evaluation,
                                                privilegiePremierStage: e.target.checked,
                                                privilegieDeuxiemeStage: !e.target.checked
                                            });
                                        }}
                                    />
                                    <label className="form-check-label">Premier stage</label>
                                </div>
                                <div className="form-check">
                                    <input
                                        type="checkbox"
                                        className="form-check-input"
                                        checked={evaluation.privilegieDeuxiemeStage}
                                        onChange={(e) => {
                                            setEvaluation({
                                                ...evaluation,
                                                privilegiePremierStage: !e.target.checked,
                                                privilegieDeuxiemeStage: e.target.checked
                                            });
                                        }}
                                    />
                                    <label className="form-check-label">Deuxième stage</label>
                                </div>
                            </div>

                            <div className="mb-3">
                                <label>Ce milieu est ouvert à accueillir :</label>
                                <input
                                    type="number"
                                    className="form-control"
                                    min={1}
                                    value={evaluation.nombreStagiairesAccueillis || ""}
                                    onChange={(e) => handleChange(e, "nombreStagiairesAccueillis")}
                                    placeholder="Nombre de stagiaires"
                                    required
                                />
                            </div>

                            <div className="mb-3">
                                <label>Ce milieu désire accueillir le même stagiaire pour un prochain stage :</label>
                                <br></br>
                                <div className="form-check form-check-inline">
                                    <input
                                        type="radio"
                                        className="form-check-input"
                                        name="souhaiteRevoirStagiaire"
                                        checked={evaluation.souhaiteRevoirStagiaire === true}
                                        onChange={() => setEvaluation({...evaluation, souhaiteRevoirStagiaire: true})}
                                        required
                                    />
                                    <label className="form-check-label">Oui</label>
                                </div>
                                <div className="form-check form-check-inline">
                                    <input
                                        type="radio"
                                        className="form-check-input"
                                        name="souhaiteRevoirStagiaire"
                                        checked={evaluation.souhaiteRevoirStagiaire === false}
                                        onChange={() => setEvaluation({...evaluation, souhaiteRevoirStagiaire: false})}
                                        required
                                    />
                                    <label className="form-check-label">Non</label>
                                </div>
                            </div>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={handleCloseModal}>
                            {t('close')}
                        </Button>
                        <Button variant="primary" type="submit">
                            Submit
                        </Button>
                    </Modal.Footer>
                </form>
            </Modal>
        </>
);
}

export default AccueilProfesseur;
