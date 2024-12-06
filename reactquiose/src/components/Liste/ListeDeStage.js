import React, {useEffect, useState} from "react";
import { useTranslation } from "react-i18next";
import { Modal, Button } from "react-bootstrap";
import { Tooltip } from 'react-tooltip';
import axios from "axios";
import "../../CSS/ListeDeStage.css";
import {FaMapMarkerAlt, FaSearch} from "react-icons/fa";

function ListeDeStage({ internships = [], userData }) {
    const { t } = useTranslation();
    const [showModal, setShowModal] = useState(false);
    const [selectedInternship, setSelectedInternship] = useState(null);
    const [appliedInternship, setAppliedInternship] = useState([]);
    const [internshipsWithImages, setInternshipsWithImages] = useState([]);
    const [searchTitle, setSearchTitle] = useState("");
    const [searchLocation, setSearchLocation] = useState("");

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

        fetchImages();
    }, [internships]);

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
            const response = await fetch(`http://localhost:8081/etudiant/${userData.credentials.email}/offre/${offreId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            if (!response.ok) {
                throw new Error(`Erreur lors de la soumission : ${response.status}`);
            }
            await response.json();
            setAppliedInternship([...appliedInternship, offreId])
        } catch (error) {
            console.error('Erreur lors de la soumission :', error);
        }
    };

    useEffect(() => {
        const fetchAppliedInternships = async () => {
            if (userData && userData.credentials && userData.credentials.email) {
                try {
                    const response = await fetch(`http://localhost:8081/etudiant/${userData.credentials.email}/offres`);
                    if (!response.ok) {
                        throw new Error(`Erreur lors de la récupération des offres : ${response.status}`);
                    }
                    const internships = await response.json();
                    setAppliedInternship(internships.map(internship => internship.id));
                } catch (error) {
                    console.error('Erreur lors de la récupération des offres :', error);
                }
            }
        };

        fetchAppliedInternships();
    }, [userData]);

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
            <Button variant="danger" onClick={handleCloseModal}>
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

    const filteredInternships = internshipsWithImages.filter(internship =>
        internship.titre.toLowerCase().includes(searchTitle.toLowerCase()) &&
        internship.localisation.toLowerCase().includes(searchLocation.toLowerCase())
    );

    const sortedInternships = filteredInternships.sort((a, b) => {
        const aApplied = appliedInternship.includes(a.id);
        const bApplied = appliedInternship.includes(b.id);
        return aApplied - bApplied;
    });

    const clearInputs = () => {
        setSearchTitle("");
        setSearchLocation("");
    };

    return (
        <div className="container mb-5">
            <div className="m-auto text-center my-4">
                <h3>{t('OffresDeStage')}</h3>
                <small className="text-muted" style={{fontSize: "1rem"}}><i>*{t('VoirStage')}</i></small>
            </div>

            <div className="row mb-3">
                <div className="col-md-8 m-auto">
                    <div className="input-group">
                        <div className="input-group-prepend">
                            <span className="input-group-text" id="search-title-icon">
                                <FaSearch/>
                            </span>
                        </div>
                        <input
                            type="text"
                            className="form-control search-input"
                            placeholder={t('RechercherParTitre')}
                            value={searchTitle}
                            onChange={(e) => setSearchTitle(e.target.value)}
                        />
                        <div className="vertical-bar">|</div>
                        <div className="input-group-prepend">
                            <span className="input-group-text" id="search-location-icon">
                                <FaMapMarkerAlt/>
                            </span>
                        </div>
                        <input
                            type="text"
                            className="form-control search-input"
                            placeholder={t('RechercherParLocalisation')}
                            value={searchLocation}
                            onChange={(e) => setSearchLocation(e.target.value)}
                        />
                        <div className="input-group-append">
                            <Button variant="outline-secondary" onClick={clearInputs}>
                                {t('Effacer')}
                            </Button>
                        </div>
                    </div>
                </div>
            </div>

            <div className="row">
                {sortedInternships.length > 0 ? (
                    sortedInternships.map((internship, index) => (
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
                                    <h6 className="card-subtitle mb-2 text-muted text-center">{internship.employeur.entreprise}</h6>

                                    <div>
                                        <p data-tooltip-id="datelimiteTip"
                                           data-tooltip-content={t('DateLimiteInfo')}
                                           data-tooltip-place="right"
                                           className="card-text text-center">
                                            <strong>{t('DateLimite')}</strong> {internship.dateLimite}
                                            <sup><b>(?)</b></sup>
                                        </p>
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

            <Tooltip id="datelimiteTip" style={{fontSize: '1.2rem', zIndex: '2'}}/>

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