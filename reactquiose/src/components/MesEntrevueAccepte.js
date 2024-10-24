import { useLocation, useNavigate } from "react-router-dom";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import EmployeurHeader from "./EmployeurHeader";
import "../CSS/MesEntrevueAccepte.css";
import {forEach} from "react-bootstrap/ElementChildren";
import {FaCalendarAlt, FaCheck, FaTimes} from "react-icons/fa";
import ConfirmModal from "./ConfirmModal";
import i18n from "i18next";
import {FaLocationPinLock} from "react-icons/fa6";

function MesEntrevueAccepte() {
    const location = useLocation();
    const userData = location.state?.userData;
    const employeurEmail = userData.credentials.email;
    const [entrevues, setEntrevues] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const [statusMessages, setStatusMessages] = useState({});
    const [showModal, setShowModal] = useState(false);
    const [currentAction, setCurrentAction] = useState(null);
    const [currentEntrevue, setCurrentEntrevue] = useState(null);
    const { t } = useTranslation();


    useEffect(() => {
        const fetchOffresEntrevues = async () => {
            if (!employeurEmail) {
                setError("Email employeur non fourni");
                setIsLoading(false);
                return;
            }

            try {
                console.log("employeurEmail", employeurEmail) // TODO mon useEffect ce fait 2 fois
                const responseEntrevuesAccepte = await fetch(`http://localhost:8081/entrevues/acceptees/employeur/${employeurEmail}`);
                const entrevuesAccepteData = await responseEntrevuesAccepte.json();

                console.log("t", t)

                if (responseEntrevuesAccepte.status === 404) {
                    setEntrevues([]);
                    return;
                }
                console.log("entrevuesAccepteData", entrevuesAccepteData)
                setEntrevues(entrevuesAccepteData);

            } catch (error) {
                setError(error.message);
            } finally {
                setIsLoading(false);
            }
        };


        fetchOffresEntrevues();
    }, [employeurEmail]);


    useEffect(() => {
        entrevues.forEach(entrevue => {
            setDecisionCandidate(entrevue).then(r => console.log("Decision updated"));
        });
    }, [entrevues]);




    const handleCandidatureAcceptee = (entrevueAcceptee) => {
        setEntrevues(prevEntrevues =>
            prevEntrevues.map(entrevue =>
                entrevue.etudiantDTO === entrevueAcceptee.etudiantDTO && entrevue.offreDeStageDTO === entrevueAcceptee.offreDeStageDTO
                    ? { ...entrevue, status: 'accepter' }
                    : entrevue
            )
        );

        setDecisionCandidate(entrevueAcceptee).then(r => console.log("Decision updated"));
    }

    const handleCandidatureRejete = (entrevueRejete) => {
        setEntrevues(prevEntrevues =>
            prevEntrevues.map(entrevue =>
                entrevue.etudiantDTO === entrevueRejete.etudiantDTO && entrevue.offreDeStageDTO === entrevueRejete.offreDeStageDTO
                    ? { ...entrevue, status: 'refuser' }
                    : entrevue
            )
        );

        setDecisionCandidate(entrevueRejete).then(r => console.log("Decision updated"));
    }

    const setDecisionCandidate = async (entrevue) => {
        const decision = await getDecisionCandidate(entrevue);
        if (decision !== null) {
            setStatusMessages(prevStatusMessages => ({
                ...prevStatusMessages,
                [entrevue.id]: decision ? t('CandidatureAcceptee') : t('CandidatureRejetee')
            }));
        }
    }

    const handleAccept = async (entrevue) => {
        setCurrentAction(() => () => acceptEntrevue(entrevue));
        setCurrentEntrevue(entrevue);
        setShowModal(true);
    };

    const handleRefuse = async (entrevue) => {
        setCurrentAction(() => () => refuseEntrevue(entrevue));
        setCurrentEntrevue(entrevue);
        setShowModal(true);
    };

    const acceptEntrevue = async (entrevue) => {
        try {
            const response = await fetch(`http://localhost:8081/candidatures/accepter/${entrevue.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: ("accepter"),
            });

            if (response.ok) {
                handleCandidatureAcceptee(entrevue);
            } else {
                console.error('Erreur lors de l\'acceptation de l\'entrevue');
            }
        } catch (error) {
            console.error('Erreur réseau:', error);
        }
        setShowModal(false);
    };

    const refuseEntrevue = async (entrevue) => {
        try {
            const response = await fetch(`http://localhost:8081/candidatures/refuser/${entrevue.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: ("refuser"),
            });

            if (response.ok) {
                handleCandidatureRejete(entrevue);
            } else {
                console.error('Erreur lors du refus de l\'entrevue');
            }
        } catch (error) {
            console.error('Erreur réseau:', error);
        }
        setShowModal(false);
    };


    const getDecisionCandidate = async (entrevue) => {
        try {
            const response = await fetch(`http://localhost:8081/candidatures/${entrevue.id}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                const data = await response.json();
                if (data === null || data === undefined) {
                    console.error('Erreur lors de la récupération de la décision de la candidature');
                    return null;
                }

                console.log('Status:', data.accepte ? 'Accepted' : 'Rejected');
                return data.accepte;
            }
        } catch (error) {
            console.error('Erreur réseau:', error);
        }
        return null;
    }


    const groupInterviewsByOffer = (entrevues) => {
        return entrevues.reduce((acc, entrevue) => {
            const offerId = entrevue.offreDeStageDTO.id;
            if (!acc[offerId]) {
                acc[offerId] = {
                    offer: entrevue.offreDeStageDTO,
                    entrevues: []
                };
            }
            acc[offerId].entrevues.push(entrevue);
            return acc;
        }, {});
    }


    if (isLoading) {
        return <div style={{ fontSize: "1.5rem" }}>{t('ChargementDesEntrevues')}</div>;
    }

    if (error) {
        return <div style={{ fontSize: "1.5rem" }}>{t('Erreur')} {error}</div>;
    }

    const showButtonsIfDateBeforeToday = (entrevue) => {
        const today = new Date();
        const dateEntrevue = new Date(entrevue.dateHeure);
        return today > dateEntrevue;
    }

    const formatDate = (dateString) => {
        const options = { day: '2-digit', month: 'long', year: 'numeric', hour: '2-digit', minute: '2-digit' };
        return new Date(dateString).toLocaleDateString(i18n.language, options);
    }


    const groupedInterviews = groupInterviewsByOffer(entrevues);


    return (
        <>
            <EmployeurHeader userData={userData}/>
            <div className="container-fluid p-4 mes-entrevues-container">
                <div className="container mt-5">
                    <h1 className="text-center mt-5 page-title">{t('vosEntrevues')}</h1>

                    <legend className="mb-5 mt-0"><i>*{t('AttendreDateEntrevue')}</i></legend>

                    {Object.keys(entrevues).length === 0 ? (
                        <div className="alert alert-info mt-3 no-offres-alert">{t('AccuneOffreTrouve')}</div>
                    ) : (
                        <div className="row mt-3 mb-3">
                            {Object.values(groupedInterviews).map(({offer, entrevues}) => (
                                <div key={offer.id} className="col-md-12 offre-card">
                                    <h5 className="offre-title">{t('Offre')} #{offer.id}: {offer.titre}</h5>
                                    <ul className="entrevue-list">
                                        {entrevues.map((entrevue) => (
                                            <li key={entrevue.id} className="entrevue-item text-capitalize">
                                                <div style={{
                                                    minWidth: "15em",
                                                    marginRight: "3em",
                                                }}>
                                                    <span style={{ fontSize: "1rem" }}>
                                                        <strong>{t('Entrevue')}</strong> - {entrevue.etudiantDTO.firstName} {entrevue.etudiantDTO.lastName}
                                                    </span>
                                                    <br/>

                                                    <span className="entrevue-details">
                                                        <FaCalendarAlt /> &nbsp;
                                                        {formatDate(entrevue.dateHeure)}
                                                    </span>
                                                    <br/>


                                                    <span className="entrevue-details">
                                                        <FaLocationPinLock /> &nbsp;
                                                        {entrevue.location}
                                                    </span>
                                                </div>

                                                {showButtonsIfDateBeforeToday(entrevue) && (
                                                    <>
                                                        <div className="m-auto d-flex">
                                                            {statusMessages[entrevue.id] ? (
                                                                <div
                                                                    className="status-message">{statusMessages[entrevue.id]}</div>
                                                            ) : (
                                                                <div className="entrevue-actions">
                                                                    <div className="icon-block">
                                                                        <button
                                                                            className={`btn btn-lg rounded-start-pill custom-btn icon-accept`}
                                                                            onClick={() => handleAccept(entrevue)}
                                                                            style={{ margin: "0", fontSize: "1.2rem" }}
                                                                        >
                                                                            {t('Embaucher')}
                                                                        </button>
                                                                    </div>
                                                                    <div className="icon-block">
                                                                        <button
                                                                            className={`btn btn-lg rounded-end-pill custom-btn icon-refuse`}
                                                                            onClick={() => handleRefuse(entrevue)}
                                                                            style={{ margin: "0", fontSize: "1.2rem" }}
                                                                        >
                                                                            {t('Refuser')}
                                                                        </button>
                                                                    </div>
                                                                </div>
                                                            )}
                                                        </div>
                                                    </>
                                                )}
                                            </li>
                                        ))}
                                    </ul>
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>
            <ConfirmModal
                show={showModal}
                onClose={() => setShowModal(false)}
                onConfirm={currentAction}
                message={t('ConfirmerVotreChoix')}
            />
        </>
    )
        ;
}

export default MesEntrevueAccepte;
