import { render, screen } from "@testing-library/react";
import ListeEmployeurs from "../ListeEmployeurs";
import { BrowserRouter } from "react-router-dom";
import '@testing-library/jest-dom/extend-expect';  // Pour les matchers comme .toBeInTheDocument()
import { useTranslation } from 'react-i18next';

// Mock de useTranslation pour éviter les erreurs liées à i18n pendant le test
jest.mock('react-i18next', () => ({
    useTranslation: () => ({
        t: (key) => key,  // retourne la clé pour simplifier
    }),
}));

const MockListeEmployeurs = () => {
    return (
        <BrowserRouter>
            <ListeEmployeurs />
        </BrowserRouter>
    );
};

describe("ListeEmployeurs", () => {
    test("affiche le span avec le statut s'il est présent", async () => {
        const mockEmployeurs = [
            {
                id: 1,
                employeur: {
                    entreprise: "EntrepriseTest",
                    credentials: { email: "test@entreprise.com" },
                    phoneNumber: "123-456-7890",
                },
                titre: "Stage en Développement",
                localisation: "Montréal",
                status: "valide",
            },
        ];

        global.fetch = jest.fn(() =>
            Promise.resolve({
                ok: true,
                json: () => Promise.resolve(mockEmployeurs),
            })
        );

        render(<MockListeEmployeurs />);

        const statusSpan = await screen.findByText("valide");
        expect(statusSpan).toBeInTheDocument();
    });

    test("affiche le span avec le statut s'il est refuse", async () => {
        const mockEmployeurs = [
            {
                id: 1,
                employeur: {
                    entreprise: "EntrepriseTest",
                    credentials: { email: "test@entreprise.com" },
                    phoneNumber: "123-456-7890",
                },
                titre: "Stage en Développement",
                localisation: "Montréal",
                status: "refuse",
            },
        ];

        global.fetch = jest.fn(() =>
            Promise.resolve({
                ok: true,
                json: () => Promise.resolve(mockEmployeurs),
            })
        );

        render(<MockListeEmployeurs />);

        const statusSpan = await screen.findByText("refuse");
        expect(statusSpan).toBeInTheDocument();
    });
    test("affiche le span avec le statut s'il est en attent", async () => {
        const mockEmployeurs = [
            {
                id: 1,
                employeur: {
                    entreprise: "EntrepriseTest",
                    credentials: { email: "test@entreprise.com" },
                    phoneNumber: "123-456-7890",
                },
                titre: "Stage en Développement",
                localisation: "Montréal",
                status: "attent",
            },
        ];

        global.fetch = jest.fn(() =>
            Promise.resolve({
                ok: true,
                json: () => Promise.resolve(mockEmployeurs),
            })
        );

        render(<MockListeEmployeurs />);

        const statusSpan = await screen.findByText("attent");
        expect(statusSpan).toBeInTheDocument();
    });
});
