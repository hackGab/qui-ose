import React from 'react';
import i18n, {t} from "i18next";
import { FaTimes } from "react-icons/fa";
import {handleLinkClick} from "./headerUtils";


export const deplacementVersNotif = async (url, index, notifId, setNotifications, navigate, setActiveLink, userData) => {
    await handleDeleteNotification(index, notifId, setNotifications);
    handleLinkClick(url, setActiveLink, navigate, userData);
};

export const handleDeleteNotification = async (index, notifId, setNotifications) => {
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


export async function fetchNotifications(email) {
    const url = `http://localhost:8081/notification/allUnread/${email}`;
    const response = await fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    });

    if (!response.ok) {
        throw new Error(`Erreur lors de la requête: ${response.status}`);
    }

    return response.json();
}


export function translateTimeAgo(time, unit) {
    const currentLanguage = i18n.language ? i18n.language.split('-')[0].toLowerCase() : 'fr';
    const translatedUnit = i18n.t(unit, { lng: currentLanguage });
    const ago = i18n.t('ago', { lng: currentLanguage });
    if (currentLanguage === 'fr') {
        return `${ago} ${time} ${translatedUnit}`;
    } else {
        return `${time} ${translatedUnit} ${ago}`;
    }
}

export function extractTimeAndUnit(tempsDepuisReception) {
    const regex = /(\d+)\s*(secondes|minutes|heures|jours)/;
    const match = tempsDepuisReception.match(regex);
    if (match) {
        return { time: match[1], unit: match[2] };
    }
    return null;
}

export function renderNotifications(notifications, setActiveLink, userData, navigate, setNotifications) {
    return notifications.length > 0 ? (
        notifications.map((notification, index) => {
            const { time, unit } = extractTimeAndUnit(notification.tempsDepuisReception);
            const translatedTime = translateTimeAgo(time, unit);
            return (
                <React.Fragment key={notification.id}>
                    <div className="dropdown-link">
                        <div onClick={() => deplacementVersNotif(notification.url, index, notification.id, setNotifications, navigate, setActiveLink, userData)}>
                            {t(notification.message)} {notification.titreOffre} - {translatedTime}
                        </div>
                        <div data-testid="delete-icon" className="delete-icon"
                             onClick={() => handleDeleteNotification(index, notification.id, setNotifications)}>
                            <FaTimes />
                        </div>
                    </div>
                    <hr className="m-1" />
                </React.Fragment>
            );
        })
    ) : (
        <div className="dropdown-link">{t('noNotifications')}</div>
    );
}