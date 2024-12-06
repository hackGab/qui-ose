import React, {useEffect, useState} from "react";
import {useLocation} from "react-router-dom";
import EmployeurHeader from "./Header/EmployeurHeader";
import "../CSS/SoumettreOffre.css";
import {useTranslation} from "react-i18next";


function SoumettreOffre() {
    const location = useLocation();
    const userData = location.state?.userData;
    const employeurEmail = userData.credentials.email;
    const [showModal, setShowModal] = useState(false);
    const [file, setFile] = useState(null);
    const [temporaryFile, setTemporaryFile] = useState(null);
    const [temporaryFileData, setTemporaryFileData] = useState(null);
    const [fileData, setFileData] = useState("");
    const [dragActive, setDragActive] = useState(false);
    const [titre, setTitre] = useState("");
    const [localisation, setLocalisation] = useState("");
    const [nbCandidats, setNbCandidats] = useState(0);
    const [dateLimite, setDateLimite] = useState("");
    const { t } = useTranslation();
    const [limite, setLimite] = useState(0);
    const [season, setSeason] = useState("");
    const [year, setYear] = useState("");
    const [erreur, setErreur] = useState("");


    useEffect(() => {
        const currentMonth = new Date().getMonth() + 1;
        const currentYear = new Date().getFullYear();
        setLimite(currentYear)
        let defaultSeason;
        let defaultYear = currentYear;

        if (currentMonth >= 9) { // Si on est en automne
            defaultSeason = "HIVER";
            defaultYear += 1;
        } else if (currentMonth >= 5) { // Si on est en été
            defaultSeason = "AUTOMNE";
        } else { // Si on est en hiver ou printemps
            defaultSeason = "ETE";
        }

        setSeason(defaultSeason);
        setYear(String(defaultYear).slice(-2));
    }, []);

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
            setErreur("");
            setTemporaryFile(uploadedFile);
            const reader = new FileReader();
            reader.onload = (e) => {
                setTemporaryFileData(e.target.result);
            };
            reader.readAsDataURL(uploadedFile);
        }
        else {
            setErreur((t('erreurFichierPDF')));
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
            setErreur("");
        }
        else {
            setErreur((t('erreurFichierPDF')));
        }
    };



    const handleSubmit = () => {
        if (temporaryFile) {
            const session = `${season}${year}`;
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
                session,
            };

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
            setShowModal(false);
            setTemporaryFile(null);
        }
    };

    const getMinYearForSession = (season) => {
        const currentYear = new Date().getFullYear();
        const currentMonth = new Date().getMonth() + 1;

        let minYear = currentYear;

        if (season === "AUTOMNE" || season === "HIVER") {

            minYear = currentYear + 1;
        } else if (season === "ETE") {

            if (currentMonth >= 5) {
                minYear = currentYear + 1;
            }
        }

        return String(minYear).slice(-2);
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

    const verificationSession = (data) => {
        //console.log("session ", data);
    }

    return (
        <>
            <EmployeurHeader userData={userData} onSendData={verificationSession}/>
            <div className="container-fluid p-4">

                <div className="text-center my-4">
                    {file ? (
                        <h2>{t('OffreLoad')}</h2>
                    ) : (
                        <h2 className="text-warning">{t('AjoutOffrePourContinuer')}</h2>
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
                        {file == null ? t('AjouterOffre') :
                            file.status === 'Attente' ? t('OffreAttenteConfirmation') :
                                file.status === 'Approuvé' ? t('OffreApprouvée') :
                                    file.status === 'Rejeté' ? t('OffreRefusée') : t('OffreNonRemise')}
                    </button>
                </div>

                {file && (
                    <div className="d-flex justify-content-center my-3">
                        <button className="btn btn-info" onClick={openFile}>
                            {t('SeeOffer')}
                        </button>
                    </div>
                )}


                {showModal && (
                    <div className="custom-modal-overlay">
                        <div className="modal modal-custom" tabIndex="-1" role="dialog">
                            <div className="modal-dialog" role="document">
                                <div className="modal-content">
                                    <div className="modal-header">
                                        <h5 className="modal-title"><b>{t('SoumettreOffreEmploi')}</b></h5>
                                    </div>
                                    <div className="modal-body">
                                        {temporaryFile && (
                                            <div className="file-details mt-3">
                                                <h6><strong>{t('fileName')}</strong> {temporaryFile.name}</h6>
                                                <h6><strong>{t('fileType')}</strong> {temporaryFile.type}</h6>
                                                <h6><strong>{t('fileDate')}</strong> {new Date().toLocaleDateString()}
                                                </h6>
                                            </div>
                                        )}

                                        {erreur && (
                                            <div className="alert alert-danger mt-3">{erreur}</div>
                                        )}

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
                                                accept=".pdf"
                                            />
                                        </div>

                                        <div className="form-group mt-3">
                                            <label htmlFor="titre"><b>{t('TitreDeOffre')}</b></label>
                                            <input
                                                type="text"
                                                className="form-control"
                                                id="titre"
                                                style={{textAlign: "center"}}
                                                value={titre}
                                                onChange={(e) => setTitre(e.target.value)}
                                            />
                                        </div>

                                        <div className="form-group mt-3">
                                            <label htmlFor="localisation"><b>{t('localisation')}</b></label>
                                            <input
                                                type="text"
                                                className="form-control"
                                                id="localisation"
                                                style={{textAlign: "center"}}
                                                value={localisation}
                                                onChange={(e) => setLocalisation(e.target.value)}
                                            />
                                        </div>

                                        <div className="form-group mt-3">
                                            <label htmlFor="nbCandidats"><b>{t('NombreDeCandidats')}</b></label>
                                            <input
                                                type="number"
                                                className="form-control"
                                                style={{textAlign: "center"}}
                                                id="nbCandidats"
                                                value={nbCandidats}
                                                min={0}
                                                onChange={(e) => setNbCandidats(e.target.value)}
                                            />
                                        </div>

                                        <div className="form-group mt-3">
                                            <label htmlFor="session"><b>{t('Session')}</b></label>
                                            <div className="d-flex">
                                                <select
                                                    className="form-control mr-2"
                                                    value={season}
                                                    onChange={(e) => setSeason(e.target.value)}
                                                >
                                                    <option value="AUTOMNE">{t('AUTOMNE')}</option>
                                                    <option value="HIVER">{t('HIVER')}</option>
                                                    <option value="ETE">{t('ETE')}</option>
                                                </select>
                                                <input
                                                    type="number"
                                                    className="form-control"
                                                    value={year}
                                                    onChange={(e) => setYear(e.target.value.slice(-2))}
                                                    placeholder="Année (ex: 24)"
                                                    min={getMinYearForSession(season)}
                                                    max="99"
                                                />
                                            </div>
                                        </div>

                                        <div className="form-group mt-3">
                                            <label htmlFor="dateLimite"><b>{t('DateLimite')}</b></label>
                                            <input
                                                type="date"
                                                className="form-control"
                                                id="dateLimite"
                                                style={{textAlign: "center"}}
                                                value={dateLimite}
                                                onChange={(e) => setDateLimite(e.target.value)}
                                            />
                                        </div>
                                    </div>

                                    <div className="modal-footer">
                                        <button className="btn btn-success" onClick={handleSubmit}>
                                            {t('Soumettre')}
                                        </button>
                                        <button type="button" className="btn btn-danger" onClick={fermerAffichageOffre}>
                                            {t('Fermer')}
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </>
    );
}

export default SoumettreOffre;
