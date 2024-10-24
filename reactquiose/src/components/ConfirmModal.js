import React from 'react';
import '../CSS/ConfirmModal.css';

const ConfirmModal = ({ show, onClose, onConfirm, message }) => {
    if (!show) {
        return null;
    }

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <p>{message}</p>
                <div className="modal-buttons">
                    <button onClick={onConfirm} className="btn btn-primary">Confirmer</button>
                    <button onClick={onClose} className="btn btn-secondary">Annuler</button>
                </div>
            </div>
        </div>
    );
};

export default ConfirmModal;