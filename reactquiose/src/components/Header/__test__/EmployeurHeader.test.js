import React from 'react';
import { render, screen, fireEvent, act, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import i18n from '../../../utils/i18n/i18n';
import {extractTimeAndUnit, translateTimeAgo} from "../../../utils/notificationUtils";
import EtudiantHeader from "../EtudiantHeader";
import EmployeurHeader from "../EmployeurHeader";

describe('EmployeurHeader Component - Notifications', () => {
    const userData = {
        credentials: {
            email: 'testEmployeur@example.com'
        }
    };

    beforeEach(() => {
        global.fetch = jest.fn(() =>
            Promise.resolve({
                ok: true,
                json: () => Promise.resolve([
                    { id: 1, message: 'Notification 1', tempsDepuisReception: '5 heures', url: '/SignerContrat' },
                ])
            })
        );

        i18n.changeLanguage('fr');
    });

    it('Devrait afficher en français le temps depuis la notification', () => {
        i18n.changeLanguage('fr');
        expect(translateTimeAgo(5, 'minutes')).toBe('il y a 5 minutes');
        expect(translateTimeAgo(10, 'heures')).toBe('il y a 10 heures');
    });

    it('Devrait afficher en anglais le temps depuis la notification', () => {
        i18n.changeLanguage('en');
        expect(translateTimeAgo(5, 'minutes')).toBe('5 minutes ago');
        expect(translateTimeAgo(10, 'heures')).toBe('10 hours ago');
    });

    it('Devrait afficher les notifications', async () => {
        await act(async () => {
            render(
                <MemoryRouter>
                    <EmployeurHeader userData={userData} onSendData={jest.fn()} />
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

    it('Devrait effacer notification quand on clique', async () => {
        await act(async () => {
            render(
                <MemoryRouter>
                    <EmployeurHeader userData={userData} onSendData={jest.fn()} />
                </MemoryRouter>
            );
        });
        const notificationIcon = screen.getByText('🕭');
        fireEvent.click(notificationIcon);
        let notifications = screen.getAllByText(/Notification \d/);
        expect(notifications.length).toBe(1);

        const deleteIcon = screen.getAllByTestId('delete-icon')[0];
        expect(deleteIcon).toBeInTheDocument();
        fireEvent.click(deleteIcon);

        notifications = screen.queryAllByText(/Notification \d/);
        expect(notifications.length).toBe(0);
    });

    it('Devrait naviguer vers l\'URL de la notification lorsqu\'une notification est cliquée', async () => {
        await act(async () => {
            render(
                <MemoryRouter initialEntries={['/']}>
                    <Routes>
                        <Route path="/" element={<EmployeurHeader userData={userData} onSendData={jest.fn()}/>} />
                        <Route path="/SignerContrat" element={<div>Signature de contrat Page</div>} />
                    </Routes>
                </MemoryRouter>
            );
        });

        const notificationIcon = screen.getByText('🕭');
        fireEvent.click(notificationIcon);

        const notification = screen.getByText('Notification 1 - il y a 5 heures');
        fireEvent.click(notification);

        await waitFor(() => expect(screen.getByText('Signature de contrat Page')).toBeInTheDocument());
    });

    it('Devrait effacer la notification et naviguer vers l\'URL lorsqu\'une notification est cliquée', async () => {
        await act(async () => {
            render(
                <MemoryRouter initialEntries={['/']}>
                    <Routes>
                        <Route path="/" element={<EmployeurHeader userData={userData} onSendData={jest.fn()}/>} />
                        <Route path="/SignerContrat" element={<div>Signature de contrat Page</div>} />
                    </Routes>
                </MemoryRouter>
            );
        });

        const notificationIcon = screen.getByText('🕭');
        fireEvent.click(notificationIcon);

        const notification = screen.getByText('Notification 1 - il y a 5 heures');
        fireEvent.click(notification);

        await waitFor(() => expect(screen.queryByText('Notification 1 - il y a 5 heures')).not.toBeInTheDocument());
        await waitFor(() => expect(screen.getByText('Signature de contrat Page')).toBeInTheDocument());
    });


    // Tests des throw new Error
    it('Devrait lancer une erreur lorsqu\'une notification est cliquée avec un mauvais ID', async () => {
        global.fetch = jest.fn((url, options) => {
            if (options && options.method === 'PUT') {
                return Promise.reject(new Error('Erreur: Erreur lors de la suppression de la notification avec un mauvais ID'));
            }
            return Promise.resolve({
                ok: true,
                json: () => Promise.resolve([
                    { id: 1, message: 'Notification 1', tempsDepuisReception: '2 heures', url: '/SignerContrat' },
                ])
            });
        });

        const consoleErrorMock = jest.spyOn(console, 'error').mockImplementation(() => {});

        await act(async () => {
            render(
                <MemoryRouter>
                    <EmployeurHeader userData={userData} onSendData={jest.fn()}/>
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

    it('Devrait retourner erreur quand le fetchNotifications échoue', async () => {
        global.fetch = jest.fn(() => Promise.reject(new Error('Erreur lors de la requête')));

        const consoleErrorMock = jest.spyOn(console, 'error').mockImplementation(() => {});

        await act(async () => {
            render(
                <MemoryRouter>
                    <EmployeurHeader userData={userData} onSendData={jest.fn()}/>
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

    it('Devrait gérer erreur quand le regex ne correspond pas dans extractTimeAndUnit', () => {
        const result = extractTimeAndUnit('invalid string');
        expect(result).toBeNull();
    });
});