import React, { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import '../CSS/GestionnaireHeader.css';
import logo from '../images/logo.png';

function GestionnaireHeader() {
    const [profileMenuOpen, setProfileMenuOpen] = useState(false);
    const location = useLocation();
    const [activeLink, setActiveLink] = useState(location.pathname);

    const toggleProfileMenu = () => {
        setProfileMenuOpen(!profileMenuOpen);
    };

    const handleLinkClick = (path) => {
        setActiveLink(path);
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
                        Étudiant
                    </Link>
                    <Link
                        className={`nav-link ${activeLink === '/role/professeur' ? 'active' : ''}`}
                        to="/role/professeur"
                        onClick={() => handleLinkClick('/role/professeur')}
                    >
                        Professeur
                    </Link>
                    <Link
                        className={`nav-link ${activeLink === '/role/employeur' ? 'active' : ''}`}
                        to="/role/employeur"
                        onClick={() => handleLinkClick('/role/employeur')}
                    >
                        Employeur
                    </Link>
                </div>
                <div className="profile-menu">
                    <div className="notification-icon">🔔</div>
                    <div
                        className="profile-button"
                        onClick={toggleProfileMenu}
                    >
                        Profil ▼
                    </div>
                    {profileMenuOpen && (
                        <div className="profile-dropdown">
                            <Link className="dropdown-link" to="/profile">Mon Profil</Link>
                            <Link className="dropdown-link" to="/settings">Paramètres</Link>
                            <Link className="dropdown-link" to="/logout">Déconnexion</Link>
                        </div>
                    )}
                </div>
            </nav>
        </header>
    );
}

export default GestionnaireHeader;
