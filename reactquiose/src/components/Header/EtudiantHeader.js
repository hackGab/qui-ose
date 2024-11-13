import React, {useEffect, useState} from 'react';
import {Link, useLocation, useNavigate} from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import '../../CSS/Header.css'
import logo from '../../images/logo.png';
import "../../CSS/BoutonLangue.css";
import i18n from "i18next";
import {FaCross, FaTimes} from "react-icons/fa";

    function EtudiantHeader({ userData }) {
        const { t } = useTranslation();
        const [profileMenuOpen, setProfileMenuOpen] = useState(false);
        const [notificationMenuOpen, setNotificationMenuOpen] = useState(false);
        const [notifications, setNotifications] = useState([
            { description: "Vous avez été accepté pour le stage de Programmeur analyste", tempsDepuisReception: "2 heures", url: "/stagesAppliquees" },
            { description: "Vous avez été rejeté pour le stage de Développeur Web", tempsDepuisReception: "1 jour", url: "/stagesAppliquees" },
            { description: "Vous avez une entrevue pour le stage de Programmeur analyste", tempsDepuisReception: "2 jours", url: "/mesEntrevues" },
            { description: "Vous avez une entrevue pour le stage de Développeur Web", tempsDepuisReception: "1 semaine", url: "/mesEntrevues"},
            { description: "Vous avez été accepté pour le stage de Programmeur analyste", tempsDepuisReception: "2 heures", url: "/stagesAppliquees" },
            { description: "Vous avez été rejeté pour le stage de Développeur Web", tempsDepuisReception: "1 jour", url: "/stagesAppliquees" },
            { description: "Vous avez une entrevue pour le stage de Programmeur analyste", tempsDepuisReception: "2 jours", url: "/mesEntrevues" },
            { description: "Vous avez une entrevue pour le stage de Développeur Web", tempsDepuisReception: "1 semaine", url: "/mesEntrevues"},
            { description: "Vous avez été accepté pour le stage de Programmeur analyste", tempsDepuisReception: "2 heures", url: "/stagesAppliquees" },
            { description: "Vous avez été rejeté pour le stage de Développeur Web", tempsDepuisReception: "1 jour", url: "/stagesAppliquees" },
            { description: "Vous avez une entrevue pour le stage de Programmeur analyste", tempsDepuisReception: "2 jours", url: "/mesEntrevues" },
            { description: "Vous avez une entrevue pour le stage de Développeur Web", tempsDepuisReception: "1 semaine", url: "/mesEntrevues"},
            { description: "Vous avez été accepté pour le stage de Programmeur analyste", tempsDepuisReception: "2 heures", url: "/stagesAppliquees" },
            { description: "Vous avez été rejeté pour le stage de Développeur Web", tempsDepuisReception: "1 jour", url: "/stagesAppliquees" },
            { description: "Vous avez une entrevue pour le stage de Programmeur analyste", tempsDepuisReception: "2 jours", url: "/mesEntrevues" },
            { description: "Vous avez une entrevue pour le stage de Développeur Web", tempsDepuisReception: "1 semaine", url: "/mesEntrevues"},
            { description: "Vous avez été accepté pour le stage de Programmeur analyste", tempsDepuisReception: "2 heures", url: "/stagesAppliquees" },
        ]);
        const navigate = useNavigate();
        const location = useLocation();
        const [file, setFile] = useState(null);
        const [stagesAppliquees, setStagesAppliquees] = useState([]);
        const [activeLink, setActiveLink] = useState(location.pathname);
        const [nbEntrevuesEnAttente, setNbEntrevuesEnAttente] = useState(0);
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

        const handleDeleteNotification = (index) => {
            setNotifications((prevNotifications) =>
                prevNotifications.filter((_, i) => i !== index)
            );
        };

        const toggleProfileMenu = () => {
            setProfileMenuOpen(!profileMenuOpen);
            setNotificationMenuOpen(false);
        };

        function toggleNotificationMenu() {
            setNotificationMenuOpen(!notificationMenuOpen);
            setProfileMenuOpen(false)
        }

        const changeLanguage = (lng) => {
            i18n.changeLanguage(lng);
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
        },[userData]);

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


        // Pas encore le constructeur pour
        // useEffect(() => {
        //     if (userData) {
        //         const url = `http://localhost:8081/notifications/${userData.credentials.email}`;
        //
        //         fetch(url)
        //             .then((response) => {
        //                 if (!response.ok) {
        //                     throw new Error(`Erreur lors de la requête: ${response.status}`);
        //                 }
        //                 return response.json();
        //             })
        //             .then((data) => {
        //                 setNotifications(data);
        //             })
        //             .catch((error) => {
        //                 console.error('Erreur:', error);
        //             });
        //     }
        // }, [userData]);

        return (
            <header className="gestionnaire-header">
                <nav className="navbar">
                    <div className="logo" onClick={handleClickLogo} style={{ cursor: 'pointer' }}>
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
                    </div>

                    <div className="profile-menu">
                        <div className="notification-icon" onClick={toggleNotificationMenu}>
                            🕭 <span className="notification-count">{notifications.length}</span>
                        </div>
                        {notificationMenuOpen && (
                            <div className="dropdown notification-dropdown">
                                {notifications.length > 0 ? (
                                    notifications.map((notification, index) => (
                                        <>
                                            <div key={index} className="dropdown-link">
                                                <div onClick={() => handleLinkClick(notification.url)}>
                                                    {notification.description} - {notification.tempsDepuisReception} avant
                                                </div>
                                                <div data-testid="delete-icon" className="delete-icon"
                                                     onClick={() => handleDeleteNotification(index)}>
                                                    <FaTimes/>
                                                </div>
                                            </div>
                                            <hr className="m-1"/>
                                        </>
                                    ))
                                ) : (
                                    <div className="dropdown-link">{t('noNotifications')}</div>
                                )}
                            </div>
                        )}

                        <div className="profile-button" onClick={toggleProfileMenu}>
                            {t('profile')} ▼
                        </div>
                        {profileMenuOpen && (
                            <div className="dropdown profile-dropdown">
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
