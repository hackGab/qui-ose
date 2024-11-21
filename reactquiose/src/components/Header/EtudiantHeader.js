import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import '../../CSS/Header.css';
import logo from '../../images/logo.png';
import "../../CSS/BoutonLangue.css";
import ProfileMenu from './ProfileMenu';
import {calculateNextSessions} from "../../utils/methodes/dateUtils";
import {hardCodedSessions} from "../../utils/variables/hardCodedSessions";


function EtudiantHeader({ userData ,onSendData}) {
    const { t } = useTranslation();
    const [notifications, setNotifications] = useState([]);
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
        return localStorage.getItem('session') || initialSession;
    });

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


    const handleDeleteNotification = async (index, notifId) => {
        setNotifications((prevNotifications) =>
            prevNotifications.filter((_, i) => i !== index)
        );

        try {
            const response = await fetch(`http://localhost:8081/notification/markAsRead/${notifId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Erreur lors de la suppression de la notification');
            }
        } catch (err) {
            console.error('Erreur:', err);
        }
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
                    //console.log('Réponse du serveur:', data);

                    if (data.cv) {
                        setFile(data.cv);
                        setStagesAppliquees(data.offresAppliquees);

                        //console.log('CV:', data.cv);
                        //console.log('Stages appliquées:', data.offresAppliquees);
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


    useEffect(() => {
        if (userData) {
            const fetchNotifications = () => {
                console.log('Fetching notifications...')
                const url = `http://localhost:8081/notification/allUnread/${userData.credentials.email}`;
                fetch(url, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error(`Erreur lors de la requête: ${response.status}`);
                        }
                        return response.json();
                    })
                    .then(data => {
                        console.log('Réponse du serveur:', data);
                        setNotifications(data);
                    })
                    .catch(error => {
                        console.error('Erreur:', error);
                    });
            };

            fetchNotifications(); // Fetch notifications initially

            const intervalId = setInterval(fetchNotifications, 60000); // Fetch notifications every 60 seconds

            return () => clearInterval(intervalId); // Clear interval on component unmount
        }
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
                        onClick={() => handleLinkClick('/accueilEtudiant', setActiveLink, navigate, userData)}
                    >
                        {t('accueil')}
                    </span>
                    {file && file.status === "validé" && (
                        <span
                            className={`nav-link ${activeLink === '/stagesAppliquees' ? 'active' : ''}`}
                            onClick={() => handleLinkClick('/stagesAppliquees', setActiveLink, navigate, userData)}
                        >
                            {t('stagesAppliquées')} ({stagesAppliquees.length})
                        </span>
                    )}

                    <span
                        className={`nav-link ${activeLink === '/mesEntrevues' ? 'active' : ''}`}
                        onClick={() => handleLinkClick('/mesEntrevues', setActiveLink, navigate, userData)}
                    >
                        {t('mesEntrevues')} ({nbEntrevuesEnAttente || 0})
                    </span>
                    <span
                        className={`nav-link ${activeLink === '/signerContrat' ? 'active' : ''}`}
                        onClick={() => handleLinkClick('/signerContrat', setActiveLink, navigate, userData)}
                    >
                        {t('SignerContrat')}
                    </span>

                    <div className="filter-options">
                        <label>Filtre :</label>
                        <div className="session-dropdown">
                            <select value={session} onChange={(e) => handleSessionChange(e.target.value)}>
                                {availableSessions.map(sessionOption => (
                                    <option key={sessionOption.id}
                                            value={sessionOption.id}>{sessionOption.label}</option>
                                ))}
                            </select>
                        </div>
                    </div>
                </div>

                <ProfileMenu userData={userData} setActiveLink={setActiveLink}/>
            </nav>
        </header>
    );
}

export default EtudiantHeader;
