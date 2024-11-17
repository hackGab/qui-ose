import React, {useEffect, useState} from 'react';
import {Link, useLocation, useNavigate} from 'react-router-dom';
import {useTranslation} from 'react-i18next';
import '../CSS/Header.css'
import logo from '../images/logo.png';
import "../CSS/BoutonLangue.css";
import i18n from "i18next";
import {hardCodedSessions} from "../utils/methodes/hardCodedSessions";
import {calculateNextSessions} from "../utils/methodes/dateUtils";

function EtudiantHeader({userData,onSendData}) {
    const {t} = useTranslation();
    const [profileMenuOpen, setProfileMenuOpen] = useState(false);
    const navigate = useNavigate();
    const location = useLocation();
    const [file, setFile] = useState(null);
    const [stagesAppliquees, setStagesAppliquees] = useState([]);
    const [activeLink, setActiveLink] = useState(location.pathname);
    const [nbEntrevuesEnAttente, setNbEntrevuesEnAttente] = useState(0);
    const [availableSessions, setAvailableSessions] = useState([]);
    const nextSession = calculateNextSessions();
    const initialSession = nextSession.slice(0, -2);
    const [session, setSession] = useState(() => {
        return localStorage.getItem('session') || initialSession; // Si la session est enregistrée, on la récupère
    });

    const handleClickLogo = () => {
        if (userData) {
            navigate("/accueilEtudiant", {state: {userData: userData}});
        }
    };

    const handleLinkClick = (path) => {
        setActiveLink(path);
        if (userData) {
            navigate(path, {state: {userData: userData}});
        }
    };

    const toggleProfileMenu = () => {
        setProfileMenuOpen(!profileMenuOpen);
    };

    const changeLanguage = (lng) => {
        i18n.changeLanguage(lng);
    };

    const handleSessionChange = (newSession) => {
        setSession(newSession);
        localStorage.setItem('session', newSession);
        sendData("session", newSession);
    };

    const sendData = (key, value) => {
        onSendData({
            [key]: value
        });
    };


    useEffect(() => {
        onSendData({
            session: session
        });
        setAvailableSessions(hardCodedSessions);
    }, []);
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

    useEffect(() => {
        fetch(`http://localhost:8081/entrevues/enAttente/etudiant/${userData.credentials.email}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Erreur lors de la récupération des entrevues');
                }
                return response.json();
            })
            .then(data => {
                console.log('Réponse du serveur :', data);
                setNbEntrevuesEnAttente(data.length);
            })
            .catch(err => {
                console.error('Erreur:', err);
            });

    }, [userData]);


    return (
        <header className="gestionnaire-header">
            <nav className="navbar">
                <div className="logo" onClick={handleClickLogo} style={{cursor: 'pointer'}}>
                    <img src={logo} alt="Logo" className="header-logo"/>
                    <div className="logo-text">Qui-Ose</div>
                </div>

                <div className="nav-links">
                        <span
                            className={`nav-link ${activeLink === '/accueilEtudiant' ? 'active' : ''}`}
                            onClick={() => handleLinkClick('/accueilEtudiant')}
                        >
                            {t('accueil')}
                        </span>
                    {file && file.status === "validé" && (
                        <span
                            className={`nav-link ${activeLink === '/stagesAppliquees' ? 'active' : ''}`}
                            onClick={() => handleLinkClick('/stagesAppliquees')}
                        >
                                {t('stagesAppliquées')} ({stagesAppliquees.length})
                            </span>
                    )}

                    <span
                        className={`nav-link ${activeLink === '/mesEntrevues' ? 'active' : ''}`}
                        onClick={() => handleLinkClick('/mesEntrevues')}
                    >
                            {t('mesEntrevues')} ({nbEntrevuesEnAttente || 0})
                        </span>
                    <span
                        className={`nav-link ${activeLink === '/signerContrat' ? 'active' : ''}`}
                        onClick={() => handleLinkClick('/signerContrat')}
                    >
                            {t('SignerContrat')}
                        </span>

                    <div className="filter-options">
                        <label>Filtre :</label>
                        {/*{filterByYear ? (*/}
                        {/*    // Affichage uniquement par année*/}
                        {/*    <div className="year-dropdown">*/}
                        {/*        <select value={year} onChange={(e) => handleYearChange(e.target.value)}>*/}
                        {/*            {availableYears.map(yearOption => (*/}
                        {/*                <option key={yearOption} value={yearOption}>{yearOption}</option>*/}
                        {/*            ))}*/}
                        {/*        </select>*/}
                        {/*    </div>*/}
                        {/*) : (*/}
                        <div className="session-dropdown">
                            <select value={session} onChange={(e) => handleSessionChange(e.target.value)}>
                                {availableSessions.map(sessionOption => (
                                    <option key={sessionOption.id}
                                            value={sessionOption.id}>{sessionOption.label}</option>
                                ))}
                            </select>
                        </div>

                        {/*<button className="filter-toggle-button profile-button" onClick={toggleFilterMode} >*/}
                        {/*    Filtrer par {filterByYear ? 'session' : 'année'}*/}
                        {/*</button>*/}
                    </div>
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
