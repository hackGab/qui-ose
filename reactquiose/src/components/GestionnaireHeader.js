// src/components/GestionnaireHeader.js
import React, { useEffect, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import '../CSS/Header.css';
import logo from '../images/logo.png';
import i18n from "i18next";
import "../CSS/BoutonLangue.css";
import { calculateNextSessions } from '../utils/methodes/dateUtils';

function GestionnaireHeader() {
    const { t } = useTranslation();
    const [profileMenuOpen, setProfileMenuOpen] = useState(false);
    const location = useLocation();
    const [activeLink, setActiveLink] = useState(location.pathname);

    useEffect(() => {
        let fetchDate = calculateNextSessions();

        const urlFetchOffresWithSession = `http://localhost:8081/offreDeStage/session/${fetchDate}`;

        fetch(urlFetchOffresWithSession, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(response => {
                if (!response.ok) {
                    console.log(response);
                    throw new Error('lors de la récupération des données');
                }
                return response.json();
            })
            .then(data => {
                console.log(data);
            })
            .catch(error => {
                console.error(error);
            });
    }, []);

    const toggleProfileMenu = () => {
        setProfileMenuOpen(!profileMenuOpen);
    };

    const handleLinkClick = (path) => {
        setActiveLink(path);
    };

    const changeLanguage = (lng) => {
        i18n.changeLanguage(lng);
    };

    return (
        <header className="gestionnaire-header">
            <nav className="navbar">
                <div className="logo">
                    <img src={logo} alt="Logo" className="header-logo" />
                    <Link to="/accueilGestionnaire" className="logo-link">
                        <div className="logo-text">Qui-Ose</div>
                    </Link>
                </div>
                <div className="nav-links">
                    <Link
                        className={`nav-link ${activeLink === '/listeEtudiants' ? 'active' : ''}`}
                        to="/listeEtudiants"
                        onClick={() => handleLinkClick('/listeEtudiants')}
                    >
                        {t('etudiant')}
                    </Link>
                    <Link
                        className={`nav-link ${activeLink === '/listeProfesseurs' ? 'active' : ''}`}
                        to="/listeProfesseurs"
                        onClick={() => handleLinkClick('/listeProfesseurs')}
                    >
                        {t('prof')}
                    </Link>
                    <Link
                        className={`nav-link ${activeLink === '/listeEmployeurs' ? 'active' : ''}`}
                        to="/listeEmployeurs"
                        onClick={() => handleLinkClick('/listeEmployeurs')}
                    >
                        {t('employeur')}
                    </Link>
                    <Link
                        className={`nav-link ${activeLink === '/listeCandidatures' ? 'active' : ''}`}
                        to="/listeCandidatures"
                        onClick={() => handleLinkClick('/listeCandidatures')}
                    >
                        {t('candidatures')}
                    </Link>
                </div>
                <div className="profile-menu">
                    <div className="notification-icon">🕭</div>
                    <div
                        className="profile-button"
                        onClick={toggleProfileMenu}
                    >
                        {t('profile')} ▼
                    </div>
                    {profileMenuOpen && (
                        <div className="profile-dropdown">
                            <Link className="dropdown-link" to="/profile">{t('myProfile')}</Link>
                            <Link className="dropdown-link" to="/settings">{t('settings')}</Link>
                            <Link className="dropdown-link" to="/login">{t('logout')}</Link>
                            <Link onClick={() => changeLanguage('en')} className="language-button dropdown-link">{t('Anglais')}</Link>
                            <Link onClick={() => changeLanguage('fr')} className="language-button dropdown-link">{t('Francais')}</Link>
                        </div>
                    )}
                </div>
            </nav>
        </header>
    );
}

export default GestionnaireHeader;
