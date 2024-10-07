import React from "react";
import { useLocation } from "react-router-dom";

function AccueilProfesseur() {
    const location = useLocation();
    const { userData } = location.state || {}; // Get userData from navigation state

    return (
        <div className="container mt-3">
            <h1 className="text-center mb-4">Accueil Professeur</h1>

            {/* Optionally display some of the professor's data */}
            <div className="card bg-light mb-3">
                <div className="card-body text-center">
                    <h5 className="card-title">Bienvenue, {userData?.firstName}</h5>
                    <p className="card-text">Email: {userData?.credentials.email}</p>
                    <p className="card-text">Rôle: Professeur</p>
                </div>
            </div>
        </div>
    );
}

export default AccueilProfesseur;
