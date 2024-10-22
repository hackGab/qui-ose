import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useNavigate, useLocation } from "react-router-dom";
import { useTranslation } from 'react-i18next';
import '../CSS/MesEntrevues.css';
import EtudiantHeader from "./EtudiantHeader";
import AffichageEntrevue from "./AffichageEntrevue";

function MesEntrevues({ sendDataToParent}) {
    const { t } = useTranslation();
    const location = useLocation();
    const [userData, setUserData] = useState(location.state?.userData || null);

    const [entrevues, setEntrevues] = useState([
        {
            id: 1,
            titre: 'Entrevue 1',
            offreDeStage: 'Développeur web',
            dateHeure: '2021-11-15 10:00:00',
            status: 'accepter',
            location: 'Sur place'
        },
        {
            id: 2,
            titre: 'Entrevue 2',
            offreDeStage: 'Programmeur analyste Web',
            dateHeure: '2021-11-16 10:00:00',
            status: 'en attente',
            location: 'Sur Zoom'
        },
        {
            id: 3,
            titre: 'Entrevue 3',
            offreDeStage: 'Développeur web',
            dateHeure: '2021-11-17 10:00:00',
            status: 'rejeter',
            location: 'Sur place'
        },
        {
            id: 4,
            titre: 'Entrevue 4',
            offreDeStage: 'Programmeur analyste Web',
            dateHeure: '2021-11-18 10:00:00',
            status: 'accepter',
            location: 'Sur Zoom'
        },
        {
            id: 5,
            titre: 'Entrevue 5',
            offreDeStage: 'Développeur web',
            dateHeure: '2021-11-19 10:00:00',
            status: 'accepter',
            location: 'Sur place'
        },
    ]);

    const entrevuesAccepter = entrevues.filter(entrevue => entrevue.status.toLowerCase() === 'accepter');
    const entrevuesEnAttente = entrevues.filter(entrevue => entrevue.status.toLowerCase() === 'en attente');
    const nbEntrevuesEnAttente = entrevuesEnAttente.length;

    useEffect(() => {
        sendDataToParent(nbEntrevuesEnAttente);
    }, [nbEntrevuesEnAttente, sendDataToParent]);

    return (
        <>
            {/* Passer nbEntrevuesEnAttente et userData */}
            <EtudiantHeader nbEntrevuesEnAttente={nbEntrevuesEnAttente} userData={userData} />
            <div className="container-fluid p-4">
                <div className="container flex-grow-1 pt-4">
                    <h1 className="mb-0 text-center" style={{ fontSize: "4em" }}>{t('entrevueListTitle')}</h1>
                    <p className="text-center mb-4"  style={{ fontSize: "2em" }}>{t('entrevueListSubtitle')}</p>
                    <div className="row p-2 text-center w-100 m-auto">

                        {/* Entrevues acceptées */}
                        <div className="col-5 m-auto">
                            <h2 className="entrevuesTitreBox">Acceptées</h2>
                            <div className="row p-1 shadow w-100 m-auto entrevueBox">
                                {entrevuesAccepter.map((entrevue) => <AffichageEntrevue key={entrevue.id} entrevue={entrevue} t={t} />)}
                            </div>
                        </div>

                        {/* Entrevues en attente */}
                        <div className="col-5 m-auto mt-0">
                            <h2 className="entrevuesTitreBox">En attente</h2>
                            <div className="row p-1 shadow w-100 m-auto entrevueBox">
                                {entrevuesEnAttente.map((entrevue) => <AffichageEntrevue key={entrevue.id} entrevue={entrevue} t={t} />)}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}

export default MesEntrevues;
