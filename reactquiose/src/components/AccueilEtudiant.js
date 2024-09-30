import React, { useState } from "react";

function AccueilEtudiant() {
    const [showModal, setShowModal] = useState(false);

    const afficherAjoutCV = () => {
        setShowModal(true);
    };

    const fermerAffichageCV = () => {
        setShowModal(false);
    };

    return (
        <div className="container-fluid p-3">
            {/* Bouton en haut à gauche avec les classes Bootstrap */}
            <div className="d-flex justify-content-start">
                <button className="btn btn-primary btn-lg rounded-pill" onClick={afficherAjoutCV}>
                    Bouton Visuel
                </button>
            </div>
            <div className="text-center mt-5">
                <h1>Bienvenue sur la page d'accueil étudiant</h1>
            </div>

            {/* Modal Bootstrap */}
            {showModal && (
                <div className="modal show d-block" tabIndex="-1" role="dialog">
                    <div className="modal-dialog" role="document">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">Message</h5>
                            </div>
                            <div className="modal-body">
                                <p>Allo</p>
                            </div>
                            <div className="modal-footer">
                                <button type="button" className="btn btn-secondary" onClick={fermerAffichageCV}>
                                    Fermer
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default AccueilEtudiant;
