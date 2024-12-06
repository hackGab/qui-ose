import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useLocation } from "react-router-dom";
import { useTranslation } from 'react-i18next';
import '../../CSS/MesEntrevues.css';
import EtudiantHeader from "../Header/EtudiantHeader";
import AffichageEntrevue from "./AffichageEntrevue";
import i18n from "i18next";
import {getLocalStorageSession} from "../../utils/methodes/getSessionLocalStorage";
import {FaCalendarAlt, FaMapMarkerAlt, FaSearch} from "react-icons/fa";
import {Button} from "react-bootstrap";


function MesEntrevues() {
    const { t } = useTranslation();
    const location = useLocation();
    const [userData, setUserData] = useState(location.state?.userData || null);
    const [entrevues, setEntrevues] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [session, setSession] = useState(getLocalStorageSession());
    const [searchTitle, setSearchTitle] = useState('');
    const [searchDate, setSearchDate] = useState('');
    const [searchCompany, setSearchCompany] = useState('');

    const fetchSession = (session) => {

        const email = userData?.credentials.email;
        if (email) {
            fetch(`http://localhost:8081/entrevues/etudiant/${email}/session/${session}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Erreur lors de la récupération des entrevues');
                    }
                    return response.json();
                })
                .then(data => {

                    setEntrevues(data);
                    setLoading(false);
                })
                .catch(err => {
                    setError(err.message);
                    setLoading(false);
                });
        }
    }

    useEffect(() => {
        const email = userData?.credentials.email;
        if (email) {
            fetchSession(session)
        }
    }, [userData]);

    const handleEntrevueAcceptee = (entrevueAcceptee) => {


        setEntrevues(prevEntrevues =>
            prevEntrevues.map(entrevue =>
                entrevue.etudiantDTO === entrevueAcceptee.etudiantDTO && entrevue.offreDeStageDTO === entrevueAcceptee.offreDeStageDTO
                    ? { ...entrevue, status: 'accepter' }
                    : entrevue
            )
        );

        EtudiantHeader.nbEntrevuesEnAttente = EtudiantHeader.nbEntrevuesEnAttente - 1;
    };

    const handleEntrevueRejete = (entrevueRejete) => {


        setEntrevues(prevEntrevues =>
            prevEntrevues.map(entrevue =>
                entrevue.etudiantDTO === entrevueRejete.etudiantDTO && entrevue.offreDeStageDTO === entrevueRejete.offreDeStageDTO
                    ? { ...entrevue, status: 'refuser' }
                    : entrevue
            )
        );

        EtudiantHeader.nbEntrevuesEnAttente = EtudiantHeader.nbEntrevuesEnAttente - 1;
    };

    const filterEntrevues = () => {
        return entrevues.filter(entrevue => {
            const titleMatch = entrevue.offreDeStageDTO.titre.toLowerCase().includes(searchTitle.toLowerCase());
            const dateMatch = entrevue.dateHeure.includes(searchDate);
            const companyMatch = entrevue.offreDeStageDTO.employeur.entreprise.toLowerCase().includes(searchCompany.toLowerCase());
            return titleMatch && dateMatch && companyMatch;
        });
    };

    const entrevuesAccepter = filterEntrevues().filter(entrevue => entrevue.status.toLowerCase() === 'accepter');
    const entrevuesEnAttente = filterEntrevues().filter(entrevue => entrevue.status.toLowerCase() === 'en attente');

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

        setSession(session.session)
        fetchSession(session.session)
    }

    function clearInputs() {
        setSearchTitle('');
        setSearchCompany('');
        setSearchDate('');
    }

    return (
        <>
            <EtudiantHeader userData={userData} onSendData={verificationSession} />
            <div className="container-fluid p-4">
                <div className="container flex-grow-1 pt-4">
                    <h1 className="mb-0 text-center" style={{fontSize: "4em"}}>{t('entrevueListTitle')}</h1>
                    <p className="text-center mb-4" style={{fontSize: "2em"}}>{t('entrevueListSubtitle')}</p>
                    <small style={{fontSize: "1rem"}}>{t('entrevueListConseil')}</small>

                    <div className="row mt-3 mb-3">
                        <div className="col-md-10 m-auto">
                            <div className="input-group">
                                <input
                                    type="text"
                                    className="form-control search-input"
                                    placeholder={t('RechercherParTitre')}
                                    value={searchTitle}
                                    onChange={(e) => setSearchTitle(e.target.value)}
                                />
                                <div className="input-group-prepend">
                                    <span className="input-group-text" id="search-title-icon">
                                        <FaSearch/>
                                    </span>
                                </div>
                                <div className="vertical-bar">|</div>
                                <input
                                    type="text"
                                    className="form-control search-input"
                                    placeholder={t('RechercherParEntreprise')}
                                    value={searchCompany}
                                    onChange={(e) => setSearchCompany(e.target.value)}
                                />
                                <div className="input-group-prepend">
                                    <span className="input-group-text" id="search-company-icon">
                                        <FaMapMarkerAlt/>
                                    </span>
                                </div>
                                <div className="vertical-bar">|</div>
                                <input
                                    type="date"
                                    className="form-control search-input"
                                    value={searchDate}
                                    onChange={(e) => setSearchDate(e.target.value)}
                                />
                                <div className="input-group-append">
                                    <Button variant="outline-secondary" onClick={clearInputs}>
                                        {t('Effacer')}
                                    </Button>
                                </div>
                            </div>
                        </div>
                    </div>

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
