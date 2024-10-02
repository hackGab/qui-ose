import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";

function SoumettreOffre() {
    const [formData, setFormData] = useState({
        titre: "",
        description: "",
        responsabilites: "",
        qualifications: "",
        duree: "",
        localisation: "",
        salaire: "",
        dateLimite: "", // Cette valeur n'est pas nécessaire ici car elle est calculée dans le handleSubmit
    });

    const [employeurEmail, setEmployeurEmail] = useState(null); // State to hold employer email
    const [errors, setErrors] = useState({});
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const location = useLocation(); // Hook to access passed state

    // Retrieve the employer's email from the state passed during navigation
    useEffect(() => {
        if (location.state && location.state.employeurEmail) {
            setEmployeurEmail(location.state.employeurEmail); // Set email to state
        }
    }, [location.state]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const validateForm = () => {
        const newErrors = {};
        if (!formData.titre) newErrors.titre = "Le titre est requis";
        if (!formData.description) newErrors.description = "La description est requise";
        if (!formData.localisation) newErrors.localisation = "Le lieu est requis"; // Corrected to 'localisation'
        if (!formData.duree || formData.duree <= 0) newErrors.duree = "La durée doit être positive";
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (e) => {
        console.log("Soumettre l'offre" + JSON.stringify(formData)); // Use JSON.stringify for better logging
        e.preventDefault();
        if (validateForm()) {
            setLoading(true);
            try {
                const url = `http://localhost:8081/offreDeStage/creerOffreDeStage/${employeurEmail}`;

                const response = await fetch(url, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        titre: formData.titre,
                        description: formData.description,
                        responsabilites: formData.responsabilites, // Corrected to 'responsabilites'
                        qualifications: formData.qualifications,
                        duree: formData.duree,
                        localisation: formData.localisation,
                        salaire: formData.salaire,
                        dateLimite: new Date().toISOString().split("T")[0],
                        employeur: null,
                       // Example: today's date
                    }),
                });

                if (!response.ok) {
                    throw new Error("Failed to submit the offer");
                }

                const result = await response.json();
                console.log("Offre soumise avec succès:", result);

            } catch (error) {
                console.error("Erreur lors de la soumission:", error);
            } finally {
                setLoading(false);
            }
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
                    <label htmlFor="responsabilites">Responsabilites *</label>
                    <input
                        type="text"
                        className="form-control"
                        id="responsabilites"
                        name="responsabilites"
                        value={formData.responsabilites} // Ensure this is part of formData
                        onChange={handleChange}
                        required
                    />
                    {errors.responsabilites && <small className="text-danger">{errors.responsabilites}</small>}
                </div>

                <div className="form-group">
                    <label htmlFor="qualifications">Qualifications *</label>
                    <input
                        type="text"
                        className="form-control"
                        id="qualifications"
                        name="qualifications" // Corrected to 'localisation'
                        value={formData.qualifications}
                        onChange={handleChange}
                        required
                    />
                    {errors.qualifications && <small className="text-danger">{errors.qualifications}</small>}
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
                    <label htmlFor="localisation">Localisation *</label>
                    <input
                        type="text"
                        className="form-control"
                        id="localisation"
                        name="localisation" // Change to match formData
                        value={formData.localisation}
                        onChange={handleChange}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="salaire">Salaire</label>
                    <input
                        type="text"
                        className="form-control"
                        id="salaire"
                        name="salaire"
                        value={formData.salaire}
                        onChange={handleChange}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="dateLimite">Date limite</label>
                    <input
                        type="date"
                        className="form-control"
                        id="dateLimite"
                        name="dateLimite"
                        value={formData.dateLimite}
                        onChange={handleChange}
                    />
                </div>

                <button type="submit" className="btn btn-primary" disabled={loading}>
                    {loading ? "Soumission en cours..." : "Soumettre l'offre"}
                </button>
            </form>
        </div>
    );
}

export default SoumettreOffre;
