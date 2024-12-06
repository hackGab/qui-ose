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
    const [loading, setLoading] = useState(true);
    const [offres, setOffres] = useState([]);
    const [error, setError] = useState(null);
    const [selectedSession, setSelectedSession] = useState(getLocalStorageSession); // État pour la session active
    const [visibleStatus, setVisibleStatus] = useState({ 'Attente': true });
    const [searchTerm, setSearchTerm] = useState('');

    const verifificationSession = (data) => {
        setSelectedSession(data.session);
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
                    throw new Error('lors de la récupération des données');
                }
                return response.json();
            })
            .then(data => {
                setOffres(data);
                setLoading(false);
            })
            .catch(error => {
                setError(error.message);
                setLoading(false);
            });
    };


    useEffect(() => {
        setSelectedSession(getLocalStorageSession());
        fetch(`http://localhost:8081/offreDeStage/session/${selectedSession}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('lors de la récupération des données');
                }
                return response.json();
            })
            .then(data => {
                setOffres(data);
                setLoading(false);
            })
            .catch(error => {
                setError(error.message);
                setLoading(false);
            });
    }, []);


    const toggleVisibility = (status) => {
        setVisibleStatus(prevState => ({
            ...prevState,
            [status]: !prevState[status]
        }));
    };

    const handleSearch = (event) => {
        setSearchTerm(event.target.value);
    };

    const filteredOffres = offres.filter(offre =>
        offre.employeur.entreprise.toLowerCase().includes(searchTerm.toLowerCase()) ||
        offre.titre.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const groupedOffres = filteredOffres.reduce((acc, offre) => {
        const status = offre.status || 'sans-cv';
        if (!acc[status]) {
            acc[status] = [];
        }
        acc[status].push(offre);
        return acc;
    }, {});

    const sortedStatuses = Object.keys(groupedOffres).sort((a, b) => {
        if (a === 'En attente') return -1;
        if (b === 'En attente') return 1;
        return a.localeCompare(b);
    });

    useEffect(() => {
        if (filteredOffres.length > 0) {
            const firstStatus = filteredOffres[0].status || 'sans-cv';
            setVisibleStatus(prevState => ({
                ...prevState,
                [firstStatus]: true
            }));
        }
    }, [searchTerm]);


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
            <GestionnaireHeader onSendData={verifificationSession}/>
            <div className="container-fluid p-4">
                <div className="container flex-grow-1 pt-5 mt-5">
                    <h1 className="mb-4 text-center">{t('employerListTitle')}</h1>

                    {offres.length === 0 ? (
                        <p className="text-center mt-5">{t('AucunEmployeurTrouve')}</p>
                    ) : (
                        <p className="text-center mb-4">{t('employerListSubtitle')}</p>
                    )}

                    <input
                        type="text"
                        className="form-control mb-4"
                        placeholder={t('searchEmployeur')}
                        value={searchTerm}
                        onChange={handleSearch}
                    />
                    {sortedStatuses.map(status => (
                        <details key={status} open={visibleStatus[status]}>
                            <summary className="cursor-pointer">
                                {t(status)} ({groupedOffres[status].length})
                            </summary>
                            <div className="row">
                                {groupedOffres[status].map(offreDeStage => (
                                    <div className="col-12 col-md-6 col-lg-4 mb-4" key={offreDeStage.id}>
                                        <Link
                                            to={`/detailsEmployeur/${offreDeStage.employeur.email}/${offreDeStage.id}`}
                                            className="text-decoration-none"
                                            state={{ offre: offreDeStage }}>

                                            <div className={`card shadow w-100 ${status.toLowerCase().replaceAll(' ', '')}`}>
                                                <span className={`position-absolute bottom-0 end-0 badge m-2 custom-badge ${status.toLowerCase().replaceAll(' ', '')}`}>
                                                    {t(status)}
                                                </span>
                                                <div className="card-body">
                                                    <h5 className="card-title">{offreDeStage.employeur.entreprise + " - " + offreDeStage.titre}</h5>
                                                    <p className="card-text">
                                                        <FaEnvelope /> {offreDeStage.employeur.credentials?.email}
                                                        <br />
                                                        <FaPhone /> {offreDeStage.employeur.phoneNumber}
                                                        <br />
                                                        <span className="badge bg-primary">{offreDeStage.localisation}</span>
                                                    </p>
                                                </div>
                                            </div>
                                        </Link>
                                    </div>
                                ))}
                            </div>
                        </details>
                    ))}
                </div>
            </div>
        </>
    );
}

export default ListeEmployeurs;
