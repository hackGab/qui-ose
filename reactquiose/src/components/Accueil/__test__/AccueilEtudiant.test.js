import React from "react";
import { render, screen, waitFor } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import AccueilEtudiant from "../AccueilEtudiant";

beforeEach(() => {
    jest.clearAllMocks();
});

global.fetch = jest.fn();

describe("AccueilEtudiant", () => {
    it("affiche un message d'erreur si la requête échoue", async () => {
        // Simuler une erreur sur la requête fetch
        fetch.mockImplementationOnce(() =>
            Promise.reject(new Error("Erreur réseau"))
        );

        render(
            <MemoryRouter>
                <AccueilEtudiant />
            </MemoryRouter>
        );

        // Vérifiez qu'un message ou une erreur s'affiche après une requête échouée
        await waitFor(() =>
            expect(console.error).toHaveBeenCalledWith(
                expect.stringContaining("Erreur:")
            )
        );
    });

    it("charge et affiche les stages si le CV est validé", async () => {
        // Simuler une réponse fetch pour l'utilisateur
        fetch
            .mockImplementationOnce(() =>
                Promise.resolve({
                    ok: true,
                    json: () => Promise.resolve({
                        cv: {
                            status: "validé",
                            data: "dummy_cv_data",
                        },
                    }),
                })
            )
            .mockImplementationOnce(() =>
                Promise.resolve({
                    ok: true,
                    json: () =>
                        Promise.resolve([
                            { id: 1, title: "Stage 1" },
                            { id: 2, title: "Stage 2" },
                        ]),
                })
            );

        render(
            <MemoryRouter>
                <AccueilEtudiant />
            </MemoryRouter>
        );

        // Vérifiez que les stages sont affichés
        await waitFor(() => {
            expect(screen.getByText(/Stage 1/i)).toBeInTheDocument();
            expect(screen.getByText(/Stage 2/i)).toBeInTheDocument();
        });
    });

    it("affiche un message si le CV est rejeté", async () => {
        // Simuler une réponse fetch avec un CV rejeté
        fetch.mockImplementationOnce(() =>
            Promise.resolve({
                ok: true,
                json: () => Promise.resolve({
                    cv: {
                        status: "rejeté",
                        rejetMessage: "Format incorrect.",
                    },
                }),
            })
        );

        render(
            <MemoryRouter>
                <AccueilEtudiant />
            </MemoryRouter>
        );

        // Vérifiez que le message de rejet est affiché
        await waitFor(() => {
            expect(screen.getByText(/Format incorrect./i)).toBeInTheDocument();
        });
    });
});
