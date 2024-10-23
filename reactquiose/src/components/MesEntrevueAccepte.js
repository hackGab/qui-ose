import { useLocation, useNavigate } from "react-router-dom";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import EmployeurHeader from "./EmployeurHeader";
import "../CSS/MesEntrevueAccepte.css";
import {forEach} from "react-bootstrap/ElementChildren";

function MesEntrevueAccepte() {
    const location = useLocation();
    const userData = location.state?.userData;
    const employeurEmail = userData.credentials.email;
    const [offres, setOffres] = useState([]);
    const [entrevues, setEntrevues] = useState({});
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const { t } = useTranslation();

    useEffect(() => {
        const fetchOffresEntrevues = async () => {
            if (!employeurEmail) {
                setError("Email employeur non fourni");
                setIsLoading(false);
                return;
            }

            try {
                const response = await fetch(`http://localhost:8081/offreDeStage/offresEmployeur/${employeurEmail}`);
                if (response.status === 404) {
                    setOffres([]);
                    return;
                }

                const data = await response.json();
                setOffres(data);
                console.log("offres", data);

                for (const offre of data) {
                    const responseEntrevues = await fetch(`http://localhost:8081/entrevues/entrevueAcceptee/offre/${offre.id}`);
                    const entrevuesData = await responseEntrevues.json();

                    console.log(entrevuesData)

                    setEntrevues((prevEntrevues) => ({
                        ...prevEntrevues,
                        [offre.id]: entrevuesData,
                    }));
                }
            } catch (error) {
                setError(error.message);
            } finally {
                setIsLoading(false);
            }
        };


        fetchOffresEntrevues();
    }, [employeurEmail]);

    if (isLoading) {
        return <div>{t('ChargementDesEntrevues')}</div>;
    }

    if (error) {
        return <div>{t('Erreur')} {error}</div>;
    }

    return (
        <>
            <EmployeurHeader userData={userData}/>
            <div className="container-fluid p-4 mes-entrevues-container">
                <div className="container mt-5">
                    <h1 className="text-center mt-5 page-title">{t('vosEntrevues')}</h1>

                    {entrevues.length === 0 ? (
                        <div className="alert alert-info mt-3 no-offres-alert">{t('AccuneOffreTrouve')}</div>
                    ) : (
                        <div className="row mt-3">
                            {offres
                                .filter((offre) => entrevues[offre.id] && entrevues[offre.id].length > 0)
                                .map((offre) => (
                                    <div key={offre.id} className="col-md-12 offre-card">
                                        <h5 className="offre-title">{t('Offre')} #{offre.id}: {offre.titre}</h5>
                                        <ul className="entrevue-list">
                                            {entrevues[offre.id].map((entrevue, index) => (
                                                <li key={index} className="entrevue-item text-capitalize">
                                                    <strong>{t('Entrevue')}</strong> - {entrevue.etudiantDTO.firstName} {entrevue.etudiantDTO.lastName} <br />
                                                    <span className="entrevue-details">{new Date(entrevue.dateHeure).toLocaleString()} - {entrevue.location}</span>

                                                {/* mes 2 boutons pour accepter ou refuser */}
                                                </li>
                                            ))}
                                        </ul>
                                    </div>
                                ))}
                        </div>
                    )}
                </div>
            </div>
        </>
    );
}

export default MesEntrevueAccepte;
