import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Link } from 'react-router-dom';
import { FaEnvelope, FaPhone } from 'react-icons/fa';
import { useTranslation } from 'react-i18next';
import GestionnaireHeader from "./GestionnaireHeader";
import '../CSS/ListeEtudiants.css';

function ListeEtudiants() {
    const { t } = useTranslation();
    const [etudiants, setEtudiants] = useState([
        {
            id: 1,
            email: 'jean.dupont@example.com',
            first_name: 'Jean',
            last_name: 'Dupont',
            phone_number: '123-456-7890',
            departement: 'Informatique',
            status: 'validated',
        },
        {
            id: 2,
            email: 'marie.curie@example.com',
            first_name: 'Marie',
            last_name: 'Curie',
            phone_number: '098-765-4321',
            departement: 'Chimie',
            status: 'rejected',
        },
        {
            id: 3,
            email: 'paul.martin@example.com',
            first_name: 'Paul',
            last_name: 'Martin',
            phone_number: '321-654-0987',
            departement: 'Mathématiques',
            status: 'pending',
        },
        {
            id: 4,
            email: 'lisa.simpson@example.com',
            first_name: 'Lisa',
            last_name: 'Simpson',
            phone_number: '456-789-0123',
            departement: 'Physique',
            status: 'validated',
        }
    ]);

    const [error, setError] = useState(null);

    /*
    useEffect(() => {
        fetch('https://backend.com/api/etudiants')
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
    */

    if (error) {
        return <p className="text-center mt-5 text-danger">Erreur: {error}</p>;
    }

    return (
        <div className="container-fluid d-flex flex-column min-vh-100">
            <GestionnaireHeader />
            <div className="container flex-grow-1 pt-5 mt-5">
                <h1 className="mb-4 text-center">{t('studentListTitle')}</h1>
                <p className="text-center mb-4">{t('studentListSubtitle')}</p>
                <div className="row">
                    {etudiants.map((etudiant) => (
                        <div className="col-12 col-md-6 col-lg-4 mb-4" key={etudiant.id}>
                            <Link to={`/detailsEtudiant/${etudiant.id}`} className="text-decoration-none">
                                <div className={`card shadow w-100 ${etudiant.status}`}>
                                    <div className="card-body">
                                        <h5 className="card-title">{`${etudiant.first_name} ${etudiant.last_name}`}</h5>
                                        <p className="card-text">
                                            <FaEnvelope /> {etudiant.email}<br />
                                            <FaPhone /> {etudiant.phone_number}<br />
                                            <span className="badge bg-info">{t('department')}: {etudiant.departement}</span>
                                        </p>
                                    </div>
                                </div>
                            </Link>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}

export default ListeEtudiants;
