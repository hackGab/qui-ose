import { useEffect, useState } from 'react';
import { fetchNotifications } from '../utils/notificationUtils';

export const useFetchNotifications = (userData) => {
    const [notifications, setNotifications] = useState([]);

    useEffect(() => {
        if (userData) {
            const fetchNotificationsData = async () => {
                try {
                    const data = await fetchNotifications(userData.credentials.email);
                    const sortedData = data.sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp));
                    setNotifications(sortedData);
                } catch (error) {
                    console.error('Erreur:', error);
                }
            };

            fetchNotificationsData();

            const intervalId = setInterval(fetchNotificationsData, 60000); // Fetch notifications every 60 seconds

            return () => clearInterval(intervalId);
        }
    }, [userData]);

    return [notifications, setNotifications];
};