import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.min.css'; // Assurez-vous que Bootstrap est importé
import "../CSS/SoumettreOffre.css";
import EmployeurHeader from "./EmployeurHeader";

function SoumettreOffre() {
    const [formData, setFormData] = useState({
        titre: "",
        description: "",
        responsabilites: "",
        qualification: "",
        duree: "",
        localisation: "",
        exigences: "",
        dateDebutSouhaitee: "",
        typeRemuneration: "",
        disponibilite: "",
        contactInfo: "",
    });

    const [employeurEmail, setEmployeurEmail] = useState(null);
    const [errors, setErrors] = useState({});
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const location = useLocation();

    useEffect(() => {
        if (location.state && location.state.employeurEmail) {
            setEmployeurEmail(location.state.employeurEmail);
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
        if (!formData.localisation) newErrors.localisation = "Le lieu est requis";
        if (!formData.duree || formData.duree <= 0) newErrors.duree = "La durée doit être positive";
        if (!formData.exigences) newErrors.exigences = "Les exigences sont requises";
        if (!formData.dateDebutSouhaitee) newErrors.dateDebutSouhaitee = "La date de début souhaitée est requise";
        if (!formData.typeRemuneration) newErrors.typeRemuneration = "Le type de rémunération est requis";
        if (!formData.disponibilite) newErrors.disponibilite = "La disponibilité est requise";
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (e) => {
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
                        responsabilites: formData.responsabilites,
                        qualification: formData.qualification,
                        duree: formData.duree,
                        localisation: formData.localisation,
                        exigences: formData.exigences,
                        dateDebutSouhaitee: formData.dateDebutSouhaitee,
                        typeRemuneration: formData.typeRemuneration,
                        disponibilite: formData.disponibilite,
                        dateLimite: new Date().toISOString().split("T")[0],
                        salaire: formData.salaire,
                        contactInfo: formData.contactInfo,
                    }),
                });

                if (!response.ok) {
                    throw new Error("Échec de la soumission de l'offre");
                }

                const result = await response.json();
                console.log("Offre soumise avec succès:", result);
                // Optionnel : naviguer vers une autre page ou réinitialiser le formulaire

            } catch (error) {
                console.error("Erreur lors de la soumission:", error);
            } finally {
                setLoading(false);
            }
        }
    };

    return (
        <div className="container-fluid d-flex flex-column min-vh-100">
            <EmployeurHeader />

        <div className="container mt-5">
            <h2 className="text-center mt-5">Soumettre une Offre de Stage</h2>
            <form onSubmit={handleSubmit} className="p-4 border rounded shadow">
                {Object.keys(errors).length > 0 && (
                    <div className="alert alert-danger" role="alert">
                        {Object.values(errors).map((error, index) => (
                            <div key={index}>{error}</div>
                        ))}
                    </div>
                )}

                <div className="form-group mb-3">
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
                </div>

                <div className="form-group mb-3">
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
                </div>

                <div className="form-group mb-3">
                    <label htmlFor="responsabilites">Responsabilités *</label>
                    <input
                        type="text"
                        className="form-control"
                        id="responsabilites"
                        name="responsabilites"
                        value={formData.responsabilites}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group mb-3">
                    <label htmlFor="qualification">Qualifications *</label>
                    <input
                        type="text"
                        className="form-control"
                        id="qualification"
                        name="qualification"
                        value={formData.qualification}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group mb-3">
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
                </div>

                <div className="form-group mb-3">
                    <label htmlFor="localisation">Localisation *</label>
                    <input
                        type="text"
                        className="form-control"
                        id="localisation"
                        name="localisation"
                        value={formData.localisation}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group mb-3">
                    <label htmlFor="salaire">Salaire *</label>
                    <input
                        type="text"
                        className="form-control"
                        id="salaire"
                        name="salaire"
                        value={formData.salaire}
                        onChange={handleChange}
                    />
                </div>

                <div className="form-group mb-3">
                    <label htmlFor="exigences">Exigences *</label>
                    <input
                        type="text"
                        className="form-control"
                        id="exigences"
                        name="exigences"
                        value={formData.exigences}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group mb-3">
                    <label htmlFor="dateDebutSouhaitee">Date de début souhaitée *</label>
                    <input
                        type="date"
                        className="form-control"
                        id="dateDebutSouhaitee"
                        name="dateDebutSouhaitee"
                        value={formData.dateDebutSouhaitee}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group mb-3">
                    <label htmlFor="typeRemuneration">Type de rémunération *</label>
                    <input
                        type="text"
                        className="form-control"
                        id="typeRemuneration"
                        name="typeRemuneration"
                        value={formData.typeRemuneration}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group mb-3">
                    <label htmlFor="disponibilite">Disponibilité *</label>
                    <input
                        type="text"
                        className="form-control"
                        id="disponibilite"
                        name="disponibilite"
                        value={formData.disponibilite}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group mb-3">
                    <label htmlFor="contactInfo">Informations de contact *</label>
                    <input
                        type="text"
                        className="form-control"
                        id="contactInfo"
                        name="contactInfo"
                        value={formData.contactInfo}
                        onChange={handleChange}
                    />
                </div>

                <button type="submit" className="btn btn-primary w-100" disabled={loading}>
                    {loading ? "Soumission en cours..." : "Soumettre l'offre"}
                </button>
            </form>
        </div>
    </div>
    );
}

export default SoumettreOffre;
