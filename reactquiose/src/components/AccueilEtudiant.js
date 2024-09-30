import React, { useState } from "react";

function AccueilEtudiant() {
    const [showModal, setShowModal] = useState(false);
    const [file, setFile] = useState(null);

    const afficherAjoutCV = () => {
        setShowModal(true);
    };

    const fermerAffichageCV = () => {
        setShowModal(false);
    };

    const handleFileChange = (event) => {
        const uploadedFile = event.target.files[0];
        if (uploadedFile && uploadedFile.type === "application/pdf") {
            setFile(uploadedFile);
            console.log("Fichier PDF chargé :", uploadedFile);
        } else {
            alert("Veuillez déposer un fichier PDF uniquement.");
        }
    };

    const handleSubmit = () => {
        if (file) {
            // Logique pour envoyer le fichier au backend ou le stocker
            console.log("Fichier prêt à être soumis :", file);
            // Réinitialiser après l'envoi
            setFile(null);
            setShowModal(false);
        } else {
            alert("Veuillez d'abord charger un fichier.");
        }
    };

    return (
        <div className="container-fluid p-3">
            {/* Bouton en haut à gauche avec les classes Bootstrap */}
            <div className="d-flex justify-content-start">
                <button className="btn btn-primary btn-lg rounded-pill" onClick={afficherAjoutCV}>
                    Ajouter un CV
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
                                <h5 className="modal-title">Manipuler Curriculum Vitae</h5>
                                <button type="button" className="close" onClick={fermerAffichageCV}>
                                    <span>&times;</span>
                                </button>
                            </div>
                            <div className="modal-body">
                                <p>Déposez votre fichier PDF ci-dessous :</p>
                                <input
                                    type="file"
                                    className="form-control-file"
                                    accept="application/pdf"
                                    onChange={handleFileChange}
                                />
                                {file && (
                                    <p className="mt-3 text-success">
                                        Fichier sélectionné : {file.name}
                                    </p>
                                )}
                            </div>
                            <div className="modal-footer">
                                <button type="button" className="btn btn-secondary" onClick={fermerAffichageCV}>
                                    Fermer
                                </button>
                                <button type="button" className="btn btn-primary" onClick={handleSubmit}>
                                    Soumettre
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
