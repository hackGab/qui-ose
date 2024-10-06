import React from "react";
import { BrowserRouter as Router, Navigate, Route, Routes } from "react-router-dom";
import Formulaire from "./components/Formulaire";
import AccueilEtudiant from "./components/AccueilEtudiant";
import AccueilEmployeur from "./components/AccueilEmployeur";
import AccueilGestionnaire from "./components/AccueilGestionnaire";
import AccueilProfesseur from "./components/AccueilProfesseur";
import SoumettreOffre from "./components/SoumettreOffre";
import VisualiserOffres from "./components/VisualiserOffres";
import "./App.css";

function App() {
    return (
        <Router>
            <div className="App-body">
                <Routes>
                    {/* Default Route */}
                    <Route path="/" element={<Navigate to="/login" />} />

                    {/* Public Routes */}
                    <Route path="/signUp" element={<Formulaire title="Inscription" />} />
                    <Route path="/login" element={<Formulaire title="Connexion" />} />

                    {/* Direct Access Routes */}
                    <Route path="/accueilEtudiant" element={<AccueilEtudiant />} />
                    <Route path="/accueilEmployeur" element={<AccueilEmployeur />} />
                    <Route path="/accueilGestionnaire" element={<AccueilGestionnaire />} />
                    <Route path="/accueilProfesseur" element={<AccueilProfesseur />} />
                    <Route path="/soumettre-offre" element={<SoumettreOffre />} />
                    <Route path="visualiser-offres" element={<VisualiserOffres />} />

                    {/* Unauthorized Access */}
                    <Route path="/nonAutorise" element={<div>Accès non autorisé</div>} />

                    {/* Catch-All Route */}
                    <Route path="*" element={<Navigate to="/login" replace />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
