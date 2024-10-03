import { BrowserRouter as Router, Navigate, Route, Routes } from 'react-router-dom';
import Formulaire from "./components/Formulaire";
import './App.css';
import AccueilEtudiant from "./components/AccueilEtudiant";
import AccueilEmployeur from "./components/AccueilEmployeur";
import AccueilGestionnaire from "./components/AccueilGestionnaire";
import AccueilProfesseur from "./components/AccueilProfesseur";
import ListeEtudiants from './components/ListeEtudiants';
import DetailsEtudiants from './components/DetailsEtudiants';
import ListeEmployeurs from "./components/ListeEmployeurs";
import DetailsEmployeurs from "./components/DetailsEmployeur";

function App() {
    return (
        <Router>
            <div className='App-body'>
                <Routes>
                    <Route path='/' element={<Navigate to='/login' />} />
                    <Route path='/signUp' element={<Formulaire title="Inscription" />} />
                    <Route path='/login' element={<Formulaire title="Connexion" />} />
                    <Route path='/accueilEtudiant' element={<AccueilEtudiant />} />
                    <Route path='/accueilEmployeur' element={<AccueilEmployeur />} />
                    <Route path='/accueilGestionnaire' element={<AccueilGestionnaire />} />
                    <Route path='/accueilProfesseur' element={<AccueilProfesseur />} />
                    <Route path='/listeEtudiants' element={<ListeEtudiants />} />
                    <Route path='/listeEmployeurs' element={<ListeEmployeurs />} />
                    <Route path='/detailsEtudiant/:id' element={<DetailsEtudiants />} />
                    <Route path='detailsEmployeur/:id' element={<DetailsEmployeurs />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
