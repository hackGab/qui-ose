import React, { useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import '../CSS/GestionnaireHeader.css';
import logo from '../images/logo.png';
import "../CSS/BoutonLangue.css";
import i18n from "i18next";

function EtudiantHeader({ nbEntrevuesEnAttente, userData }) {
    const navigate = useNavigate();
    const { t } = useTranslation();
    const [profileMenuOpen, setProfileMenuOpen] = useState(false);
    const location = useLocation();
    const [activeLink, setActiveLink] = useState(location.pathname);

    const handleClickLogo = () => {
        if (userData) {
            navigate("/accueilEtudiant", { state: { userData: userData } });
        }
    };

    const handleLinkClick = (path) => {
        setActiveLink(path);
        if (userData) {
            navigate(path, { state: { userData: userData } });
        }
    };

    const toggleProfileMenu = () => {
        setProfileMenuOpen(!profileMenuOpen);
    };

    const changeLanguage = (lng) => {
        i18n.changeLanguage(lng);
    };

    return (
        <header className="gestionnaire-header">
            <nav className="navbar">
                <div className="logo" onClick={handleClickLogo} style={{ cursor: 'pointer' }}>
                    <img src={logo} alt="Logo" className="header-logo" />
                    <div className="logo-text">Qui-Ose</div>
                </div>

                <div className="nav-links">
                    <span
                        className={`nav-link ${activeLink === '/accueilEtudiant' ? 'active' : ''}`}
                        onClick={() => handleLinkClick('/accueilEtudiant')}
                        style={{ cursor: 'pointer' }}
                    >
                        {t('accueil')}
                    </span>
                    <span
                        className={`nav-link ${activeLink === '/mesEntrevues' ? 'active' : ''}`}
                        onClick={() => handleLinkClick('/mesEntrevues')}
                    >
                        {t('mesEntrevues')} ({nbEntrevuesEnAttente || 0})
                    </span>
                </div>

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
                            <span onClick={() => changeLanguage('en')} className="language-button dropdown-link">
                                {t('Anglais')}
                            </span>
                            <span onClick={() => changeLanguage('fr')} className="language-button dropdown-link">
                                {t('Francais')}
                            </span>
                        </div>
                    )}
                </div>
            </nav>
        </header>
    );
}

export default EtudiantHeader;
