import {render, screen, waitFor} from "@testing-library/react";
import AccueilGestionnaire from "../AccueilGestionnaire";
import { BrowserRouter } from "react-router-dom";
import { useLocation } from 'react-router-dom';
import { act } from "react"; // Updated import

jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useLocation: jest.fn()
}));

jest.mock('react-i18next', () => ({
    useTranslation: () => ({
        t: (key) => key,
    }),
}));

const MockAccueilGestionnaire = () => (
    <BrowserRouter>
        <AccueilGestionnaire />
    </BrowserRouter>
);

describe("AccueilGestionnaire Notifications", () => {
    beforeEach(() => {
        useLocation.mockReturnValue({
            state: { userData: { firstName: "John", lastName: "Doe" } }
        });
    });

    afterEach(() => {
        jest.restoreAllMocks();
        global.fetch.mockClear();
    });

    test("should display notification badge when cvAttentes and offreDeStageAttent > 0", async () => {
        global.fetch = jest.fn(() =>
            Promise.resolve({
                json: () => Promise.resolve(5)
            })
        );

        render(<MockAccueilGestionnaire />);

        const notificationBadges = await screen.findAllByText('5');
        expect(notificationBadges.length).toBeGreaterThan(0);

        const notificationBadge = notificationBadges[0];
        expect(notificationBadge).toBeInTheDocument();
    });


    test("should not display notification badge when cvAttentes = 0", async () => {
        global.fetch = jest.fn(() =>
            Promise.resolve({
                json: () => Promise.resolve(0)
            })
        );

        render(<MockAccueilGestionnaire />);

        // Check that no notification badge is displayed
        const notificationBadge = screen.queryByText('0');
        expect(notificationBadge).not.toBeInTheDocument();
    });

    test("should handle fetch error gracefully", async () => {
        global.fetch = jest.fn(() =>
            Promise.reject(new Error("Network Error"))
        );

        await act(async () => {
            render(<MockAccueilGestionnaire />);
        });

        expect(global.fetch).toHaveBeenCalled();
    });

    test("should not display notification when contratsAGenerer is not set", async () => {
        global.fetch = jest.fn(() =>
            Promise.resolve({
                json: () => Promise.resolve(5)
            })
        );

        render(<MockAccueilGestionnaire />);

        const notifContrat = screen.queryByTestId('notif-contrat');
        await waitFor(() => expect(notifContrat).not.toBeInTheDocument());
    });


    test("should display notification badge when contratsAGenerer > 0", async () => {
        global.fetch = jest.fn((url) => {
            if (url.includes('candidatures/all')) {
                return Promise.resolve({
                    json: () => Promise.resolve([{ id: 1 }, { id: 2 }])
                });
            }
            if (url.includes('contrat/all')) {
                return Promise.resolve({
                    json: () => Promise.resolve([{ candidature: { id: 1 } }])
                });
            }
            return Promise.reject(new Error("Unknown URL"));
        });

        await act(async () => {
            render(<MockAccueilGestionnaire />);
        });

        await waitFor(() => {
            const notifContrat = screen.getByTestId('notif-contrat');
            expect(notifContrat).toBeInTheDocument();
            expect(notifContrat).toHaveTextContent('1');
        });
    });


    test("should handle errors gracefully", async () => {
        global.fetch = jest.fn(() =>
            Promise.reject(new Error("Network Error"))
        );

        await act(async () => {
            render(<MockAccueilGestionnaire />);
        });

        expect(global.fetch).toHaveBeenCalled();
    });

    test("should filter candidatsSansContrat correctly", async () => {
        global.fetch = jest.fn((url) => {
            if (url.includes('candidatures/all')) {
                return Promise.resolve({
                    json: () => Promise.resolve([{ id: 1 }, { id: 2 }])
                });
            }
            if (url.includes('contrat/all')) {
                return Promise.resolve({
                    json: () => Promise.resolve([{ candidature: { id: 1 } }])
                });
            }
            return Promise.reject(new Error("Unknown URL"));
        });

        await act(async () => {
            render(<MockAccueilGestionnaire />);
        });

        await waitFor(() => {
            const candidatsSansContrat = screen.getByTestId('notif-contrat');
            expect(candidatsSansContrat).toHaveTextContent('1');
        });
    });

});
