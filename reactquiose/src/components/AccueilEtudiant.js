import React, { useState } from "react";
import { useLocation } from "react-router-dom"; // Importer useLocation

function AccueilEtudiant() {
    const location = useLocation();
    const userData = location.state?.userData;

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
        } else {
            alert("Veuillez déposer un fichier PDF uniquement.");
        }
    };

    const handleSubmit = () => {
        if (file) {
            console.log("Fichier prêt à être soumis :", file);

            setFile(null);
            setShowModal(false);
        } else {
            alert("Veuillez d'abord charger un fichier.");
        }
    };

    return (
        <div className="container-fluid p-3">
            {userData && (
                <div className="alert alert-info">
                    <h5>Bienvenue, {userData.nom} !</h5>
                    <p>Email : {userData.credentials.email}</p>
                    <p>Rôle : {userData.role}</p>
                </div>
            )}

            <div className="d-flex justify-content-start">
                <button className="btn btn-primary btn-lg rounded-pill" onClick={afficherAjoutCV}>
                    Ajouter un CV
                </button>
            </div>
            <div className="text-center mt-5">
                <h1>Bienvenue sur la page d'accueil étudiant</h1>
            </div>

            {showModal && (
                <div className="modal show d-block" tabIndex="-1" role="dialog">
                    <div className="modal-dialog" role="document">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">Manipuler Curriculum Vitae</h5>
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
