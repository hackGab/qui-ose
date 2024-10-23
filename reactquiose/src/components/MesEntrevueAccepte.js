import { useLocation, useNavigate } from "react-router-dom";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import EmployeurHeader from "./EmployeurHeader";
import "../CSS/MesEntrevueAccepte.css";
import {forEach} from "react-bootstrap/ElementChildren";

function MesEntrevueAccepte() {
    const location = useLocation();
    const userData = location.state?.userData;
    const employeurEmail = userData.credentials.email;
    const [entrevues, setEntrevues] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
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




    const handleCandidatureAcceptee = (entrevueAcceptee) => {
        console.log('Entrevue acceptée:', entrevueAcceptee);

        setEntrevues(prevEntrevues =>
            prevEntrevues.map(entrevue =>
                entrevue.etudiantDTO === entrevueAcceptee.etudiantDTO && entrevue.offreDeStageDTO === entrevueAcceptee.offreDeStageDTO
                    ? { ...entrevue, status: 'accepter' }
                    : entrevue
            )
        );
    }

    const handleCandidatureRejete = (entrevueRejete) => {
        console.log('Entrevue refusée:', entrevueRejete);

        setEntrevues(prevEntrevues =>
            prevEntrevues.map(entrevue =>
                entrevue.etudiantDTO === entrevueRejete.etudiantDTO && entrevue.offreDeStageDTO === entrevueRejete.offreDeStageDTO
                    ? { ...entrevue, status: 'refuser' }
                    : entrevue
            )
        );
    }

    const handleAccept = async (entrevue) => {
        try {
            const response = await fetch(`http://localhost:8081/entrevues/accepter/${entrevue.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: ("accepter"),
            });

            if (response.ok) {
                console.log('Entrevue acceptée:', entrevue);
                handleCandidatureAcceptee(entrevue);
            } else {
                console.error('Erreur lors de l\'acceptation de l\'entrevue');
            }
        } catch (error) {
            console.error('Erreur réseau:', error);
        }
        // setShowModal(false);
    };

    const handleRefuse = async (entrevue) => {
        console.log("entrevue", entrevue)
        try {
            const response = await fetch(`http://localhost:8081/entrevues/refuser/${entrevue.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: ("refuser"),
            });

            if (response.ok) {
                console.log('Entrevue refusée:', entrevue);
                handleCandidatureRejete(entrevue);
            } else {
                console.error('Erreur lors du refus de l\'entrevue');
            }
        } catch (error) {
            console.error('Erreur réseau:', error);
        }
        // setShowModal(false);
    };



    if (isLoading) {
        return <div>{t('ChargementDesEntrevues')}</div>;
    }

    if (error) {
        return <div>{t('Erreur')} {error}</div>;
    }

    function showButtonsIfDateBeforeToday(entrevue) {
        const today = new Date();
        const dateEntrevue = new Date(entrevue.dateHeure);
        return today > dateEntrevue;
    }

    return (
        <>
            <EmployeurHeader userData={userData}/>
            <div className="container-fluid p-4 mes-entrevues-container">
                <div className="container mt-5">
                    <h1 className="text-center mt-5 page-title">{t('vosEntrevues')}</h1>

                    {Object.keys(entrevues).length === 0 ? (
                        <div className="alert alert-info mt-3 no-offres-alert">{t('AccuneOffreTrouve')}</div>
                    ) : (
                        <div className="row mt-3">
                            {entrevues.map((entrevue) => (
                                <div key={entrevue.offreDeStageDTO.id} className="col-md-12 offre-card">
                                    <h5 className="offre-title">{t('Offre')} #{entrevue.offreDeStageDTO.id}: {entrevue.offreDeStageDTO.titre}</h5>
                                    <ul className="entrevue-list">
                                        <li key={entrevue.id} className="entrevue-item text-capitalize">
                                            <strong>{t('Entrevue')}</strong> - {entrevue.etudiantDTO.firstName} {entrevue.etudiantDTO.lastName} <br />

                                            <span className="entrevue-details">{new Date(entrevue.dateHeure).toLocaleString()} - {entrevue.location}</span>

                                            {/*{ entrevue.status === 'accepter' && <span className="badge badge-success">{t('Accepter')}</span> }*/}
                                            {/*{ entrevue.status === 'refuser' && <span className="badge badge-danger">{t('Refuser')}</span> }*/}

                                            { showButtonsIfDateBeforeToday(entrevue) && (
                                                <div className="entrevue-actions">
                                                    <button className="btn btn-success" onClick={() => handleAccept(entrevue)}>{t('Accepter')}</button>
                                                    <button className="btn btn-danger" onClick={() => handleRefuse(entrevue)}>{t('Refuser')}</button>
                                                </div>
                                            ) }

                                        </li>
                                    </ul>
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>
        </>
    );
}

export default MesEntrevueAccepte;
