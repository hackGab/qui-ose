import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import '../../CSS/AccueilEmployeur.css';
import EmployeurHeader from "../Header/EmployeurHeader";
import { useTranslation } from "react-i18next";

function AccueilEmployeur() {
    const navigate = useNavigate();
    const location = useLocation();
    const [userData, setUserData] = useState(location.state?.userData || null);
    const { t } = useTranslation();

    const handleClickSubmit = () => {
        if (userData?.credentials?.email) {
            navigate("/soumettre-offre", { state: {  userData: userData } });
        }
    };

    const handleClickView = () => {
        if (userData?.credentials?.email) {
            navigate("/visualiser-offres", { state: { userData: userData } });
        }
    };

    const handleClickEntrevue = () => {
        if (userData?.credentials?.email) {
            navigate("/visualiser-entrevue-accepter", { state: {  userData: userData } });
        }
    };

    const verificationSession = async (data) => {

    }

    return (
        <>
            <EmployeurHeader userData={userData} onSendData={verificationSession}/>
            <div className="container-fluid p-4">

                <div className="container mt-5">

                    <h2 className="text-center my-1 text-capitalize" style={{ color: "#01579b" }}>{t('Bienvenue')}, {userData ? userData.firstName + " " + userData.lastName : ""}!</h2>

                    <h1 className="text-center mt-2 mb-4">{t('accueilEmployeur')}</h1>

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

                            <div className="card mb-3 text-center clickable-card" onClick={handleClickEntrevue}>
                                <div className="card-body">
                                    <h5 className="card-title">{t('VisualiserLesEntrevueAccepte')}</h5>
                                    <p className="card-text">{t('VisualiserLesEntrevueAccepteClick')}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}

export default AccueilEmployeur;