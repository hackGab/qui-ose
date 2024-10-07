import React, { useEffect, useState } from "react";
import { useLocation,useNavigate } from "react-router-dom";
import "../CSS/VisualiserOffres.css";
import EmployeurHeader from "./EmployeurHeader";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEdit, faTrash } from '@fortawesome/free-solid-svg-icons';

function VisualiserOffres() {
    const location = useLocation();
    const navigate = useNavigate();
    const employeurEmail = location.state?.employeurEmail;
    const [offres, setOffres] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedOffre, setSelectedOffre] = useState(null);
    const [deletingId, setDeletingId] = useState(null);

    useEffect(() => {
        const fetchOffres = async () => {
            if (!employeurEmail) {
                setError("Email employeur non fourni");
                setIsLoading(false);
                return;
            }

            try {
                const response = await fetch(`http://localhost:8081/offreDeStage/offresEmployeur/${employeurEmail}`);
                if (response.status === 404){
                    setOffres([]);
                    return;
                }

                const data = await response.json();

                if (data.length === 0) {
                    setOffres([]);
                } else {
                    setOffres(data);
                }
            } catch (error) {
                setError(error.message);
            } finally {
                setIsLoading(false);
            }
        };

        fetchOffres();
    }, [employeurEmail]);

    const getStatusClass = (status) => {
        switch (status) {
            case "Accepter":
                return "status-green";
            case "Rejeter":
                return "status-red";
            case "Attente":
            default:
                return "status-yellow";
        }
    };

    const handleOffreClick = (index) => {
        if (selectedOffre === index) {
            setSelectedOffre(null);
        } else {
            setSelectedOffre(index);
        }
    };

    const openPDF = (data) => {
        const pdfWindow = window.open();
        pdfWindow.document.write(
            `<iframe src="${data}" style="border:0; top:0; left:0; bottom:0; right:0; width:100%; height:100%;" allowfullscreen></iframe>`
        );
    };

    const deleteOffre = async (id) => {
        const confirmDelete = window.confirm("Voulez-vous vraiment supprimer cette offre ?");
        if (confirmDelete) {
            setDeletingId(id);
            try {
                const response = await fetch(`http://localhost:8081/offreDeStage/${id}`, {
                    method: "DELETE",
                });
                if (response.status === 204) {
                    setOffres(offres.filter((offre) => offre.id !== id));
                    setSelectedOffre(null);
                } else {
                    setError("Erreur lors de la suppression de l'offre");
                }
            } catch (error) {
                setError("Erreur lors de la suppression de l'offre");
            } finally {
                setDeletingId(null);
            }
        }
    };

    const handleUpdateClick = (offre, employeurEmail) => {
        navigate("/update-offre", { state: { offre,employeurEmail } });
    };

    if (isLoading) {
        return <div>Chargement des offres...</div>;
    }

    if (error) {
        return <div>Erreur : {error}</div>;
    }

    return (
        <div className="container mt-5">
            <EmployeurHeader />
            <h1 className="text-center mt-5">Vos offres d'emploi</h1>

            {offres.length === 0 ? (
                <div className="alert alert-info mt-3">Aucune offre trouvée</div>
            ) : (
                <div className="row mt-3">
                    {offres.map((offre, index) => (
                        <div key={index} className="col-md-4 mb-4">
                            <div
                                className={`card offre-card ${deletingId === offre.id ? "fade-out" : ""}`}
                                onClick={() => handleOffreClick(index)}
                            >
                                <div className="card-body">
                                    <h5 className="card-title">{offre.titre}</h5>
                                    <p className="card-text">
                                        <strong>Localisation :</strong> {offre.localisation} <br/>
                                        <strong>Nombre de candidats :</strong> {offre.nbCandidats}
                                    </p>
                                    <small>
                                        Date de publication : {new Date(offre.datePublication).toLocaleDateString()}
                                        <br/>
                                        Date limite : {new Date(offre.dateLimite).toLocaleDateString()}
                                    </small>
                                    <div className={`status-badge ${getStatusClass(offre.status)}`}>
                                        <strong>Status :</strong> {offre.status}
                                    </div>

                                    {selectedOffre === index && (
                                        <>
                                            <div
                                                className="card pdf-card mt-4"
                                                style={{backgroundColor: "#f0f0f0"}}
                                                onClick={() => openPDF(offre.data)}
                                            >
                                                <div className="card-body text-center">
                                                    <h5 className="card-title">Voir le fichier PDF</h5>
                                                    <p className="card-text">Cliquez ici pour ouvrir le PDF de l'offre
                                                        sélectionnée.</p>
                                                </div>
                                            </div>
                                            <div className="d-flex justify-content-between mt-4">
                                                <FontAwesomeIcon
                                                    icon={faEdit}
                                                    size="2x"
                                                    className="text-warning"
                                                    style={{cursor: "pointer"}}
                                                    onClick={(e) => {
                                                        e.stopPropagation();
                                                        handleUpdateClick(offre, employeurEmail);
                                                    }}
                                                />

                                                <FontAwesomeIcon
                                                    icon={faTrash}
                                                    size="2x"
                                                    className="text-danger"
                                                    style={{cursor: "pointer"}}
                                                    onClick={(e) => {
                                                        e.stopPropagation();
                                                        deleteOffre(offre.id);
                                                    }}
                                                />
                                            </div>
                                        </>
                                    )}
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default VisualiserOffres;