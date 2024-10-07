import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import "bootstrap/dist/css/bootstrap.min.css";
import { Icon } from 'react-icons-kit';
import { eyeOff } from 'react-icons-kit/feather/eyeOff';
import { eye } from 'react-icons-kit/feather/eye';
import { useTranslation } from "react-i18next";

function Connexion() {
    // State hooks
    const [email, setEmail] = useState('');
    const [mpd, setMpd] = useState('');
    const [type, setType] = useState('password');
    const [icon, setIcon] = useState(eyeOff);
    const [errorMessages, setErrorMessages] = useState('');
    const navigate = useNavigate();
    const { t } = useTranslation();

    // Toggle password visibility
    const afficherMdp = () => {
        setIcon(type === 'password' ? eye : eyeOff);
        setType(type === 'password' ? 'text' : 'password');
    };

    const handleLogin = async (userData) => {
        try {
            // First API call to login endpoint
            const response = await fetch('http://localhost:8080/user/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(userData),
            });

            if (!response.ok) {
                throw new Error(t('connexionEchouee'));
            }

            const data = await response.json();
            const accessToken = data.accessToken;

            const userResponse = await fetch('http://localhost:8080/user/me', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`
                }
            });

            const userDataResponse = await userResponse.json();

            return { userData: userDataResponse, accessToken };

        } catch (error) {
            console.error('Erreur lors de la connexion:', error);
            throw new Error(error.message || t('erreurLorsConnexion'));
        }
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        setErrorMessages('');

        const userData = { email: email, password: mpd.trim() };

        try {
            const { userData: fetchedUserData, accessToken } = await handleLogin(userData);
            navigateToDashboard(fetchedUserData);

        } catch (error) {
            setErrorMessages(error.message);
        }
    };

    const navigateToDashboard = (userData) => {
        const path = `/${
            userData.role === 'ETUDIANT' ? 'accueilEtudiant' :
                userData.role === 'EMPLOYEUR' ? 'accueilEmployeur' :
                    userData.role === 'GESTIONNAIRE' ? 'accueilGestionnaire' :
                        'accueilProfesseur'
        }`;
        navigate(path, { state: { userData } });
    };

    return (
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
    );
}

export default Connexion;