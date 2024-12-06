import logo from "../../images/logo.png";
import {Link, useNavigate} from "react-router-dom";
import React, {useState} from "react";
import {changeLanguage} from "i18next";
import {useTranslation} from "react-i18next";

function ProfesseurHeader({userData}) {
    const {t} = useTranslation();
    const navigate = useNavigate();
    const [profileMenuOpen, setProfileMenuOpen] = useState(false);
    const handleClickLogo = () => {
        if (userData) {
            navigate("/accueilProfesseur", { state: { userData: userData } });
        }
    };
    const toggleProfileMenu = () => {
        setProfileMenuOpen(!profileMenuOpen);
    };

    return (
        <header className="gestionnaire-header">
            <nav className="navbar">
                <div className="logo" onClick={handleClickLogo} style={{ cursor: 'pointer' }}>
                    <img src={logo} alt="Logo" className="header-logo"/>
                    <div className="logo-text">Qui-Ose</div>
                </div>

                <div className="profile-menu">
                    <div className="notification-icon">🕭</div>
                    <div className="profile-button" onClick={toggleProfileMenu}>
                        {t('profile')} ▼
                    </div>
                    {profileMenuOpen && (
                        <div className="dropdown profile-dropdown">
                            <Link className="dropdown-link" to="/login">{t('logout')}</Link>
                            <button onClick={() => changeLanguage('en')}
                                    className="language-button dropdown-link text-left no-underline">
                                {t('Anglais')}
                            </button>
                            <button onClick={() => changeLanguage('fr')}
                                    className="language-button dropdown-link text-left no-underline">
                                {t('Francais')}
                            </button>
                        </div>
                    )}
                </div>
            </nav>
        </header>
    );
}
export default ProfesseurHeader;