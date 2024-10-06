import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import "../CSS/VisualiserOffres.css";
import EmployeurHeader from "./EmployeurHeader"; // Assurez-vous de mettre à jour le CSS

function VisualiserOffres() {
    const location = useLocation();
    const employeurEmail = location.state?.employeurEmail;
    const [offres, setOffres] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedOffre, setSelectedOffre] = useState(null); // Gérer l'offre sélectionnée

    useEffect(() => {
        const fetchOffres = async () => {
            if (!employeurEmail) {
                setError("Email employeur non fourni");
                setIsLoading(false);
                return;
            }

            try {
                const response = await fetch(`http://localhost:8081/offreDeStage/offresEmployeur/${employeurEmail}`);
                if (!response.ok) {
                    throw new Error("Erreur lors de la récupération des offres");
                }
                const data = await response.json();
                setOffres(data);
            } catch (error) {
                setError(error.message);
            } finally {
                setIsLoading(false);
            }
        };

        fetchOffres();
    }, [employeurEmail]);

    // Fonction pour définir la classe CSS en fonction du statut
    const getStatusClass = (status) => {
        switch (status) {
            case "Accepter":
                return "status-green";
            case "Rejeter":
                return "status-red";
            case "Attente":
            default:
                return "status-yellow";
        }
    };

    // Fonction pour gérer le clic sur une offre
    const handleOffreClick = (index) => {
        // Si on clique sur la même offre, la désélectionner
        if (selectedOffre === index) {
            setSelectedOffre(null);
        } else {
            setSelectedOffre(index);
        }
    };

    // Fonction pour ouvrir le PDF dans une nouvelle fenêtre
    const openPDF = (data) => {
        const pdfWindow = window.open();
        pdfWindow.document.write(
            `<iframe src="${data}" style="border:0; top:0; left:0; bottom:0; right:0; width:100%; height:100%;" allowfullscreen></iframe>`
        );
    };

    if (isLoading) {
        return <div>Chargement des offres...</div>;
    }

    if (error) {
        return <div>Erreur : {error}</div>;
    }

    return (
        <div className="container-fluid d-flex flex-column min-vh-100">
            <EmployeurHeader />

            <div className="container mt-5">
                <h1 className="text-center mt-5">Vos offres d'emploi</h1>

                {offres.length === 0 ? (
                    <div className="alert alert-info mt-3">Aucune offre trouvée</div>
                ) : (
                    <div className="list-group mt-3">
                        {offres.map((offre, index) => (
                            <div
                                key={index}
                                className={`card offre-card ${selectedOffre === index ? "hovered" : ""}`}
                                onClick={() => handleOffreClick(index)} // Gérer le clic ici
                            >
                                <div className="card-body">
                                    <h5 className="card-title">{offre.titre}</h5>
                                    <p className="card-text">
                                        <strong>Localisation :</strong> {offre.localisation} <br />
                                        <strong>Nombre de candidats :</strong> {offre.nbCandidats}
                                    </p>
                                    <small>
                                        Date de publication : {new Date(offre.datePublication).toLocaleDateString()} <br />
                                        Date limite : {new Date(offre.dateLimite).toLocaleDateString()}
                                    </small>
                                    <div className={`status-badge ${getStatusClass(offre.status)}`}>
                                        <strong>Status :</strong> {offre.status}
                                    </div>

                                    {selectedOffre === index && (
                                        <div
                                            className="card pdf-card mt-4"
                                            style={{ backgroundColor: "#f0f0f0" }} // Couleur de fond gris
                                            onClick={() => openPDF(offre.data)} // Remplacez "data" par le champ correspondant au PDF
                                        >
                                            <div className="card-body text-center">
                                                <h5 className="card-title">Voir le fichier PDF</h5>
                                                <p className="card-text">Cliquez ici pour ouvrir le PDF de l'offre sélectionnée.</p>
                                            </div>
                                        </div>
                                    )}
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}

export default VisualiserOffres;
