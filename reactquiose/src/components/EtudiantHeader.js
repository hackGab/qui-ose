import React, {useEffect, useState} from 'react';
import {Link, useLocation, useNavigate} from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import '../CSS/GestionnaireHeader.css';
import logo from '../images/logo.png';
import "../CSS/BoutonLangue.css";
import i18n from "i18next";

function EtudiantHeader() {
    const { t } = useTranslation();
    const [profileMenuOpen, setProfileMenuOpen] = useState(false);
    const navigate = useNavigate();
    const location = useLocation();
    const userData = location.state?.userData;
    const [file, setFile] = useState(null);
    const [stagesAppliquees, setStagesAppliquees] = useState([]);

    const toggleProfileMenu = () => {
        setProfileMenuOpen(!profileMenuOpen);
    };

    const changeLanguage = (lng) => {
        i18n.changeLanguage(lng);
    };

    useEffect(() => {
        if (userData) {
            const url = `http://localhost:8081/etudiant/credentials/${userData.credentials.email}`;

            fetch(url)
                .then((response) => {
                    if (!response.ok) {
                        throw new Error(`Erreur lors de la requête: ${response.status}`);
                    }
                    return response.json();
                })
                .then((data) => {
                    console.log('Réponse du serveur:', data);

                    if (data.cv) {
                        setFile(data.cv);
                        setStagesAppliquees(data.offresAppliquees);

                        console.log('CV:', data.cv);
                        console.log('Stages appliquées:', data.offresAppliquees);
                    }
                })
                .catch((error) => {
                    console.error('Erreur:', error);
                });
        }
    }, [userData]);

    return (
        <header className="gestionnaire-header">
            <nav className="navbar">
                <div className="logo">
                    <img src={logo} alt="Logo" className="header-logo"/>
                    <Link to="/accueilEtudiant" className="logo-link">
                        <div className="logo-text">Qui-Ose</div>
                    </Link>
                </div>
                {file && file.status === "validé" && location.pathname === "/accueilEtudiant" && (
                    <div className="nav-text-center">
                        <a className="nav-link" onClick={() => navigate('/stagesAppliquees', { state: { userData } })}>
                            <span>{t('stagesAppliquées')} ({stagesAppliquees.length})</span>
                        </a>
                    </div>
                )}
                <div className="profile-menu">
                    <div className="notification-icon">🕭</div>
                    <div className="profile-button" onClick={toggleProfileMenu}>
                        {t('profile')} ▼
                    </div>
                    {profileMenuOpen && (
                        <div className="profile-dropdown">
                            <Link className="dropdown-link" to="/profile">{t('myProfile')}</Link>
                            <Link className="dropdown-link" to="/settings">{t('settings')}</Link>
                            <Link className="dropdown-link" to="/login">{t('logout')}</Link>
                            <Link onClick={() => changeLanguage('en')} className="language-button dropdown-link">
                                {t('Anglais')}
                            </Link>
                            <Link onClick={() => changeLanguage('fr')} className="language-button dropdown-link">
                                {t('Francais')}
                            </Link>
                        </div>
                    )}
                </div>
            </nav>
        </header>
    );
}

export default EtudiantHeader;
