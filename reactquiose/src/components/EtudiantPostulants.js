import React, {useEffect, useState} from "react";
import {useLocation, useParams} from "react-router-dom";
import {useTranslation} from "react-i18next";
import EmployeurHeader from "./Header/EmployeurHeader";
import "../CSS/EtudiantPostulants.css";
import {Button, Form, Modal} from "react-bootstrap";
import {FaEnvelope, FaPhone} from "react-icons/fa";

function EtudiantPostulants() {
    const { offreId } = useParams();
    const location = useLocation();
    const user = location.state?.userData;
    const offre = location.state?.offre;
    const [etudiants, setEtudiants] = useState([]);
    const [etudiantsAvecEntrevue, setEtudiantsAvecEntrevue] = useState(new Set());
    const [acceptedEtudiants, setAcceptedEtudiants] = useState([]);
    const [entrevues, setEntrevues] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const { t } = useTranslation();

    const [showModal, setShowModal] = useState(false);
    const [selectedEtudiant, setSelectedEtudiant] = useState(null);
    const [entrevueData, setEntrevueData] = useState({
        dateHeure: "",
        location: ""
    });

    const [showRejectModal, setShowRejectModal] = useState(false);

    useEffect(() => {
        const fetchData = async () => {
            if (!offreId) {
                setError("L'ID de l'offre est requis.");
                setIsLoading(false);
                return;
            }


            try {
                const [etudiantsResponse, entrevueResponse] = await Promise.all([
                    fetch(`http://localhost:8081/offreDeStage/${offreId}/etudiants`),
                    fetch(`http://localhost:8081/entrevues/offre/${offreId}`)
                ]);

                if (!etudiantsResponse.ok) {
                    throw new Error("Erreur dans la réponse du serveur");
                }

                const etudiantsData = await etudiantsResponse.json();
                setEtudiants(etudiantsData);
               

                if (entrevueResponse.ok) {
                    const entrevueData = await entrevueResponse.json();

                    setEntrevues(entrevueData);

                    const etudiantsAvecEntrevueSet = new Set(entrevueData
                        .map(entrevue => entrevue.etudiantDTO.credentials.email));
                    setEtudiantsAvecEntrevue(etudiantsAvecEntrevueSet);
                }

            } catch (error) {
                setError(error.message);
            } finally {
                setIsLoading(false);
            }
        };

        fetchData();
    }, [offreId]);

    const handleShowModal = (etudiant) => {
        setSelectedEtudiant(etudiant);
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setSelectedEtudiant(null);
        setEntrevueData({ dateHeure: "", location: "" });
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setEntrevueData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleRejectEtudiant = async () => {
        if (!selectedEtudiant) return;

        const email = selectedEtudiant.credentials?.email || '';
    

        try {
            const response = await fetch(`http://localhost:8081/etudiant/${email}/retirerOffre/${offreId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                setEtudiants((prevEtudiants) => {
                    return prevEtudiants.filter((etudiant) => etudiant.credentials.email !== email);
                });
                handleCloseRejectModal();
            } 
        } catch (error) {
            console.error("Erreur réseau lors du rejet de l'étudiant");
        }
    };



    const handleShowRejectModal = (etudiant) => {
        setSelectedEtudiant(etudiant);
        setShowRejectModal(true);
    };

    const handleCloseRejectModal = () => {
        setShowRejectModal(false);
        setSelectedEtudiant(null);
    };

    const handleCreateEntrevue = async () => {
        const entrevueDTO = {
            ...entrevueData,
            status: "En attente",
            etudiantDTO: selectedEtudiant
        };

        try {
            const response = await fetch(`http://localhost:8081/entrevues/creerEntrevue/${selectedEtudiant.email}/${offreId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(entrevueDTO)
            });

            if (response.ok) {
                setAcceptedEtudiants(prevState => [...prevState, selectedEtudiant.email]);
                handleCloseModal();
            } else {
                const errorData = await response.json();
                alert("Erreur lors de la création de l'entrevue : " + errorData.message);
            }
        } catch (error) {
            alert("Erreur réseau lors de la création de l'entrevue");
        }
    };


    if (isLoading) {
        return <div className="text-center mt-5">
            <div className="spinner-border" role="status"></div>
            <br/>
            <span className="sr-only">{t('chargementEtudiants')}</span>
        </div>;
    }

    if (error) {
        return <div>{t('erreurRecuperationEtudiants')}</div>;
    }


    const openFile = (data) => {
        if (data) {
            const pdfWindow = window.open();
            pdfWindow.document.write(
                `<iframe src="${data}" style="border:0; top:0; left:0; bottom:0; right:0; width:100%; height:100%;" allowfullscreen></iframe>`
            );
        } else {
            alert("Aucun fichier à afficher !");
        }
    };

    const verificationSession = (data) => {
        //console.log("session ", data);
    }

    const formatDepartementLabel = (departement) => {
        return departement
            ? departement.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, char => char.toUpperCase())
            : '';
    };

    return (
        <>
            <EmployeurHeader userData={ user } onSendData={verificationSession}/>
            <div className="container-fluid p-4">
                <div className="container flex-grow-1 pt-5 mt-5 text-center">

                    { etudiants.length === 0 ? (
                        <div className="alert alert-info mt-3" style={{ fontSize: "3.5em" }}>{t('AucuneEtudiantAPostuler')}</div>
                    ) : (
                        <>
                            <h3>{t('EtudiantsPostulants')} {t('AuPosteDe')} <u>{offre.titre}</u> :</h3>
                            <div className="row mt-4">
                                {etudiants
                                    .filter((etudiant) => {
                                        if (etudiantsAvecEntrevue.has(etudiant.credentials.email)) {
                                            let entrevueEtudiant = null
                                            entrevues.forEach((entrevue) => {
                                                if (entrevue.etudiantDTO.credentials.email === etudiant.credentials.email) {
                                                    entrevueEtudiant = entrevue;
                                                }
                                            });

                                            if (entrevueEtudiant.status == "refuser" || entrevueEtudiant.status == "accepter") {
                                                return false;
                                            }
                                            else {
                                                return true;
                                            }
                                        }
                                        else {
                                            return true;
                                        }
                                    })
                                    .map((etudiant) => (
                                    <div key={etudiant.id} className="col-10 col-sm-6 col-md-4 col-lg-3 mb-4 m-auto text-center">
                                        <div className="card-container">
                                            <div className="card">
                                                <div className="card-body">
                                                    <h5 className="card-title text-capitalize">{etudiant.firstName} {etudiant.lastName}</h5>
                                                    <p className="card-text text-truncate">
                                                        <span><FaEnvelope/> <a
                                                            href={`mailto:${etudiant.email}`}>{etudiant.email}</a></span>
                                                        <br/>
                                                        <FaPhone/> {etudiant.phoneNumber}
                                                        <br/>
                                                         {formatDepartementLabel(etudiant.departement)}
                                                    </p>

                                                    <Button variant="primary"
                                                            onClick={() => openFile(etudiant.cv.data)}>{t('viewCV')}</Button>
                                                    <div className="button-container">
                                                        <Button
                                                            variant="success"
                                                            onClick={() => handleShowModal(etudiant)}
                                                            disabled={acceptedEtudiants.includes(etudiant.email) || etudiantsAvecEntrevue.has(etudiant.credentials.email)}>
                                                            {acceptedEtudiants.includes(etudiant.email)
                                                                ? t('accepte')
                                                                : etudiantsAvecEntrevue.has(etudiant.credentials.email)
                                                                    ? t('EnAttenteDeConfirmation')
                                                                    : t('creerEntrevue')}
                                                        </Button>

                                                        {acceptedEtudiants.includes(etudiant.email) ? null : etudiantsAvecEntrevue.has(etudiant.email) ? null : (
                                                            <Button variant="danger"
                                                                    onClick={() => handleShowRejectModal(etudiant)}>
                                                                {t('rejeter')}
                                                            </Button>
                                                        )}
                                                    </div>

                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    ))}
                            </div>
                        </>
                    )}
                </div>
            </div>

            <Modal show={showModal} onHide={handleCloseModal}>
                <Modal.Header closeButton>
                    <Modal.Title>{t('creerEntrevue')}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group controlId="dateHeure">
                            <Form.Label>{t('dateEtHeure')}</Form.Label>
                            <Form.Control
                                type="datetime-local"
                                name="dateHeure"
                                value={entrevueData.dateHeure}
                                onChange={handleInputChange}
                                required
                            />
                        </Form.Group>
                        <Form.Group controlId="location">
                            <Form.Label>{t('localisation')}</Form.Label>
                            <Form.Control
                                type="text"
                                name="location"
                                value={entrevueData.location}
                                onChange={handleInputChange}
                                required
                            />
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleCloseModal}>
                        {t('annuler')}
                    </Button>
                    <Button variant="primary" onClick={handleCreateEntrevue}>
                        {t('creer')}
                    </Button>
                </Modal.Footer>
            </Modal>

            <Modal show={showRejectModal} onHide={handleCloseRejectModal}>
                <Modal.Header closeButton>
                    <Modal.Title>{t('rejeterEtudiant')}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p>{t('confirmationRejet', { studentName: selectedEtudiant?.firstName })}?</p>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleCloseRejectModal}>
                        {t('annuler')}
                    </Button>
                    <Button variant="danger" onClick={handleRejectEtudiant}>
                        {t('rejeter')}
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default EtudiantPostulants;
