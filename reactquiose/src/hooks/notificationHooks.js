import { useEffect, useState } from 'react';
import { fetchNotifications, extractTimeAndUnit } from '../utils/notificationUtils';

export const useFetchNotifications = (userData) => {
    const [notifications, setNotifications] = useState([]);

    useEffect(() => {
        if (!userData) return;

        const fetchAndSortNotifications = async () => {
            try {
                const data = await fetchNotifications(userData.credentials.email);
                const units = { secondes: 1, minutes: 60, heures: 3600, jours: 86400 };
                const sortedData = data.sort((a, b) => {
                    const timeA = extractTimeAndUnit(a.tempsDepuisReception);
                    const timeB = extractTimeAndUnit(b.tempsDepuisReception);
                    return timeA && timeB ? (parseInt(timeA.time) * units[timeA.unit]) - (parseInt(timeB.time) * units[timeB.unit]) : 0;
                });
                setNotifications(sortedData);
            } catch (error) {
                console.error('Erreur:', error);
            }
        };

        fetchAndSortNotifications();
        const intervalId = setInterval(fetchAndSortNotifications, 60000);

        return () => clearInterval(intervalId);
    }, [userData]);

    return [notifications, setNotifications];
};