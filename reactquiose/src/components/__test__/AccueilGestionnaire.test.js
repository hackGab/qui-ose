import { render, screen } from "@testing-library/react";
import AccueilGestionnaire from "../AccueilGestionnaire";
import { BrowserRouter } from "react-router-dom";
import { useLocation } from 'react-router-dom';

jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useLocation: jest.fn()
}));

describe("AccueilGestionnaire Notifications", () => {
    beforeEach(() => {
        useLocation.mockReturnValue({
            state: { userData: { firstName: "John", lastName: "Doe" } }
        });
    });

    test("should display notification badge when cvAttentes > 0", async () => {
        global.fetch = jest.fn()
            .mockResolvedValueOnce({
                json: () => Promise.resolve(3)
            })
            .mockResolvedValueOnce({
                json: () => Promise.resolve(7)
            });

        render(
            <BrowserRouter>
                <AccueilGestionnaire />
            </BrowserRouter>
        );

        const notificationBadgeOffre = await screen.findByText('3');
        expect(notificationBadgeOffre).toBeInTheDocument();
    });

    test("should not display notification badge when cvAttentes = 0", async () => {
        global.fetch = jest.fn(() =>
            Promise.resolve({
                json: () => Promise.resolve(0)
            })
        );

        render(
            <BrowserRouter>
                <AccueilGestionnaire />
            </BrowserRouter>
        );

        const notificationBadge = screen.queryByText('0');
        expect(notificationBadge).not.toBeInTheDocument();
    });

    test("should display notification badge for offres de stage when offresAttentes > 0", async () => {
        global.fetch = jest.fn()
            .mockResolvedValueOnce({
                json: () => Promise.resolve(3)
            })
            .mockResolvedValueOnce({
                json: () => Promise.resolve(7)
            });

        render(
            <BrowserRouter>
                <AccueilGestionnaire />
            </BrowserRouter>
        );

        const notificationBadgeOffre = await screen.findByText('7');
        expect(notificationBadgeOffre).toBeInTheDocument();
    });

    test("should not display notification badge for offres de stage when offresAttentes = 0", async () => {
        global.fetch = jest.fn()
            .mockResolvedValueOnce({
                json: () => Promise.resolve(3)
            })
            .mockResolvedValueOnce({
                json: () => Promise.resolve(0)
            });

        render(
            <BrowserRouter>
                <AccueilGestionnaire />
            </BrowserRouter>
        );

        const notificationBadgeOffre = screen.queryByText('0');
        expect(notificationBadgeOffre).not.toBeInTheDocument();
    });
});
