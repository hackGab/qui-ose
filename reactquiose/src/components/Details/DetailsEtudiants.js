import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useLocation } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import GestionnaireHeader from "../Header/GestionnaireHeader";
import '../../CSS/DetailsEtudiants.css';

function DetailsEtudiants() {
    const {t} = useTranslation();
    const location = useLocation();
    const student = location.state?.student;
    const [selectedStatus, setSelectedStatus] = useState(null);
    const [rejectionReason, setRejectionReason] = useState('');

    const updateCVStatus = (status) => {
        const body = {
            status: status,
            rejectionReason: status === 'rejeté' ? rejectionReason : null
        };

        fetch(`http://localhost:8081/gestionnaire/validerOuRejeterCV/${student.cv.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(body)
        })
            .then(response => {
                if (response.ok) {
                    window.location.href = '/listeEtudiants';
                }
            })
            .catch(error => {
                console.error('Erreur lors de la mise à jour du CV:', error);
            });
    };

    const handleStatusSelect = (status) => {
        setSelectedStatus(status);
        if (status === 'rejeté') {
            setRejectionReason('');
        }
    };

    const handleConfirm = () => {
        if (selectedStatus) {
            updateCVStatus(selectedStatus);
        }
    };

    const verificationSession = (data) => {

    }

    if (!student) {
        return <div>{t('studentNotFound')}</div>;
    }

    const isCvMissing = !student.cv?.data;
    const isCvValidated = student.cv?.status === 'validé';
    const isCvRejected = student.cv?.status === 'rejeté';

    return (
        <>
            <GestionnaireHeader onSendData={verificationSession}/>
             <div className="details-container">

                <h1 className="mb-4 detail-title">{t('studentDetailsTitle')}</h1>

                <div className="row">
                    <div className="col-md-6">
                        <h5>{t('personalInfo')}</h5>
                        <div className="details-info">
                            <p><strong>{t('nomDetail')}:</strong> {student.firstName} {student.lastName}</p>
                            <p><strong>{t('emailDetail')}:</strong> {student.credentials.email}</p>
                            <p><strong>{t('telephoneDetail')}:</strong> {student.phoneNumber}</p>
                            <p><strong>{t('departmentDetail')}:</strong> {student.departement.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, char => char.toUpperCase())}</p>
                        </div>
                    </div>

                    <div className="col-md-6">
                        <h5 className="mb-3">{t('studentCV')}</h5>
                        <div className="iframe-container">
                            {isCvMissing ? (
                                <p className="text-danger text-center">{t('cvNotSubmitted')}</p>
                            ) : (
                                <iframe
                                    src={student.cv.data}
                                    title={t('studentCV')}
                                    className="cv-frame"
                                ></iframe>
                            )}
                        </div>

                        { !isCvMissing && (
                            <div className="mt-4">
                                <h5>{t('actions')}</h5>
                                <div className="btn-group-vertical w-100">
                                    <button
                                        className={`btn ${isCvValidated ? 'btn-secondary' : 'btn-success'} mb-2`}
                                        onClick={() => handleStatusSelect('validé')}
                                        disabled={isCvValidated}
                                    >
                                        {t('validate')}
                                    </button>
                                    <button
                                        className={`btn ${isCvRejected ? 'btn-secondary' : 'btn-danger'} mb-2`}
                                        onClick={() => handleStatusSelect('rejeté')}
                                        disabled={isCvRejected}
                                    >
                                        {t('reject')}
                                    </button>

                                    {selectedStatus === 'rejeté' && (
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
                        )}
                    </div>
                </div>
            </div>
        </>
    );
}

export default DetailsEtudiants;
