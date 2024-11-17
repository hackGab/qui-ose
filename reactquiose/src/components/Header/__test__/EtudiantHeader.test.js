import React from 'react';
import { render, screen, fireEvent, act, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import EtudiantHeader, { translateTimeAgo, extractTimeAndUnit } from '../EtudiantHeader';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import i18n from '../../../utils/i18n/i18n';

describe('EtudiantHeader Component - Notifications', () => {
    const userData = {
        credentials: {
            email: 'test@example.com'
        }
    };

    beforeEach(() => {
        global.fetch = jest.fn(() =>
            Promise.resolve({
                ok: true,
                json: () => Promise.resolve([
                    { id: 1, message: 'Notification 1', tempsDepuisReception: '2 heures', url: '/stagesAppliquees' },
                    { id: 2, message: 'Notification 2', tempsDepuisReception: '3 heures', url: '/mesEntrevues' }
                ])
            })
        );

        i18n.changeLanguage('fr'); // Changez la langue si nécessaire
    });

    it('should translate time ago correctly in French', () => {
        i18n.changeLanguage('fr');
        expect(translateTimeAgo(2, 'heures')).toBe('il y a 2 heures');
        expect(translateTimeAgo(5, 'minutes')).toBe('il y a 5 minutes');
    });

    it('should translate time ago correctly in English', () => {
        i18n.changeLanguage('en');
        expect(translateTimeAgo(2, 'heures')).toBe('2 hours ago');
        expect(translateTimeAgo(5, 'minutes')).toBe('5 minutes ago');
    });

    it('should render notifications', async () => {
        await act(async () => {
            render(
                <MemoryRouter>
                    <EtudiantHeader userData={userData} />
                </MemoryRouter>
            );
        });
        const notificationIcon = screen.getByText('🕭');
        fireEvent.click(notificationIcon);
        const notifications = screen.getAllByText(/Notification \d/);
        expect(notifications.length).toBeGreaterThan(0);
        notifications.forEach(notification => {
            expect(notification).toBeInTheDocument();
        });
    });

    it('should delete a notification when delete icon is clicked', async () => {
        await act(async () => {
            render(
                <MemoryRouter>
                    <EtudiantHeader userData={userData} />
                </MemoryRouter>
            );
        });
        const notificationIcon = screen.getByText('🕭');
        fireEvent.click(notificationIcon);
        let notifications = screen.getAllByText(/Notification \d/);
        expect(notifications.length).toBe(2);

        const deleteIcon = screen.getAllByTestId('delete-icon')[0];
        expect(deleteIcon).toBeInTheDocument();
        fireEvent.click(deleteIcon);

        notifications = screen.queryAllByText(/Notification \d/);
        expect(notifications.length).toBe(1);
    });

    it('should navigate to the notification URL when a notification is clicked', async () => {
        await act(async () => {
            render(
                <MemoryRouter initialEntries={['/']}>
                    <Routes>
                        <Route path="/" element={<EtudiantHeader userData={userData} />} />
                        <Route path="/stagesAppliquees" element={<div>Stages Appliquées Page</div>} />
                    </Routes>
                </MemoryRouter>
            );
        });

        const notificationIcon = screen.getByText('🕭');
        fireEvent.click(notificationIcon);

        const notification = screen.getByText('Notification 1 - il y a 2 heures');
        fireEvent.click(notification);

        await waitFor(() => expect(screen.getByText('Stages Appliquées Page')).toBeInTheDocument());
    });

    it('should delete the notification and navigate to the URL when a notification is clicked', async () => {
        await act(async () => {
            render(
                <MemoryRouter initialEntries={['/']}>
                    <Routes>
                        <Route path="/" element={<EtudiantHeader userData={userData} />} />
                        <Route path="/stagesAppliquees" element={<div>Stages Appliquées Page</div>} />
                    </Routes>
                </MemoryRouter>
            );
        });

        const notificationIcon = screen.getByText('🕭');
        fireEvent.click(notificationIcon);

        const notification = screen.getByText('Notification 1 - il y a 2 heures');
        fireEvent.click(notification);

        await waitFor(() => expect(screen.queryByText('Notification 1 - il y a 2 heures')).not.toBeInTheDocument());
        await waitFor(() => expect(screen.getByText('Stages Appliquées Page')).toBeInTheDocument());
    });



    // Tests des throw new Error
    it('should throw an error when fetch deleteNotification fails with a bad ID', async () => {
        global.fetch = jest.fn((url, options) => {
            if (options && options.method === 'PUT') {
                return Promise.reject(new Error('Erreur: Erreur lors de la suppression de la notification avec un mauvais ID'));
            }
            return Promise.resolve({
                ok: true,
                json: () => Promise.resolve([
                    { id: 1, message: 'Notification 1', tempsDepuisReception: '2 heures', url: '/stagesAppliquees' },
                    { id: 2, message: 'Notification 2', tempsDepuisReception: '3 heures', url: '/mesEntrevues' }
                ])
            });
        });

        const consoleErrorMock = jest.spyOn(console, 'error').mockImplementation(() => {});

        await act(async () => {
            render(
                <MemoryRouter>
                    <EtudiantHeader userData={userData} />
                </MemoryRouter>
            );
        });

        // Manually call fetch with a bad notifId
        const badNotifId = 'bad-id';
        try {
            await fetch(`http://localhost:8081/notification/markAsRead/${badNotifId}`, { method: 'PUT' });
        } catch (error) {
            console.error(error.toString());
        }

        await waitFor(() => {
            expect(consoleErrorMock).toHaveBeenCalledWith(expect.stringContaining('Erreur: Erreur lors de la suppression de la notification avec un mauvais ID'));
        });

        consoleErrorMock.mockRestore();
    });


    it('should throw an error when fetchNotifications fails', async () => {
        global.fetch = jest.fn(() => Promise.reject(new Error('Erreur lors de la requête')));

        const consoleErrorMock = jest.spyOn(console, 'error').mockImplementation(() => {});

        await act(async () => {
            render(
                <MemoryRouter>
                    <EtudiantHeader userData={userData} />
                </MemoryRouter>
            );
        });

        try {
            await fetch(`http://localhost:8081/notification/allUnread/mauvaisEmail`, { method: 'GET' });
        } catch (error) {
            console.error(error.toString());
        }

        await waitFor(() => {
            expect(consoleErrorMock).toHaveBeenCalledWith(expect.stringContaining('Erreur lors de la requête'));
        });

        consoleErrorMock.mockRestore();
    });


    it('should handle error when regex does not match in extractTimeAndUnit', () => {
        const result = extractTimeAndUnit('invalid string');
        expect(result).toBeNull();
    });
});