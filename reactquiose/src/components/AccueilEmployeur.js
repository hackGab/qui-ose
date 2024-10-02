import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function AccueilEmployeur() {
    const navigate = useNavigate();
    const [userData, setUserData] = useState(null);
    const [loading, setLoading] = useState(true);

    // Retrieve and parse the authState from local storage
    const auth = JSON.parse(localStorage.getItem("authState"));
    const token = auth?.accessToken; // Use optional chaining to safely access the token

    useEffect(() => {
        const fetchUserData = async () => {
            if (!token) {
                // Redirect to login if there's no token
                navigate("/login", { replace: true });
                return;
            }

            try {
                const response = await fetch("http://localhost:8081/user/me", {
                    method: "GET",
                    headers: {
                        "Authorization": `Bearer ${token}`, // Include the token in the authorization header
                        "Content-Type": "application/json"
                    }
                });

                if (!response.ok) {
                    throw new Error("Failed to fetch user data");
                }

                const data = await response.json();

                setUserData(data); // Store user data in the state
            } catch (error) {
                console.error("Error fetching user data:", error);
                navigate("/login", { replace: true }); // Navigate to login if there's an error
            } finally {
                setLoading(false); // Set loading to false once data is fetched
            }
        };

        fetchUserData();
    }, [navigate, token]); // Added token to dependency array

    const handleClick = () => {
        if (userData?.credentials?.email) {
            navigate("/soumettre-offre", { state: { employeurEmail: userData.credentials.email } });
        }
    };
    const handleProfileClick = () => {
        navigate("/profil-employeur");
    };

    // Show a loading message while data is being fetched
    if (loading) {
        return <div>Loading...</div>;
    }

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
                            <button className="btn btn-outline-primary mt-3" onClick={handleProfileClick}>
                                Voir mon profil
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default AccueilEmployeur;
