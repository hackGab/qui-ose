import React, { useState } from 'react';
import logo from '../images/logo.png';
import { Link,useLocation } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import '../CSS/EmployerHeader.css';

function EmployeurHeader() {
    const { t } = useTranslation();
    const [profileMenuOpen, setProfileMenuOpen] = useState(false);
    const location = useLocation();
    const [activeLink, setActiveLink] = useState(location.pathname);

    const toggleProfileMenu = () => {
        setProfileMenuOpen(!profileMenuOpen);
    }

    return (
        <header className="gestionnaire-header">
            <nav className="navbar">
                <div className="logo">
                    <img src={logo} alt="Logo" className="header-logo" />
                    <Link to="/accueilEmployeur" className="logo-link">
                        <div className="logo-text">Qui-Ose</div>
                    </Link>
                </div>
                {/*<div className="nav-links">*/}
                {/*    <Link*/}
                {/*        className={`nav-link ${activeLink === '/listeEtudiants' ? 'active' : ''}`}*/}
                {/*        to="/listeEtudiants"*/}
                {/*        onClick={() => handleLinkClick('/listeEtudiants')}*/}
                {/*    >*/}
                {/*        {t('etudiant')}*/}
                {/*    </Link>*/}
                {/*    <Link*/}
                {/*        className={`nav-link ${activeLink === '/role/professeur' ? 'active' : ''}`}*/}
                {/*        to="/role/professeur"*/}
                {/*        onClick={() => handleLinkClick('/role/professeur')}*/}
                {/*    >*/}
                {/*        {t('prof')}*/}
                {/*    </Link>*/}
                {/*    <Link*/}
                {/*        className={`nav-link ${activeLink === '/role/employeur' ? 'active' : ''}`}*/}
                {/*        to="/role/employeur"*/}
                {/*        onClick={() => handleLinkClick('/role/employeur')}*/}
                {/*    >*/}
                {/*        {t('employeur')}*/}
                {/*    </Link>*/}
                {/*</div>*/}
                <div className="profile-menu">
                    <div className="notification-icon">🔔</div>
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
                        </div>
                    )}
                </div>
            </nav>
        </header>
    );
}

export default EmployeurHeader;