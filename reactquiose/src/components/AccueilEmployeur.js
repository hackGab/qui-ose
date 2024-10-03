import React, { useEffect, useState } from "react";
import { useNavigate,useLocation  } from "react-router-dom";

function AccueilEmployeur() {
    const navigate = useNavigate();
    const location = useLocation(); // Get the location object to access the state
    const [userData, setUserData] = useState(location.state?.userData || null); // Initialize userData with state


    const handleClick = () => {
        if (userData?.credentials?.email) {
            navigate("/soumettre-offre", { state: { employeurEmail: userData.credentials.email } });
        }
    };


    return (
        <div className="container mt-3">
            <h1 className="text-center mb-4">Accueil Employeur</h1>
            <div className="row justify-content-center">
                <div className="col-md-8">
                    <div className="card mb-3" style={{ cursor: "pointer" }} onClick={handleClick}>
                        <div className="card-body text-center bg-primary text-white">
                            <h5 className="card-title">Soumettre une offre d'emploi</h5>
                            <p className="card-text">Cliquez ici pour soumettre une nouvelle offre d'emploi.</p>
                        </div>
                    </div>
                </div>
            </div>

            {/* Section Profil à droite */}
            <div className="row justify-content-center mt-4">
                <div className="col-md-6">
                    <div className="card bg-light mb-3">
                        <div className="card-body text-center">
                            <h5 className="card-title">Bienvenue, {userData?.firstName}</h5>
                            <p className="card-text">Email: {userData?.credentials?.email}</p>
                            <p className="card-text">Rôle: {userData?.role}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default AccueilEmployeur;
