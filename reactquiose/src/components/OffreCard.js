import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEdit, faTrash } from "@fortawesome/free-solid-svg-icons";

const OffreCard = ({ offre, onEdit, onViewList, onDelete }) => {
    const getStatusClass = (status) => {
        switch (status) {
            case "Validé": return "status-green";
            case "Rejeté": return "status-red";
            default: return "status-yellow";
        }
    };

    return (
        <div className="col-md-4 mb-4">
            <div className="card">
                <div className="card-body">
                    <h5 className="card-title">{offre.titre}</h5>
                    <p className="card-text">
                        <strong>Localisation:</strong> {offre.localisation}
                    </p>
                    <div className={`status-badge ${getStatusClass(offre.status)}`}>
                        {offre.status}
                    </div>
                    <div className="d-flex justify-content-between mt-3">
                        <FontAwesomeIcon icon={faEdit} className="text-warning" onClick={onEdit} />
                        <FontAwesomeIcon icon={faTrash} className="text-danger" onClick={onDelete} />
                    </div>
                </div>
            </div>
        </div>
    );
};

export default OffreCard;
