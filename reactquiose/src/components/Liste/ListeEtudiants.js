import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Link } from 'react-router-dom';
import { FaEnvelope, FaPhone } from 'react-icons/fa';
import { useTranslation } from 'react-i18next';
import GestionnaireHeader from "../Header/GestionnaireHeader";
import '../../CSS/ListeEtudiants.css';

function ListeEtudiants() {
    const { t } = useTranslation();
    const [etudiants, setEtudiants] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');
    const [visibleStatus, setVisibleStatus] = useState({ 'Attente': true });


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

                setEtudiants(data);
                setLoading(false);
            })
            .catch(error => {
                setError(error.message);
                setLoading(false);
            });
    }, []);


    const handleSearch = (event) => {
        setSearchTerm(event.target.value);
    };

    const filteredEtudiants = etudiants.filter(etudiant =>
        etudiant.firstName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        etudiant.lastName.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const groupedEtudiants = filteredEtudiants.reduce((acc, etudiant) => {
        const status = etudiant.cv ? etudiant.cv.status : 'Aucun CV';
        if (!acc[status]) {
            acc[status] = [];
        }
        acc[status].push(etudiant);
        return acc;
    }, {});

    const sortedStatuses = Object.keys(groupedEtudiants).sort((a, b) => {
        if (a === 'En attente') return -1;
        if (b === 'En attente') return 1;
        return a.localeCompare(b);
    });


    const formatDepartementLabel = (departement) => {
        return departement
            ? departement.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, char => char.toUpperCase())
            : '';
    };

    const verificationSession = (data) => {};

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
            <GestionnaireHeader onSendData={verificationSession}/>
            <div className="container-fluid p-4">
                <div className="container flex-grow-1 pt-5 mt-5">
                    <h1 className="mb-4 text-center">{t('studentListTitle')}</h1>

                    {etudiants.length === 0 ? (
                        <p className="text-center mt-5">{t('AucunEtudiantTrouve')}</p>
                    ) : (
                        <p className="text-center mb-4">{t('studentListSubtitle')}</p>
                    )}

                    <input
                        type="text"
                        className="form-control mb-4"
                        placeholder={t('searchStudents')}
                        value={searchTerm}
                        onChange={handleSearch}
                    />
                    {sortedStatuses.map(status => (
                        <details key={status} open={visibleStatus[status]}>
                            <summary className="cursor-pointer">
                                {t(status)} ({groupedEtudiants[status].length})
                            </summary>
                            <div className="row">
                                {groupedEtudiants[status].map(etudiant => (
                                    <div className="col-12 col-md-6 col-lg-4 mb-4" key={etudiant.id}>
                                        <Link
                                            to={`/detailsEtudiant/${etudiant.email}`}
                                            className="text-decoration-none"
                                            state={{student: etudiant}}
                                        >
                                            <div
                                                className={`card shadow w-100 position-relative ${status.toLowerCase().replaceAll(' ', '')}`}>
                                                <span
                                                    className={`position-absolute top-0 end-0 badge m-2 custom-badge ${status.toLowerCase().replaceAll(' ', '')}`}>
                                                    {status ? t(status) : t('AucunCV')}
                                                </span>
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
                                ))}
                            </div>
                        </details>
                    ))}
            </div>
        </div>
</>
)
    ;
}

export default ListeEtudiants;
