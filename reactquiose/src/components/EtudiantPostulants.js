import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { useTranslation } from "react-i18next";
import EmployeurHeader from "./EmployeurHeader";
import {Button} from "react-bootstrap";
import {FaEnvelope, FaPhone} from "react-icons/fa";

function EtudiantPostulants() {
    const { offreId } = useParams();
    const [offre, setOffre] = useState(null);
    const [etudiants, setEtudiants] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null)
    const { t } = useTranslation();


    useEffect(() => {
        if (!offreId) {
            setError("L'ID de l'offre est requis.");
            setIsLoading(false);
            return;
        }

        const fetchEtudiants = async () => {
            try {
                const response = await fetch(`http://localhost:8081/offreDeStage/${offreId}/etudiants`);

                if (!response.ok) {
                    if (response.status === 404) {
                        setEtudiants([]); // Aucun étudiant trouvé
                    } else {
                        throw new Error("dans la réponse du serveur");
                    }
                    return;
                }

                const data = await response.json();
                setEtudiants(data);
            } catch (error) {
                setError(error.message);
            } finally {
                setIsLoading(false);
            }
        };

        const fetchOffre = async () => {
            try {
                const response = await fetch(`http://localhost:8081/offreDeStage/${offreId}`);

                if (!response.ok) {
                    throw new Error("dans la réponse du serveur");
                }

                const data = await response.json();
                setOffre(data);
            } catch (error) {
                setError(error.message);
            }
        }

        fetchEtudiants();
        fetchOffre();
    }, [offreId]);

    if (isLoading) {
        return <div>Chargement des étudiants...</div>;
    }

    if (error) {
        return <div>Erreur: {error}</div>;
    }

    if (etudiants.length === 0) {
        return <div>Aucun étudiant n'a postulé à cette offre.</div>;
    }

    const openFile = (data) => {
        if (data) {
            const pdfWindow = window.open();
            pdfWindow.document.write(
                `<iframe src="${data}" style="border:0; top:0; left:0; bottom:0; right:0; width:100%; height:100%;" allowfullscreen></iframe>`
            );
        } else {
            alert("Aucun fichier à afficher !");
        }
    };

    return (
        <>
            <EmployeurHeader />
            <div className="container-fluid p-4">
                <div className="container flex-grow-1 pt-5 mt-5 text-center">
                    <h3>{t('EtudiantsPostulants')} {t('AuPosteDe')} <u>{offre.titre}</u> :</h3>
                    <div className="row mt-4">
                        {etudiants.map((etudiant) => (
                            <div key={etudiant.id} className="col-10 col-sm-6 col-md-4 col-lg-3 mb-4 m-auto text-center">
                                <div className="card-container">
                                    <div className="card">
                                        <div className="card-body">
                                            <h5 className="card-title text-capitalize">{etudiant.firstName} {etudiant.lastName}</h5>
                                            <p className="card-text text-truncate">
                                                <span><FaEnvelope /> <a href={`mailto:${etudiant.email}`}>{etudiant.email}</a></span>
                                                <br />
                                                <FaPhone /> {etudiant.phoneNumber}
                                                <br />
                                                {t('departmentDetail')} {etudiant.departement}
                                            </p>
                                            <Button variant="primary" onClick={() => openFile(etudiant.cv.data)}>{t('viewCV')}</Button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </>
    );
}

export default EtudiantPostulants;