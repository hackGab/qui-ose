import EmployeurHeader from "./EmployeurHeader";
import React, {useEffect, useState} from "react";
import {useLocation} from "react-router-dom";
import {useTranslation} from "react-i18next";
import "../CSS/SignerContrat.css";
import { eyeOff } from 'react-icons-kit/feather/eyeOff';
import { eye } from 'react-icons-kit/feather/eye';
import {Icon} from "react-icons-kit";
import TableauContrat from "./TableauContrat.js";
import i18n from "i18next";



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
    const [contrat, setContrat] = useState({
        lieuStage: 'Montreal',
        dateDebut: '2023-01-01',
        dateFin: '2023-12-31',
        semaines: '15',
        heureHorraireDebut: '10:00',
        heureHorraireFin: '23:00',
        heuresParSemaine: '35',
        tauxHoraire: '37',
        description: 'Description du stage',
        collegeEngagement: 'André-Grasset',
        entrepriseEngagement: 'Montréal',
        etudiantEngagement: 'Bob',
    });

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
    //                 setContrat(data.contrat);
    //             } else {
    //                 setError('Aucun contrat trouvé');
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
        console.log(i18n.language)


        // Envoi de la signature du contrat au serveur
        const signature = mpd;
        if (!signature) {
            setError('Veuillez entrer votre mot de passe');
            return;
        }


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
                    animationBtn();
                    setContratSigne(true);
                    setError(null);
                }
                else {
                    animationBtnError();
                    setError('Mot de passe incorrect');
                }
            })
            .catch((error) => {
                animationBtnError();
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
            }, 2250);
        }, 2250);
    }

    const animationBtnError = () => {
        setButtonClass("onclick");
        setTimeout(() => {
            setButtonClass("refuse");
            setTimeout(() => {
                setButtonClass("");
                setContratSigne(false);
            }, 2250);
            setError("Erreur lors de la signature du contrat");
        }, 1250);
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


                            {/* Formulaire qui affiche le contrat */}
                            <TableauContrat contrat={contrat}/>

                            <legend style={{ fontSize: "1rem", textAlign: "center", color: "red", marginTop: "2em", marginBottom: "0" }}><i>{t('ChampsObligatoires')}</i></legend>

                            <div className="row">
                                <form className="mt-3 m-auto col-md-6 col-10">
                                    <div className="form-group">
                                        <label htmlFor="mdp">{t('SignatureDe')}
                                            <span className="text-lowercase">{userData.role}: <i>({t('MotDePasse')})</i></span></label>
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
                                        className={`btn-signer ${buttonClass} ${i18n.language === 'fr-CA' ? 'btn-signer-fr' : 'btn-signer-en'}`}
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