import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import "bootstrap/dist/css/bootstrap.min.css";
import { Icon } from 'react-icons-kit';
import { eyeOff } from 'react-icons-kit/feather/eyeOff';
import { eye } from 'react-icons-kit/feather/eye';
import { useTranslation } from "react-i18next";

function Connexion() {
    const [email, setEmail] = useState('');
    const [mpd, setMpd] = useState('');
    const [type, setType] = useState('password');
    const [icon, setIcon] = useState(eyeOff);
    const [errorMessages, setErrorMessages] = useState('');
    const navigate = useNavigate();
    const {t} = useTranslation();

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
        console.log('Données envoyées au backend:', userData);

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
                console.log('Réponse du serveur:', data);
                const accessToken = data.accessToken;

                // Récupérer l'utilisateur
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
                    return response.json(); // Récupérer les données utilisateur
                }
                throw new Error(t('erreurLorsRecuperationUtilisateur')); // Gérer les erreurs
            })
            .then((userData) => {
                console.log('Données utilisateur:', userData);
                // Redirection en fonction du rôle
                if (userData.role === 'ETUDIANT') {
                    navigate('/accueilEtudiant', { state: { userData } });
                } else if (userData.role === 'EMPLOYEUR') {
                    navigate('/accueilEmployeur');
                } else if (userData.role === 'GESTIONNAIRE') {
                    navigate('/accueilGestionnaire');
                } else if (userData.role === 'PROFESSEUR') {
                    navigate('/accueilProfesseur');
                }
            })
            .catch((error) => {
                console.error('Erreur lors de la connexion:', error);
                setErrorMessages(t('erreurLorsConnexion'));
            });
    };

    return (
        <form className='pt-0 m-auto' onSubmit={handleSubmit}>
            {errorMessages && <div className='alert alert-danger' style={{textAlign: 'center', fontSize: '2vmin'}}>{errorMessages}</div>}
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
                        autoComplete="off"
                        required
                    />
                </div>

                <span onClick={afficherMdp} style={{cursor: 'pointer'}}>
                                <Icon icon={icon} size={20}/>
                            </span>
                <div className="form-group">
                    <label htmlFor="mdp">{t('MotDePasse')}</label>
                    <div className="input-group">
                        <input
                            type={type}
                            className="form-control"
                            id="mdp"
                            name="mdp"
                            placeholder="********"
                            value={mpd}
                            onChange={(e) => setMpd(e.target.value)}
                            autoComplete="new-password"
                            required
                        />
                       
                    </div>
                </div>

            </div>

            <button className="btn btn-primary w-50" type="submit">{t('Connecter')}</button>

            <small style={{marginTop: '10px'}}>
                {t('NoAccount')} <a href="/signUp">{t('Sinscrire')}</a>
            </small>
        </form>
    );
}

export default Connexion;
