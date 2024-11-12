import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Link } from 'react-router-dom';
import { FaEnvelope, FaPhone } from 'react-icons/fa';
import { useTranslation } from 'react-i18next';
import GestionnaireHeader from "./Header/GestionnaireHeader";
import '../CSS/ListeEtudiants.css';

function ListeEtudiants() {
    const { t } = useTranslation();
    const [etudiants, setEtudiants] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch('http://localhost:8081/etudiant/all', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Erreur lors de la récupération des données');
                }
                return response.json();
            })
            .then(data => {
                console.log(data);
                setEtudiants(data);
                setLoading(false);
            })
            .catch(error => {
                setError(error.message);
                setLoading(false);
            });
    }, []);

    const formatDepartementLabel = (departement) => {
        return departement
            ? departement.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, char => char.toUpperCase())
            : '';
    };

    if (loading) {
        return <div className="text-center mt-5">
            <div className="spinner-border" role="status"></div>
            <br/>
            <span className="sr-only">{t('chargementEtudiants')}</span>
        </div>;
    }

    if (error) {
        return <p className="text-center mt-5 text-danger">Erreur {error}</p>;
    }

    return (
        <>
            <GestionnaireHeader/>
            <div className="container-fluid p-4">
                <div className="container flex-grow-1 pt-5 mt-5">
                    <h1 className="mb-4 text-center">{t('studentListTitle')}</h1>

                    {etudiants.length === 0 ? (
                        <p className="text-center mt-5">{t('AucunEtudiantTrouve')}</p>
                    ) : (
                        <p className="text-center mb-4">{t('studentListSubtitle')}</p>
                    )}

                    <div className="row">
                        {etudiants.map((etudiant) => {
                            const status = etudiant.cv ? etudiant.cv.status : null;
                            return (
                                <div className="col-12 col-md-6 col-lg-4 mb-4" key={etudiant.id}>
                                    <Link
                                        to={`/detailsEtudiant/${etudiant.email}`}
                                        className="text-decoration-none"
                                        state={{ student: etudiant }}
                                    >
                                        <div className={`card shadow w-100 ${status ? status.toLowerCase() : 'sans-cv'}`}>
                                            <div className="card-body">
                                                <h5 className="card-title text-capitalize">{`${etudiant.firstName} ${etudiant.lastName}`}</h5>
                                                <p className="card-text">
                                                    <FaEnvelope/> {etudiant.credentials.email}<br/>
                                                    <FaPhone/> {etudiant.phoneNumber}<br/>
                                                    <span className="badge bg-info d-block department-label">
                                                        {t('department')}: {formatDepartementLabel(etudiant.departement)}
                                                    </span>
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

export default ListeEtudiants;
