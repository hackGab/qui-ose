import React from "react";
import { useLocation } from "react-router-dom";

function ListeDeStage() {
    const location = useLocation();
    const internships = location.state?.internships || []; // Récupération des offres de stages

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
        <div className="container">
            <h3 className="text-center my-4">Offres de Stage</h3>
            <div className="row">
                {internships.length > 0 ? (
                    internships.map((internship, index) => (
                        <div key={index} className="col-lg-4 col-md-6 col-sm-12 p-2">
                            <div className="card my-3 h-100">
                                <div className="card-body">
                                    <h5 className="card-title">{internship.titre}</h5>
                                    <h6 className="card-subtitle mb-2 text-muted">{internship.localisation}</h6>
                                    <p className="card-text">
                                        <strong>Date limite de candidature:</strong> {internship.dateLimite}
                                    </p>
                                    <p className="card-text">
                                        <strong>Date de publication:</strong> {internship.datePublication}
                                    </p>
                                    <div className="d-flex justify-content-center my-3">
                                        <button className="btn btn-info" onClick={() => openFile(internship.data)}>
                                            Voir candidature
                                        </button>
                                    </div>
                                    <p className="card-text">
                                        <strong>Nombre de candidats:</strong> {internship.nbCandidats}
                                    </p>
                                </div>
                            </div>
                        </div>
                    ))
                ) : (
                    <p>Aucune offre de stage à afficher.</p>
                )}
            </div>
        </div>
    );
}

export default ListeDeStage;
