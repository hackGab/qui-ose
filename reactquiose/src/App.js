import React from "react";
import { BrowserRouter as Router, Navigate, Route, Routes } from "react-router-dom";
import { AuthProvider } from "./context/AuthProvider"; // Import path updated
import Formulaire from "./components/Formulaire";
import AccueilEtudiant from "./components/AccueilEtudiant";
import AccueilEmployeur from "./components/AccueilEmployeur";
import AccueilGestionnaire from "./components/AccueilGestionnaire";
import AccueilProfesseur from "./components/AccueilProfesseur";
import SoumettreOffre from "./components/SoumettreOffre";
import PrivateRoute from "./components/PrivateRoute"; // Import path updated

function App() {
    return (
        <AuthProvider>
            <Router>
                <div className="App-body">
                    <Routes>
                        <Route path="/" element={<Navigate to="/login" />} />
                        <Route path="/signUp" element={<Formulaire title="Inscription" />} />
                        <Route path="/login" element={<Formulaire title="Connexion" />} />

                        {/* Routes for different user roles */}
                        <Route
                            path="/accueilEtudiant"
                            element={
                                <PrivateRoute allowedRoles={['ETUDIANT']}>
                                    <AccueilEtudiant />
                                </PrivateRoute>
                            }
                        />

                        <Route
                            path="/accueilEmployeur"
                            element={
                                <PrivateRoute allowedRoles={['EMPLOYEUR']}>
                                    <AccueilEmployeur />
                                </PrivateRoute>
                            }
                        />

                        <Route
                            path="/accueilGestionnaire"
                            element={
                                <PrivateRoute allowedRoles={['GESTIONNAIRE']}>
                                    <AccueilGestionnaire />
                                </PrivateRoute>
                            }
                        />

                        <Route
                            path="/accueilProfesseur"
                            element={
                                <PrivateRoute allowedRoles={['PROFESSEUR']}>
                                    <AccueilProfesseur />
                                </PrivateRoute>
                            }
                        />

                        <Route
                            path="/soumettre-offre"
                            element={
                                <PrivateRoute allowedRoles={['EMPLOYEUR']}>
                                    <SoumettreOffre />
                                </PrivateRoute>
                            }
                        />

                        {/* Route for unauthorized access */}
                        <Route path="/nonAutorise" element={<div>Accès non autorisé</div>} />
                    </Routes>
                </div>
            </Router>
        </AuthProvider>
    );
}

export default App;
