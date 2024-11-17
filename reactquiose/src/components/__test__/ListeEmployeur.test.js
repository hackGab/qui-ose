import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import ListeEmployeurs from '../ListeEmployeurs';
import '@testing-library/jest-dom/extend-expect';

jest.mock('react-i18next', () => ({
    useTranslation: () => ({
        t: (key) => key,
    }),
}));

const MockListeEmployeurs = () => (
    <BrowserRouter>
        <ListeEmployeurs />
    </BrowserRouter>
);

describe('ListeEmployeurs', () => {
    afterEach(() => {
        jest.clearAllMocks();
    });

    test('affiche "Loading..." lors du chargement', () => {
        global.fetch = jest.fn(() => new Promise(() => {}));

        render(<MockListeEmployeurs />);

        expect(screen.getByText((content, element) =>
            element.tagName.toLowerCase() === 'span' && content === 'chargementEmployeurs'
        )).toBeInTheDocument();
    });

    test('affiche une erreur en cas de problème réseau', async () => {
        global.fetch = jest.fn(() =>
            Promise.reject(new Error('Network Error'))
        );

        render(<MockListeEmployeurs />);

        // Use a regular expression to match the error message
        const errorMessage = await screen.findByText(/Erreur: Network Error/i);
        expect(errorMessage).toBeInTheDocument();
    });


    test('affiche les employeurs avec le statut "valide"', async () => {
        const mockEmployeurs = [
            {
                id: 1,
                employeur: {
                    entreprise: 'EntrepriseTest',
                },
                titre: 'Stage en Développement',
                localisation: 'Montréal',
                status: 'valide',
            },
        ];

        global.fetch = jest.fn(() =>
            Promise.resolve({
                ok: true,
                json: () => Promise.resolve(mockEmployeurs),
            })
        );

        render(<MockListeEmployeurs />);
        const statusSpan = await screen.findByText('valide');
        expect(statusSpan).toBeInTheDocument();
    });

    test('affiche les employeurs avec le statut "refuse"', async () => {
        const mockEmployeurs = [
            {
                id: 2,
                employeur: {
                    entreprise: 'EntrepriseRefuse',
                },
                titre: 'Stage en Marketing',
                localisation: 'Québec',
                status: 'refuse',
            },
        ];

        global.fetch = jest.fn(() =>
            Promise.resolve({
                ok: true,
                json: () => Promise.resolve(mockEmployeurs),
            })
        );

        render(<MockListeEmployeurs />);
        const statusSpan = await screen.findByText('refuse');
        expect(statusSpan).toBeInTheDocument();
    });

    test('affiche les employeurs avec le statut "attent"', async () => {
        const mockEmployeurs = [
            {
                id: 3,
                employeur: {
                    entreprise: 'EntrepriseAttente',
                },
                titre: 'Stage en Design',
                localisation: 'Toronto',
                status: 'attent',
            },
        ];

        global.fetch = jest.fn(() =>
            Promise.resolve({
                ok: true,
                json: () => Promise.resolve(mockEmployeurs),
            })
        );

        render(<MockListeEmployeurs />);
        const statusSpan = await screen.findByText('attent');
        expect(statusSpan).toBeInTheDocument();
    });
});
