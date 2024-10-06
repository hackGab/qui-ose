import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import EtudiantHeader from "./EtudiantHeader";

function AccueilEtudiant() {
    const location = useLocation();
    const userData = location.state?.userData;

    const [showModal, setShowModal] = useState(false);
    const [file, setFile] = useState(null);
    const [temporaryFile, setTemporaryFile] = useState(null);
    const [temporaryFileData, setTemporaryFileData] = useState(null);
    const [fileData, setFileData] = useState("");
    const [dragActive, setDragActive] = useState(false);
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
        console.log(temporaryFile);
        if (temporaryFile) {
            const donnesCV = {
                name: temporaryFile.name,
                type: temporaryFile.type,
                uploadDate: new Date(),
                data: temporaryFileData,
                status: "Confirme",
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
                    setFile(data);
                    setFileData(data.data);
                })
                .then(() => {
                    if (ancienId) {
                        const urlDestruction = `http://localhost:8080/cv/supprimerCV/${ancienId}`;

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

            <div className="text-center my-4" style={{ paddingTop: "80px" }}>
                {file ? (
                    <h2>Votre CV est chargé !</h2>
                ) : (
                    <h2 className="text-warning">Veuillez ajouter votre CV pour continuer.</h2>
                )}
            </div>

            <div className="d-flex justify-content-center my-3">
                <button
                    className={`btn btn-lg rounded-pill custom-btn ${
                        file == null ? 'btn-secondary' :
                            file.status === 'Attente' ? 'btn-warning' :
                                file.status === 'Confirme' ? 'btn-success' :
                                    file.status === 'Rejeté' ? 'btn-danger' : 'btn-primary'
                    }`}
                    onClick={afficherAjoutCV}
                    style={{ marginTop: "20px" }}
                >
                    {file == null ? 'Ajouter CV' :
                        file.status === 'Attente' ? 'CV en attente de confirmation' :
                            file.status === 'Confirme' ? 'CV Approuvé' :
                                file.status === 'Rejeté' ? 'CV Rejeté' : 'CV non remis'}
                </button>
            </div>

            {file && (
                <div className="d-flex justify-content-center my-3">
                    <button className="btn btn-info" onClick={() => openFile(fileData)}>
                        Voir mon CV
                    </button>
                </div>
            )}

            {file && file.status === 'Confirme' && (
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
                                                    <button className="btn btn-info"
                                                            onClick={() => openFile(internship.data)}>
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

            {showModal && (
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
                            backgroundColor: 'white',
                            padding: '20px',
                            borderRadius: '8px',
                            boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
                            zIndex: 1001,
                        }}
                    >
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
                                    border: "2px dashed #aaa",
                                    padding: "20px",
                                    cursor: "pointer",
                                    textAlign: "center",
                                }}
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
                                    <h6><strong>Date de remise :</strong> {new Date().toLocaleDateString()}</h6>
                                </div>
                            )}
                        </div>
                        <div className="modal-footer">
                            <button className="btn btn-primary" onClick={handleSubmit}>
                                Soumettre
                            </button>
                            <button type="button" className="btn btn-secondary" onClick={fermerAffichageCV}>
                                Fermer
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default AccueilEtudiant;
