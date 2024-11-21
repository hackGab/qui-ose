import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import ListeCandidature from "../ListeCandidature"; // Remplacez par le bon chemin
import { MemoryRouter } from "react-router-dom";
import '@testing-library/jest-dom/extend-expect';

describe('ListeCandidature', () => {
    afterEach(() => {
        jest.clearAllMocks();
    });

    beforeEach(() => {
        localStorage.clear();
        localStorage.setItem('session', 'HIVER24');
    });
    it('appelle fetch avec les bonnes URLs pour la session correcte', async () => {
        global.fetch = jest.fn((url) => {
            if (url === 'http://localhost:8081/candidatures/session/HIVER24') {
                return Promise.resolve({
                    ok: true,
                    json: () => Promise.resolve([]), // Réponse vide pour les candidatures
                });
            }
            if (url === 'http://localhost:8081/contrat/session/HIVER24') {
                return Promise.resolve({
                    ok: true,
                    json: () => Promise.resolve([]), // Réponse vide pour les contrats
                });
            }
            return Promise.reject(new Error('URL inconnue dans le test'));
        });

        render(
            <MemoryRouter>
                <ListeCandidature />
            </MemoryRouter>
        );

        await screen.findByText('AucuneCandidatureTrouvee'); // Assurez-vous que le texte apparaît

        // Vérifiez que fetch a été appelé avec les bonnes URLs
        expect(fetch).toHaveBeenCalledWith(
            'http://localhost:8081/candidatures/session/HIVER24');
        expect(fetch).toHaveBeenCalledWith(
            'http://localhost:8081/contrat/session/HIVER24');
    });


    it('affiche les candidatures et met à jour selon une session différente', async () => {
        localStorage.setItem('session', 'HIVER25');

        global.fetch = jest.fn()
            .mockResolvedValueOnce({
                ok: true,
                json: () => Promise.resolve([{
                    candidature: {
                        id: 1,
                        entrevueId: 1,
                        entrevue: null,
                        accepte: true
                    },
                }])
            })
            .mockResolvedValueOnce({
                ok: true,
                json: () => Promise.resolve([{
                    candidature: {
                        id: 1,
                        entrevueId: 1,
                        entrevue: null,
                        accepte: true
                    },
                    collegeEngagement: "a",
                    dateDebut: "2024-11-16",
                    dateFin: "2024-11-30",
                    dateSignatureEmployeur: "2024-11-20",
                    dateSignatureEtudiant: "2024-11-20",
                    dateSignatureGestionnaire: "2024-11-20",
                    description: "a",
                    employeurSigne: true,
                    entrepriseEngagement: "a",
                    etudiantEngagement: "a",
                    etudiantSigne: true,
                    gestionnaireSigne: true,
                    heureHorraireDebut: "05:30:00",
                    heureHorraireFin: "08:30:00",
                    heuresParSemaine: 4,
                    lieuStage: "chez moi",
                    nbSemaines: 2,
                    tauxHoraire: 0,
                    uuid: "aee7c428-3e5b-46b7-8784-d7874a3686e0"
                }])
            });

        render(
            <MemoryRouter>
                <ListeCandidature />
            </MemoryRouter>
        );

        // await screen.findByText('Cannot read properties of undefined (reading \'then\')'); // Assurez-vous que le texte apparaît
        // expect(screen.getByText(' Error ')).toBeInTheDocument();
    });

});
