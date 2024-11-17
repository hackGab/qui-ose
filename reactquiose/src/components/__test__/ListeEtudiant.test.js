import { render, screen } from "@testing-library/react";
import ListeEtudiants from "../ListeEtudiants";
import { BrowserRouter } from "react-router-dom";
import '@testing-library/jest-dom/extend-expect';

jest.mock('react-i18next', () => ({
    useTranslation: () => ({
        t: (key) => key,
    }),
}));

const MockListeEtudiants = () => {
    return (
        <BrowserRouter>
            <ListeEtudiants />
        </BrowserRouter>
    );
};

describe("ListeEtudiants", () => {
    test("affiche le span avec le statut 'valide' si présent", async () => {
        const mockEtudiants = [
            {
                id: 1,
                firstName: "Jean",
                lastName: "Dupont",
                credentials: { email: "jean.dupont@example.com" },
                phoneNumber: "123-456-7890",
                departement: "INFORMATIQUE",
                cv: { status: "valide" }
            },
        ];

        global.fetch = jest.fn(() =>
            Promise.resolve({
                ok: true,
                json: () => Promise.resolve(mockEtudiants),
            })
        );

        render(<MockListeEtudiants />);

        const statusSpan = await screen.findByText("valide");
        expect(statusSpan).toBeInTheDocument();
    });


    test("affiche le span avec le statut 'refuse' si présent", async () => {
        const mockEtudiants = [
            {
                id: 1,
                firstName: "Marie",
                lastName: "Lefevre",
                credentials: { email: "marie.lefevre@example.com" },
                phoneNumber: "123-456-7890",
                departement: "COMPTABILITE",
                cv: { status: "refuse" }
            },
        ];

        global.fetch = jest.fn(() =>
            Promise.resolve({
                ok: true,
                json: () => Promise.resolve(mockEtudiants),
            })
        );

        render(<MockListeEtudiants />);

        const statusSpan = await screen.findByText("refuse");
        expect(statusSpan).toBeInTheDocument();
    });

    test("affiche le span avec le statut 'en attent' si présent", async () => {
        const mockEtudiants = [
            {
                id: 1,
                firstName: "Pierre",
                lastName: "Martin",
                credentials: { email: "pierre.martin@example.com" },
                phoneNumber: "123-456-7890",
                departement: "MARKETING",
                cv: { status: "en attent" }
            },
        ];

        global.fetch = jest.fn(() =>
            Promise.resolve({
                ok: true,
                json: () => Promise.resolve(mockEtudiants),
            })
        );

        render(<MockListeEtudiants />);

        const statusSpan = await screen.findByText("en attent");
        expect(statusSpan).toBeInTheDocument();
    });

    test("devrait gérer une erreur de fetch correctement", async () => {
        global.fetch = jest.fn(() =>
            Promise.reject(new Error("Erreur lors de la récupération des données"))
        );

        render(<MockListeEtudiants />);

        const errorMessage = await screen.findByText((content, element) =>
            content.includes('Erreur') && content.includes('Erreur lors de la récupération des données')
        );

        expect(errorMessage).toBeInTheDocument();
        expect(global.fetch).toHaveBeenCalled();
    });



    test("affiche un indicateur de chargement pendant que les données sont récupérées", async () => {
        const mockEtudiants = [
            {
                id: 1,
                firstName: "Jean",
                lastName: "Dupont",
                credentials: { email: "jean.dupont@example.com" },
                phoneNumber: "123-456-7890",
                departement: "INFORMATIQUE",
                cv: { status: "valide" }
            },
        ];

        global.fetch = jest.fn(() =>
            new Promise((resolve) => {
                setTimeout(() => {
                    resolve({
                        ok: true,
                        json: () => Promise.resolve(mockEtudiants),
                    });
                }, 500);
            })
        );

        render(<MockListeEtudiants />);

        const loadingMessage = screen.getByText(/chargement/i);
        expect(loadingMessage).toBeInTheDocument();

        const statusValide = await screen.findByText("valide");
        expect(statusValide).toBeInTheDocument();
    });

});
