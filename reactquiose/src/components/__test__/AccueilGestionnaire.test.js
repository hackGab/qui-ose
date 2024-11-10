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
        global.fetch = jest.fn(() =>
            Promise.resolve({
                json: () => Promise.resolve(5)
            })
        );

        render(
            <BrowserRouter>
                <AccueilGestionnaire />
            </BrowserRouter>
        );

        const notificationBadge = await screen.findByText('5');

        expect(notificationBadge).toBeInTheDocument();
    });

    test("should not display notification badge when cvAttentes = 0", async () => {
        global.fetch = jest.fn(() =>
            Promise.resolve({
                json: () => Promise.resolve(0) // No notifications
            })
        );

        render(
            <BrowserRouter>
                <AccueilGestionnaire />
            </BrowserRouter>
        );

        // Check that no notification badge is displayed
        const notificationBadge = screen.queryByText('0');
        expect(notificationBadge).not.toBeInTheDocument();
    });
});
