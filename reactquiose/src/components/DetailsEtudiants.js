import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useLocation } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import GestionnaireHeader from "./GestionnaireHeader";
import '../CSS/DetailsEtudiants.css';

function DetailsEtudiants() {
    const { t } = useTranslation();
    const location = useLocation();
    const student = location.state?.student;  // Récupère les détails de l'étudiant depuis l'état de location

    // Vérification si les données de l'étudiant existent
    if (!student) {
        return <div>{t('studentNotFound')}</div>;
    }

    // Vérification si seulement le CV est manquant
    const isCvMissing = !student.cv?.data;

    return (
        <div className="details-container">
            <GestionnaireHeader />

            <h1 className="mb-4 detail-title">{t('studentDetailsTitle')}</h1>

            <div className="row">
                <div className="col-md-6">
                    <h5>{t('personalInfo')}</h5>
                    <div className="details-info">
                        <p><strong>{t('nomDetail')}:</strong> {student.firstName} {student.lastName}</p>
                        <p><strong>{t('emailDetail')}:</strong> {student.credentials.email}</p>
                        <p><strong>{t('telephoneDetail')}:</strong> {student.phoneNumber}</p>
                        <p><strong>{t('departmentDetail')}:</strong> {student.departement}</p>
                    </div>
                </div>

                <div className="col-md-6">
                    <h5 className="mb-3">{t('studentCV')}</h5>
                    <div className="iframe-container">
                        {isCvMissing ? (
                            <p className="text-danger text-center">{t('cvNotSubmitted')}</p>  // Affiche un message si le CV est manquant
                        ) : (
                            <iframe
                                src={student.cv.data}  // Utilisez la propriété data pour afficher le CV PDF
                                title={t('studentCV')}
                                className="cv-frame"
                            ></iframe>
                        )}
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
