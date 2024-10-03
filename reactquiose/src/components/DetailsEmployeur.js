import React, { useEffect, useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useParams } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import '../CSS/DetailsEtudiants.css';
import GestionnaireHeader from "./GestionnaireHeader";

function DetailsEmployeurs() {
    const { t } = useTranslation();
    const { id } = useParams();
    const [employer, setEmployer] = useState(null);
    const [error, setError] = useState(null);

    /*
    useEffect(() => {
        fetch(`https://backend.com/api/employeurs/${id}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Erreur lors de la récupération des données');
                }
                return response.json();
            })
            .then(data => {
                setEmployer(data);
            })
            .catch(error => {
                setError(error.message);
            });
    }, [id]);
    */

    // Hardcoded employer data for testing
    const employeurs = [
        {
            id: 1,
            email: 'employer1@example.com',
            company_name: 'Tech Corp',
            contact_person: 'John Smith',
            phone_number: '123-456-7890',
            industry: 'Informatique',
            stageUrl: 'https://example.com/stage1.pdf',
        },
        {
            id: 2,
            email: 'employer2@example.com',
            company_name: 'Bio Labs',
            contact_person: 'Jane Doe',
            phone_number: '098-765-4321',
            industry: 'Biotechnologie',
            stageUrl: 'https://example.com/stage2.pdf',
        },
    ];

    useEffect(() => {
        const foundEmployer = employeurs.find(employer => employer.id === parseInt(id));
        setEmployer(foundEmployer);
    }, [id]);

    if (error) {
        return <div className="text-danger">{t('error')}: {error}</div>;
    }

    if (!employer) {
        return <div>{t('employerNotFound')}</div>;
    }

    return (
        <div className="details-container">
            <GestionnaireHeader />

            <h1 className="mb-4 detail-title">{t('employerDetailsTitle')}</h1>

            <div className="row">
                <div className="col-md-6">
                    <h5>{t('companyInfo')}</h5>
                    <div className="details-info">
                        <p><strong>{t('nomDetail')}:</strong> {employer.company_name}</p>
                        <p><strong>{t('emailDetail')}:</strong> {employer.contact_person}</p>
                        <p><strong>{t('telephoneDetail')}:</strong> {employer.email}</p>
                        <p><strong>{t('industry')}:</strong> {employer.industry}</p>
                    </div>
                </div>

                <div className="col-md-6">
                    <h5 className="mb-3">{t('employerStage')}</h5>
                    <div className="details-info">
                        <p><strong>{t('titleStage')}:</strong> {employer.company_name}</p>
                        <p><strong>{t('stageDetail')}:</strong> {employer.contact_person}</p>
                        <p><strong>{t('dureeStage')}:</strong> {employer.email}</p>
                        <p><strong>{t('localisation')}:</strong> {employer.phone_number}</p>
                        <p><strong>{t('Exigence')}:</strong> {employer.industry}</p>
                        <p><strong>{t('DateDebut')}:</strong> {employer.company_name}</p>
                        <p><strong>{t('typeRenumeration')}:</strong> {employer.contact_person}</p>
                        <p><strong>{t('Disponiblite')}:</strong> {employer.email}</p>
                        <p><strong>{t('contact')}:</strong> {employer.phone_number}</p>
                    </div>

                    <div className="mt-4">
                        <h5>{t('actions')}</h5>
                        <div className="btn-group-vertical w-100">
                            <button className="btn btn-success mb-2">{t('validate')}</button>
                            <button className="btn btn-danger mb-2">{t('reject')}</button>
                            <button className="btn btn-primary">{t('confirm')}</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default DetailsEmployeurs;
