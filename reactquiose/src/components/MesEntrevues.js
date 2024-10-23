import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useNavigate, useLocation } from "react-router-dom";
import { useTranslation } from 'react-i18next';
import '../CSS/MesEntrevues.css';
import EtudiantHeader from "./EtudiantHeader";
import AffichageEntrevue from "./AffichageEntrevue";

function MesEntrevues() {
    const { t } = useTranslation();
    const location = useLocation();
    const [userData, setUserData] = useState(location.state?.userData || null);
    const [entrevues, setEntrevues] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const email = userData?.credentials.email; // Assurez-vous que userData contient l'email de l'étudiant
        if (email) {
            fetch(`http://localhost:8081/entrevues/etudiant/${email}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Erreur lors de la récupération des entrevues');
                    }
                    return response.json();
                })
                .then(data => {
                    console.log('Réponse du serveur:', data);
                    setEntrevues(data);
                    setLoading(false);
                })
                .catch(err => {
                    setError(err.message);
                    setLoading(false);
                });
        }
    }, [userData]);

    const handleEntrevueAcceptee = (entrevueAcceptee) => {
        setEntrevues(prevEntrevues =>
            prevEntrevues.map(entrevue =>
                entrevue.id === entrevueAcceptee.id
                    ? { ...entrevue, status: 'accepter' }
                    : entrevue
            )
        );
    };

    const entrevuesAccepter = entrevues.filter(entrevue => entrevue.status.toLowerCase() === 'accepter');
    const entrevuesEnAttente = entrevues.filter(entrevue => entrevue.status.toLowerCase() === 'en attente');
    const nbEntrevuesEnAttente = entrevuesEnAttente.length;

    if (loading) {
        return <div>Chargement...</div>;
    }

    if (error) {
        return <div>Erreur: {error}</div>;
    }

    return (
        <>
            <EtudiantHeader userData={userData} />
            <div className="container-fluid p-4">
                <div className="container flex-grow-1 pt-4">
                    <h1 className="mb-0 text-center" style={{ fontSize: "4em" }}>{t('entrevueListTitle')}</h1>
                    <p className="text-center mb-4" style={{ fontSize: "2em" }}>{t('entrevueListSubtitle')}</p>
                    <div className="row p-2 text-center w-100 m-auto">

                        {/* Entrevues acceptées */}
                        <div className="col-5 m-auto">
                            <h2 className="entrevuesTitreBox">Acceptées</h2>
                            <div className="row p-1 shadow w-100 m-auto entrevueBox">
                                {entrevuesAccepter.map((entrevue) => (
                                    <AffichageEntrevue
                                        key={entrevue.id}
                                        entrevue={entrevue}
                                        t={t}
                                    />
                                ))}
                            </div>
                        </div>

                        {/* Entrevues en attente */}
                        <div className="col-5 m-auto mt-0">
                            <h2 className="entrevuesTitreBox">En attente</h2>
                            <div className="row p-1 shadow w-100 m-auto entrevueBox">
                                {entrevuesEnAttente.map((entrevue) => (
                                    <AffichageEntrevue
                                        key={entrevue.id}
                                        entrevue={entrevue}
                                        t={t}
                                        onAccept={handleEntrevueAcceptee} // Passer la fonction pour gérer l'acceptation
                                    />
                                ))}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}

export default MesEntrevues;
