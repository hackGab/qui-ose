import React from "react";
import {useNavigate, useLocation} from "react-router-dom";

function AccueilEmployeur() {
    const navigate = useNavigate();
    const location = useLocation();
    const {userData} = location.state || {}; // Get userData from navigation state

    const handleClick = () => {
        // Redirige vers la page de soumission d'offre
        navigate("/soumettre-offre", {state: {userData}});
    };

    const handleProfileClick = () => {
        // Redirige vers la page de profil de l'employeur
        navigate("/profil-employeur", {state: {userData}});
    };

    return (
        <div className="container mt-3">
            <div className="row">
                <div className="col-md-8">
                    <h1 className="text-center mb-4">Accueil Employeur</h1>
                    <div className="row justify-content-center">
                        <div className="col-md-6">
                            <div
                                className="card text-white bg-primary mb-3"
                                onClick={handleClick}
                                style={{cursor: "pointer"}}
                            >
                                <div className="card-body text-center">
                                    <h5 className="card-title">Soumettre offre d'emploi</h5>
                                    <p className="card-text">
                                        Cliquez ici pour soumettre une nouvelle offre d'emploi.
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Profile section on the right */}
                <div className="card bg-light mb-3">
                    <div className="card-body text-center">
                        <h5 className="card-title">Bienvenue, {userData?.firstName}</h5>
                        <p className="card-text">Email: {userData?.credentials.email}</p>
                        <p className="card-text">Rôle: {userData?.role}</p>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default AccueilEmployeur;
