import React, { useState } from 'react';
import logo from '../images/logo.png';
import {Link, useLocation, useNavigate} from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import '../CSS/Header.css'
import i18n from "i18next";
import "../CSS/BoutonLangue.css";

function EmployeurHeader({ userData }) {
    const { t } = useTranslation();
    const [profileMenuOpen, setProfileMenuOpen] = useState(false);
    const navigate = useNavigate();
    const location = useLocation();
    const [activeLink, setActiveLink] = useState(location.pathname);

    const handleClickLogo = () => {
        if (userData) {
            navigate("/accueilEmployeur", { state: { userData: userData } });
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
    }

    const changeLanguage = (lng) => {
        i18n.changeLanguage(lng);
    };

    return (
        <header className="gestionnaire-header">
            <nav className="navbar">
                <div className="logo" onClick={handleClickLogo} style={{cursor: 'pointer'}}>
                    <img src={logo} alt="Logo" className="header-logo"/>
                    <div className="logo-text">Qui-Ose</div>
                </div>

                <div className="nav-links">
                    <span
                        className={`nav-link ${activeLink === '/accueilEmployeur' ? 'active' : ''}`}
                        onClick={() => handleLinkClick('/accueilEmployeur')}
                    >
                        {t('VisualiserOffres')}
                    </span>
                    <span
                        className={`nav-link ${activeLink === '/soumettre-offre' ? 'active' : ''}`}
                        onClick={() => handleLinkClick('/soumettre-offre')}
                    >
                        {t('SoummetreUnOffre')}
                    </span>
                    <span
                        className={`nav-link ${activeLink === '/visualiser-entrevue-accepter' ? 'active' : ''}`}
                        onClick={() => handleLinkClick('/visualiser-entrevue-accepter')}
                    >
                        {t('EntrevueAcceptee')}
                    </span>
                    <span
                    className={`nav-link ${activeLink === '/signerContrat' ? 'active' : ''}`}
                    onClick={() => handleLinkClick('/signerContrat')}
                    >
                    {t('SignerContrat')}
                </span>                </div>

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
                            <Link className="dropdown-link" to="/logout">{t('logout')}</Link>
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

export default EmployeurHeader;