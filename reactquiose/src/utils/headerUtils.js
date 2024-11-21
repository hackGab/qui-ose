import i18n from "i18next";

export const handleLinkClick = (path, setActiveLink, navigate, userData) => {
    setActiveLink(path);
    if (userData)
        navigate(path, { state: { userData: userData } });
};

export const toggleProfileMenu = (profileMenuOpen, setProfileMenuOpen, setNotificationMenuOpen) => {
    setProfileMenuOpen(!profileMenuOpen);
    setNotificationMenuOpen(false);
};

export const toggleNotificationMenu = (notificationMenuOpen, setNotificationMenuOpen, setProfileMenuOpen) => {
    setNotificationMenuOpen(!notificationMenuOpen);
    setProfileMenuOpen(false);
};

export const changeLanguage = (lng) => {
    i18n.changeLanguage(lng);
};