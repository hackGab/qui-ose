import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useLocation } from "react-router-dom";
import { useTranslation } from 'react-i18next';
import '../CSS/MesEntrevues.css';
import EtudiantHeader from "./EtudiantHeader";
import AffichageEntrevue from "./AffichageEntrevue";
import i18n from "i18next";
import {getLocalStorageSession} from "../utils/methodes/getSessionLocalStorage";

function MesEntrevues() {
    const { t } = useTranslation();
    const location = useLocation();
    const [userData, setUserData] = useState(location.state?.userData || null);
    const [entrevues, setEntrevues] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [session, setSession] = useState(getLocalStorageSession())

    useEffect(() => {
        const email = userData?.credentials.email;
        if (email) {
            // fetch(`http://localhost:8081/entrevues/etudiant/${email}/session/${session}`) ici

            fetch(`http://localhost:8081/entrevues/etudiant/${email}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Erreur lors de la récupération des entrevues');
                    }
                    return response.json();
                })
                .then(data => {
                    console.log('Réponse du serveur:', data);
                    setEntrevues(data);
                    setLoading(false);
                })
                .catch(err => {
                    setError(err.message);
                    setLoading(false);
                });
        }
    }, [userData]);

    const handleEntrevueAcceptee = (entrevueAcceptee) => {
        console.log('Entrevue acceptée:', entrevueAcceptee);

        setEntrevues(prevEntrevues =>
            prevEntrevues.map(entrevue =>
                entrevue.etudiantDTO === entrevueAcceptee.etudiantDTO && entrevue.offreDeStageDTO === entrevueAcceptee.offreDeStageDTO
                    ? { ...entrevue, status: 'accepter' }
                    : entrevue
            )
        );
    };

    const handleEntrevueRejete = (entrevueRejete) => {
        console.log('Entrevue refusée:', entrevueRejete);

        setEntrevues(prevEntrevues =>
            prevEntrevues.map(entrevue =>
                entrevue.etudiantDTO === entrevueRejete.etudiantDTO && entrevue.offreDeStageDTO === entrevueRejete.offreDeStageDTO
                    ? { ...entrevue, status: 'refuser' }
                    : entrevue
            )
        );
    };


    const entrevuesAccepter = entrevues.filter(entrevue => entrevue.status.toLowerCase() === 'accepter');
    const entrevuesEnAttente = entrevues.filter(entrevue => entrevue.status.toLowerCase() === 'en attente');


    if (loading) {
        return <div className="text-center mt-5">
            <div className="spinner-border" role="status"></div>
            <br/>
            <span className="sr-only">{t('chargementEntrevues')}</span>
        </div>;
    }

    if (error) {
        return <div>Erreur: {error}</div>;
    }

    const verificationSession = (session) => {
        console.log(session);
    }


    return (
        <>
            <EtudiantHeader userData={userData} onSendData={verificationSession} />
            <div className="container-fluid p-4">
                <div className="container flex-grow-1 pt-4">
                    <h1 className="mb-0 text-center" style={{ fontSize: "4em" }}>{t('entrevueListTitle')}</h1>
                    <p className="text-center mb-4" style={{ fontSize: "2em" }}>{t('entrevueListSubtitle')}</p>
                    <small style={{ fontSize: "1rem"}}>{t('entrevueListConseil')}</small>
                    <div className="row p-2 text-center w-100 m-auto">
                        <div className="col-md-5 m-auto">
                            <h2 className="entrevuesTitreBox">{t('Acceptees')}</h2>
                            <div className="row p-1 shadow w-100 m-auto entrevueBox">
                                {entrevuesAccepter.length > 0 ? (
                                    entrevuesAccepter.map((entrevue) => (
                                        <AffichageEntrevue
                                            key={entrevue.id}
                                            entrevue={entrevue}
                                            t={t}
                                        />
                                    ))
                                ) : (
                                    <p>{t('AucuneEntrevueAcceptee')}</p>
                                )}
                            </div>
                        </div>

                        <div className="col-md-5 m-auto mt-0">
                            <h2 className="entrevuesTitreBox">{t('EnAttente')}</h2>
                            <div className="row p-1 shadow w-100 m-auto entrevueBox">
                                {entrevuesEnAttente.length > 0 ? (
                                    entrevuesEnAttente.map((entrevue) => (
                                        <AffichageEntrevue
                                            key={entrevue.id}
                                            entrevue={entrevue}
                                            t={t}
                                            onAccept={handleEntrevueAcceptee}
                                            onReject={handleEntrevueRejete}
                                        />
                                    ))
                                ) : (
                                    <p>{t('AucuneEntrevueEnAttente')}</p>
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}

export default MesEntrevues;
