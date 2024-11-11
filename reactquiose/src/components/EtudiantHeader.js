import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import '../CSS/Header.css';
import logo from '../images/logo.png';
import "../CSS/BoutonLangue.css";
import i18n from "i18next";

function EtudiantHeader({ userData }) {
    const { t } = useTranslation();
    const [profileMenuOpen, setProfileMenuOpen] = useState(false);
    const navigate = useNavigate();
    const location = useLocation();
    const [file, setFile] = useState(null);
    const [stagesAppliquees, setStagesAppliquees] = useState([]);
    const [activeLink, setActiveLink] = useState(location.pathname);
    const [nbEntrevuesEnAttente, setNbEntrevuesEnAttente] = useState(0);
    const [storedUserData, setStoredUserData] = useState(null);


    useEffect(() => {
        const storedData = localStorage.getItem('userData');
        if (storedData) {
            setStoredUserData(JSON.parse(storedData));
        }
    }, []);

    const handleClickLogo = () => {
        const storedUserData = JSON.parse(localStorage.getItem('userData'));
        if (storedUserData) {
            navigate("/accueilEtudiant", { state: { userData: storedUserData } });
        }
    };

    const handleLinkClick = (path) => {
        const storedUserData = JSON.parse(localStorage.getItem('userData'));
        if (storedUserData) {
            setActiveLink(path);
            navigate(path, { state: { userData: storedUserData } });
        }
    };

    const toggleProfileMenu = () => {
        setProfileMenuOpen(!profileMenuOpen);
    };

    const changeLanguage = (lng) => {
        i18n.changeLanguage(lng);

        if (storedUserData) {
            localStorage.setItem('userData', JSON.stringify(storedUserData));
        }
    };

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
                    if (data.cv) {
                        setFile(data.cv);
                        setStagesAppliquees(data.offresAppliquees);
                    }
                })
                .catch((error) => {
                    console.error('Erreur:', error);
                });
        }
    }, [userData]);

    useEffect(() => {
        if (userData) {
            fetch(`http://localhost:8081/entrevues/enAttente/etudiant/${userData.credentials.email}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Erreur lors de la récupération des entrevues');
                    }
                    return response.json();
                })
                .then(data => {
                    setNbEntrevuesEnAttente(data.length);
                })
                .catch(err => {
                    console.error('Erreur:', err);
                });
        }
    }, [userData]);

    return (
        <header className="gestionnaire-header">
            <nav className="navbar">
                <div className="logo" onClick={handleClickLogo} style={{ cursor: 'pointer' }}>
                    <img src={logo} alt="Logo" className="header-logo" />
                    <div className="logo-text">Qui-Ose</div>
                </div>

                <div className="nav-links">
                    <span className={`nav-link ${activeLink === '/accueilEtudiant' ? 'active' : ''}`}
                          onClick={() => handleLinkClick('/accueilEtudiant')}>
                        {t('accueil')}
                    </span>
                    {file && file.status === "validé" && (
                        <span className={`nav-link ${activeLink === '/stagesAppliquees' ? 'active' : ''}`}
                              onClick={() => handleLinkClick('/stagesAppliquees')}>
                            {t('stagesAppliquées')} ({stagesAppliquees.length})
                        </span>
                    )}
                    <span className={`nav-link ${activeLink === '/mesEntrevues' ? 'active' : ''}`}
                          onClick={() => handleLinkClick('/mesEntrevues')}>
                        {t('mesEntrevues')} ({nbEntrevuesEnAttente || 0})
                    </span>
                    <span className={`nav-link ${activeLink === '/signerContrat' ? 'active' : ''}`}
                          onClick={() => handleLinkClick('/signerContrat')}>
                        {t('SignerContrat')}
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
