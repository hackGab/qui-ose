import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import EmployeurHeader from "./EmployeurHeader";
import "../CSS/SoumettreOffre.css";

function SoumettreOffre() {
    const location = useLocation();
    const employeurEmail = location.state?.employeurEmail; // Récupérer l'email du state
    const [showModal, setShowModal] = useState(false);
    const [file, setFile] = useState(null);
    const [temporaryFile, setTemporaryFile] = useState(null);
    const [temporaryFileData, setTemporaryFileData] = useState(null);
    const [fileData, setFileData] = useState("");
    const [dragActive, setDragActive] = useState(false);
    const [internships, setInternships] = useState([]);

    useEffect(() => {
        console.log("Email de l'employeur:", employeurEmail);
    }, [employeurEmail]);

    const afficherAjoutOffre = () => {
        console.log("Affichage de l'ajout d'offre");
        setShowModal(true);
        setTemporaryFile(file ? { ...file } : null);
    };

    const fermerAffichageOffre = () => {
        setShowModal(false);
        setTemporaryFile(null);
    };

    const handleFileChange = (event) => {
        const uploadedFile = event.target.files[0];
        if (uploadedFile && uploadedFile.type === "application/pdf") {
            setTemporaryFile(uploadedFile);
            const reader = new FileReader();
            reader.onload = (e) => {
                setTemporaryFileData(e.target.result);
            };
            reader.readAsDataURL(uploadedFile);
        }
    };

    const handleDrag = (event) => {
        event.preventDefault();
        event.stopPropagation();
        if (event.type === "dragenter" || event.type === "dragover") {
            setDragActive(true);
        } else if (event.type === "dragleave") {
            setDragActive(false);
        }
    };

    const handleDrop = (event) => {
        event.preventDefault();
        event.stopPropagation();
        setDragActive(false);
        const uploadedFile = event.dataTransfer.files[0];
        if (uploadedFile && uploadedFile.type === "application/pdf") {
            handleFileChange({ target: { files: [uploadedFile] } });
        }
    };

    const handleSubmit = () => {
        if (temporaryFile) {
            const donnesOffre = {
                name: temporaryFile.name,
                type: temporaryFile.type,
                uploadDate: new Date(),
                data: temporaryFileData,
                status: "Attente",
            };

            const urlAjout = `http://localhost:8080/offre/creerOffre/${employeurEmail}`;
            let ancienId = file ? file.id : null;

            fetch(urlAjout, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(donnesOffre),
            })
                .then((response) => {
                    if (!response.ok) {
                        throw new Error(`Erreur lors de la requête: ${response.status}`);
                    }
                    return response.json();
                })
                .then((data) => {
                    setFile(data);
                    setFileData(data.data);
                    console.log("Réponse du serveur:", data);
                })
                .then(() => {
                    if (ancienId) {
                        const urlDestruction = `http://localhost:8080/offre/supprimerOffre/${ancienId}`;

                        fetch(urlDestruction, {
                            method: "DELETE",
                            headers: {
                                "Content-Type": "application/json",
                            },
                        }).catch((error) => {
                            console.error("Erreur:", error);
                        });
                    }
                })
                .catch((error) => {
                    console.error("Erreur:", error);
                });

            setShowModal(false);
            setTemporaryFile(null);
        }
    };

    const openFile = () => {
        if (file) {
            const pdfWindow = window.open();
            pdfWindow.document.write(
                `<iframe src="${fileData}" style="border:0; top:0; left:0; bottom:0; right:0; width:100%; height:100%;" allowfullscreen></iframe>`
            );
        } else {
            alert("Aucun fichier à afficher !");
        }
    };

    const handleClick = () => {
        document.getElementById("fileInput").click();
    };

    return (
         <div className="container-fluid p-4">
             <EmployeurHeader />

             <div className="text-center my-4">
                 {file ? (
                    <h2>Votre offre d'emploi est chargée !</h2>
                ) : (
                    <h2 className="text-warning">Veuillez ajouter votre offre d'emploi pour continuer.</h2>
                )}
            </div>

            <div className="d-flex justify-content-center my-3">
                <button
                    className={`btn btn-lg rounded-pill custom-btn ${file == null ? 'btn-secondary' :
                        file.status === 'Attente' ? 'btn-warning' :
                            file.status === 'Approuvé' ? 'btn-success' :
                                file.status === 'Rejeté' ? 'btn-danger' : 'btn-primary'}`}
                    onClick={afficherAjoutOffre}
                >
                    {file == null ? 'Ajouter Offre' :
                        file.status === 'Attente' ? 'Offre en attente de confirmation' :
                            file.status === 'Approuvé' ? 'Offre Approuvée' :
                                file.status === 'Rejeté' ? 'Offre Rejetée' : 'Offre non remise'}
                </button>
            </div>

            {file && (
                <div className="d-flex justify-content-center my-3">
                    <button className="btn btn-info" onClick={openFile}>
                        Voir mon offre d'emploi
                    </button>
                </div>
            )}

            <div className="text-center my-4">
                <h3>Stages</h3>
                <div className="d-flex justify-content-center">
                    {internships.length > 0 ? (
                        <ul className="list-unstyled">
                            {internships.map((internship, index) => (
                                <li key={index}>
                                    {internship.title} - {internship.company} ({internship.duration})
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p>Aucun stage à afficher.</p>
                    )}
                </div>
            </div>

            {showModal && (
                <div className="custom-modal-overlay">
                    <div className="modal modal-custom" tabIndex="-1" role="dialog">
                        <div className="modal-dialog" role="document">
                            <div className="modal-content">
                                <div className="modal-header">
                                    <h5 className="modal-title">Soumettre une offre d'emploi</h5>
                                </div>
                                <div className="modal-body">
                                    <div
                                        onDragEnter={handleDrag}
                                        onDragOver={handleDrag}
                                        onDragLeave={handleDrag}
                                        onDrop={handleDrop}
                                        onClick={handleClick}
                                        className={`drop-zone ${dragActive ? "active" : ""}`}
                                    >
                                        <p>Déposez votre fichier PDF ici ou cliquez pour le télécharger.</p>
                                        <input
                                            type="file"
                                            id="fileInput"
                                            onChange={handleFileChange}
                                            style={{ display: "none" }}
                                        />
                                    </div>

                                    {temporaryFile && (
                                        <div className="file-details mt-3">
                                            <h6><strong>Nom du fichier :</strong> {temporaryFile.name}</h6>
                                            <h6><strong>Type du fichier :</strong> {temporaryFile.type}</h6>
                                            <h6><strong>Date de soumission :</strong> {new Date().toLocaleDateString()}</h6>
                                        </div>
                                    )}
                                </div>
                                <div className="modal-footer">
                                    <button className="btn btn-primary" onClick={handleSubmit}>
                                        Soumettre
                                    </button>
                                    <button type="button" className="btn btn-secondary" onClick={fermerAffichageOffre}>
                                        Fermer
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default SoumettreOffre;
