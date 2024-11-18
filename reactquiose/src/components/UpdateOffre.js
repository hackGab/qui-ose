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
    const [status, setStatus] = useState(offre?.status || "Attente");
    const [dateLimite, setDateLimite] = useState(offre?.dateLimite || "");
    const [pdfFile, setPdfFile] = useState(null);
    const [dragActive, setDragActive] = useState(false);
    const { t } = useTranslation();

    useEffect(() => {
        if (!offre) {
            navigate("/visualiser-offres", { state: { employeurEmail } });
        }
    }, [offre, employeurEmail, navigate]);

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
        console.log(formData.get("titre"));
        console.log(formData.get("localisation"));
        console.log(formData.get("nbCandidats"));
        console.log(formData.get("status"));
        console.log(formData.get("dateLimite"));
        console.log(formData);

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
                navigate("/visualiser-offres", { state: { employeurEmail } });
            } else {
                console.error("Erreur lors de la mise à jour de l'offre");
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

    return (
        <>
            <EmployeurHeader userData={userData} />
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
