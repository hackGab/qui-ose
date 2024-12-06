import React from 'react';
import { FaClock } from 'react-icons/fa';

const ContratCard = ({ contrat, userData, t, setSelectedContrat }) => (
    <div key={contrat.uuid} className="col-12 col-md-6 col-lg-4 mb-4">
        <div className={`card shadow w-100 position-relative mt-4
                         ${userData.role === 'EMPLOYEUR'
            ? (contrat.employeurSigne ? 'signer' : 'pasSigner')
            : (contrat.etudiantSigne ? 'signer' : 'pasSigner')}
            `}
             onClick={() => setSelectedContrat(contrat)}>
            <div className="card-header">
                <h5 className="card-title">
                    {contrat.description ? String(contrat.description) : t('DescriptionIndisponible')}
                </h5>

                <p className="card-text">
                    {contrat.lieuStage ? String(contrat.lieuStage) : t('LieuIndisponible')}
                </p>
            </div>

            <div className="card-body">
                <p className="card-text">
                    - {t('entrepriseEngagement')} <span className="text-lowercase">{contrat.entrepriseEngagement ? String(contrat.entrepriseEngagement) : t('NomIndisponible')}</span>
                    <br/>
                    <br/>
                    - <b>{t('description')} :</b> {contrat.description ? String(contrat.description) : t('DescriptionIndisponible')}
                    <br/>
                    <br/>
                    <b>
                        <FaClock/> {t('dateDebut')}: {contrat.dateDebut ? String(contrat.dateDebut) : t('Indisponible')}
                        <br/>
                        <FaClock/> {t('dateFin')}: {contrat.dateFin ? String(contrat.dateFin) : t('Indisponible')}
                    </b>
                </p>
                <p className={`card-text badge custom-badge text-white
                    ${userData.role === 'EMPLOYEUR'
                    ? (contrat.employeurSigne ? 'signer' : 'pasSigner')
                    : (contrat.etudiantSigne ? 'signer' : 'pasSigner')}
                    `}
                >
                    {userData.role === 'EMPLOYEUR'
                        ? (contrat.employeurSigne ? t('EmployeurDejaSigne') : t('EmployeurPasEncoreSigne'))
                        : (contrat.etudiantSigne ? t('EtudiantDejaSigne') : t('EtudiantPasEncoreSigne'))
                    }
                </p>
            </div>
        </div>
    </div>
);

export default ContratCard;