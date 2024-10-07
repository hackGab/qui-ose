import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useLocation } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import GestionnaireHeader from "./GestionnaireHeader";
import '../CSS/DetailsEtudiants.css';

function DetailsEtudiants() {
    const { t } = useTranslation();
    const location = useLocation();
    const student = location.state?.student;
    const [selectedStatus, setSelectedStatus] = useState(null);
    const [rejectionReason, setRejectionReason] = useState('');
    console.log('Student details:', student);

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
                console.log('Response status:', response.status);
                if (response.ok) {
                    window.location.href = '/listeEtudiants';
                } else {
                    console.error('Error details:', response.statusText);
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

    if (!student) {
        return <div>{t('studentNotFound')}</div>;
    }

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
                            <p className="text-danger text-center">{t('cvNotSubmitted')}</p>
                        ) : (
                            <iframe
                                src={student.cv.data}
                                title={t('studentCV')}
                                className="cv-frame"
                            ></iframe>
                        )}
                    </div>

                    <div className="mt-4">
                        <h5>{t('actions')}</h5>
                        <div className="btn-group-vertical w-100">
                            <button
                                className={`btn ${selectedStatus === 'validé' ? 'btn-success' : 'btn-gray'} mb-2`}
                                onClick={() => handleStatusSelect('validé')}
                            >
                                {t('validate')}
                            </button>
                            <button
                                className={`btn ${selectedStatus === 'rejeté' ? 'btn-danger' : 'btn-gray'} mb-2`}
                                onClick={() => handleStatusSelect('rejeté')}
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
                </div>
            </div>
        </div>
    );
}

export default DetailsEtudiants;
