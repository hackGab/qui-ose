import React, { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import '../CSS/GestionnaireHeader.css';
import logo from '../images/logo.png';
import "../CSS/BoutonLangue.css";
import i18n from "i18next";

function EtudiantHeader({nbEntrevuesEnAttente}) {
    const { t } = useTranslation();
    const [profileMenuOpen, setProfileMenuOpen] = useState(false);
    const location = useLocation();
    const [activeLink, setActiveLink] = useState(location.pathname);



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
                    <img src={logo} alt="Logo" className="header-logo"/>
                    <Link to="/accueilEtudiant" className="logo-link">
                        <div className="logo-text">Qui-Ose</div>
                    </Link>
                </div>

                <div className="nav-links">
                    <Link
                        className={`nav-link ${activeLink === '/accueilEtudiant' ? 'active' : ''}`}
                        to="/accueilEtudiant"
                        onClick={() => handleLinkClick('/accueilEtudiant')}
                    >
                        {t('accueil')}
                    </Link>
                    <Link
                        className={`nav-link ${activeLink === '/mesEntrevues' ? 'active' : ''}`}
                        to="/mesEntrevues"
                        onClick={() => handleLinkClick('/mesEntrevues')}
                    >
                        {t('mesEntrevues')} ({nbEntrevuesEnAttente || 0})
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
                            <Link onClick={() => changeLanguage('en')}
                                  className="language-button dropdown-link">{t('Anglais')}</Link>
                            <Link onClick={() => changeLanguage('fr')}
                                  className="language-button dropdown-link">{t('Francais')}</Link>
                        </div>
                    )}
                </div>
            </nav>
        </header>
    );
}

export default EtudiantHeader;
