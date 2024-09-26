import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';  // Utilisez useNavigate pour rediriger
import "bootstrap/dist/css/bootstrap.min.css";
import { Icon } from 'react-icons-kit';
import { eyeOff } from 'react-icons-kit/feather/eyeOff';
import { eye } from 'react-icons-kit/feather/eye';
import {useTranslation} from "react-i18next";
import i18n from "i18next";

function Connexion() {
    const [email, setEmail] = useState('');
    const [mpd, setMpd] = useState('');
    const [type, setType] = useState('password');
    const [icon, setIcon] = useState(eyeOff);
    const [errorMessages, setErrorMessages] = useState('');
    const navigate = useNavigate();
    const {t} = useTranslation();

    const afficherMdp = () => {
        if (type === 'password') {
            setIcon(eye);
            setType('text');
        } else {
            setIcon(eyeOff);
            setType('password');
        }
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        const userData = {
            email: email,
            password: mpd.trim(),
        };
        console.log('Données envoyées au backend:', userData);
        fetch('http://localhost:8080/user/login', {
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
                console.log('Réponse du serveur:', data);
                navigate('/accueil');
            })
            .catch((error) => {
                console.error('Erreur lors de la connexion:', error);
                setErrorMessages(t('erreurLorsConnexion'));
            });
    };

    return (
        <form className='pt-0 m-auto' onSubmit={handleSubmit}>
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
                        required
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="mpd">{t('MotDePasse')}</label>
                    <div className="input-group">
                        <input
                            type={type}
                            className="form-control"
                            id="mpd"
                            name="mpd"
                            placeholder="********"
                            value={mpd}
                            onChange={(e) => setMpd(e.target.value)}
                            autoComplete="current-password"
                            required
                        />
                        <div className="input-group-append">
                            <span className="input-group-text" onClick={afficherMdp}>
                                <Icon icon={icon} size={20} />
                            </span>
                        </div>
                    </div>
                </div>

            </div>

            <button className="btn btn-primary w-50" type="submit">{t('Connecter')}</button>
            <small style={{marginTop: '10px'}}>{t('DejaUnCompte')}<a href="/signUp">{t('Sinscrire')}</a></small>
            {errorMessages && <div className='alert alert-danger' style={{marginTop: '20px', textAlign: 'center'}}>{errorMessages}</div>}
        </form>
    );
}

export default Connexion;
