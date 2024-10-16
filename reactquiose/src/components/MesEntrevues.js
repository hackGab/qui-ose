import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import '../CSS/MesEntrevues.css';
import EtudiantHeader from "./EtudiantHeader";
import AffichageEntrevue from "./AffichageEntrevue";

function MesEntrevues() {
    const { t } = useTranslation();
    const [entrevues, setEntrevues] = useState([
        {
            id: 1,
            titre: 'Entrevue 1',
            offre: 'Développeur web',
            entreprise: 'Google',
            date: '2021-11-15',
            heureDebut: '13:00',
            heureFin: '14:00',
            duree: 60,
            status: 'accepter',
            localisation: 'Sur place'
        },
        {
            id: 2,
            titre: 'Entrevue 2',
            offre: 'Programmeur analyste Web',
            entreprise: 'Microsoft',
            date: '2021-11-16',
            heureDebut: '14:00',
            heureFin: "14:30",
            duree: 30,
            status: 'en attente',
            localisation: 'Sur Zoom'
        },
        {
            id: 3,
            titre: 'Entrevue 3',
            offre: 'Développeur web',
            entreprise: 'Apple',
            date: '2021-11-17',
            heureDebut: '15:00',
            heureFin: '16:00',
            duree: 60,
            status: 'rejeter',
            localisation: 'Sur place'
        },
        {
            id: 4,
            titre: 'Entrevue 4',
            offre: 'Programmeur analyste Web',
            entreprise: 'Amazon',
            date: '2021-11-18',
            heureDebut: '16:00',
            heureFin: '16:15',
            duree: 15,
            status: 'accepter',
            localisation: 'Sur Zoom'
        },
        {
            id: 5,
            titre: 'Entrevue 5',
            offre: 'Développeur web',
            entreprise: 'Facebook',
            date: '2021-11-19',
            heureDebut: '17:00',
            heureFin: '17:30',
            duree: 30,
            status: 'accepter',
            localisation: 'Sur place'
        },
    ]);

    // Filtrer les entrevues par status
    const entrevuesAccepter = entrevues.filter(entrevue => entrevue.status.toLowerCase() === 'accepter');
    const entrevuesEnAttente = entrevues.filter(entrevue => entrevue.status.toLowerCase() === 'en attente');


    return (
        <>
            <EtudiantHeader/>
            <div className="container-fluid p-4">
                <div className="container flex-grow-1 pt-4">
                    <h1 className="mb-0 text-center" style={{ fontSize: "4em" }}>{t('entrevueListTitle')}</h1>
                    <p className="text-center mb-4"  style={{ fontSize: "2em" }}>{t('entrevueListSubtitle')}</p>
                    <div className="row p-2 text-center w-100 m-auto">

                        {/* Entrevues accepter */}
                        <div className="col-5 m-auto">
                            <h2 className="entrevuesTitreBox">Acceptées</h2>

                            <div className="row p-1 shadow w-100 m-auto entrevueBox">
                                {entrevuesAccepter.map((entrevue) => <AffichageEntrevue entrevue={entrevue} t={t} />)}
                            </div>
                        </div>


                        {/* Entrevues en attente */}
                        <div className="col-5 m-auto mt-0">
                            <h2 className="entrevuesTitreBox">En attente</h2>

                            <div className="row p-1 shadow w-100 m-auto entrevueBox">
                                {entrevuesEnAttente.map((entrevue) => <AffichageEntrevue entrevue={entrevue} t={t} />)}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}

export default MesEntrevues;
