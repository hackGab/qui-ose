import React, { useState } from 'react';
import { useLocation, useNavigate} from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import '../../CSS/Header.css';
import logo from '../../images/logo.png';
import "../../CSS/BoutonLangue.css";
import ProfileMenu from './ProfileMenu';
import { handleLinkClick } from "../../utils/headerUtils";

function EmployeurHeader({ userData }) {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const location = useLocation();
    const [activeLink, setActiveLink] = useState(location.pathname);

    const handleClickLogo = () => {
        if (userData) {
            navigate("/accueilEmployeur", { state: { userData: userData } });
        }
    };

    return (
        <header className="gestionnaire-header">
            <nav className="navbar">
                <div className="logo" onClick={handleClickLogo} style={{cursor: 'pointer'}}>
                    <img src={logo} alt="Logo" className="header-logo"/>
                    <div className="logo-text">Qui-Ose</div>
                </div>

                <div className="nav-links">
                    <span
                        className={`nav-link ${activeLink === '/accueilEmployeur' ? 'active' : ''}`}
                        onClick={() => handleLinkClick('/accueilEmployeur', setActiveLink, navigate, userData)}
                    >
                        {t('VisualiserOffres')}
                    </span>
                    <span
                        className={`nav-link ${activeLink === '/soumettre-offre' ? 'active' : ''}`}
                        onClick={() => handleLinkClick('/soumettre-offre', setActiveLink, navigate, userData)}
                    >
                        {t('SoummetreUnOffre')}
                    </span>
                    <span
                        className={`nav-link ${activeLink === '/visualiser-entrevue-accepter' ? 'active' : ''}`}
                        onClick={() => handleLinkClick('/visualiser-entrevue-accepter', setActiveLink, navigate, userData)}
                    >
                        {t('EntrevueAcceptee')}
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

export default EmployeurHeader;