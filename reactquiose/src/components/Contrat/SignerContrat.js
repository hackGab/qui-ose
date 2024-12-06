import React, {useEffect, useState} from "react";
import {useLocation} from "react-router-dom";
import {useTranslation} from "react-i18next";
import {eyeOff, eye} from 'react-icons-kit/feather';
import {Icon} from "react-icons-kit";
import EmployeurHeader from "../Header/EmployeurHeader";
import EtudiantHeader from "../Header/EtudiantHeader";
import TableauContrat from "./TableauContrat.js";
import "../../CSS/SignerContrat.css";
import {getLocalStorageSession} from "../../utils/methodes/getSessionLocalStorage";
import {FaClock} from "react-icons/fa";
import ContratCard from "./ContratCard";

function SignerContrat() {
    const location = useLocation();
    const userData = location.state?.userData;
    const {t, i18n} = useTranslation();

    // State
    const [type, setType] = useState('password');
    const [mdp, setMdp] = useState("");
    const [icon, setIcon] = useState(eyeOff);
    const [buttonClass, setButtonClass] = useState("");
    const [contrats, setContrats] = useState([]);
    const [selectedContrat, setSelectedContrat] = useState(null);
    const [error, setError] = useState(null);
    const [session, setSession] = useState(getLocalStorageSession())

    // Toggle password visibility
    const togglePasswordVisibility = () => {
        setIcon(type === 'password' ? eye : eyeOff);
        setType(type === 'password' ? 'text' : 'password');
    };

    const fetchSession = async (session) => {
        try {
            const response = await fetch(`http://localhost:8081/contrat/getContrats-${userData.role.toLowerCase()}/${userData.credentials.email}/session/${session}`);
            if (!response.ok) throw new Error(`Erreur: ${response.status}`);

            const data = await response.json();
            setContrats(data);
        } catch (error) {
            messageErreur(t('ErreurRecuperationContrat'));
            console.error(error);
        }
    }

    useEffect(() => {
        fetchSession(session)
    }, [userData.credentials.email, userData.role, t]);

    const signerContrat = async (event) => {
        event.preventDefault();
        if (!mdp) {
            messageErreur(t('VeuillezEntrerMotDePasse'));
            return;
        }

        try {
            const response = await fetch(`http://localhost:8081/contrat/signer-${userData.role.toLowerCase()}/${selectedContrat.uuid}`, {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({password: mdp}),
            });

            const data = await response.json();
            if (data) {
                handleAnimation(true, null);
                updateContratSignStatus();
                setSelectedContrat(data);

                contrats.forEach((contrat, index) => {
                    if (contrat.uuid === data.uuid) {
                        contrats[index] = data;
                    }
                });
            } else {
                handleAnimation(false, t('MotDePasseIncorrect'));
            }
        } catch (error) {
            handleAnimation(false, t('ErreurLorsRecuperation'));
            console.error(error);
        }
    };

    const handleAnimation = (isSuccess, erreur) => {
        setButtonClass("onclick");
        setTimeout(() => {
            setButtonClass(isSuccess ? "validate" : "refuse");
            messageErreur(erreur);

            setTimeout(() => setButtonClass(""), 2250);
        }, 1250);
    };

    const messageErreur = (erreur) => {
        setTimeout(() => {
            setError(erreur);
            setTimeout(() => setError(null), 5000);
        }, 200);
    }

    const isButtonDisabled = () => {
        if (userData.role === 'EMPLOYEUR') {
            return selectedContrat?.employeurSigne;
        } else if (userData.role === 'ETUDIANT') {
            return selectedContrat?.etudiantSigne;
        }
        return false;
    };

    const verificationSession = (data) => {
        setSession(data.session);
        fetchSession(data.session)
    }

    const updateContratSignStatus = () => {
        setSelectedContrat(prevContrat => {
            if (userData.role === 'EMPLOYEUR') {
                return {...prevContrat, employeurSigne: true};
            } else if (userData.role === 'ETUDIANT') {
                return {...prevContrat, etudiantSigne: true};
            }
            return prevContrat;
        });
    };

    const signedContrats = contrats.filter(contrat =>
        (userData.role === 'EMPLOYEUR' && contrat.employeurSigne) ||
        (userData.role === 'ETUDIANT' && contrat.etudiantSigne)
    );

    const unsignedContrats = contrats.filter(contrat =>
        (userData.role === 'EMPLOYEUR' && !contrat.employeurSigne) ||
        (userData.role === 'ETUDIANT' && !contrat.etudiantSigne)
    );

    const shouldOpenSignedDetails = unsignedContrats.length === 0 && signedContrats.length > 0;

    return (
        <>
            {userData?.role === 'EMPLOYEUR' ? <EmployeurHeader userData={userData} onSendData={verificationSession}/> :
                <EtudiantHeader userData={userData} onSendData={verificationSession}/>}

            <div className="container-fluid p-4 mes-entrevues-container">
                <div className="container mt-5">
                    {selectedContrat ? (
                        <div>

                            <div className="text-center mt-3">
                                <h2 className="text-center mt-5 page-title">
                                    {selectedContrat.employeurSigne && userData.role === 'EMPLOYEUR' || selectedContrat.etudiantSigne && userData.role === 'ETUDIANT' ? t('ContratSigne') : t('VeuillezSignerContrat')}
                                </h2>
                            </div>

                            <TableauContrat contrat={selectedContrat}/>

                            <div className="text-center mt-3">
                                {selectedContrat.employeurSigne && userData.role === 'EMPLOYEUR' || selectedContrat.etudiantSigne && userData.role === 'ETUDIANT' ? (
                                    <button onClick={() => setSelectedContrat(null)}
                                            className="btn btn-secondary mt-3 w-75">
                                        {t('Retour')}
                                    </button>
                                ) : (
                                    <>
                                        {error && <div className='alert alert-danger text-center'>{error}</div>}
                                        <legend className="text-center text-danger mt-2">
                                            <i>{t('ChampsObligatoires')}</i></legend>
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
                                                            value={mdp}
                                                            onChange={(e) => setMdp(e.target.value)}
                                                        />
                                                        <span onClick={togglePasswordVisibility}
                                                              className="icon-toggle align-content-center ps-1">
                                                            <Icon icon={icon} size={30}/>
                                                        </span>
                                                    </div>
                                                </div>
                                                <button
                                                    type="submit"
                                                    className={`btn-success btn
                                                        ${buttonClass}
                                                        ${isButtonDisabled() ? 'btn-disabled' : ''} 
                                                        ${(i18n.language === 'fr' || i18n.language === 'fr-CA') ? 'btn-signer-fr' : 'btn-signer-en'}
                                                    `}
                                                    disabled={isButtonDisabled()}
                                                />
                                            </form>
                                            <div className="text-center mt-3">
                                                <button onClick={() => setSelectedContrat(null)}
                                                        className="btn btn-secondary mt-3 w-75">
                                                    {t('Retour')}
                                                </button>
                                            </div>
                                        </div>
                                    </>
                                )}
                            </div>
                        </div>
                    ) : (
                        <div className="row">
                            <div className="text-center mb-4">
                                <h4>{t('CliquezSurLesContratsPourSigner')}</h4>
                            </div>
                            <details open={!shouldOpenSignedDetails} className="p-0">
                                <summary>{t('ContratsPasSignes')} ({unsignedContrats.length})</summary>
                                <div className="row">
                                    {unsignedContrats.map((contrat) => (
                                        <ContratCard key={contrat.uuid} contrat={contrat} userData={userData} t={t}
                                                     setSelectedContrat={setSelectedContrat}/>
                                    ))}
                                </div>
                            </details>
                            <details open={shouldOpenSignedDetails} className="p-0">
                                <summary>{t('ContratsSignes')} ({signedContrats.length})</summary>
                                <div className="row">
                                    {signedContrats.map((contrat) => (
                                        <ContratCard key={contrat.uuid} contrat={contrat} userData={userData} t={t}
                                                     setSelectedContrat={setSelectedContrat}/>
                                    ))}
                                </div>
                            </details>
                        </div>
                        )}
                </div>
            </div>
        </>
    );
}

export default SignerContrat;
