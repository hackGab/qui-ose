import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import '../CSS/AccueilEmployeur.css';
import EmployeurHeader from "./EmployeurHeader";
import { useTranslation } from "react-i18next";

function AccueilEmployeur() {
    const navigate = useNavigate();
    const location = useLocation();
    const [userData, setUserData] = useState(location.state?.userData || null);
    const { t } = useTranslation();
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
                <h1 className="text-center mt-5 mb-4">{t('accueilEmployeur')}</h1>

                <div className="row justify-content-center">
                    <div className="col-md-8">
                        <div className="card mb-3 text-center clickable-card" onClick={handleClickSubmit}>
                            <div className="card-body">
                                <h5 className="card-title">{t('SoumettreOffreEmploi')}</h5>
                                <p className="card-text">{t('soumettreUneNouvelleOffre')}</p>
                            </div>
                        </div>

                        <div className="card mb-3 text-center clickable-card" onClick={handleClickView}>
                            <div className="card-body">
                                <h5 className="card-title">{t('VisualiserLesOffresEmploi')}</h5>
                                <p className="card-text">{t('visualiserLesOffresEmploiSoumise')}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default AccueilEmployeur;