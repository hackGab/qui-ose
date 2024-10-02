import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";

function AccueilEtudiant() {
    const location = useLocation();
    const userData = location.state?.userData;

    const [showModal, setShowModal] = useState(false);
    const [file, setFile] = useState(null);
    const [temporaryFile, setTemporaryFile] = useState(null);
    const [fileData, setFileData] = useState("");
    const [dragActive, setDragActive] = useState(false);

    useEffect(() => {
        if (userData) {
            const url = `http://localhost:8080/etudiant/credentials/${userData.credentials.email}`;

            fetch(url)
                .then((response) => {
                    if (!response.ok) {
                        throw new Error(`Erreur lors de la requête: ${response.status}`);
                    }
                    return response.json();
                })
                .then((data) => {
                    if (data.cv) {
                        setFile(data.cv);
                        setFileData(data.cv.data);
                        console.log('Réponse du serveur:', data);
                    } else {
                        alert("VEUILLEZ AJOUTER VOTRE CV");
                    }
                })
                .catch((error) => {
                    console.error('Erreur:', error);
                });
        }
    }, [userData]);

    const afficherAjoutCV = () => {
        setShowModal(true);
        setTemporaryFile(file ? { ...file } : null);
    };

    const fermerAffichageCV = () => {
        setShowModal(false);
        setTemporaryFile(null);
    };

    const handleFileChange = (event) => {
        const uploadedFile = event.target.files[0];
        if (uploadedFile && uploadedFile.type === "application/pdf") {
            setTemporaryFile(uploadedFile);
            const reader = new FileReader();
            reader.onload = (e) => {
                setFileData(e.target.result);
            };
            reader.readAsDataURL(uploadedFile);
        } else {
            alert("Veuillez déposer un fichier PDF uniquement.");
        }
    };

    // Gestion du drag-and-drop
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
        } else {
            alert("Veuillez déposer un fichier PDF uniquement.");
        }
    };

    const handleSubmit = () => {
        if (temporaryFile) {
            const donnesCV = {
                name: temporaryFile.name,
                type: temporaryFile.type,
                uploadDate: new Date(),
                data: fileData,
                status: "Attente",
            };

            const urlAjout = `http://localhost:8080/cv/creerCV/${userData.credentials.email}`;

            let ancienId = file ? file.id : null;

            fetch(urlAjout, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(donnesCV),
            })
                .then((response) => {
                    if (!response.ok) {
                        throw new Error(`Erreur lors de la requête: ${response.status}`);
                    }
                    return response.json();
                })
                .then((data) => {
                    alert("CV envoyé avec succès !");
                    setFile(data);
                    setFileData(data.data);
                    console.log("Réponse du serveur:", data);
                })
                .then(() => {
                    if (ancienId) {
                        const urlDestruction = `http://localhost:8080/cv/supprimerCV/${ancienId}`;

                        fetch(urlDestruction, {
                            method: "DELETE",
                            headers: {
                                "Content-Type": "application/json",
                            },
                        })
                            .catch((error) => {
                                console.error("Erreur:", error);
                            });
                    }
                })
                .catch((error) => {
                    alert(`Erreur lors de l'envoi: ${error.message}`);
                    console.error("Erreur:", error);
                });

            setShowModal(false);
            setTemporaryFile(null);
        } else {
            alert("Veuillez d'abord charger un fichier.");
        }
    };

    const openFile = () => {
        if (file) {
            const pdfWindow = window.open();
            pdfWindow.document.write(
                `<iframe src="${fileData}" frameborder="0" style="border:0; top:0; left:0; bottom:0; right:0; width:100%; height:100%;" allowfullscreen></iframe>`
            );
        } else {
            alert("Aucun fichier à afficher !");
        }
    };

    const handleClick = () => {
        document.getElementById("fileInput").click();
    };

    return (
        <div className="container-fluid p-3" style={{ minHeight: "100vh", display: "flex", flexDirection: "column", paddingTop: "10px" }}>
            <nav className="navbar navbar-expand-lg navbar-light bg-info mb-4" style={{ width: "100%" }}>
                <div className="container-fluid">
                    <span className="navbar-brand mb-0 h1">
                        {userData ? `Bienvenu(e), ${userData.firstName} ${userData.lastName}` : "Bienvenue"}
                    </span>
                </div>
            </nav>

            <div className="d-flex justify-content-between align-items-center" style={{ marginTop: "10px" }}>
                <button
                    className={`btn btn-lg rounded-pill custom-btn ${file == null ? 'btn-secondary' :
                        file.status === 'Attente' ? 'btn-warning' :
                            file.status === 'Aprouvé' ? 'btn-success' :
                                file.status === 'Rejeté' ? 'btn-danger' : 'btn-primary'}`}
                    onClick={afficherAjoutCV}
                >
                    {file == null ? 'Ajouter CV' :
                        file.status === 'Attente' ? 'CV en attente' :
                            file.status === 'Aprouvé' ? 'CV Approuvé' :
                                file.status === 'Rejeté' ? 'CV Rejeté' : 'CV non remis'}
                </button>

                {file && (
                    <button className="btn btn-info" onClick={openFile}>
                        Voir mon CV
                    </button>
                )}
            </div>

            {showModal && (
                <div className="custom-modal-overlay">
                    <div className="modal show d-block modal-custom" tabIndex="-1" role="dialog">
                        <div className="modal-dialog modal-dialog-centered" role="document">
                            <div className="modal-content">
                                <div className="modal-header">
                                    <h5 className="modal-title">Manipuler Curriculum Vitae</h5>
                                </div>
                                <div className="modal-body">
                                    <div
                                        onDragEnter={handleDrag}
                                        onDragOver={handleDrag}
                                        onDragLeave={handleDrag}
                                        onDrop={handleDrop}
                                        onClick={handleClick}
                                        style={{
                                            border: dragActive ? "2px dashed #00aaff" : "2px dashed #ddd",
                                            borderRadius: "10px",
                                            padding: "40px",
                                            textAlign: "center",
                                            cursor: "pointer",
                                            backgroundColor: dragActive ? "#f0f8ff" : "#fafafa",
                                            transition: "background-color 0.3s ease",
                                        }}
                                    >
                                        <p>Déposez votre fichier PDF ici ou cliquez pour le télécharger.</p>
                                        <input
                                            type="file"
                                            id="fileInput"
                                            className="form-control-file"
                                            accept="application/pdf"
                                            onChange={handleFileChange}
                                            style={{ display: "none" }}
                                        />
                                    </div>
                                    {temporaryFile && (
                                        <div className="mt-3">
                                            <p className="text-success">Fichier sélectionné : {temporaryFile.name}</p>
                                            <p className="text-secondary">Type : {temporaryFile.type}</p>
                                            <p className="text-secondary">Date : {temporaryFile.uploadDate ? temporaryFile.uploadDate : "Non remis"}</p>
                                            <p className="text-secondary">Status : {temporaryFile.status ? temporaryFile.status : "Non remis"}</p>
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
                </div>
            )}
        </div>
    );
}

export default AccueilEtudiant;
