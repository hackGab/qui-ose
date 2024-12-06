import React, { useEffect, useState, useRef } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import '../../CSS/Header.css';
import logo from '../../images/logo.png';
import i18n from "i18next";
import "../../CSS/BoutonLangue.css";
import { hardCodedSessions } from "../../utils/variables/hardCodedSessions";
import { calculateNextSessions } from "../../utils/methodes/dateUtils";

function GestionnaireHeader({ onSendData }) {
    const { t } = useTranslation();
    const [profileMenuOpen, setProfileMenuOpen] = useState(false);
    const [notificationMenuOpen, setNotificationMenuOpen] = useState(false);
    const [navLinksOpen, setNavLinksOpen] = useState(false);
    const location = useLocation();
    const [activeLink, setActiveLink] = useState(location.pathname);

    const nextSession = calculateNextSessions();
    const initialSession = nextSession.slice(0, -2);

    const [session, setSession] = useState(() => {
        return localStorage.getItem('session') || initialSession;
    });

    const [availableSessions, setAvailableSessions] = useState([]);
    const navRef = useRef(null);

    useEffect(() => {
        onSendData({
            session: session,
        });

        setAvailableSessions(hardCodedSessions);
    }, []);

    const toggleProfileMenu = () => {
        setProfileMenuOpen(!profileMenuOpen);
    };

    const sendData = (type, data) => {
        if (type === "session") {
            onSendData({
                session: data,
            });
        }
    }

    const handleLinkClick = (path) => {
        setActiveLink(path);
        setNavLinksOpen(false); // Close the hamburger menu when a link is clicked
    };

    const changeLanguage = (lng) => {
        i18n.changeLanguage(lng);
    };

    const handleSessionChange = (newSession) => {
        setSession(newSession);
        localStorage.setItem('session', newSession);
        sendData("session", newSession);
    };

    const closeMenus = () => {
        setProfileMenuOpen(false);
        setNotificationMenuOpen(false);
    };

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (navRef.current && !navRef.current.contains(event.target)) {
                setNavLinksOpen(false);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, [navRef]);

    return (
        <header className="gestionnaire-header">
            <nav className="navbar" ref={navRef}>
                <div className="logo">
                    <img src={logo} alt="Logo" className="header-logo" />
                    <Link to="/accueilGestionnaire" className="logo-link">
                        <div className="logo-text">Qui-Ose</div>
                    </Link>
                </div>
                <div className={`hamburger-menu ${navLinksOpen ? 'active' : ''}`} onClick={() => { setNavLinksOpen(!navLinksOpen); closeMenus(); }}>
                    <div></div>
                    <div></div>
                    <div></div>
                </div>
                <div className={`nav-links ${navLinksOpen ? 'show' : ''}`}>
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

                    {location.pathname !== '/listeProfesseurs' && location.pathname !== '/listeEtudiants' && (
                        <div className="filter-options" data-testid="filtreSession">
                            <label>{t('filtrerParSession')}</label>
                            <div className="session-dropdown">
                                <select value={session} onChange={(e) => handleSessionChange(e.target.value)}>
                                    {availableSessions.map(sessionOption => (
                                        <option key={sessionOption.id}
                                                value={sessionOption.id}>{sessionOption.label}</option>
                                    ))}
                                </select>
                            </div>
                        </div>
                    )}
                </div>
                <div className="profile-menu">
                    <div className="profile-button" onClick={toggleProfileMenu}>
                        {t('profile')} ▼
                    </div>
                    {profileMenuOpen && (
                        <div className="dropdown profile-dropdown">
                            <Link className="dropdown-link" to="/login">{t('logout')}</Link>
                            <button onClick={() => changeLanguage('en')}
                                    className="language-button dropdown-link text-left">{t('Anglais')}
                            </button>
                            <button onClick={() => changeLanguage('fr')}
                                    className="language-button dropdown-link text-left">{t('Francais')}
                            </button>
                        </div>
                    )}
                </div>
            </nav>
        </header>
    );
}

export default GestionnaireHeader;