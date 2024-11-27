// routes.js
import AccueilEtudiant from "../components/Accueil/AccueilEtudiant";
import Formulaire from "../components/Formulaire";
import VisualiserOffres from "../components/VisualiserOffres";
import AccueilGestionnaire from "../components/Accueil/AccueilGestionnaire";
import AccueilProfesseur from "../components/Accueil/AccueilProfesseur";
import SoumettreOffre from "../components/SoumettreOffre";
import ListeEtudiants from "../components/Liste/ListeEtudiants";
import DetailsEtudiants from "../components/Details/DetailsEtudiants";
import DetailsProfesseur from "../components/Details/DetailsProfesseur";
import DetailsEmployeurs from "../components/Details/DetailsEmployeur";
import ListeEmployeurs from "../components/Liste/ListeEmployeurs";
import ListeProfesseurs from "../components/Liste/ListeProfesseurs";
import ListeDeStage from "../components/Liste/ListeDeStage";
import EtudiantPostulants from "../components/EtudiantPostulants";
import StagesAppliqueesPage from "../components/StagesAppliqueesPage";
import MesEntrevues from "../components/MesEntrevues";
import UpdateOffre from "../components/UpdateOffre";
import ListeEntrevuesAcceptees from "../components/MesEntrevueAccepte";
import ListeCandidature from "../components/Liste/ListeCandidature";
import SignerContrat from "../components/SignerContrat";
import { Navigate } from "react-router-dom";


export const routes = [
    { path: "/", element: <Navigate to="/login" /> },
    { path: "/signUp", element: <Formulaire title="Inscription" /> },
    { path: "/login", element: <Formulaire title="Connexion" /> },
    { path: "/accueilEtudiant", element: <AccueilEtudiant /> },
    { path: "/mesEntrevues", element: <MesEntrevues /> },
    { path: "/stagesAppliquees", element: <StagesAppliqueesPage /> },
    { path: "/accueilEmployeur", element: <VisualiserOffres /> },
    { path: "/accueilGestionnaire", element: <AccueilGestionnaire /> },
    { path: "/accueilProfesseur", element: <AccueilProfesseur /> },
    { path: "/soumettre-offre", element: <SoumettreOffre /> },
    { path: "/offre/:offreId/etudiants", element: <EtudiantPostulants /> },
    { path: "/update-offre", element: <UpdateOffre /> },
    { path: "/listeEtudiants", element: <ListeEtudiants /> },
    { path: "/detailsEtudiant/:email", element: <DetailsEtudiants /> },
    { path: "/detailsProfesseur/:email", element: <DetailsProfesseur /> },
    { path: "/detailsEmployeur/:email/:id", element: <DetailsEmployeurs /> },
    { path: "/listeEmployeurs", element: <ListeEmployeurs /> },
    { path: "/listeProfesseurs", element: <ListeProfesseurs /> },
    { path: "/listeDeStage", element: <ListeDeStage /> },
    { path: "/visualiser-entrevue-accepter", element: <ListeEntrevuesAcceptees /> },
    { path: "/listeCandidatures", element: <ListeCandidature /> },
    { path: "/SignerContrat", element: <SignerContrat /> },
    { path: "/nonAutorise", element: <div>Accès non autorisé</div> },
    { path: "*", element: <Navigate to="/login" replace /> },
];
