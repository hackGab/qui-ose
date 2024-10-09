import React, { useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import '../CSS/AccueilEmployeur.css';
import EmployeurHeader from "./EmployeurHeader";

function AccueilEmployeur() {
    const navigate = useNavigate();
    const location = useLocation();
    const [userData, setUserData] = useState(location.state?.userData || null);

    const handleClickSubmit = () => {
        console.log("Soumettre une offre d'emploi");
        if (userData?.credentials?.email) {
            navigate("/soumettre-offre", { state: { employeurEmail: userData.credentials.email } });
        }
    };

    const handleClickView = () => {
        if (userData?.credentials?.email) {
            navigate("/visualiser-offres", { state: { employeurEmail: userData.credentials.email } });
        }
    };

    return (
        <div className="container-fluid d-flex flex-column min-vh-100">
            <EmployeurHeader />

            <div className="container mt-5">
                <h1 className="text-center mt-5 mb-4">Accueil Employeur</h1>

                <div className="row justify-content-center">
                    <div className="col-md-8">
                        <div className="card mb-3 text-center clickable-card" onClick={handleClickSubmit}>
                            <div className="card-body">
                                <h5 className="card-title">Soumettre une offre d'emploi</h5>
                                <p className="card-text">Cliquez ici pour soumettre une nouvelle offre d'emploi.</p>
                            </div>
                        </div>

                        <div className="card mb-3 text-center clickable-card" onClick={handleClickView}>
                            <div className="card-body">
                                <h5 className="card-title">Visualiser les offres d'emploi</h5>
                                <p className="card-text">Cliquez ici pour visualiser les offres d'emploi que vous avez soumises.</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default AccueilEmployeur;
