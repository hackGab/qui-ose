import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import { Modal, Button } from "react-bootstrap";

function ListeDeStage({ internships = [], userData }) {
    const { t } = useTranslation();
    const [showModal, setShowModal] = useState(false);
    const [selectedInternship, setSelectedInternship] = useState(null);

    const openModal = (internship) => {
        setSelectedInternship(internship);
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setSelectedInternship(null);
    };

    const postulerAuStage = async (offreId) => {
        try {
            console.log('Postuler au stage :', offreId);
            console.log('Etudiant :', userData.credentials.email);
            const response = await fetch(`http://localhost:8081/etudiant/${userData.credentials.email}/offre/${offreId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            if (!response.ok) {
                throw new Error(`Erreur lors de la soumission : ${response.status}`);
            }
            const etudiantDTO = await response.json();
            console.log('Candidature réussie :', etudiantDTO);
            alert("Candidature réussie !");
        } catch (error) {
            console.error('Erreur lors de la soumission :', error);
            alert("Échec de la candidature. Veuillez réessayer.");
        }
    };


    return (
        <div className="container">
            <h3 className="text-center my-4">{t('OffresDeStage')}</h3>
            <div className="row">
                {internships.length > 0 ? (
                    internships.map((internship, index) => (
                        <div key={index} className="col-lg-4 col-md-6 col-sm-12 p-2">
                            <div className="card my-3 h-100">
                                <div className="card-body d-flex flex-column justify-content-center align-items-center">
                                    <h5 className="card-title text-center">{internship.titre}</h5>
                                    <h6 className="card-subtitle mb-2 text-muted text-center">{internship.localisation}</h6>
                                    <p className="card-text text-center">
                                        <strong>{t('DateLimite')}</strong> {internship.dateLimite}
                                    </p>
                                    <p className="card-text text-center">
                                        <strong>{t('DateDePublication')}</strong> {internship.datePublication}
                                    </p>
                                    <div className="d-flex justify-content-center my-3">
                                        <button className="btn btn-info" onClick={() => openModal(internship)}>
                                            {t('VoirCandidature')}
                                        </button>
                                    </div>
                                    <p className="card-text text-center">
                                        <strong>{t('NombreDeCandidats')}</strong> {internship.nbCandidats}
                                    </p>
                                </div>
                            </div>
                        </div>
                    ))
                ) : (
                    <div className="d-flex justify-content-center align-items-center">
                        <p>{t('AucuneOffreAfficher')}</p>
                    </div>
                )}
            </div>

            <Modal show={showModal} onHide={handleCloseModal} centered>
                <Modal.Header closeButton>
                    <Modal.Title>{t('CV')}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {selectedInternship ? (
                        <>
                            <h5>{selectedInternship.titre}</h5>
                            <h6>{selectedInternship.localisation}</h6>
                            <p><strong>{t('DateLimite')}</strong>: {selectedInternship.dateLimite}</p>
                            <p><strong>{t('DateDePublication')}</strong>: {selectedInternship.datePublication}</p>
                            <iframe src={selectedInternship.data} style={{ width: "100%", height: "400px", border: "none" }} title="CV"></iframe>
                        </>
                    ) : (
                        <p>{t('AucunCVÀAfficher')}</p>
                    )}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleCloseModal}>
                        {t('Fermer')}
                    </Button>
                    <Button
                        variant="success"
                        onClick={() => postulerAuStage(selectedInternship.id)}
                        disabled={!selectedInternship}
                    >
                        {t('Postuler')}
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
}

export default ListeDeStage;
