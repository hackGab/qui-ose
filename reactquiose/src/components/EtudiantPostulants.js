import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { useTranslation } from "react-i18next";
import EmployeurHeader from "./EmployeurHeader";
import "../CSS/EtudiantPostulants.css";
import { Button, Modal, Form } from "react-bootstrap";

function EtudiantPostulants() {
    const { offreId } = useParams();
    const [etudiants, setEtudiants] = useState([]);
    const [etudiantsAvecEntrevue, setEtudiantsAvecEntrevue] = useState(new Set());
    const [acceptedEtudiants, setAcceptedEtudiants] = useState([]);
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
                console.log(etudiantsData);

                if (entrevueResponse.ok) {
                    const entrevueData = await entrevueResponse.json();
                    const etudiantsAvecEntrevueSet = new Set(entrevueData.map(entrevue => entrevue.etudiant_id));
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
                setEtudiants((prev) => prev.filter((e) => e.id !== selectedEtudiant.id));
                handleCloseRejectModal();
            } else {
                const errorData = await response.json();
                alert(`Erreur lors du rejet de l'étudiant : ${errorData.message} (Code: ${response.status})`);
            }
        } catch (error) {
            alert("Erreur réseau lors du rejet de l'étudiant");
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
        return <div>Chargement des étudiants...</div>;
    }

    if (error) {
        return <div>Erreur: {error}</div>;
    }

    if (etudiants.length === 0) {
        return (
            <div className="no-students-message">
                <h4>Aucun étudiant n'a postulé à cette offre.</h4>
            </div>
        );
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

    return (
        <div>
            <EmployeurHeader />
            <div className="container mt-5">
                <h5>{t('Etudiantspostulants')} :</h5>
                <div className="row">
                    {etudiants.map((etudiant) => (
                        <div key={etudiant.id} className="col-12 col-sm-6 col-md-4 col-lg-3 mb-4">
                            <div className={`card-container ${acceptedEtudiants.includes(etudiant.email) ? 'accepted' : ''}`}>
                                <div className="card">
                                    <div className="card-body">
                                        <h5 className="card-title">{etudiant.firstName} {etudiant.lastName}</h5>
                                        <p className="card-text"><strong>{t('emailDetail')} :</strong> {etudiant.email}</p>
                                        <p className="card-text"><strong>{t('telephoneDetail')} :</strong> {etudiant.phoneNumber}</p>
                                        <p className="card-text"><strong>{t('departmentDetail')} :</strong> {etudiant.departement}</p>
                                        <Button variant="primary" onClick={() => openFile(etudiant.cv.data)}>{t('viewCV')}</Button>
                                        <div className="d-flex justify-content-between mt-2">
                                            <Button
                                                variant="success"
                                                onClick={() => handleShowModal(etudiant)}
                                                disabled={acceptedEtudiants.includes(etudiant.email) || etudiantsAvecEntrevue.has(etudiant.id)}>
                                                {acceptedEtudiants.includes(etudiant.email)
                                                    ? t('accepte')
                                                    : etudiantsAvecEntrevue.has(etudiant.id)
                                                        ? t('entretienDejaCree')
                                                        : t('creerEntrevue')}
                                            </Button>
                                            { acceptedEtudiants.includes(etudiant.email) ?
                                                null:
                                                etudiantsAvecEntrevue.has(etudiant.id) ?
                                                    null: (
                                                <Button variant="danger" onClick={() => handleShowRejectModal(etudiant)}>
                                                    {t('rejeter')}
                                                </Button>
                                                    )
                                            }
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    ))}
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
        </div>
    );
}

export default EtudiantPostulants;
