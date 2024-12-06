import {render, screen, fireEvent, waitFor} from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import {BrowserRouter, MemoryRouter, Route, Routes, useLocation} from 'react-router-dom';
import GestionnaireHeader from '../GestionnaireHeader';
import i18n from '../../../utils/i18n/i18n';
import ListeEtudiants from "../../Liste/ListeEtudiants";
import ListeEmployeurs from "../../Liste/ListeEmployeurs";
import {hardCodedSessions} from "../../../utils/variables/hardCodedSessions";
import {calculateNextSessions} from "../../../utils/methodes/dateUtils";


describe('GestionnaireHeader', () => {

    beforeEach(() => {
        localStorage.clear();
        localStorage.setItem('session', calculateNextSessions());
    });

    const verfiySession = jest.fn();

    it('ne devrait pas afficher le filtre si le path est /listeProfesseurs ou /listeEtudiants', () => {
        render(
            <MemoryRouter initialEntries={['/listeProfesseurs']}>
                <GestionnaireHeader  onSendData={verfiySession}/>
            </MemoryRouter>
        );
        expect(screen.queryByRole('combobox')).toBeNull();
    });

    it('ne devrait pas afficher le filtre si le path est /listeEtudiants', () => {
        render(
            <MemoryRouter initialEntries={['/listeEtudiants']}>
                <GestionnaireHeader  onSendData={verfiySession}/>
            </MemoryRouter>
        );
        expect(screen.queryByRole('combobox')).toBeNull();
    });

    it('devrait afficher le filtre si le path n\'est pas /listeProfesseurs ou /listeEtudiants', () => {
        render(
            <MemoryRouter initialEntries={['/listeEmployeurs']}>
                <GestionnaireHeader  onSendData={verfiySession}/>
            </MemoryRouter>
        );
        expect(screen.getByTestId('filtreSession')).toBeInTheDocument();
    });


    it('devrait changer la session correctement', () => {

        const onSendDataMock = jest.fn();
        render(
            <MemoryRouter initialEntries={['/listeEmployeurs']}>
                <GestionnaireHeader  onSendData={onSendDataMock}/>
            </MemoryRouter>
        );

        const selectElement = screen.getByRole('combobox');

        fireEvent.click(selectElement);
        for (let i = 0 ; i < hardCodedSessions.length; i++) {
            expect(screen.getByText(hardCodedSessions[i].label)).toBeInTheDocument();
        }

        expect(selectElement.value).toBe("HIVER25");

        const newSession = "AUTOMNE25";
        fireEvent.change(selectElement, { target: { value: newSession } });

        expect(selectElement.value).toBe(newSession);

        expect(localStorage.getItem('session')).toBe(newSession);

        expect(onSendDataMock).toHaveBeenCalledWith({
            session: newSession
        });
    });
});
