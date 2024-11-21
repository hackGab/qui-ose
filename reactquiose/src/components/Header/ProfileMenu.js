import React, { useState } from 'react';
import {Link, useNavigate} from 'react-router-dom';
import { toggleProfileMenu, toggleNotificationMenu, changeLanguage } from '../../utils/headerUtils';
import { useTranslation } from 'react-i18next';
import { useFetchNotifications } from '../../hooks/notificationHooks';
import {renderNotifications} from "../../utils/notificationUtils";

const ProfileMenu = ({ userData, setActiveLink }) => {
    const { t } = useTranslation();
    const [notifications, setNotifications] = useFetchNotifications(userData);
    const [notificationMenuOpen, setNotificationMenuOpen] = useState(false);
    const [profileMenuOpen, setProfileMenuOpen] = useState(false);
    const navigate = useNavigate();

    return (
        <div className="profile-menu">
            <div className="notification-icon" onClick={() => toggleNotificationMenu(notificationMenuOpen, setNotificationMenuOpen, setProfileMenuOpen)}>
                🕭 <span className="notification-count">{notifications.length}</span>
            </div>
            {notificationMenuOpen && (
                <div className="dropdown notification-dropdown">
                    {renderNotifications(notifications, setActiveLink, userData, navigate, setNotifications)}
                </div>
            )}

            <div className="profile-button" onClick={() => toggleProfileMenu(profileMenuOpen, setProfileMenuOpen, setNotificationMenuOpen)}>
                {t('profile')} ▼
            </div>
            {profileMenuOpen && (
                <div className="dropdown profile-dropdown">
                    <Link className="dropdown-link" to="/profile">{t('myProfile')}</Link>
                    <Link className="dropdown-link" to="/settings">{t('settings')}</Link>
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