import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { Modal, Button } from "react-bootstrap";
import axios from "axios";
import { useLocation } from "react-router-dom";
import "../../CSS/ListeDeStage.css";
import EtudiantHeader from "../Header/EtudiantHeader";
import {getLocalStorageSession} from "../../utils/methodes/getSessionLocalStorage";
function StagesAppliquees() {
    const location = useLocation();
    const user = location.state?.userData;
    const { t } = useTranslation();
    const [showModal, setShowModal] = useState(false);
    const [stagesAppliquees, setStagesAppliquees] = useState([]);
    const [selectedInternship, setSelectedInternship] = useState(null);
    const [stagesWithImages, setStagesWithImages] = useState([]);
    const [session, setSession] = useState(getLocalStorageSession());
    const [entrevues, setEntrevues] = useState([]);
    const [candidatureDecision, setCandidatureDecision] = useState([]);
    const [loading, setLoading] = useState(true);
    const [decisionEmployeurCandidature, setDecisionEmployeurCandidature] = useState({});

    useEffect(() => {
        if (!user) return;

        const url = `http://localhost:8081/etudiant/credentials/${user.credentials.email}`;

        fetch(url)
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`Erreur lors de la requête: ${response.status}`);
                }
                return response.json();
            })
            .then((data) => {
                setStagesAppliquees(data.offresAppliquees || []);
                return data.offresAppliquees;
            })
            .then((offresAppliquees) => {
                if (offresAppliquees.length > 0) {
                    const fetchImages = async () => {
                        const apiKey = 'YaQ86E_nZfoK9ks-hpmvKbP9Gal_JCSLlcgDairpDGM';
                        const updatedStages = await Promise.all(offresAppliquees.map(async stage => {
                            try {
                                const response = await axios.get(`https://api.unsplash.com/search/photos?query=${stage.localisation}&client_id=${apiKey}`);
                                if (response.data.results.length > 0) {
                                    return { ...stage, imageUrl: response.data.results[0].urls.regular };
                                } else {
                                    return { ...stage, imageUrl: '' };
                                }
                            } catch (error) {
                                console.error('Erreur lors de la récupération de l’image :', error);
                                return { ...stage, imageUrl: '' };
                            }
                        }));
                        setStagesWithImages(updatedStages);
                    };

                    fetchImages();
                }
            })
            .catch((error) => {
                console.error('Erreur:', error);
            });

    }, [user]);


    useEffect(() => {
        if (!user || !session) return;

        const email = user.credentials.email;
        if (!email) return;

        const getEntrevuesByUserAndSession = async () => {
            try {
                const response = await axios.get(`http://localhost:8081/entrevues/etudiant/${email}/session/${session.session}`);
                setEntrevues(response.data);
                setLoading(false)
            } catch (error) {
                console.error('Erreur lors de la récupération des entrevues:', error);
            }
        };

        getEntrevuesByUserAndSession();
    }, [user, session]);


    useEffect(() => {
        if (!entrevues.length) return;

        const getCandidatureDecision = async () => {
            try {
                const entrevueDecision = [];
                for (const entrevue of entrevues) {
                    const response = await axios.get(`http://localhost:8081/candidatures/${entrevue.id}`);
                    entrevueDecision.push(response.data);
                }
                setCandidatureDecision(entrevueDecision);
            } catch (error) {
                console.error('Erreur lors de la récupération des candidatures:', error);
            }
        };

        getCandidatureDecision();
    }, [entrevues]);


    useEffect(() => {
        const decisions = {};
        entrevues.forEach(entrevue => {
            const candidature = candidatureDecision.find(c => c.entrevueId === entrevue.id);
            if (!candidature) {
                decisions[entrevue.offreDeStageDTO?.id] = 'Candidature en attente';
            }
            else {
                decisions[entrevue.offreDeStageDTO?.id] = candidature.accepte ? 'Candidature Acceptée' : 'Candidature Rejetée';
            }
        });

        setDecisionEmployeurCandidature(decisions);
    }, [entrevues, candidatureDecision]);


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


    const retirerApplication = (stage) => {
        const url = `http://localhost:8081/etudiant/${user.credentials.email}/retirerOffre/${stage.id}`;

        fetch(url, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`Erreur lors de la suppression: ${response.status}`);
                }
                return response.json();
            })
            .then((data) => {
                setShowModal(false);
                setSelectedInternship(null);
                setStagesWithImages(stagesWithImages.filter((s) => s.id !== stage.id));
                setStagesAppliquees(stagesAppliquees.filter((s) => s.id !== stage.id));
            })
            .catch((error) => {
                console.error("Erreur lors du retrait de l'application :", error);
            });
    };


    const openModal = (stage) => {
        setSelectedInternship(stage);
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setSelectedInternship(null);
    };

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

    const verificationSession = (session) => {
        setSession(session)
    }

    if (loading) {
        return <div className="text-center mt-5">
            <div className="spinner-border" role="status"></div>
            <br/>
            <span className="sr-only">{t('ChargementDesOffres')}</span>
        </div>;
    }

    return (
        <>
            <EtudiantHeader userData={user} onSendData={verificationSession}/>
            <div className="container-fluid p-4">
                <div className="m-auto text-center my-4">
                    <h3>{t('stagesAppliquées')}</h3>
                    <small className="text-muted" style={{fontSize: "1rem"}}><i>*{t('VoirStage')}</i></small>
                </div>

                <div className="row p-2 m-3">
                    {stagesWithImages.length > 0 ? (
                        stagesWithImages
                            .filter(stage => stage.session === session.session)
                            .length > 0 ? (
                            stagesWithImages
                                .filter(stage => stage.session === session.session)
                                .map((stage, index) => (
                                    <div key={index} className="col-lg-4 col-md-6 col-sm-12 p-2">
                                        <div className="card my-3 h-100 internship-card"
                                             onClick={() => openModal(stage)}>
                                            <div className="internship-image"
                                                 style={{backgroundImage: `url(${stage.imageUrl})`}}>
                                                <div className="overlay">
                                                    <div>
                                                        <p className="text-center text-white" style={{ wordWrap: "normal" }}>
                                                            { t(decisionEmployeurCandidature[stage.id]) || t('En attente d\'entrevue')}
                                                        </p>
                                                    </div>
                                                </div>
                                            </div>
                                            <div
                                                className="card-body d-flex flex-column justify-content-center align-items-center"
                                                style={{
                                                    backgroundColor: 'rgba(255, 255, 255, 0.7)',
                                                    flex: 1,
                                                    zIndex: 1
                                                }}>
                                                <h5 className="card-title text-center">{stage.titre}</h5>
                                                <h6 className="card-subtitle mb-2 text-muted text-center">{stage.localisation}</h6>

                                                <p className="card-text text-center">
                                                    <strong>{t('DateLimite')}</strong> {stage.dateLimite}
                                                </p>

                                                <p className="card-text text-center">
                                                    <strong>{t('DateDePublication')}</strong> {stage.datePublication}
                                                </p>

                                                <p className="card-text text-center">
                                                    <strong>{t('NombreDeCandidats')}</strong> {stage.nbCandidats}
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                ))
                        ) : (
                            <div className="d-flex justify-content-center align-items-center w-100">
                                <p className="text-muted">{t('AucuneOffreAfficher')}</p>
                            </div>
                        )
                    ) : (
                        <div className="d-flex justify-content-center align-items-center w-100">
                            <p className="text-muted">{t('AucuneOffreAfficher')}</p>
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
                                <Button className="m-2 bg-danger"
                                        onClick={() => retirerApplication(selectedInternship)}>
                                    Retirer application
                                </Button>
                            </>
                        ) : (
                            <p>{t('AucuneOffreAfficher')}</p>
                        )}
                    </Modal.Body>
                </Modal>
            </div>
        </>
    );
}

export default StagesAppliquees;
