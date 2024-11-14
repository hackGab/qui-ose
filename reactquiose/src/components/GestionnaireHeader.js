import React, {useEffect, useState} from 'react';
import {Link, useLocation} from 'react-router-dom';
import {useTranslation} from 'react-i18next';
import '../CSS/Header.css';
import logo from '../images/logo.png';
import i18n from "i18next";
import "../CSS/BoutonLangue.css";
import {calculateNextSessions} from '../utils/methodes/dateUtils';

function GestionnaireHeader() {
    const {t} = useTranslation();
    const [profileMenuOpen, setProfileMenuOpen] = useState(false);
    const location = useLocation();
    const [activeLink, setActiveLink] = useState(location.pathname);

    // Obtenir la prochaine session et année
    const nextSession = calculateNextSessions();
    const initialSession = nextSession.slice(0, -2); // Extrait la partie session, ex : "HIVER" de "HIVER25"
    const initialYear = nextSession.slice(-2); // Extrait la partie année, ex : "25" de "HIVER25"

    // Initialisation des states pour la session et l'année avec récupération depuis localStorage
    const [session, setSession] = useState(() => {
        return localStorage.getItem('session') || initialSession; // Si la session est enregistrée, on la récupère
    });
    const [year, setYear] = useState(() => {
        return localStorage.getItem('year') || initialYear; // Si l'année est enregistrée, on la récupère
    });
    const [filterByYear, setFilterByYear] = useState(() => {
        const savedFilterByYear = localStorage.getItem('filterByYear');
        return savedFilterByYear === 'true'; // Convertir en booléen
    });

    // Stocker les offres récupérées
    const [offres, setOffres] = useState([]);

    // State des sessions et années disponibles (hardcodées)
    const [availableSessions, setAvailableSessions] = useState([]);
    const [availableYears, setAvailableYears] = useState([]);

    useEffect(() => {
        // Hard-coder les sessions de 2024 à 2026
        const hardCodedSessions = [
            {id: 'HIVER24', label: 'HIVER 24'},
            {id: 'ETE24', label: 'ETE 24'},
            {id: 'AUTOMNE24', label: 'AUT 24'},
            {id: 'HIVER25', label: 'HIVER 25'},
            {id: 'ETE25', label: 'ETE 25'},
            {id: 'AUTOMNE25', label: 'AUT 25'},
            {id: 'HIVER26', label: 'HIVER 26'},
            {id: 'ETE26', label: 'ETE 26'},
            {id: 'AUTOMNE26', label: 'AUT 26'}
        ];

        const hardCodedYears = ['24', '25', '26'];

        // Mise à jour des états avec les sessions et années hardcodées
        setAvailableSessions(hardCodedSessions);
        setAvailableYears(hardCodedYears);
    }, []); // Cela ne sera exécuté qu'une fois lors du montage initial

    useEffect(() => {
        // Déterminer l'URL de la requête en fonction du filtre
        let urlFetch = '';
        if (filterByYear) {
            // Filtrage par année
            urlFetch = `http://localhost:8081/offreDeStage/annee/${year}`;
        } else {
            // Filtrage par session
            urlFetch = `http://localhost:8081/offreDeStage/session/${session}`;
        }

        console.log(urlFetch);


        // Effectuer la requête fetch
        fetch(urlFetch, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(response => {
                if (!response.ok) {
                    console.log(response);
                    throw new Error('Erreur lors de la récupération des données');
                }
                return response.json();
            })
            .then(data => {
                console.log(data);
                setOffres(data); // Enregistrer les données dans l'état
            })
            .catch(error => {
                console.error(error);
            });
    }, [year, session, filterByYear]); // Requête mise à jour quand l'année, la session ou le mode de filtrage changent

    const toggleProfileMenu = () => {
        setProfileMenuOpen(!profileMenuOpen);
    };

    const handleLinkClick = (path) => {
        setActiveLink(path);
    };

    const changeLanguage = (lng) => {
        i18n.changeLanguage(lng);
    };

    const toggleFilterMode = () => {
        setFilterByYear((prevFilterByYear) => {
            const newFilterByYear = !prevFilterByYear;
            localStorage.setItem('filterByYear', newFilterByYear); // Enregistrer dans localStorage
            return newFilterByYear;
        });
    };

    // Mettre à jour session et année dans localStorage lorsqu'ils changent
    const handleSessionChange = (newSession) => {
        setSession(newSession);
        localStorage.setItem('session', newSession); // Sauvegarder dans localStorage
    };

    const handleYearChange = (newYear) => {
        setYear(newYear);
        localStorage.setItem('year', newYear); // Sauvegarder dans localStorage
    };

    return (
        <header className="gestionnaire-header">
            <nav className="navbar">
                <div className="logo">
                    <img src={logo} alt="Logo" className="header-logo"/>
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
                <div className="filter-options">
                    <label>Filtre :</label>
                    {filterByYear ? (
                        // Affichage uniquement par année
                        <div className="year-dropdown">
                            <select value={year} onChange={(e) => handleYearChange(e.target.value)}>
                                {availableYears.map(yearOption => (
                                    <option key={yearOption} value={yearOption}>{yearOption}</option>
                                ))}
                            </select>
                        </div>
                    ) : (
                        // Affichage par session et année
                        <div className="session-dropdown">
                            <select value={session} onChange={(e) => handleSessionChange(e.target.value)}>
                                {availableSessions.map(sessionOption => (
                                    <option key={sessionOption.id}
                                            value={sessionOption.id}>{sessionOption.label}</option>
                                ))}
                            </select>
                        </div>
                    )}
                    <button className="filter-toggle-button profile-button" onClick={toggleFilterMode}>
                        Filtrer par {filterByYear ? 'session' : 'année'}
                    </button>
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
                                  className="language-button dropdown-link">{t('Français')}</Link>
                        </div>
                    )}
                </div>
            </nav>
        </header>
    );
}

export default GestionnaireHeader;
