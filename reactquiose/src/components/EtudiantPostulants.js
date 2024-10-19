import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import "../CSS/VisualiserOffres.css";

function EtudiantPostulants() {
    const { offreId } = useParams();
    const [etudiants, setEtudiants] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (!offreId) {
            setError("L'ID de l'offre est requis.");
            setIsLoading(false);
            return;
        }

        const fetchEtudiants = async () => {
            try {
                const response = await fetch(`http://localhost:8081/offreDeStage/${offreId}/etudiants`);

                if (!response.ok) {
                    if (response.status === 404) {
                        setEtudiants([]); // Aucun étudiant trouvé
                    } else {
                        throw new Error("Erreur dans la réponse du serveur");
                    }
                    return;
                }

                const data = await response.json();
                setEtudiants(data);
            } catch (error) {
                setError(error.message);
            } finally {
                setIsLoading(false);
            }
        };

        fetchEtudiants();
    }, [offreId]);

    if (isLoading) {
        return <div>Chargement des étudiants...</div>;
    }

    if (error) {
        return <div>Erreur: {error}</div>;
    }

    if (etudiants.length === 0) {
        return <div>Aucun étudiant n'a postulé à cette offre.</div>;
    }

    return (
        <div>
            <h5>Étudiants postulants :</h5>
            <div className="row">
                {etudiants.map((etudiant) => (
                    <div key={etudiant.id} className="col-12 col-lg mb-4">
                        <div className="card-container">
                            <div className="card">
                                <div className="card-body">
                                    <h5 className="card-title">{etudiant.firstName} {etudiant.lastName}</h5>
                                    <p className="card-text"><strong>Email:</strong> {etudiant.email}</p>
                                    <p className="card-text"><strong>Numéro de téléphone:</strong> {etudiant.phoneNumber}
                                    </p>
                                    <p className="card-text"><strong>Programme:</strong> {etudiant.departement}</p>

                                </div>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default EtudiantPostulants;