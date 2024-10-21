import React, {useEffect, useState} from "react";
import { useTranslation } from "react-i18next";
import { Modal, Button } from "react-bootstrap";
import { Tooltip } from 'react-tooltip';
import axios from "axios";
import "../CSS/ListeDeStage.css";


function ListeDeStage({ internships = [], userData }) {
    const { t } = useTranslation();
    const email = userData.credentials.email;
    const [showModal, setShowModal] = useState(false);
    const [selectedInternship, setSelectedInternship] = useState(null);
    const [appliedInternship, setAppliedInternship] = useState([]);
    const [internshipsWithImages, setInternshipsWithImages] = useState([]);

    // Permet de récupérer les images des stages depuis l'API Unsplash
    useEffect(() => {
        const fetchImages = async () => {
            const apiKey = 'YaQ86E_nZfoK9ks-hpmvKbP9Gal_JCSLlcgDairpDGM';
            const updatedInternships = await Promise.all(internships.map(async internship => {
                try {
                    const response = await axios.get(`https://api.unsplash.com/search/photos?query=${internship.localisation}&client_id=${apiKey}`);
                    if (response.data.results.length > 0) {
                        return { ...internship, imageUrl: response.data.results[0].urls.regular };
                    } else {
                        return { ...internship, imageUrl: '' };
                    }
                } catch (error) {
                    console.error('Erreur lors de la récupération de l’image :', error);
                    return { ...internship, imageUrl: '' };
                }
            }));
            setInternshipsWithImages(updatedInternships);
        };

        fetchImages().then(r => console.log('Images récupérées :', r));
    }, [internships]);


    // Permet de gérer le mode plein écran pour les offres de stage
    useEffect(() => {
        const handleFullscreenChange = () => {
            const iframeModal = document.getElementById('pdfIframeModal');
            const iframeFullscreen = document.getElementById('pdfIframeFullscreen');
            if (!document.fullscreenElement) {
                iframeModal.style.display = "block";
                iframeFullscreen.style.display = "none";
            }
            else {
                iframeModal.style.display = "none";
                iframeFullscreen.style.display = "block";
            }

        };

        document.addEventListener('fullscreenchange', handleFullscreenChange);

        return () => {
            document.removeEventListener('fullscreenchange', handleFullscreenChange);
        };
    }, []);


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
            console.log('Etudiant :', email);
            const response = await fetch(`http://localhost:8081/etudiant/${email}/offre/${offreId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            if (!response.ok) {
                throw new Error(`Erreur lors de la soumission : ${response.status}`);
            }
            const etudiantDTO = await response.json();
            setAppliedInternship([...appliedInternship, offreId])
            console.log('Candidature réussie :', etudiantDTO);
            alert("Candidature réussie !");
        } catch (error) {
            console.error('Erreur lors de la soumission :', error);
            alert("Échec de la candidature. Veuillez réessayer.");
        }
    };

    useEffect(() => {
        const fetchAppliedInternships = async () => {
            try {
                const response = await fetch(`http://localhost:8081/etudiant/${email}/offres`);
                if (!response.ok) {
                    throw new Error(`Erreur lors de la récupération des offres : ${response.status}`);
                }
                const internships = await response.json();
                setAppliedInternship(internships.map(internship => internship.id));
            } catch (error) {
                console.error('Erreur lors de la récupération des offres :', error);
            }
        };

        fetchAppliedInternships();
    }, [email]);

    const afficherIframesOffre = () => (
        <>
            <iframe id="pdfIframeModal"
                    src={`${selectedInternship.data}#zoom=55`}
                    style={{width: "100%", height: "57vh", border: "none"}}
                    title="Offre de stage"></iframe>
            <iframe id="pdfIframeFullscreen"
                    src={`${selectedInternship.data}#zoom=150`}
                    style={{display: "none", width: "100%", height: "30em", border: "none"}}
                    title="Offre de stage"></iframe>
        </>
    );

    const afficherButtonsOffre = (internship) => (
        <Modal.Footer>
            <Button variant="secondary" onClick={handleCloseModal}>
                {t('Fermer')}
            </Button>

            {appliedInternship.includes(internship.id) ? (
                <Button variant="primary" disabled>
                    {t('DejaPostuler')}
                </Button>
            ) : (
                <Button
                    variant="success"
                    onClick={() => postulerAuStage(selectedInternship.id)}
                    disabled={!selectedInternship}
                >
                    {t('Postuler')}
                </Button>
            )}
        </Modal.Footer>
    );


    return (
        <div className="container mb-5">
            <div className="m-auto text-center my-4">
                <h3>{t('OffresDeStage')}</h3>
                <small className="text-muted" style={{ fontSize: "1rem" }}><i>*{t('VoirStage')}</i></small>
            </div>

            <div className="row">
                {internshipsWithImages.length > 0 ? (
                    internshipsWithImages.map((internship, index) => (
                        <div key={index} className="col-lg-4 col-md-6 col-sm-12 p-2">
                            <div className="card my-3 h-100 internship-card" onClick={() => openModal(internship)}>
                                <div className="internship-image"
                                     style={{backgroundImage: `url(${internship.imageUrl})`}}>
                                    <div className="overlay">
                                        <div>
                                            {appliedInternship.includes(internship.id) ? (
                                                <p className="text-center text-white"><i>{t('DejaPostuler')}</i></p>
                                            ) : null}
                                        </div>
                                    </div>
                                </div>
                                <div className="card-body d-flex flex-column justify-content-center align-items-center"
                                     style={{
                                         backgroundColor: 'rgba(255, 255, 255, 0.7)',
                                         flex: 1,
                                         zIndex: 1
                                     }}>
                                    <h5 className="card-title text-center">{internship.titre}</h5>
                                    <h6 className="card-subtitle mb-2 text-muted text-center">{internship.localisation}</h6>

                                    <div>
                                        <p data-tooltip-id="datelimiteTip"
                                           data-tooltip-content={t('DateLimiteInfo')}
                                           data-tooltip-place="right"
                                           className="card-text text-center">
                                            <strong>{t('DateLimite')}</strong> {internship.dateLimite}
                                            <sup><b>(?)</b></sup>
                                        </p>
                                        <Tooltip id="datelimiteTip" style={{fontSize: '1.2rem'}}/>
                                    </div>

                                    <p className="card-text text-center">
                                        <strong>{t('DateDePublication')}</strong> {internship.datePublication}
                                    </p>

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
                    <Modal.Title>{t('DetailsDuStage')}</Modal.Title>
                </Modal.Header>
                <Modal.Body className="text-center">
                    {selectedInternship ? (
                        <>
                            {afficherIframesOffre()}
                            <Button className="m-2" onClick={() => {
                                const iframeFullscreen = document.getElementById('pdfIframeFullscreen');
                                if (iframeFullscreen.requestFullscreen) {
                                    iframeFullscreen.requestFullscreen();
                                }
                            }}>{t('OuvrirEnPleinEcran')}</Button>
                        </>
                    ) : (
                        <p>{t('AucuneOffreAfficher')}</p>
                    )}
                </Modal.Body>
                {selectedInternship && afficherButtonsOffre(selectedInternship)}
            </Modal>
        </div>
    );
}

export default ListeDeStage;
