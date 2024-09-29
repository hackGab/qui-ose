import React from "react";
import { Link } from "react-router-dom"; // Ajoutez cette ligne
import 'bootstrap/dist/css/bootstrap.min.css';
import '../CSS/AccueilGestionnaire.css';

function AccueilGestionnaire() {
    const sections = [
        { title: "Étudiant", notifications: 5, image: "../images/Etudiant.png", link: "/listeEtudiants" }, // Ajoutez un lien
        { title: "Professeur", notifications: 2, image: "path_to_teacher_image.png" },
        { title: "Employeur", notifications: 3, image: "path_to_employer_image.png" },
    ];

    return (
        <div className="container accueil-gestionnaire">
            <h1>Tableau de Bord</h1>
            <div className="row justify-content-center">
                {sections.map((section, index) => (
                    <div
                        className={`col-lg-4 col-md-6 mb-4 d-flex justify-content-center`}
                        key={index}
                    >
                        <Link to={section.link} className="section">
                            <div className="notification-badge">{section.notifications}</div>
                            <h2 className="section-title">{section.title}</h2>
                            <img className="section-image" src={section.image} alt={section.title} />
                        </Link>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default AccueilGestionnaire;
