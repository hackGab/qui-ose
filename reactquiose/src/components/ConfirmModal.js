import React from 'react';
import { useTranslation } from "react-i18next";

const ConfirmModal = ({ show, onClose, onConfirm, message }) => {
    const { t } = useTranslation();

    if (!show) {
        return null;
    }

    return (
        <div
            style={{
                position: 'fixed',
                top: 0,
                left: 0,
                width: '100%',
                height: '100%',
                backgroundColor: 'rgba(0, 0, 0, 0.5)',
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                zIndex: 1000,
            }}
        >
            <div
                style={{
                    backgroundColor: '#fff',
                    padding: '2rem',
                    borderRadius: '8px',
                    boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
                    width: '400px',
                    textAlign: 'center',
                    animation: 'fadeIn 0.3s ease-in-out',
                }}
            >
                <h2 style={{ fontSize: '1.5rem', fontWeight: 'bold', marginBottom: '1rem' }}>{message}</h2>
                <div style={{ display: 'flex', gap: '1rem', justifyContent: 'space-evenly'} }>
                    <button
                        onClick={onConfirm}
                        style={{
                            backgroundColor: '#28a745',
                            color: 'white',
                            padding: '0.5rem 1.5rem',
                            borderRadius: '4px',
                            border: 'none',
                            fontSize: '1rem',
                            cursor: 'pointer',
                            transition: 'background-color 0.3s ease',
                        }}
                    >
                        {t('confirm')}
                    </button>
                    <button
                        onClick={onClose}
                        style={{
                            backgroundColor: '#dc3545',
                            color: 'white',
                            padding: '0.5rem 1.5rem',
                            borderRadius: '4px',
                            border: 'none',
                            fontSize: '1rem',
                            cursor: 'pointer',
                            transition: 'background-color 0.3s ease',
                        }}
                    >
                        {t('annuler')}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default ConfirmModal;
