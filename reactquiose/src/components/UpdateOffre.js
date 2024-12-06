import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";
import EmployeurHeader from "./Header/EmployeurHeader";
function UpdateOffre() {
    const navigate = useNavigate();
    const location = useLocation();
    const offre = location.state?.offre;
    const userData = location.state?.userData;
    const employeurEmail = location.state?.employeurEmail;
    const [titre, setTitre] = useState(offre?.titre || "");
    const [localisation, setLocalisation] = useState(offre?.localisation || "");
    const [nbCandidats, setNbCandidats] = useState(offre?.nbCandidats || 0);
    const [status] = useState(offre?.status || "Attente");
    const [dateLimite, setDateLimite] = useState(offre?.dateLimite || "");
    const [pdfFile, setPdfFile] = useState(null);
    const [dragActive, setDragActive] = useState(false);
    const [season, setSeason] = useState("");  // State for season
    const [year, setYear] = useState("");      // State for year
    const {t} = useTranslation();

    useEffect(() => {
        if (!offre) {
            navigate("/accueilEmployeur", {state: {userData}});
        }
        verifierDate(offre.session);
    }, [offre, employeurEmail, navigate]);

    const verifierDate = (date) => {
        const annee = [];
        const saison = [];
        for (let i = 0; i < date.length; i++) {
            if (!isNaN(date[i])) {
                annee.push(date[i]);
            } else {
                saison.push(date[i]);
            }
        }

        setYear(annee.join(''));
        setSeason(saison.join(''));

    }

    const handleUpdate = async (e) => {
        e.preventDefault();

        const formData = new FormData();

        formData.append("titre", titre);
        formData.append("localisation", localisation);
        formData.append("nbCandidats", nbCandidats);
        formData.append("dateLimite", dateLimite);
        formData.append("status", status);
        formData.append("employeur", offre.employeur);
        formData.append("datePublication", offre.datePublication);
        formData.append("session", `${season} ${year}`);

        if (pdfFile) {
            const reader = new FileReader();
            reader.onloadend = async () => {
                const base64data = reader.result;
                formData.append("data", base64data);
                await sendUpdateRequest(formData);
            };
            reader.readAsDataURL(pdfFile);
        } else {
            await sendUpdateRequest(formData);
        }
    };

    const sendUpdateRequest = async (formData) => {
        const data = {
            id: offre.id,
            titre: formData.get("titre"),
            localisation: formData.get("localisation"),
            nbCandidats: formData.get("nbCandidats"),
            status: formData.get("status"),
            dateLimite: formData.get("dateLimite"),
            employeur: offre.employeur,
            datePublication: formData.get("datePublication"),
            data: formData.get("data"),
            session: formData.get("session"),
        };

        if (data.data === null) {
            data.data = offre.data;
        }

        try {
            const response = await fetch(`http://localhost:8081/offreDeStage/${offre.id}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                navigate("/accueilEmployeur", { state: { userData } });
            }
        } catch (error) {
            console.error("Erreur:", error);
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
            setPdfFile(uploadedFile);
        }
    };

    const verificationSession = (data) => {
       // console.log("session ", data);
    }

    return (
        <>
            <EmployeurHeader userData={userData} onSendData={verificationSession}/>
            <div className="container-fluid p-4">
                <div className="container mt-5">
                    <h2>{t('MiseAJourOffre')}</h2>
                    <form onSubmit={handleUpdate} onDragEnter={handleDrag} onDragOver={handleDrag} onDrop={handleDrop}>
                        <div className="form-group">
                            <label>{t('TitreDeOffre')}</label>
                            <input
                                type="text"
                                className="form-control"
                                value={titre}
                                onChange={(e) => setTitre(e.target.value)}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>{t('localisation')}</label>
                            <input
                                type="text"
                                className="form-control"
                                value={localisation}
                                onChange={(e) => setLocalisation(e.target.value)}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>{t('NombreDeCandidats')}</label>
                            <input
                                type="number"
                                className="form-control"
                                value={nbCandidats}
                                onChange={(e) => setNbCandidats(e.target.value)}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>{t('DateLimite')}</label>
                            <input
                                type="date"
                                className="form-control"
                                value={dateLimite}
                                onChange={(e) => setDateLimite(e.target.value)}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>{t('Session')}</label>
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
                                    min="0"
                                    max="99"
                                />
                            </div>
                        </div>
                        <div className="form-group">
                            <label>{t('FichierPDFO')}</label>
                            <input
                                type="file"
                                className="form-control"
                                accept="application/pdf"
                                onChange={(e) => setPdfFile(e.target.files[0])}
                            />
                            {pdfFile && (
                                <p>{t('FichierSelectionner')} {pdfFile.name}</p>
                            )}
                        </div>
                        <button type="submit" className="btn btn-primary mt-3">{t('MiseAJourOffre')}</button>
                    </form>
                </div>
            </div>
        </>
    );
}

export default UpdateOffre;
