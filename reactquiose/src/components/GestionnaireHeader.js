import React from "react";
import { Link } from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.min.css';
import '../CSS/GestionnaireHeader.css';

import logo from '../images/logo.png';

function GestionnaireHeader() {
    return (
        <header className="gestionnaire-header bg-dark text-white">
            <nav className="navbar navbar-expand-lg navbar-dark">
                <div className="container-fluid">
                    <Link className="navbar-brand" to="/accueilGestionnaire">
                        <img src={logo} alt="Logo" className="header-logo" />
                        Tableau de Bord
                    </Link>
                    <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse" id="navbarNav">
                        <ul className="navbar-nav ms-auto">
                            <li className="nav-item">
                                <Link className="nav-link custom-button" to="/listeEtudiants">Étudiant</Link>
                            </li>
                            <li className="nav-item">
                                <Link className="nav-link custom-button" to="/role/professeur">Professeur</Link>
                            </li>
                            <li className="nav-item">
                                <Link className="nav-link custom-button" to="/role/employeur">Employeur</Link>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>
        </header>
    );
}

export default GestionnaireHeader;
