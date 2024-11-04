import React, {useEffect, useState} from "react";
import {Link, useLocation} from "react-router-dom";
import EtudiantHeader from "./EtudiantHeader";
import ProfesseurHeader from "./ProfesseurHeader";
import {t} from "i18next";
import {useTranslation} from "react-i18next";
import {FaEnvelope, FaPhone} from "react-icons/fa";

function AccueilProfesseur() {
    const {t} = useTranslation();
    const location = useLocation();
    const { userData } = location.state || {};
    const [listeEtudiants, setListeEtudiants] = useState([]);

    useEffect(() => {
        if (userData) {
            const url = `http://localhost:8081/professeur/etudiants/${userData.credentials.email}`;

            fetch(url)
                .then(response => response.json())
                .then(data => {
                    console.log("Liste des étudiants à évaluer:", data);
                    setListeEtudiants(data);
                }
            );
        }
    }, []);

    return (
        <>
            <ProfesseurHeader userData={userData} />
            <div className="container-fluid p-4">

                <h2 className="text-center my-4 text-capitalize"
                    style={{color: "#01579b"}}>{t('Bienvenue')}, {userData ? userData.firstName + " " + userData.lastName : ""}!</h2>

                <div className="container flex-grow-1 pt-5 mt-5">
                    <h1 className="mb-4 text-center">{t('studentListTitle')}</h1>

                    {listeEtudiants.length === 0 ? (
                        <p className="text-center mt-5">{t('AucunEtudiantTrouve')}</p>
                    ) : (
                        <p className="text-center mb-4">{t('studentListSubtitle')}</p>
                    )}

                    <div className="row">
                        {listeEtudiants.map((etudiant) => {
                            const status = etudiant.cv ? etudiant.cv.status : null;
                            return (
                                <div className="col-12 col-md-6 col-lg-4 mb-4" key={etudiant.id}>
                                        <div
                                            className={`card shadow w-100 ${status ? status.toLowerCase() : 'sans-cv'}`}>
                                            <div className="card-body">
                                                <h5 className="card-title text-capitalize">{`${etudiant.firstName} ${etudiant.lastName}`}</h5>
                                                <p className="card-text">
                                                    <FaEnvelope/> {etudiant.credentials.email}<br/>
                                                    <FaPhone/> {etudiant.phoneNumber}<br/>
                                                    <span
                                                        className="badge bg-info">{t('department')}: {etudiant.departement}</span>
                                                </p>
                                            </div>
                                        </div>
                                </div>
                            );
                        })}
                    </div>
                </div>
            </div>
        </>
    );
}

export default AccueilProfesseur;
