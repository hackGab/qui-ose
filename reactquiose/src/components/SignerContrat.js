import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { eyeOff, eye } from 'react-icons-kit/feather';
import { Icon } from "react-icons-kit";
import EmployeurHeader from "./EmployeurHeader";
import EtudiantHeader from "./EtudiantHeader";
import TableauContrat from "./TableauContrat.js";
import "../CSS/SignerContrat.css";

function SignerContrat() {
    const location = useLocation();
    const userData = location.state?.userData;
    const { t, i18n } = useTranslation();

    // State
    const [type, setType] = useState('password');
    const [mpd, setMpd] = useState('');
    const [icon, setIcon] = useState(eyeOff);
    const [buttonClass, setButtonClass] = useState("");
    const [contrats, setContrats] = useState([]);
    const [selectedContrat, setSelectedContrat] = useState(null);
    const [error, setError] = useState(null);

    // Toggle password visibility
    const togglePasswordVisibility = () => {
        setIcon(type === 'password' ? eye : eyeOff);
        setType(type === 'password' ? 'text' : 'password');
    };

    // Fetch contrats data on mount
    useEffect(() => {
        const fetchContrats = async () => {
            try {
                let response;
                response = await fetch(`http://localhost:8081/contrat/getContrats-${userData.role.toLowerCase()}/${userData.credentials.email}`);

                if (!response.ok) throw new Error(`Erreur: ${response.status}`);

                const data = await response.json();
                console.log(data);
                setContrats(data);
            } catch (error) {
                setError(t('ErreurRecuperationContrat'));
                console.error(error);
            }
        };

        fetchContrats();
    }, [userData.credentials.email, userData.role, t]);

    const signerContrat = async (event) => {
        event.preventDefault();
        if (!mpd) {
            setError(t('VeuillezEntrerMotDePasse'));
            return;
        }
        try {
            const response = await fetch(`http://localhost:8081/contrat/signer-employer/${selectedContrat.uuid}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ signature: mpd })
            });

            const data = await response.json();
            console.log(data);
            if (data) {
                handleAnimation(true);
                setError(null);
            } else {
                handleAnimation(false);
                setError(t('MotDePasseIncorrect'));
            }
        } catch (error) {
            handleAnimation(false);
            setError(t('ErreurSignatureContrat'));
            console.error(error);
        }
    };

    const isButtonDisabled = () => {
        if (userData.role === 'EMPLOYEUR') {
            return selectedContrat?.employeurSigne;
        } else if (userData.role === 'ETUDIANT') {
            return selectedContrat?.etudiantSigne;
        }
        return false;
    };

    const handleAnimation = (isSuccess) => {
        setButtonClass("onclick");
        setTimeout(() => {
            setButtonClass(isSuccess ? "validate" : "refuse");
            setTimeout(() => setButtonClass(""), 2250);
        }, 1250);
    };

    return (
        <>
            {userData?.role === 'EMPLOYEUR' ? (
                <EmployeurHeader userData={userData} />
            ) : (
                <EtudiantHeader userData={userData} />
            )}

            <div className="container-fluid p-4 mes-entrevues-container">
                <div className="container mt-5">
                    {selectedContrat ? (
                        <div>
                            <h1 className="text-center mt-5 page-title">{t('VeuillezSignerContrat')}</h1>
                            {error && <div className='alert alert-danger text-center'>{error}</div>}

                            <TableauContrat contrat={selectedContrat} />

                            <legend className="text-center text-danger mt-2"><i>{t('ChampsObligatoires')}</i></legend>

                            <div className="row">
                                <form className="mt-3 m-auto col-md-6 col-10" onSubmit={signerContrat}>
                                    <div className="form-group">
                                        <label htmlFor="mdp">
                                            {t('SignatureDe')} {userData.role} <i>({t('MotDePasse')})</i>
                                        </label>
                                        <div className="input-group">
                                            <input
                                                type={type}
                                                className="form-control"
                                                id="mdp"
                                                placeholder="********"
                                                value={mpd}
                                                onChange={(e) => setMpd(e.target.value)}
                                                required
                                            />
                                            <span onClick={togglePasswordVisibility} className="icon-toggle align-content-center ps-1">
                                                <Icon icon={icon} size={30}/>
                                            </span>
                                        </div>
                                    </div>


                                    <button
                                        type="submit"
                                        className={`btn-signer ${buttonClass} ${isButtonDisabled() ? 'btn-disabled' : ''} ${i18n.language === 'fr-CA' ? 'btn-signer-fr' : 'btn-signer-en'}`}
                                        disabled={isButtonDisabled()}
                                    >
                                    </button>
                                </form>
                                <button onClick={() => setSelectedContrat(null)} className="btn btn-secondary mt-3">
                                    {t('Retour')}
                                </button>
                            </div>
                        </div>
                    ) : (
                        <div className="row">
                            <div className="text-center mb-4">
                                <h4>{t('CliquezSurLesContratsPourSigner')}</h4>
                            </div>
                            {contrats.map((contrat) => (
                                <div key={contrat.uuid} className="col-md-4">
                                    <div className="card mt-4" onClick={() => setSelectedContrat(contrat)}>
                                        <div className="card-body">
                                            <h5 className="card-title">
                                                {contrat.entrepriseEngagement ? String(contrat.entrepriseEngagement) : t('NomIndisponible')}
                                            </h5>
                                            <p className="card-text">
                                                {contrat.description ? String(contrat.description) : t('DescriptionIndisponible')}
                                            </p>
                                            <p className="card-text">
                                                {t('DateDebut')}: {contrat.dateDebut ? String(contrat.dateDebut) : t('Indisponible')}
                                            </p>
                                            <p className="card-text">
                                                {t('DateFin')}: {contrat.dateFin ? String(contrat.dateFin) : t('Indisponible')}
                                            </p>
                                            {userData.role === 'EMPLOYEUR' && (
                                                <p className="card-text text-success">
                                                    {contrat.employeurSigne ? t('EmployeurDejaSigne') : t('EmployeurPasEncoreSigne')}
                                                </p>
                                            )}
                                            {userData.role === 'ETUDIANT' && (
                                                <p className="card-text text-success">
                                                    {contrat.etudiantSigne ? t('EtudiantDejaSigne') : t('EtudiantPasEncoreSigne')}
                                                </p>
                                            )}
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>
        </>
    );
}

export default SignerContrat;
