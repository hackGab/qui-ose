import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import EtudiantHeader from "./EtudiantHeader";
import "../CSS/AccueilEtudiant.css";
import {useTranslation} from "react-i18next";

function AccueilEtudiant() {
    const location = useLocation();
    const userData = location.state?.userData;

    const [showModal, setShowModal] = useState(false);
    const [file, setFile] = useState(null);
    const [temporaryFile, setTemporaryFile] = useState(null);
    const [temporaryFileData, setTemporaryFileData] = useState(null);
    const [fileData, setFileData] = useState("");
    const [dragActive, setDragActive] = useState(false);
    const [rejectionMessage, setRejectionMessage] = useState("");
    const {t} = useTranslation();
    const [internships, setInternships] = useState([
        {
            titre: "Stage 1",
            localisation: "Paris",
            dateLimite: new Date(),
            datePublication: new Date(),
            data: "Yessir",
            nbCandidats: 10
        },
        {
            titre: "Stage 2",
            localisation: "Lyon",
            dateLimite: new Date(),
            datePublication: new Date(),
            data: "No Mane",
            nbCandidats: 20
        },
        {
            titre: "Stage 3",
            localisation: "Marseille",
            dateLimite: new Date(),
            datePublication: new Date(),
            data: "Medium mane",
            nbCandidats: 5
        },
        {
            titre: "Stage 4",
            localisation: "Toulouse",
            dateLimite: new Date(),
            datePublication: new Date(),
            data: "Big Mane",
            nbCandidats: 50
        }
    ]);

    useEffect(() => {
        if (userData) {
            const url = `http://localhost:8081/etudiant/credentials/${userData.credentials.email}`;

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
                        console.log('CV:', data.cv.rejetMessage);

                        if (data.cv.status === 'rejeté') {
                            setRejectionMessage(data.cv.rejetMessage || "Le CV a été rejeté sans raison spécifiée.");
                        } else {
                            setRejectionMessage("");
                        }
                    }

                    // Récupération des stages
                    const internshipsUrl = `http://localhost:8081/etudiant/stages/${userData.credentials.email}`;
                    fetch(internshipsUrl)
                        .then((response) => {
                            if (!response.ok) {
                                throw new Error(`Erreur lors de la requête: ${response.status}`);
                            }
                            return response.json();
                        })
                        .then((data) => {
                            setInternships(data);
                        })
                        .catch((error) => {
                            console.error('Erreur lors de la récupération des stages:', error);
                        });
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
                setTemporaryFileData(e.target.result);
            };
            reader.readAsDataURL(uploadedFile);
        } else {
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
        } else {
        }
    };

    const handleSubmit = () => {
        if (temporaryFile) {
            const donnesCV = {
                name: temporaryFile.name,
                type: temporaryFile.type,
                uploadDate: new Date(),
                data: temporaryFileData,
                status: "Attente",
            };

            const urlAjout = `http://localhost:8081/cv/creerCV/${userData.credentials.email}`;
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
                    setFile(data);
                    setFileData(data.data);
                    console.log("Réponse du serveur:", data);
                })
                .then(() => {
                    if (ancienId) {
                        const urlDestruction = `http://localhost:8081/cv/supprimerCV/${ancienId}`;

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
        } else {
        }
    };

    const openFile = (data) => {
        if (data) {
            const pdfWindow = window.open();
            pdfWindow.document.write(
                `<iframe src="${data}" style="border:0; top:0; left:0; bottom:0; right:0; width:100%; height:100%;" allowfullscreen></iframe>`
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
            <EtudiantHeader />

            <div className="text-center my-4">
                {file ? (
                    <h2>{file == null ? 'Ajouter CV' :
                        file.status === 'Attente' ? t('cvPending') :
                            file.status === 'validé' ? t('cvApproved') :
                                file.status === 'rejeté' ? t('cvRejected') : t('cvRefused')}</h2>
                ) : (
                    <h2 className="text-warning">{t('pleaseAddCV')}</h2>
                )}
            </div>

            {rejectionMessage && (
                <div className="alert alert-danger text-center error-text" style={{ fontSize: "1.25rem" }}>
                    <h5>{t('rejectionReason')}</h5>
                    <p>{rejectionMessage}</p>
                </div>
            )}

            <div className="d-flex justify-content-center my-3">
                <button
                    className={`btn btn-lg rounded-top-pill custom-btn ${file == null ? 'btn-secondary' :
                        file.status === 'Attente' ? 'btn-warning' :
                            file.status === 'validé' ? 'btn-success' :
                                file.status === 'rejeté' ? 'btn-danger' : 'btn-primary'}`}
                    onClick={afficherAjoutCV}
                    style={{ width: "10em"}}
                > {t('uploadCV')}
                </button>
            </div>

            {file && (
                <div className="d-flex justify-content-center mt-3 mb-4">
                    <button className="btn btn-lg rounded-bottom-pill custom-btn btn-info" onClick={() => openFile(fileData)} style={{ width: "10em"}}>
                        {t('viewMyCV')}
                    </button>
                </div>
            )}

            <hr style={{ width: "45em", margin: "auto", borderWidth: "0.2em"}}/>

            <div className="text-center my-5">
                {file && file.status === 'validé' && (
                    <div className="text-center my-4" style={{ marginTop: "100px" }}>
                        <h3>Stages</h3>
                        <div
                            className="d-flex flex-wrap justify-content-center"
                            style={{ maxHeight: "400px", overflowY: "scroll" }}
                        >
                            {internships.length > 0 ? (
                                <div className="row w-100">
                                    {internships.map((internship, index) => (
                                        <div
                                            key={index}
                                            className="col-lg-4 col-md-6 col-sm-12 p-2"
                                        >
                                            <div className="card my-3 h-100">
                                                <div className="card-body">
                                                    <h5 className="card-title">{internship.titre}</h5>
                                                    <h6 className="card-subtitle mb-2 text-muted">
                                                        {internship.localisation}
                                                    </h6>
                                                    <p className="card-text">
                                                        <strong>Date limite de candidature:</strong>{" "}
                                                        {internship.dateLimite.toLocaleDateString()}
                                                    </p>
                                                    <p className="card-text">
                                                        <strong>Date de publication:</strong>{" "}
                                                        {internship.datePublication.toLocaleDateString()}
                                                    </p>
                                                    <div className="d-flex justify-content-center my-3">
                                                        <button className="btn btn-info" onClick={() => openFile(internship.data)}>
                                                            Voir candidature
                                                        </button>
                                                    </div>
                                                    <p className="card-text">
                                                        <strong>Nombre de candidats:</strong> {internship.nbCandidats}
                                                    </p>
                                                </div>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            ) : (
                                <p>Aucun stage à afficher.</p>
                            )}
                        </div>
                    </div>
                )}
            </div>

            {showModal && (
                <div className="custom-modal-overlay">
                    <div className="modal modal-custom" tabIndex="-1" role="dialog">
                        <div className="modal-dialog" role="document">
                            <div className="modal-content">
                                <div className="modal-header">
                                    <h5 className="modal-title">{t('manipulateCV')}</h5>
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
                                        <p>{t('dragOrClick')}</p>
                                        <input
                                            type="file"
                                            id="fileInput"
                                            onChange={handleFileChange}
                                            style={{display: "none"}}
                                        />
                                    </div>

                                    {temporaryFile && (
                                        <div className="file-details mt-3">
                                            <h6><strong>{t('fileName')}</strong> {temporaryFile.name}</h6>
                                            <h6><strong>{t('fileType')}</strong> {temporaryFile.type}</h6>
                                            <h6><strong>{t('fileDate')}</strong> {new Date().toLocaleDateString()}</h6>
                                        </div>
                                    )}
                                </div>
                                <div className="modal-footer">
                                    <button className="btn btn-primary" onClick={handleSubmit}>
                                        {t('submit')}
                                    </button>
                                    <button type="button" className="btn btn-secondary" onClick={fermerAffichageCV}>
                                        {t('close')}
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
