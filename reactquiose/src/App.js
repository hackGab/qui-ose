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

function App() {
    return (
        <Router>
            <div className="App-body">
                <Routes>
                    <Route path="/" element={<Navigate to="/login" />} />

                    <Route path="/signUp" element={<Formulaire title="Inscription" />} />
                    <Route path="/login" element={<Formulaire title="Connexion" />} />

                    <Route path="/accueilEtudiant" element={<AccueilEtudiant />} />
                    <Route path="/accueilEmployeur" element={<AccueilEmployeur />} />
                    <Route path="/accueilGestionnaire" element={<AccueilGestionnaire />} />
                    <Route path="/accueilProfesseur" element={<AccueilProfesseur />} />
                    <Route path="/soumettre-offre" element={<SoumettreOffre />} />
                    <Route path="visualiser-offres" element={<VisualiserOffres />} />
                    <Route path="/update-offre" element={<UpdateOffre />} />
                    <Route path='/listeEtudiants' element={<ListeEtudiants />} />
                    <Route path='/detailsEtudiant/:id' element={<DetailsEtudiants />} />

                    <Route path="/nonAutorise" element={<div>Accès non autorisé</div>} />

                    <Route path="*" element={<Navigate to="/login" replace />} />
                </Routes>
            </div>
      </Router>
  );
}

export default App;
