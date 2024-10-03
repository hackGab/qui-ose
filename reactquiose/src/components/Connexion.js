import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import "bootstrap/dist/css/bootstrap.min.css";
import { Icon } from 'react-icons-kit';
import { eyeOff } from 'react-icons-kit/feather/eyeOff';
import { eye } from 'react-icons-kit/feather/eye';
import { useTranslation } from "react-i18next";

function Connexion() {
    // State hooks
    const [email, setEmail] = useState('');  // Email input
    const [mpd, setMpd] = useState('');  // Password input (mpd -> Mot de passe)
    const [type, setType] = useState('password');  // Password visibility toggle
    const [icon, setIcon] = useState(eyeOff);  // Icon for password visibility
    const [errorMessages, setErrorMessages] = useState('');  // Error message handling
    const navigate = useNavigate();  // For page redirection
    const { t } = useTranslation();  // Translation hook

    // Toggle password visibility
    const afficherMdp = () => {
        setIcon(type === 'password' ? eye : eyeOff);
        setType(type === 'password' ? 'text' : 'password');
    };

    // Function to handle login API call
    const handleLogin = async (userData) => {
        try {
            // First API call to login endpoint
            const response = await fetch('http://localhost:8081/user/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(userData),
            });

            if (!response.ok) {
                throw new Error(t('connexionEchouee'));  // If the login fails
            }

            const data = await response.json();  // Parse JSON response
            const accessToken = data.accessToken;  // Extract accessToken from response

            // Fetch user data with the accessToken
            const userResponse = await fetch('http://localhost:8081/user/me', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`  // Use the accessToken
                }
            });

            const userDataResponse = await userResponse.json();

            return { userData: userDataResponse, accessToken };  // Return user data with token

        } catch (error) {
            console.error('Erreur lors de la connexion:', error);  // Handle errors
            throw new Error(error.message || t('erreurLorsConnexion'));
        }
    };

    // Handle form submission (Login)
    const handleSubmit = async (event) => {
        event.preventDefault();
        setErrorMessages('');  // Reset error messages

        // Prepare user data for login
        const userData = { email: email, password: mpd.trim() };

        try {
            const { userData: fetchedUserData, accessToken } = await handleLogin(userData);
            navigateToDashboard(fetchedUserData);  // Navigate to dashboard

        } catch (error) {
            setErrorMessages(error.message);  // Display error messages
        }
    };

    // Navigate to the appropriate dashboard based on user role
    const navigateToDashboard = (userData) => {
        const path = `/${
            userData.role === 'ETUDIANT' ? 'accueilEtudiant' :
                userData.role === 'EMPLOYEUR' ? 'accueilEmployeur' :
                    userData.role === 'GESTIONNAIRE' ? 'accueilGestionnaire' :
                        'accueilProfesseur'
        }`;
        // Redirect to the role-specific dashboard
        navigate(path, { state: { userData } });
    };

    // Render the login form
    return (
        <form className='pt-0 m-auto' onSubmit={handleSubmit}>
            {/* Error message display */}
            {errorMessages && (
                <div className='alert alert-danger' style={{ textAlign: 'center', fontSize: '2vmin' }}>
                    {errorMessages}
                </div>
            )}

            {/* Form title */}
            <legend>{t('ChampsObligatoires')}</legend>

            {/* Email input field */}
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

                {/* Password input field with toggle for visibility */}
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
                        <div className="input-group-append">
                            <span className="input-group-text" onClick={afficherMdp} style={{ cursor: 'pointer' }}>
                                <Icon icon={icon} size={20} />
                            </span>
                        </div>
                    </div>
                </div>
            </div>

            {/* Submit button */}
            <button className="btn btn-primary w-50" type="submit">{t('Connecter')}</button>

            {/* Sign-up link */}
            <small style={{ marginTop: '10px' }}>
                {t('DejaUnCompte')} <a href="/signUp">{t('Sinscrire')}</a>
            </small>
        </form>
    );
}

export default Connexion;
