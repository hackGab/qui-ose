import React from "react";
import { useNavigate } from "react-router-dom";

function AccueilEmployeur() {
    const navigate = useNavigate();

    const handleClick = () => {
        // Redirige vers la page de soumission d'offre
        navigate("/soumettre-offre");
    };

    return (
        <div className="container mt-3">
            <h1 className="text-center mb-4">Accueil Employeur</h1>
            <div className="row justify-content-center">
                <div className="col-md-4">
                    <div
                        className="card text-white bg-primary mb-3"
                        onClick={handleClick}
                        style={{ cursor: 'pointer' }}
                    >
                        <div className="card-body text-center">
                            <h5 className="card-title">Soumettre offre d'emploi</h5>
                            <p className="card-text">Cliquez ici pour soumettre une nouvelle offre d'emploi.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default AccueilEmployeur;
