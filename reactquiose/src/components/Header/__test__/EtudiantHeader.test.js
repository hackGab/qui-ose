import React from 'react';
import { render, screen, fireEvent, act } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import EtudiantHeader from '../EtudiantHeader';
import {MemoryRouter, Route, Routes} from 'react-router-dom';

describe('EtudiantHeader Component - Notifications', () => {
    const userData = {
        credentials: {
            email: 'test@example.com'
        }
    };

    it('should render notifications', () => {
        act(() => {
            render(
                <MemoryRouter>
                    <EtudiantHeader userData={userData} />
                </MemoryRouter>
            );
        });
        const notificationIcon = screen.getByText('🕭');
        fireEvent.click(notificationIcon);
        const notifications = screen.getAllByText('Vous avez été accepté pour le stage de Programmeur analyste - 2 heures avant');
        expect(notifications.length).toBeGreaterThan(0);
        notifications.forEach(notification => {
            expect(notification).toBeInTheDocument();
        });
    });

    it('should delete a notification when delete icon is clicked', () => {
        act(() => {
            render(
                <MemoryRouter>
                    <EtudiantHeader userData={userData} />
                </MemoryRouter>
            );
        });
        // Vérifier que les notifications sont affichées
        const notificationIcon = screen.getByText('🕭');
        fireEvent.click(notificationIcon);
        let notifications = screen.getAllByText('Vous avez été accepté pour le stage de Programmeur analyste - 2 heures avant');
        expect(notifications.length).toBeGreaterThanOrEqual(5);

        // Test bouton delete
        const deleteIcon = screen.getAllByTestId('delete-icon')[0];
        expect(deleteIcon).toBeInTheDocument();
        fireEvent.click(deleteIcon);

        // Vérifier que la notification a été supprimée
        notifications = screen.getAllByText('Vous avez été accepté pour le stage de Programmeur analyste - 2 heures avant');
        expect(notifications.length).toBeLessThan(5);
    });


    it('should navigate to the notification URL when a notification is clicked', () => {
        const userData = { credentials: { email: 'test@example.com' } };
        render(
            <MemoryRouter initialEntries={['/']}>
                <Routes>
                    <Route path="/" element={<EtudiantHeader userData={userData} />} />
                    <Route path="/stagesAppliquees" element={<div>Stages Appliquées Page</div>} />
                </Routes>
            </MemoryRouter>
        );

        const notificationIcon = screen.getByText('🕭');
        fireEvent.click(notificationIcon);

        const notification = screen.getAllByText('Vous avez été accepté pour le stage de Programmeur analyste - 2 heures avant')[0];
        fireEvent.click(notification);

        expect(screen.getByText('Stages Appliquées Page')).toBeInTheDocument();
    });
});