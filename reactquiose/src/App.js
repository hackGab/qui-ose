import React from "react";
import { BrowserRouter as Router, Navigate, Route, Routes } from 'react-router-dom';
import Formulaire from "./components/Formulaire";
import './App.css';
import AccueilEtudiant from "./components/Accueil/AccueilEtudiant";
import AccueilGestionnaire from "./components/Accueil/AccueilGestionnaire";
import AccueilProfesseur from "./components/Accueil/AccueilProfesseur";
import SoumettreOffre from "./components/SoumettreOffre";
import VisualiserOffres from "./components/VisualiserOffres";
import ListeEtudiants from './components/Liste/ListeEtudiants';
import DetailsEtudiants from './components/DetailsEtudiants';
import UpdateOffre from "./components/UpdateOffre";
import ListeEmployeurs from "./components/Liste/ListeEmployeurs";
import DetailsEmployeurs from "./components/DetailsEmployeur";
import ListeDeStage from "./components/Liste/ListeDeStage";
import EtudiantPostulants from "./components/EtudiantPostulants";
import StagesAppliqueesPage from "./components/StagesAppliqueesPage";
import MesEntrevues from "./components/MesEntrevues";
import ListeEntrevuesAcceptees from "./components/MesEntrevueAccepte";
import ListeCandidature from "./components/Liste/ListeCandidature";
import SignerContrat from "./components/SignerContrat";
import ListeProfesseurs from "./components/Liste/ListeProfesseurs";
import DetailsProfesseur from "./components/DetailsProfesseur";

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
		            <Route path="/stagesAppliquees" element={<StagesAppliqueesPage/>} />
                    <Route path="/accueilEmployeur" element={<VisualiserOffres />} />
                    <Route path="/accueilGestionnaire" element={<AccueilGestionnaire />} />
                    <Route path="/accueilProfesseur" element={<AccueilProfesseur />} />
                    <Route path="/soumettre-offre" element={<SoumettreOffre />} />
                    <Route path="/offre/:offreId/etudiants" element={<EtudiantPostulants />} />
                    <Route path="/update-offre" element={<UpdateOffre />} />
                    <Route path='/listeEtudiants' element={<ListeEtudiants />} />
                    <Route path='/detailsEtudiant/:email' element={<DetailsEtudiants />} />
                    <Route path='/detailsProfesseur/:email' element={<DetailsProfesseur />} />
                    <Route path='/detailsEmployeur/:email/:id' element={<DetailsEmployeurs />} />
		            <Route path='/listeEmployeurs' element={<ListeEmployeurs />} />
                    <Route path='/listeProfesseurs' element={<ListeProfesseurs />} />
		            <Route path="/listeDeStage" element={<ListeDeStage />} />
                    <Route path="/visualiser-entrevue-accepter" element={<ListeEntrevuesAcceptees />} />
                    <Route path="/listeCandidatures" element={<ListeCandidature />}/>
                    <Route path="/SignerContrat" element={<SignerContrat />} />
                    <Route path="/nonAutorise" element={<div>Accès non autorisé</div>} />
                    <Route path="*" element={<Navigate to="/login" replace />} />
                </Routes>
            </div>
      </Router>
  );
}

export default App;
