import React, { useEffect, useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Link } from 'react-router-dom';
import { FaEnvelope, FaPhone } from 'react-icons/fa';
import { useTranslation } from 'react-i18next';
import GestionnaireHeader from "../Header/GestionnaireHeader";
import '../../CSS/ListeEtudiants.css';
import {getLocalStorageSession} from "../../utils/methodes/getSessionLocalStorage";

function ListeEmployeurs() {
    const { t } = useTranslation();
    const [employeurs, setEmployeurs] = useState([]);
    const [loading, setLoading] = useState(true);
    const [offres, setOffres] = useState([]);
    const [error, setError] = useState(null);
    const [selectedSession, setSelectedSession] = useState(""); // État pour la session active

    const verifificationSession = (data) => {
        console.log(data);
        fetchBySession(data.session);
    }


    const fetchBySession = (session) => {

        const url = `http://localhost:8081/offreDeStage/session/${session}`;

        fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(response => {
                if (!response.ok) {
                    console.log(response);
                    throw new Error('lors de la récupération des données');
                }
                return response.json();
            })
            .then(data => {
                console.log(data);
                setOffres(data);
                setLoading(false);
            })
            .catch(error => {
                setError(error.message);
                setLoading(false);
            });
    };


    useEffect(() => {
        let session = getLocalStorageSession();
        fetch(`http://localhost:8081/offreDeStage/session/${session}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(response => {
                if (!response.ok) {
                    console.log(response);
                    throw new Error('lors de la récupération des données');
                }
                return response.json();
            })
            .then(data => {
                console.log(data);
                setOffres(data);
                setLoading(false);
            })
            .catch(error => {
                setError(error.message);
                setLoading(false);
            });
    }, []);

    const handleSessionChange = (event) => {
        setSelectedSession(event.target.value);
    };

    const employeursFiltres = employeurs.filter(employeur => employeur.session === selectedSession);


    if (loading) {
        return <div className="text-center mt-5">
            <div className="spinner-border" role="status"></div>
            <br/>
            <span className="sr-only">{t('chargementEmployeurs')}</span>
        </div>;
    }

    if (error) {
        return <p className="text-center mt-5 text-danger">Erreur: {error}</p>;
    }

    return (
        <>
            <GestionnaireHeader/>
            <div className="container-fluid p-4">
                <div className="container flex-grow-1 pt-5 mt-5">
                    <h1 className="mb-4 text-center">{t('employerListTitle')}</h1>

                    {offres.length === 0 ? (
                        <p className="text-center mt-5">{t('AucunEmployeurTrouve')}</p>
                    ) : (
                        <p className="text-center mb-4">{t('employerListSubtitle')}</p>
                    )}

                    <div className="row">
                        {offres.map((offreDeStage) => {
                            const status = offreDeStage ? offreDeStage.status : null;
                            return (
                                <div className="col-12 col-md-6 col-lg-4 mb-4" key={offreDeStage.id}>
                                    <Link
                                        to={`/detailsEmployeur/${offreDeStage.employeur.email}/${offreDeStage.id}`}
                                        className="text-decoration-none"
                                        state={{offre: offreDeStage}}>

                                        <div
                                            className={`card shadow w-100 ${status ? status.toLowerCase() : 'sans-cv'}`}>
                                            {status && (
                                                <span className="position-absolute top-0 end-0 badge bg-secondary m-2 custom-badge">
                                                    {t(status)}
                                                </span>
                                            )}
                                            <div className="card-body">
                                                <h5 className="card-title">{offreDeStage.employeur.entreprise + " - " + offreDeStage.titre}</h5>
                                                <p className="card-text">
                                                    <FaEnvelope/> {offreDeStage.employeur.credentials?.email}
                                                    <br/>
                                                    <FaPhone/> {offreDeStage.employeur.phoneNumber}
                                                    <br/>
                                                    <span
                                                        className="badge bg-primary">{offreDeStage.localisation}</span>
                                                </p>
                                            </div>
                                        </div>
                                    </Link>
                                </div>
                            );
                        })}
                    </div>
                </div>
            </div>
        </>
    );
}

export default ListeEmployeurs;
