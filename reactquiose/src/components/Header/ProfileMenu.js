import React, { useRef, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { toggleProfileMenu, toggleNotificationMenu, changeLanguage } from '../../utils/headerUtils';
import { useTranslation } from 'react-i18next';
import { useFetchNotifications } from '../../hooks/notificationHooks';
import { renderNotifications } from "../../utils/notificationUtils";

const ProfileMenu = ({ userData, setActiveLink, closeNav, closeMenus, profileMenuOpen, setProfileMenuOpen, notificationMenuOpen, setNotificationMenuOpen }) => {
    const { t } = useTranslation();
    const [notifications, setNotifications] = useFetchNotifications(userData);
    const navigate = useNavigate();
    const menuRef = useRef(null);

    const handleNotificationClick = () => {
        toggleNotificationMenu(notificationMenuOpen, setNotificationMenuOpen, setProfileMenuOpen);
        closeNav();
    };

    const handleProfileClick = () => {
        toggleProfileMenu(profileMenuOpen, setProfileMenuOpen, setNotificationMenuOpen);
        closeNav();
    };

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (menuRef.current && !menuRef.current.contains(event.target)) {
                closeMenus();
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, [menuRef]);

    return (
        <div className="profile-menu" ref={menuRef}>
            <div className="notification-icon" onClick={handleNotificationClick}>
                🕭 <span className="notification-count">{notifications.length}</span>
            </div>
            {notificationMenuOpen && (
                <div className="dropdown notification-dropdown">
                    {renderNotifications(notifications, setActiveLink, userData, navigate, setNotifications)}
                </div>
            )}

            <div className="profile-button" onClick={handleProfileClick}>
                {t('profile')} ▼
            </div>
            {profileMenuOpen && (
                <div className="dropdown profile-dropdown">
                    <Link className="dropdown-link" to="/login">{t('logout')}</Link>
                    <button onClick={() => changeLanguage('en')} className="language-button dropdown-link">
                        {t('Anglais')}
                    </button>
                    <button onClick={() => changeLanguage('fr')} className="language-button dropdown-link">
                        {t('Francais')}
                    </button>
                </div>
            )}
        </div>
    );
};

export default ProfileMenu;