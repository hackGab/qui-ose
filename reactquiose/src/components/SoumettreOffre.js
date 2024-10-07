import React, {useEffect, useState} from "react";
import {useLocation} from "react-router-dom";
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
    const [titre, setTitre] = useState("");
    const [localisation, setLocalisation] = useState("");
    const [nbCandidats, setNbCandidats] = useState(0);
    const [dateLimite, setDateLimite] = useState("");
    const [datePublication, setPublication] = useState("");

    useEffect(() => {
    }, [employeurEmail]);

    const afficherAjoutOffre = () => {
        setShowModal(true);
        setTemporaryFile(file ? {...file} : null);
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
            handleFileChange({target: {files: [uploadedFile]}});
        }
    };

    const handleSubmit = () => {
        if (temporaryFile) {
            const donnesOffre = {
                titre,
                localisation,
                nbCandidats,
                dateLimite,
                datePublication: new Date(),
                name: temporaryFile.name,
                type: temporaryFile.type,
                data: temporaryFileData,
                status: "Attente",
            };
            console.log("Données de l'offre:", donnesOffre);
            const urlAjout = `http://localhost:8081/offreDeStage/creerOffreDeStage/${employeurEmail}`;
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
            <EmployeurHeader/>

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


            {showModal && (
                <div className="custom-modal-overlay">
                    <div className="modal modal-custom" tabIndex="-1" role="dialog">
                        <div className="modal-dialog" role="document">
                            <div className="modal-content">
                                <div className="modal-header">
                                    <h5 className="modal-title"><b>Soumettre une offre d'emploi</b></h5>
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
                                            style={{display: "none"}}
                                        />
                                    </div>

                                    <div className="form-group mt-3">
                                        <label htmlFor="titre"><b>Titre de l'offre</b></label>
                                        <input
                                            type="text"
                                            className="form-control"
                                            id="titre"
                                            style={{ textAlign: "center" }}
                                            value={titre}
                                            onChange={(e) => setTitre(e.target.value)}
                                        />
                                    </div>

                                    <div className="form-group mt-3">
                                        <label htmlFor="localisation"><b>Localisation</b></label>
                                        <input
                                            type="text"
                                            className="form-control"
                                            id="localisation"
                                            style={{ textAlign: "center" }}
                                            value={localisation}
                                            onChange={(e) => setLocalisation(e.target.value)}
                                        />
                                    </div>

                                    <div className="form-group mt-3">
                                        <label htmlFor="nbCandidats"><b>Nombre de candidats</b></label>
                                        <input
                                            type="number"
                                            className="form-control"
                                            style={{ textAlign: "center" }}
                                            id="nbCandidats"
                                            value={nbCandidats}
                                            onChange={(e) => setNbCandidats(e.target.value)}
                                        />
                                    </div>

                                    <div className="form-group mt-3">
                                        <label htmlFor="dateLimite"><b>Date limite</b></label>
                                        <input
                                            type="date"
                                            className="form-control"
                                            id="dateLimite"
                                            style={{ textAlign: "center" }}
                                            value={dateLimite}
                                            onChange={(e) => setDateLimite(e.target.value)}
                                        />
                                    </div>

                                    {temporaryFile && (
                                        <div className="file-details mt-3">
                                            <h6><strong>Nom du fichier :</strong> {temporaryFile.name}</h6>
                                            <h6><strong>Type du fichier :</strong> {temporaryFile.type}</h6>
                                            <h6><strong>Date de soumission :</strong> {new Date().toLocaleDateString()}
                                            </h6>
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
