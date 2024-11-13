import React, { useEffect, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.min.css';
import '../CSS/AccueilGestionnaire.css';
import { useTranslation } from 'react-i18next';

import etudiantImage from '../images/Etudiant.png';
import professeurImage from '../images/Professeur.png';
import employeurImage from '../images/Employeur.png';

function AccueilGestionnaire() {
    const { t } = useTranslation();
    const location = useLocation();
    const navigate = useNavigate();
    const userData = location.state?.userData;
    console.log("AccueilGestionnaire userData: ", userData);

    const [cvAttentes, setCvAttentes] = useState(0);
    const [offresAttentes, setOffresAttentes] = useState(0);

    const sections = [
        { title: t("etudiant"), notifications: cvAttentes, image: etudiantImage, link: "/listeEtudiants" },
        { title: t("prof"), notifications: 0, image: professeurImage, link: "/listeProfesseurs" },
        { title: t("employeur"), notifications: offresAttentes, image: employeurImage, link: "/listeEmployeurs" },
    ];

    const handleNavigateCandidatures = () => {
        navigate('/listeCandidatures', { state: { userData } });
    };

    useEffect(() => {
        fetchCvAttentes();
        fetchOffresAttentes();
    }, []);

    const fetchCvAttentes = async () => {
        try {
            const response = await fetch('http://localhost:8081/cv/attentes');
            const data = await response.json();
            setCvAttentes(data);
        } catch (error) {
            console.error('Erreur lors de la récupération des CV en attente:', error);
        }
    };

    const fetchOffresAttentes = async () => {
        try {
            const response = await fetch('http://localhost:8081/offreDeStage/attentes');
            const data = await response.json();
            setOffresAttentes(data);
        } catch (error) {
            console.error('Erreur lors de la récupération des offres de stage en attente:', error);
        }
    };

    return (
        <div className="container accueil-gestionnaire">
            <h2 className="text-center my-2 text-capitalize" style={{ color: "#01579b" }}>
                {t('Bienvenue')}, {userData ? userData.firstName + " " + userData.lastName : ""}!
            </h2>

            <h1>{t("Dashboard")}</h1>
            <div className="row justify-content-center">
                {sections.map((section, index) => (
                    <div className="col-lg-4 col-md-6 mb-4 d-flex flex-column align-items-center" key={index}>
                        <h2 className="section-title-outside">{section.title}</h2>
                        <Link to={section.link} className="section">
                            <h2 className="section-title-inside">{section.title}</h2>
                            {section.notifications > 0 && (
                                <div className="notification-badge">{section.notifications}</div>
                            )}
                            <img
                                className={`section-image section-image-${section.title.toLowerCase()}`}
                                src={section.image}
                                alt={section.title}
                            />
                        </Link>
                    </div>
                ))}
            </div>

            <div className="candidature-button-container mt-5">
                <button className="candidature-button" onClick={handleNavigateCandidatures}>
                    {t("Voir les Candidatures")}
                </button>
            </div>
        </div>
    );
}

export default AccueilGestionnaire;
