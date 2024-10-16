import React from "react";
import { BrowserRouter as Router, Navigate, Route, Routes } from 'react-router-dom';
import Formulaire from "./components/Formulaire";
import './App.css';
import AccueilEtudiant from "./components/AccueilEtudiant";
import AccueilEmployeur from "./components/AccueilEmployeur";
import AccueilGestionnaire from "./components/AccueilGestionnaire";
import AccueilProfesseur from "./components/AccueilProfesseur";
import SoumettreOffre from "./components/SoumettreOffre";
import VisualiserOffres from "./components/VisualiserOffres";
import ListeEtudiants from './components/ListeEtudiants';
import DetailsEtudiants from './components/DetailsEtudiants';
import UpdateOffre from "./components/UpdateOffre";
import ListeEmployeurs from "./components/ListeEmployeurs";
import DetailsEmployeurs from "./components/DetailsEmployeur";
import ListeDeStage from  "./components/ListeDeStage";
import MesEntrevues from "./components/MesEntrevues";

function App() {
    return (
        <Router>
            <div className="App-body">
                <Routes>
                    <Route path="/" element={<Navigate to="/login" />} />

                    <Route path="/signUp" element={<Formulaire title="Inscription" />} />
                    <Route path="/login" element={<Formulaire title="Connexion" />} />

                    <Route path="/accueilEtudiant" element={<AccueilEtudiant />} />
                    <Route path="/mesEntrevues" element={<MesEntrevues />} />

                    <Route path="/accueilEmployeur" element={<AccueilEmployeur />} />
                    <Route path="/accueilGestionnaire" element={<AccueilGestionnaire />} />
                    <Route path="/accueilProfesseur" element={<AccueilProfesseur />} />
                    <Route path="/soumettre-offre" element={<SoumettreOffre />} />
                    <Route path="visualiser-offres" element={<VisualiserOffres />} />
                    <Route path="/update-offre" element={<UpdateOffre />} />
                    <Route path='/listeEtudiants' element={<ListeEtudiants />} />
                    <Route path='/detailsEtudiant/:email' element={<DetailsEtudiants />} />
                    <Route path='detailsEmployeur/:email/:id' element={<DetailsEmployeurs />} />
		            <Route path='/listeEmployeurs' element={<ListeEmployeurs />} />
		            <Route path="/listeDeStage" element={<ListeDeStage />} />
		            <Route path="/nonAutorise" element={<div>Accès non autorisé</div>} />
                    <Route path="*" element={<Navigate to="/login" replace />} />
                </Routes>
            </div>
      </Router>
  );
}

export default App;
