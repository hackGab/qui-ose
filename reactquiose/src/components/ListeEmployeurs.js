import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Link } from 'react-router-dom';
import { FaEnvelope, FaPhone } from 'react-icons/fa';
import { useTranslation } from 'react-i18next';
import GestionnaireHeader from "./GestionnaireHeader";
import '../CSS/ListeEtudiants.css';

function ListeEmployeurs() {
    const { t } = useTranslation();
    const [employeurs, setEmployeurs] = useState([
        {
            id: 1,
            email: 'contact@techcorp.com',
            company_name: 'TechCorp',
            contact_person: 'John Doe',
            phone_number: '123-456-7890',
            industry: 'Technology',
            status: 'validated',
        },
        {
            id: 2,
            email: 'info@bioinnovators.com',
            company_name: 'BioInnovators',
            contact_person: 'Alice Smith',
            phone_number: '098-765-4321',
            industry: 'Biotechnology',
            status: 'rejected',
        },
        {
            id: 3,
            email: 'hr@finservice.com',
            company_name: 'FinService',
            contact_person: 'Bob Johnson',
            phone_number: '321-654-0987',
            industry: 'Finance',
            status: 'pending',
        },
        {
            id: 4,
            email: 'contact@energyplus.com',
            company_name: 'EnergyPlus',
            contact_person: 'Lisa Ray',
            phone_number: '456-789-0123',
            industry: 'Energy',
            status: 'validated',
        }
    ]);

    const [error, setError] = useState(null);

    if (error) {
        return <p className="text-center mt-5 text-danger">Erreur: {error}</p>;
    }

    return (
        <div className="container-fluid d-flex flex-column min-vh-100">
            <GestionnaireHeader />
            <div className="container flex-grow-1 pt-5 mt-5">
                <h1 className="mb-4 text-center">{t('employerListTitle')}</h1>
                <p className="text-center mb-4">{t('employerListSubtitle')}</p>
                <div className="row">
                    {employeurs.map((employeur) => (
                        <div className="col-12 col-md-6 col-lg-4 mb-4" key={employeur.id}>
                            <Link to={`/detailsEmployeur/${employeur.id}`} className="text-decoration-none">
                                <div className={`card shadow w-100 ${employeur.status}`}>
                                    <div className="card-body">
                                        <h5 className="card-title">{`${employeur.company_name}`}</h5>
                                        <p className="card-text">
                                            <strong>{t('contactPerson')}: </strong>{employeur.contact_person}<br />
                                            <FaEnvelope /> {employeur.email}<br />
                                            <FaPhone /> {employeur.phone_number}<br />
                                            <span className="badge bg-info">{t('industry')}: {employeur.industry}</span>
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

export default ListeEmployeurs;
