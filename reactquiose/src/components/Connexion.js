import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import "bootstrap/dist/css/bootstrap.min.css";
import { Icon } from 'react-icons-kit';
import { eyeOff } from 'react-icons-kit/feather/eyeOff';
import { eye } from 'react-icons-kit/feather/eye';
import { useTranslation } from "react-i18next";
import '../CSS/BoutonLangue.css'

function Connexion() {
    const [email, setEmail] = useState('');
    const [mpd, setMpd] = useState('');
    const [type, setType] = useState('password');
    const [icon, setIcon] = useState(eyeOff);
    const [errorMessages, setErrorMessages] = useState('');
    const navigate = useNavigate();
    const { t } = useTranslation();

    const afficherMdp = () => {
        setIcon(type === 'password' ? eye : eyeOff);
        setType(type === 'password' ? 'text' : 'password');
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        const userData = {
            email: email,
            password: mpd.trim(),
        };

        fetch('http://localhost:8081/user/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(userData),
        })
            .then((response) => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error(t('connexionEchouee'));
            })
            .then((data) => {
                const accessToken = data.accessToken;

                return fetch('http://localhost:8081/user/me', {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${accessToken}`
                    }
                });
            })
            .then((response) => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error(t('erreurLorsRecuperationUtilisateur'));
            })
            .then((userData) => {
                if (userData.role === 'ETUDIANT') {
                    navigate('/accueilEtudiant', { state: { userData } });
                } else if (userData.role === 'EMPLOYEUR') {
                    navigate('/accueilEmployeur', { state: { userData } });
                } else if (userData.role === 'GESTIONNAIRE') {
                    navigate('/accueilGestionnaire', { state: { userData } });
                } else if (userData.role === 'PROFESSEUR') {
                    navigate('/accueilProfesseur', { state: { userData } });
                }
            })
            .catch((error) => {
                console.error('Erreur lors de la connexion:', error);
                setErrorMessages(t('erreurLorsConnexion'));
            });
    };


    return (
        <div>
            <form className='pt-0 m-auto' onSubmit={handleSubmit}>
                {errorMessages && (
                    <div className='alert alert-danger' style={{ textAlign: 'center', fontSize: '2vmin' }}>
                        {errorMessages}
                    </div>
                )}

                <legend>{t('ChampsObligatoires')}</legend>

                <div className='row'>
                    <div className="form-group">
                        <label htmlFor="email">{t('Email')}</label>
                        <input
                            type="email"
                            className="form-control"
                            id="email"
                            name="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            placeholder="johndoe@gmail.com"
                            autoComplete={"on"}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="mdp">{t('MotDePasse')}</label>
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
                                    autoComplete={"off"}
                                    required
                                />
                            </div>

                            <span onClick={afficherMdp} style={{cursor: 'pointer', margin: "auto", marginLeft: "0.5em"}}>
                                <Icon icon={icon} size={20}/>
                            </span>
                        </div>
                    </div>
                </div>

                <button className="btn btn-primary w-50 mt-4" type="submit">{t('Connecter')}</button>

                <small style={{marginTop: '10px'}}>
                    {t('NoAccount')} <a href="/signUp">{t('Sinscrire')}</a>
                </small>
            </form>
        </div>
    );
}

export default Connexion;