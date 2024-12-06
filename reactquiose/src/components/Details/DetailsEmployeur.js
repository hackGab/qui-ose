import 'bootstrap/dist/css/bootstrap.min.css';
import {useLocation} from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import '../../CSS/DetailsEtudiants.css';
import GestionnaireHeader from "../Header/GestionnaireHeader";
import React, {useState} from "react";

function DetailsEmployeurs() {
    const { t } = useTranslation();
    const location = useLocation();
    const offre = location.state?.offre;
    const [selectedStatus, setSelectedStatus] = useState(null);
    const [rejectionReason, setRejectionReason] = useState('');


    const updateOffreStatus = (status) => {
        const body = {
            status: status,
            rejectionReason: status === 'Rejeté' ? rejectionReason : null
        };

        fetch(`http://localhost:8081/gestionnaire/validerOuRejeterOffre/${offre.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(body)
        })
            .then(response => {
                if (response.ok) {
                    window.location.href = '/listeEmployeurs';
                }
            })
            .catch(error => {
                console.error('Erreur lors de la mise à jour de l\'offre de stage:', error);
            });
    };

    const handleStatusSelect = (status) => {
        setSelectedStatus(status);
        if (status === 'Rejeté') {
            setRejectionReason('');
        }
    };

    const verifificationSession = (data) => {

    };

    const handleConfirm = () => {
        if (selectedStatus) {
            updateOffreStatus(selectedStatus);
        }
    };


    if (!offre) {
        return <div>{t('offreNotFound')}</div>;
    }

    const isOffreValidated = offre.status === 'Validé';
    const isOffreRejected = offre.status === 'Rejeté';

    return (
        <>
            <GestionnaireHeader onSendData={verifificationSession}/>
            <div className="details-container">

                <h1 className="mb-4 detail-title">{t('employerDetailsTitle')}</h1>

                <div className="row">
                    <div className="col-md-6">
                        <h5>{t('companyInfo')}</h5>
                        <div className="details-info">
                            <p><strong>{t('nomDetail')}:</strong> {offre.employeur.firstName}</p>
                            <p><strong>{t('emailDetail')}:</strong> {offre.employeur.email}</p>
                            <p><strong>{t('telephoneDetail')}:</strong> {offre.employeur.phoneNumber}</p>
                            <p><strong>{t('industry')}:</strong> {offre.employeur.entreprise}</p>
                            <p><strong>{t('titleStage')}:</strong> {offre.titre}</p>
                            <p><strong>{t('dureeStage')}:</strong> {offre.dateLimite}</p>
                            <p><strong>{t('localisation')}:</strong> {offre.localisation}</p>
                            <p><strong>{t('DateDebut')}:</strong> {offre.datePublication}</p>
                            <p><strong>{t('Disponiblite')}:</strong> {offre.nbCandidats}</p>
                        </div>
                    </div>

                    <div className="col-md-6">
                        <h5 className="mb-3">{t('employerStage')}</h5>
                        <div className="iframe-container">
                            <iframe
                                src={offre.data}
                                className="cv-frame"
                            ></iframe>
                        </div>

                        <div className="mt-4">
                            <h5>{t('actions')}</h5>
                            <div className="btn-group-vertical w-100">
                                <button
                                    className={`btn ${selectedStatus === 'Validé' ? 'btn-secondary' : 'btn-success'} mb-2`}
                                    onClick={() => handleStatusSelect('Validé')}
                                    disabled={isOffreValidated}
                                >
                                    {t('validate')}
                                </button>
                                <button
                                    className={`btn ${selectedStatus === 'Rejeté' ? 'btn-secondary' : 'btn-danger'} mb-2`}
                                    onClick={() => handleStatusSelect('Rejeté')}
                                    disabled={isOffreRejected}
                                >
                                    {t('reject')}
                                </button>

                                {selectedStatus === 'Rejeté' && (
                                    <div className="mt-1 mb-2 col-12">
                                        <textarea
                                            rows="3"
                                            placeholder={t('enterRejectionReason')}
                                            value={rejectionReason}
                                            onChange={(e) => setRejectionReason(e.target.value)}
                                            className="form-control rejection-reason"
                                        />
                                    </div>
                                )}

                                <button className="btn btn-primary" onClick={handleConfirm}>
                                    {t('confirm')}
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}

export default DetailsEmployeurs;