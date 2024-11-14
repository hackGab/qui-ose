import React from 'react';
import { render, screen, fireEvent, act, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import EtudiantHeader from '../EtudiantHeader';
import {MemoryRouter, Route, Routes} from 'react-router-dom';

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

        const notification = screen.getByText('Notification 1 - 2 heures avant');
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

        const notification = screen.getByText('Notification 1 - 2 heures avant');
        fireEvent.click(notification);

        await waitFor(() => expect(screen.queryByText('Notification 1 - 2 heures avant')).not.toBeInTheDocument());
        await waitFor(() => expect(screen.getByText('Stages Appliquées Page')).toBeInTheDocument());
    });
});