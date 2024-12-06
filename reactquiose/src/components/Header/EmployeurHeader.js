import React, { useEffect, useState, useRef } from 'react';
import logo from '../../images/logo.png';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import '../../CSS/Header.css';
import i18n from "i18next";
import "../../CSS/BoutonLangue.css";
import ProfileMenu from './ProfileMenu';
import { handleLinkClick } from "../../utils/headerUtils";
import { hardCodedSessions } from "../../utils/variables/hardCodedSessions";
import { calculateNextSessions } from "../../utils/methodes/dateUtils";

function EmployeurHeader({ userData, onSendData}) {
    const { t } = useTranslation();
    const [profileMenuOpen, setProfileMenuOpen] = useState(false);
    const [notificationMenuOpen, setNotificationMenuOpen] = useState(false);
    const [navLinksOpen, setNavLinksOpen] = useState(false);
    const navigate = useNavigate();
    const location = useLocation();
    const [activeLink, setActiveLink] = useState(location.pathname);
    const [availableSessions, setAvailableSessions] = useState([]);
    const nextSession = calculateNextSessions();
    const initialSession = nextSession.slice(0, -2);
    const [session, setSession] = useState(() => {
        const storedSession = localStorage.getItem('session');
        if (storedSession) {
            return storedSession;
        }
        if (initialSession) {
            localStorage.setItem('session', initialSession); // Stocker initialSession si elle est absente
            return initialSession;
        }
        return '';
    });

    const [userDataState, setUserData] = useState(null);
    const navRef = useRef(null);

    const sendData = (key, value) => {
        onSendData({
            [key]: value
        });
    };

    useEffect(() => {
	if (userData) {
            localStorage.setItem('userData', JSON.stringify(userData));
        }
        onSendData({ session: session });
        setAvailableSessions(hardCodedSessions);
    }, [userData]);

   const getUserLocalStorage = () => {
        const storedUserData = localStorage.getItem('userData');
        if (storedUserData) {
            setUserData(JSON.parse(storedUserData));
            userData = userDataState;
        }
    };

    const closeNav = () => {
        setNavLinksOpen(false);
    };

    const closeMenus = () => {
        setProfileMenuOpen(false);
        setNotificationMenuOpen(false);
    };

    const handleSessionChange = (newSession) => {
        setSession(newSession);
        localStorage.setItem('session', newSession);
        sendData("session", newSession);
    };

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
        setNavLinksOpen(false);
    };

    const toggleProfileMenu = () => {
        setProfileMenuOpen(!profileMenuOpen);
    };

    const changeLanguage = (lng) => {
        i18n.changeLanguage(lng);
        getUserLocalStorage();
    };

    return (
        <header className="gestionnaire-header">
            <nav className="navbar" ref={navRef}>
                <div className="logo" onClick={handleClickLogo} style={{ cursor: 'pointer' }}>
                    <img src={logo} alt="Logo" className="header-logo" />
                    <div className="logo-text">Qui-Ose</div>
                </div>

                <div className={`hamburger-menu ${navLinksOpen ? 'active' : ''}`} onClick={() => { setNavLinksOpen(!navLinksOpen); closeMenus(); }}>
                    <div></div>
                    <div></div>
                    <div></div>
                </div>

                <div className={`nav-links ${navLinksOpen ? 'show' : ''}`}>
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
                    </span>

                    <div className="filter-options">
                        <label>{t('filtrerParSession')}</label>
                        <div className="session-dropdown">
                            <select value={session} onChange={(e) => handleSessionChange(e.target.value)}>
                                {availableSessions.map(sessionOption => (
                                    <option key={sessionOption.id} value={sessionOption.id}>{sessionOption.label}</option>
                                ))}
                            </select>
                        </div>
                    </div>
                </div>

                <ProfileMenu
                    userData={userData}
                    setActiveLink={setActiveLink}
                    closeNav={closeNav}
                    closeMenus={closeMenus}
                    profileMenuOpen={profileMenuOpen}
                    setProfileMenuOpen={setProfileMenuOpen}
                    notificationMenuOpen={notificationMenuOpen}
                    setNotificationMenuOpen={setNotificationMenuOpen}
                />
            </nav>
        </header>
    );
}

export default EmployeurHeader;
