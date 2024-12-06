import React, {useEffect, useState} from "react";
import {useLocation} from "react-router-dom";
import ProfesseurHeader from "../Header/ProfesseurHeader";
import {useTranslation} from "react-i18next";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";


function AccueilProfesseur() {
    const {t} = useTranslation();

    const EvaluationConformiteOptions = [
        {label: t("TotalementEnAccord"), value: "TOTAL_EN_ACCORD"},
        {label: t("PlutotEnAccord"), value: "PLUTOT_EN_ACCORD"},
        {label: t("PlutotEnDesaccord"), value: "PLUTOT_EN_DESACCORD"},
        {label: t("TotalementEnDesaccord"), value: "TOTAL_EN_DESACCORD"},
        {label: t("ImpossibleDeSePrononcer"), value: "IMPOSSIBLE_SE_PRONONCER"}
    ];
    const location = useLocation();
    const {userData} = location.state || {};
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
        privilegieTroisiemeStage: false,
        nombreStagiairesAccueillis: "",
        souhaiteRevoirStagiaire: null,
    });

    useEffect(() => {
        if (userData?.credentials?.email) {
            const fetchEvaluations = async () => {
                try {
                    const response = await fetch(`http://localhost:8081/professeur/evaluations/${userData.credentials.email}`);

                    if (!response.ok) {
                        throw new Error("Erreur lors de la récupération des évaluations");
                    }

                    const data = await response.json();
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
        const formattedDate = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
        selectedEvaluation.dateSignature = formattedDate;
        selectedEvaluation.signatureEnseignant = userData.credentials.email;

        setListeEvaluations(listeEvaluations.map((evaluation) => {
            if (evaluation.id === selectedEvaluation.id) {
                return selectedEvaluation;
            }
            return evaluation;
        }));

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
        } catch (error) {
            console.error("Erreur lors de la génération du PDF :", error);
        }
    };

    return (
        <>
            <ProfesseurHeader userData={userData}/>
            <div className="container-fluid p-4">
                <h2 className="text-center my-4" style={{color: "#01579b"}}>
                    {t('Bienvenue')}, {userData ? userData.firstName + " " + userData.lastName : ""}!
                </h2>

                <div className="container">
                    <h1 className="mb-4 text-center">{t('studentListTitleForEvaluation')}</h1>

                    <h5 className="mb-4 text-center">{t('studentListSubtitleForEvaluation')}</h5>

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
                                        style={{cursor: "pointer"}}
                                    >
                                        <div className="card-body">
                                            <h5 className="card-title text-primary">{evaluation.nomStagiaire}</h5>
                                            <p className="card-text">
                                                <span
                                                    className="fw-bold">{t('Entreprise')}: </span>{evaluation.nomEntreprise}
                                            </p>
                                            <p className="card-text">
                                                <span
                                                    className="fw-bold">{t('department')}: </span>{userData.departement.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, char => char.toUpperCase())}
                                            </p>

                                            {evaluation.signatureEnseignant && (
                                                <Button onClick={() => generationPDF(evaluation)}
                                                        className="btn btn-success mt-3">
                                                    {t('GenererEvaluationPDF')}
                                                </Button>
                                            )}
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
                        <Modal.Title>{t('Évaluation de')} {selectedEvaluation ? selectedEvaluation.nomStagiaire : null}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>

                        <div className="mb-3">
                            <p className="fs-3">
                            <strong>{t('LocationDuStage')}: </strong>{selectedEvaluation ? selectedEvaluation.adresse : null}
                            </p>
                            <p className="fs-3">
                                <strong>{t('DateDuStage')}: </strong>{selectedEvaluation ? selectedEvaluation.dateStage : null}
                            </p>
                            <p className="fs-3">
                                <strong>{t('NomEntreprise')}: </strong>{selectedEvaluation ? selectedEvaluation.nomEntreprise : null}
                            </p>
                            <p className="fs-3">
                                <strong>{t('TelephoneEmployeur')}: </strong>{selectedEvaluation ? selectedEvaluation.telephone : null}
                            </p>
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
                            {t('Évaluation des tâches')}
                        </h5>
                        <table className="table table-bordered">
                            <thead>
                            <tr>
                                <th>{t('Critères')}</th>
                                {EvaluationConformiteOptions.map((option) => (
                                    <th key={option.value} className="text-center">{option.label}</th>
                                ))}
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>{t('LesTachesConformite')}</td>
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
                                <td>{t('DesMesuresAccueil')}</td>
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
                                <td>{t('EncadrementSuffisant')}</td>
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

                        <h5>{t('NombreHeuresSemaine')}</h5>
                        <div className="mb-3">
                            <label>{t('PremierMois')}:</label>
                            <input
                                type="number"
                                className="form-control"
                                min={1}
                                value={evaluation.heuresEncadrementPremierMois}
                                onChange={(e) => handleChange(e, "heuresEncadrementPremierMois")}
                                placeholder={t('Heures')}
                                required
                            />
                        </div>
                        <div className="mb-3">
                            <label>{t('DeuxiemeMois')}:</label>
                            <input
                                type="number"
                                min={1}
                                className="form-control"
                                value={evaluation.heuresEncadrementDeuxiemeMois}
                                onChange={(e) => handleChange(e, "heuresEncadrementDeuxiemeMois")}
                                placeholder={t('Heures')}
                                required
                            />
                        </div>
                        <div className="mb-3">
                            <label>{t('TroisiemeMois')}:</label>
                            <input
                                type="number"
                                min={1}
                                className="form-control"
                                value={evaluation.heuresEncadrementTroisiemeMois}
                                onChange={(e) => handleChange(e, "heuresEncadrementTroisiemeMois")}
                                placeholder={t('Heures')}
                                required
                            />
                        </div>

                        <h5>{t('AutresCriteresEvaluation')}</h5>
                        <table className="table table-bordered">
                            <thead>
                            <tr>
                                <th>{t('Criteres')}</th>
                                {EvaluationConformiteOptions.map((option) => (
                                    <th key={option.value} className="text-center">{option.label}</th>
                                ))}
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>{t('RespectNormesHygiene')}</td>
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
                                <td>{t('ClimatDeTravail')}</td>
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
                                <td>{t('AccesTransportCommun')}</td>
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
                                <td>{t('SalaireInteressant')}</td>
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
                                <td>{t('CommunicationSuperviseur')}</td>
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
                                <td>{t('EquipementAdequat')}</td>
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
                                <td>{t('VolumeTravailAcceptable')}</td>
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
                            {t('Commentaires')}
                        </h5>

                        <div className="mb-3">
                            <textarea
                                className="form-control"
                                value={evaluation.commentaires || ""}
                                onChange={(e) => handleChange(e, "commentaires")}
                                placeholder={t('AjoutezVosCommentairesIci')}
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
                            {t('ObservationsGenerales')}
                        </h5>

                        <div className="mb-3">
                            <label>{t('MilieuPrivilegierPourStage')}:</label>
                            <div className="mb-3">
                                <div className="form-check">
                                    <input
                                        type="radio"
                                        className="form-check-input"
                                        name="stagePrivilege"
                                        checked={evaluation.privilegiePremierStage}
                                        onChange={() => setEvaluation({
                                            ...evaluation,
                                            privilegiePremierStage: true,
                                            privilegieDeuxiemeStage: false,
                                            privilegieTroisiemeStage: false
                                        })}
                                    />
                                    <label className="form-check-label">{t('PremierStage')}</label>
                                </div>
                                <div className="form-check">
                                    <input
                                        type="radio"
                                        className="form-check-input"
                                        name="stagePrivilege"
                                        checked={evaluation.privilegieDeuxiemeStage}
                                        onChange={() => setEvaluation({
                                            ...evaluation,
                                            privilegiePremierStage: false,
                                            privilegieDeuxiemeStage: true,
                                            privilegieTroisiemeStage: false
                                        })}
                                    />
                                    <label className="form-check-label">{t('DeuxiemeStage')}</label>
                                </div>
                            </div>

                        </div>
                        <div className="mb-3">
                            <label>{t('MilieuOuvertAccueillir')}</label>
                            <input
                                type="number"
                                className="form-control"
                                min={1}
                                value={evaluation.nombreStagiairesAccueillis || ""}
                                onChange={(e) => handleChange(e, "nombreStagiairesAccueillis")}
                                placeholder={t('NombreStagiairesAccueillis')}
                                required
                            />
                        </div>

                        <div className="mb-3">
                            <label>{t('MilieuDesireAccueillirMemeStagiaire')}</label>
                            <br/>
                            <div className="form-check form-check-inline">
                                <input
                                    type="radio"
                                    className="form-check-input"
                                    name="souhaiteRevoirStagiaire"
                                    checked={evaluation.souhaiteRevoirStagiaire === true}
                                    onChange={() => setEvaluation({...evaluation, souhaiteRevoirStagiaire: true})}
                                    required
                                />

                                <label className="form-check-label">{t('Oui')}</label>
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
                                <label className="form-check-label">{t('Non')}</label>
                            </div>
                        </div>

                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="danger" onClick={handleCloseModal}>
                            {t('close')}
                        </Button>
                        <Button variant="success" type="submit">
                            {t('submit')}
                        </Button>
                    </Modal.Footer>
                </form>
            </Modal>
        </>
    );
}

export default AccueilProfesseur;
