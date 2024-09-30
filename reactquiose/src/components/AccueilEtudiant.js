import React, { useState } from "react";
import { useLocation } from "react-router-dom";

function AccueilEtudiant() {
    const location = useLocation();
    const userData = location.state?.userData;

    const [showModal, setShowModal] = useState(false);
    const [file, setFile] = useState(null);
    const [fileData, setFileData] = useState("");

    const afficherAjoutCV = () => {
        setShowModal(true);
    };

    const fermerAffichageCV = () => {
        setShowModal(false);
    };

    // Fonction de gestion de changement de fichier
    const handleFileChange = (event) => {
        const uploadedFile = event.target.files[0];
        if (uploadedFile && uploadedFile.type === "application/pdf") {
            setFile(uploadedFile);

            // Lire le contenu du fichier comme base64
            const reader = new FileReader();
            reader.onload = (e) => {
                setFileData(e.target.result);
            };
            reader.readAsDataURL(uploadedFile);
        } else {
            alert("Veuillez déposer un fichier PDF uniquement.");
        }
    };

    // Fonction pour soumettre le fichier
    const handleSubmit = () => {
        if (file) {
            console.log("Nom du fichier:", file.name);
            console.log("Type de fichier:", file.type);
            console.log("Jour de remise:", new Date().toLocaleDateString());
            //console.log("Contenu du fichier (Base64):", fileData);
            console.log("État: en attente");

            setShowModal(false);
        } else {
            alert("Veuillez d'abord charger un fichier.");
        }
    };

    // Fonction pour afficher le fichier PDF dans un nouvel onglet
    const openFile = () => {
        if (fileData) {
            const pdfWindow = window.open();
            pdfWindow.document.write(
                `<iframe src="${fileData}" frameborder="0" style="border:0; top:0; left:0; bottom:0; right:0; width:100%; height:100%;" allowfullscreen></iframe>`
            );
        } else {
            alert("Aucun fichier à afficher !");
        }
    };

    return (
        <div className="container-fluid p-3">
            {userData && (
                <div className="alert alert-info">
                    <h5>Bienvenue, {userData.firstName} {userData.lastName} !</h5>
                    <p>Email : {userData.credentials.email}</p>
                    <p>Rôle : {userData.role}</p>
                </div>
            )}

            <div className="d-flex justify-content-start">
                <button className="btn btn-primary btn-lg rounded-pill" onClick={afficherAjoutCV}>
                    Ajouter un CV
                </button>
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
                                    <div className="mt-3">
                                        <p className="text-success">Fichier sélectionné : {file.name}</p>
                                        <p className="text-secondary">Taille : {file.size} bytes</p>
                                        <p className="text-secondary">Type : {file.type}</p>
                                    </div>
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

            {fileData && (
                <div className="mt-5 text-center">
                    <button className="btn btn-info" onClick={openFile}>
                        Voir le fichier PDF
                    </button>
                </div>
            )}
        </div>
    );
}

export default AccueilEtudiant;
