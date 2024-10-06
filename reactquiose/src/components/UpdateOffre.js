// src/components/UpdateOffre.js
import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

function UpdateOffre() {
    const navigate = useNavigate();
    const location = useLocation();
    const offre = location.state?.offre;
    const employeurEmail = location.state?.employeurEmail;
    const [titre, setTitre] = useState(offre?.titre || "");
    const [localisation, setLocalisation] = useState(offre?.localisation || "");
    const [nbCandidats, setNbCandidats] = useState(offre?.nbCandidats || 0);
    const [status, setStatus] = useState(offre?.status || "Attente");
    const [dateLimite, setDateLimite] = useState(offre?.dateLimite || "");
    const [pdfFile, setPdfFile] = useState(null); // État pour le fichier PDF
    const [dragActive, setDragActive] = useState(false);

    useEffect(() => {
        if (!offre) {
            navigate("/visualiser-offres", { state: { employeurEmail } });
        }
    }, [offre, employeurEmail, navigate]);

    const handleUpdate = async (e) => {
        e.preventDefault();

        const formData = new FormData();

        // Ajout des données de l'offre à formData
        formData.append("titre", titre);
        formData.append("localisation", localisation);
        formData.append("nbCandidats", nbCandidats);
        formData.append("dateLimite", dateLimite);
        formData.append("status", status);
        formData.append("employeur", offre.employeur);
        formData.append("datePublication", offre.datePublication);

        // Gestion du fichier PDF
        if (pdfFile) {
            const reader = new FileReader();
            reader.onloadend = async () => {
                const base64data = reader.result; // Cette valeur sera une chaîne de caractères au format "data:application/pdf;base64,..."
                formData.append("data", base64data); // Ajouter le fichier PDF en Base64
                await sendUpdateRequest(formData);
            };
            reader.readAsDataURL(pdfFile); // Lire le fichier en tant que Data URL
        } else {
            // Si aucun fichier n'est sélectionné, envoyer les autres données sans fichier
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
        }
        console.log(data);

        try {
            const response = await fetch(`http://localhost:8081/offreDeStage/${offre.id}`, {
                method: "PUT",
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
            setPdfFile(uploadedFile); // Mettre à jour l'état avec le fichier PDF
        }
    };

    return (
        <div className="container mt-5">
            <h2>Mise à jour de l'offre</h2>
            <form onSubmit={handleUpdate} onDragEnter={handleDrag} onDragOver={handleDrag} onDrop={handleDrop}>
                <div className="form-group">
                    <label>Titre</label>
                    <input
                        type="text"
                        className="form-control"
                        value={titre}
                        onChange={(e) => setTitre(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>Localisation</label>
                    <input
                        type="text"
                        className="form-control"
                        value={localisation}
                        onChange={(e) => setLocalisation(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>Nombre de candidats</label>
                    <input
                        type="number"
                        className="form-control"
                        value={nbCandidats}
                        onChange={(e) => setNbCandidats(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>Date Limite</label>
                    <input
                        type="date"
                        className="form-control"
                        value={dateLimite}
                        onChange={(e) => setDateLimite(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>Fichier PDF (optionnel)</label>
                    <input
                        type="file"
                        className="form-control"
                        accept="application/pdf"
                        onChange={(e) => setPdfFile(e.target.files[0])} // Mettre à jour l'état avec le fichier PDF sélectionné
                    />
                    {pdfFile && (
                        <p>Fichier sélectionné : {pdfFile.name}</p> // Afficher le nom du fichier sélectionné
                    )}
                </div>
                <button type="submit" className="btn btn-primary mt-3">Mettre à jour l'offre</button>
            </form>
        </div>
    );
}

export default UpdateOffre;
