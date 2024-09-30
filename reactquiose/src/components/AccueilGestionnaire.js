import React from "react";
import { Link } from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.min.css';
import '../CSS/AccueilGestionnaire.css';

import etudiantImage from '../images/Etudiant.png';
import professeurImage from '../images/Professeur.png';
import employeurImage from '../images/Employeur.png';

function AccueilGestionnaire() {
    const sections = [
        { title: "Étudiant", notifications: 5, image: etudiantImage, link: "/listeEtudiants" },
        { title: "Professeur", notifications: 2, image: professeurImage },
        { title: "Employeur", notifications: 3, image: employeurImage },
    ];

    return (
        <div className="container accueil-gestionnaire">
            <h1>Tableau de Bord</h1>
            <div className="row justify-content-center">
                {sections.map((section, index) => (
                    <div className="col-lg-4 col-md-6 mb-4 d-flex flex-column align-items-center" key={index}>
                        <h2 className="section-title-outside">{section.title}</h2>
                        <Link to={section.link} className="section">
                            <div className="notification-badge">{section.notifications}</div>
                            <img
                                className={`section-image section-image-${section.title.toLowerCase()}`}
                                src={section.image}
                                alt={section.title}
                            />
                        </Link>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default AccueilGestionnaire;
