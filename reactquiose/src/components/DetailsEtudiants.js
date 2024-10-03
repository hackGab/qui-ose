import React, { useEffect, useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useParams } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import '../CSS/DetailsEtudiants.css';
import GestionnaireHeader from "./GestionnaireHeader";

function DetailsEtudiants() {
    const { t } = useTranslation();
    const { id } = useParams();
    const [student, setStudent] = useState(null);
    const [error, setError] = useState(null);

    /*
    useEffect(() => {
        fetch(`https://backend.com/api/etudiants/${id}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Erreur lors de la récupération des données');
                }
                return response.json();
            })
            .then(data => {
                setStudent(data);
            })
            .catch(error => {
                setError(error.message);
            });
    }, [id]);
    */

    // Hardcodez les étudiants pour le test
    const etudiants = [
        {
            id: 1,
            email: 'etudiant1@example.com',
            first_name: 'Jean',
            last_name: 'Dupont',
            phone_number: '123-456-7890',
            departement: 'Informatique',
            cvUrl: 'https://example.com/cv1.pdf',
        },
        {
            id: 2,
            email: 'etudiant2@example.com',
            first_name: 'Marie',
            last_name: 'Curie',
            phone_number: '098-765-4321',
            departement: 'Chimie',
            cvUrl: 'https://example.com/cv2.pdf',
        },
    ];

    useEffect(() => {
        const foundStudent = etudiants.find(student => student.id === parseInt(id));
        setStudent(foundStudent);
    }, [id, etudiants]);

    if (error) {
        return <div className="text-danger">{t('error')}: {error}</div>;
    }

    if (!student) {
        return <div>{t('studentNotFound')}</div>;
    }

    return (
        <div className="details-container">
            <GestionnaireHeader />

            <h1 className="mb-4 detail-title">{t('studentDetailsTitle')}</h1>

            <div className="row">
                <div className="col-md-6">
                    <h5>{t('personalInfo')}</h5>
                    <div className="details-info">
                        <p><strong>{t('nomDetail')}:</strong> {student.first_name} {student.last_name}</p>
                        <p><strong>{t('emailDetail')}:</strong> {student.email}</p>
                        <p><strong>{t('telephoneDetail')}:</strong> {student.phone_number}</p>
                        <p><strong>{t('departmentDetail')}:</strong> {student.departement}</p>
                    </div>
                </div>

                <div className="col-md-6">
                    <h5 className="mb-3">{t('studentCV')}</h5>
                    <div className="iframe-container">
                        <iframe
                            src={student.cvUrl}
                            title={t('studentCV')}
                            className="cv-frame"
                        ></iframe>
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

export default DetailsEtudiants;
