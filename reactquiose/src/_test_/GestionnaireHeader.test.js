import { render, screen, fireEvent } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import GestionnaireHeader from '../components/GestionnaireHeader';

beforeEach(() => {
    localStorage.clear();
});

describe('GestionnaireHeader', () => {
    it('devrait changer la session correctement', () => {

        const onSendDataMock = jest.fn();
        render(
            <BrowserRouter>
                <GestionnaireHeader onSendData={onSendDataMock} />
            </BrowserRouter>
        );

        const selectElement = screen.getByRole('combobox');
        expect(selectElement.value).toBe("HIVER24");

        const newSession = "HIVER25";
        fireEvent.change(selectElement, { target: { value: newSession } });

        expect(selectElement.value).toBe(newSession);

        expect(localStorage.getItem('session')).toBe(newSession);

        expect(onSendDataMock).toHaveBeenCalledWith({
            session: newSession
        });
    });
});
