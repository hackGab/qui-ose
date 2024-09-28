import React, { useState } from "react";
import {useNavigate} from "react-router-dom";

function SoumettreOffre() {
    const [formData, setFormData] = useState({
        titre: "",
        description: "",
        entreprise: "",
        lieu: "",
        duree: "",
        remuneration: "",
    });
    const navigate = useNavigate();

    const [errors, setErrors] = useState({});

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleClickGoBack = (e) => {

        navigate("/accueilEmployeur")
    }

    const validateForm = () => {
        const newErrors = {};
        if (!formData.titre) newErrors.titre = "Le titre est requis";
        if (!formData.description) newErrors.description = "La description est requise";
        if (!formData.entreprise) newErrors.entreprise = "Le nom de l'entreprise est requis";
        if (!formData.lieu) newErrors.lieu = "Le lieu est requis";
        if (!formData.duree || formData.duree <= 0) newErrors.duree = "La durée doit être positive";
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (validateForm()) {
            // Envoyer les données au backend ou API
            console.log("Formulaire soumis:", formData);
        }
    };

    return (
        <div className="container mt-5">
            <h2>Soumettre une Offre de Stage</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="titre">Titre de l'offre *</label>
                    <input
                        type="text"
                        className="form-control"
                        id="titre"
                        name="titre"
                        value={formData.titre}
                        onChange={handleChange}
                        required
                    />
                    {errors.titre && <small className="text-danger">{errors.titre}</small>}
                </div>

                <div className="form-group">
                    <label htmlFor="description">Description *</label>
                    <textarea
                        className="form-control"
                        id="description"
                        name="description"
                        rows="4"
                        value={formData.description}
                        onChange={handleChange}
                        required
                    ></textarea>
                    {errors.description && <small className="text-danger">{errors.description}</small>}
                </div>

                <div className="form-group">
                    <label htmlFor="entreprise">Entreprise *</label>
                    <input
                        type="text"
                        className="form-control"
                        id="entreprise"
                        name="entreprise"
                        value={formData.entreprise}
                        onChange={handleChange}
                        required
                    />
                    {errors.entreprise && <small className="text-danger">{errors.entreprise}</small>}
                </div>

                <div className="form-group">
                    <label htmlFor="lieu">Lieu du stage *</label>
                    <input
                        type="text"
                        className="form-control"
                        id="lieu"
                        name="lieu"
                        value={formData.lieu}
                        onChange={handleChange}
                        required
                    />
                    {errors.lieu && <small className="text-danger">{errors.lieu}</small>}
                </div>

                <div className="form-group">
                    <label htmlFor="duree">Durée (en mois) *</label>
                    <input
                        type="number"
                        className="form-control"
                        id="duree"
                        name="duree"
                        min="1"
                        value={formData.duree}
                        onChange={handleChange}
                        required
                    />
                    {errors.duree && <small className="text-danger">{errors.duree}</small>}
                </div>

                <div className="form-group">
                    <label htmlFor="remuneration">Rémunération (facultatif)</label>
                    <input
                        type="text"
                        className="form-control"
                        id="remuneration"
                        name="remuneration"
                        value={formData.remuneration}
                        onChange={handleChange}
                    />
                </div>

                <button type="submit" className="btn btn-primary">
                    Soumettre l'offre
                </button>
                <button className="btn btn-success" onClick={handleClickGoBack}>revenir</button>
            </form>
        </div>
    );
}

export default SoumettreOffre;
