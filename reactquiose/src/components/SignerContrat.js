import EmployeurHeader from "./EmployeurHeader";
import React, {useEffect, useState} from "react";
import {useLocation} from "react-router-dom";
import {useTranslation} from "react-i18next";
import "../CSS/SignerContrat.css";
import { eyeOff } from 'react-icons-kit/feather/eyeOff';
import { eye } from 'react-icons-kit/feather/eye';
import {Icon} from "react-icons-kit";



function SignerContrat() {
    const location = useLocation();
    const userData = location.state?.userData;
    const employeurEmail = userData.credentials.email;
    const [type, setType] = useState('password');
    const [mpd, setMpd] = useState('');
    const [icon, setIcon] = useState(eyeOff);
    const { t } = useTranslation();
    const [buttonClass, setButtonClass] = useState("");
    const [contratSigne, setContratSigne] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    // Toggle password visibility
    const afficherMdp = () => {
        setIcon(type === 'password' ? eye : eyeOff);
        setType(type === 'password' ? 'text' : 'password');
    };

    // Fonction pour récupérer le formulaire du contrat
    // useEffect(() => {
    //     if (!employeurEmail) {
    //         setError('Aucun employeur trouvé');
    //         setIsLoading(false);
    //         return;
    //     }
    //
    //     const url = `http://localhost:8081/contrat-stage/${employeurEmail}`;
    //
    //     fetch(url)
    //         .then((response) => {
    //             if (!response.ok) {
    //                 throw new Error(`Erreur lors de la requête: ${response.status}`);
    //             }
    //             return response.json();
    //         })
    //         .then((data) => {
    //             console.log('Réponse du serveur:', data);
    //
    //             if (data.success) {
    //                 setIsLoading(false);
    //             }
    //         })
    //         .catch((error) => {
    //             console.error('Erreur lors de la requête:', error);
    //             setError(error);
    //         });
    // }, [employeurEmail]);

    if (isLoading) {
        return <div style={{ fontSize: "1.5rem" }}>{t('ChargementDuContrat')}</div>;
    }



    // Fonction pour signer le contrat
    const signerContrat = (event) => {
        event.preventDefault();
        animationBtn();

        // Envoi de la signature du contrat au serveur
        const signature = document.getElementById('mdp').value;
        const url = 'http://localhost:8081/signer-contrat';
        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ signature: signature })
        })
            .then(response => response.json())
            .then(data => {
                console.log('Réponse du serveur:', data);
                if (data.success) {
                    setContratSigne(true);
                }
            })
            .catch((error) => {
                console.error('Erreur lors de la requête:', error);
            });
    };

    // Gérer l'animation du bouton
    const animationBtn = () => {
        setButtonClass("onclick");
        setTimeout(() => {
            setButtonClass("validate");
            setTimeout(() => {
                setButtonClass("");
                setContratSigne(true);
            }, 1250);
        }, 2250);
    }


    return (
        <>
            <EmployeurHeader userData={userData}/>

            <div className="container-fluid p-4 mes-entrevues-container">
                <div className="container mt-5">

                    { !contratSigne ? (
                        <div>
                            <h1 className="text-center mt-5 page-title">{t('VeuillezSignerContrat')}</h1>

                            {error && (
                                <div className='alert alert-danger' style={{ textAlign: 'center', fontSize: '2vmin' }}>
                                    {error}
                                </div>
                            )}

                            <legend style={{ fontSize: "1rem", textAlign: "center", color: "red"}}><i>{t('ChampsObligatoires')}</i></legend>

                            <div className="row">
                                <form className="mt-5 m-auto col-md-6 col-10">
                                    <div className="form-group">
                                        {/*<label htmlFor="mdp">Signature de l'<span className="text-lowercase">{userData.role}</span>:</label>*/}
                                        {/*<input type="text" className="form-control" id="mdp" placeholder={t('EntrezVotreMdp')} required/>*/}

                                        <label htmlFor="mdp">Signature de l'<span
                                            className="text-lowercase">{userData.role}: <i>({t('MotDePasse')})</i></span></label>
                                        <div className="d-flex">
                                            <div className="input-group">
                                                <input
                                                    type={type}
                                                    className="form-control m-0"
                                                    id="mdp"
                                                    name="mdp"
                                                    placeholder="********"
                                                    value={mpd}
                                                    onChange={(e) => setMpd(e.target.value)}
                                                    autoComplete="off"
                                                    required
                                                />
                                            </div>

                                            <span onClick={afficherMdp}
                                                  style={{cursor: 'pointer', margin: "auto", marginLeft: "0.5em"}}>
                                                <Icon icon={icon} size={20}/>
                                            </span>
                                        </div>
                                    </div>

                                    <button
                                        type="submit"
                                        className={`btn-signer ${buttonClass}`}
                                        onClick={signerContrat}
                                    >
                                    </button>
                                </form>
                            </div>
                        </div>
                    ) : (
                        <h1 className="text-center mt-5 page-title">{t('ContratSigne')}</h1>
                    ) }
                </div>
            </div>
        </>
    );
}

export default SignerContrat;