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
    const [listeEtudiants, setListeEtudiants] = useState([]);
    const [selectedEtudiant, setSelectedEtudiant] = useState(null);
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
        if (userData) {
            const url = `http://localhost:8081/professeur/etudiants/${userData.credentials.email}`;
            fetch(url)
                .then(response => response.json())
                .then(data => setListeEtudiants(data));
        }
    }, [userData]);

    const handleShowModal = (etudiant) => {
        setSelectedEtudiant(etudiant);
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setSelectedEtudiant(null);
    };

    const handleChange = (e, field) => {
        setEvaluation({
            ...evaluation,
            [field]: e.target.value
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        const isFormComplete = Object.values(evaluation).every(value => value !== "");

        if (!isFormComplete) {
            alert("Veuillez remplir tous les champs obligatoires.");
            return;
        }

        console.log("Evaluation Data:", evaluation);
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

                    {listeEtudiants.length === 0 ? (
                        <p className="text-center">{t('AucunEtudiantTrouve')}</p>
                    ) : (
                        <div className="row">
                            {listeEtudiants.map((etudiant) => (
                                <div className="col-12 col-md-6 col-lg-4 mb-4" key={etudiant.id}>
                                    <div
                                        className="card shadow"
                                        onClick={() => handleShowModal(etudiant)}
                                        style={{ cursor: "pointer" }}
                                    >
                                        <div className="card-body">
                                            <h5 className="card-title">{`${etudiant.firstName} ${etudiant.lastName}`}</h5>
                                            <p className="card-text">
                                                {etudiant.credentials.email}<br />
                                                {etudiant.phoneNumber}
                                            </p>
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
                        <Modal.Title>Évaluation de {selectedEtudiant?.firstName} {selectedEtudiant?.lastName}</Modal.Title>
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
                                <td>Les tâches confiées au stagiaire sont conformes aux tâches annoncées dans l'entente
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
                                <td>La communication avec le superviseur de stage facilite le déroulement du stage.</td>
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

                        <div className="mb-3">
                            <label>Ce milieu offre des quarts de travail variables :</label>
                            <div className="row">
                                {[1, 2, 3].map((i) => (
                                    <div className="col-4 mb-2" key={i}>
                                        <label>De:</label>
                                        <input
                                            type="time"
                                            className="form-control"
                                            value={evaluation[`quartTravailDe${i}`] || ""}
                                            onChange={(e) => handleChange(e, `quartTravailDe${i}`)}
                                            required
                                        />
                                        <label>à</label>
                                        <input
                                            type="time"
                                            className="form-control"
                                            value={evaluation[`quartTravailA${i}`] || ""}
                                            onChange={(e) => handleChange(e, `quartTravailA${i}`)}
                                            required
                                        />
                                    </div>
                                ))}
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
