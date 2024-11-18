import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import '../../CSS/Header.css';
import logo from '../../images/logo.png';
import "../../CSS/BoutonLangue.css";
import ProfileMenu from './ProfileMenu';
import { handleLinkClick } from '../../utils/headerUtils';

function EtudiantHeader({ userData }) {
    const { t } = useTranslation();
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
                </div>

                <ProfileMenu userData={userData} setActiveLink={setActiveLink} />
            </nav>
        </header>
    );
}

export default EtudiantHeader;