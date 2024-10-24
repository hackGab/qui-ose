import React, { useEffect, useState } from 'react';
import GestionnaireHeader from './GestionnaireHeader';
import '../CSS/ListeCandidature.css';

function ListeCandidature() {
    const [candidatures, setCandidatures] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedCandidat, setSelectedCandidat] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [formData, setFormData] = useState({
        lieuStage: '',
        dateDebut: '',
        dateFin: '',
        semaines: '',
        horaireTravail: '',
        heuresParSemaine: '',
        tauxHoraire: '',
        description: '',
        collegeEngagement: '',
        entrepriseEngagement: '',
        etudiantEngagement: '',
    });

    useEffect(() => {
        fetch('http://localhost:8081/candidatures/all')
            .then(response => response.json())
            .then(data => {
                console.log("Candidatures:", data);
                setCandidatures(data);
                const fetchPromises = data.map(candidat =>
                    fetch(`http://localhost:8081/entrevues/${candidat.entrevueId}`)
                        .then(response => response.json())
                        .then(entrevueDetails => ({ ...candidat, entrevueDetails }))
                        .catch(() => ({ ...candidat, entrevueDetails: null }))
                );
                return Promise.all(fetchPromises);
            })
            .then(candidatsWithEntrevues => {
                console.log("Candidatures avec détails d'entrevues:", candidatsWithEntrevues);
                setCandidatures(candidatsWithEntrevues);
                setLoading(false);
            })
            .catch(err => {
                setError(err.message);
                setLoading(false);
            });
    }, []);

    if (loading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div>Error: {error}</div>;
    }

    const handleGenerateContract = (candidat) => {
        setSelectedCandidat(candidat);
        setShowModal(true);
        setFormData({
            lieuStage: candidat.entrevueDetails?.offreDeStageDTO.lieuStage || '',
            dateDebut: '',
            dateFin: '',
            semaines: '',
            horaireTravail: '',
            heuresParSemaine: '',
            tauxHoraire: candidat.entrevueDetails?.offreDeStageDTO.tauxHoraire || '',
            description: candidat.entrevueDetails?.offreDeStageDTO.description || '',
            collegeEngagement: '',
            entrepriseEngagement: '',
            etudiantEngagement: '',
        });
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setSelectedCandidat(null);
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log("Données du formulaire:", formData);
        alert("Contrat généré avec succès !");
        handleCloseModal();
    };

    return (
        <>
            <GestionnaireHeader style={{ marginBottom: '50px' }} />
            <div className="container-fluid p-4">
                <div className="container text-center">
                    <h1 className="mb-4">Liste des Candidatures et Détails des Entrevues</h1>
                    <table className="table table-striped table-hover">
                        <thead className="thead-dark">
                        <tr>
                            <th>Étudiant</th>
                            <th>Offre de Stage</th>
                            <th>Employeur</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        {candidatures.map(candidat => (
                            <tr key={candidat.id}>
                                <td>
                                    {candidat.entrevueDetails ?
                                        `${candidat.entrevueDetails.etudiantDTO.firstName} ${candidat.entrevueDetails.etudiantDTO.lastName}` :
                                        "N/A"}
                                </td>
                                <td>
                                    {candidat.entrevueDetails ?
                                        candidat.entrevueDetails.offreDeStageDTO.titre :
                                        "N/A"}
                                </td>
                                <td>
                                    {candidat.entrevueDetails ?
                                        `${candidat.entrevueDetails.offreDeStageDTO.employeur.firstName} ${candidat.entrevueDetails.offreDeStageDTO.employeur.lastName}` :
                                        "N/A"}
                                </td>
                                <td>
                                    <button
                                        className="btn btn-success"
                                        onClick={() => handleGenerateContract(candidat)}
                                    >
                                        Générer Contrat
                                    </button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>

            {showModal && (
                <div className="modal fade show" style={{ display: 'block' }} onClick={handleCloseModal}>
                    <div className="modal-dialog modal-lg" onClick={e => e.stopPropagation()}>
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">Génération de Contrat</h5>
                            </div>
                            <form onSubmit={handleSubmit}>
                                <div className="modal-body">
                                    <h6>ENDROIT DU STAGE</h6>
                                    <p>Adresse : {formData.lieuStage}</p>

                                    <h6>DUREE DU STAGE</h6>
                                    <div className="form-row">
                                        <div className="form-group col-md-6 p-1">
                                            <label>Date de début :</label>
                                            <input type="date" className="form-control" name="dateDebut" value={formData.dateDebut} onChange={handleChange} required />
                                        </div>
                                        <div className="form-group col-md-6 p-1">
                                            <label>Date de fin :</label>
                                            <input type="date" className="form-control" name="dateFin" value={formData.dateFin} onChange={handleChange} required />
                                        </div>
                                    </div>
                                    <div className="form-group">
                                        <label>Nombre total de semaines :</label>
                                        <input type="number" className="form-control" name="semaines" value={formData.semaines} onChange={handleChange} required />
                                    </div>

                                    <h6>HORAIRE DE TRAVAIL</h6>
                                    <div className="form-group">
                                        <label>Horaire de travail :</label>
                                        <input type="text" className="form-control" name="horaireTravail" value={formData.horaireTravail} onChange={handleChange} required />
                                    </div>
                                    <div className="form-group">
                                        <label>Nombre total d’heures par semaine :</label>
                                        <input type="number" className="form-control" name="heuresParSemaine" value={formData.heuresParSemaine} onChange={handleChange} required />
                                    </div>

                                    <h6>SALAIRE</h6>
                                    <p>Salaire horaire : {formData.tauxHoraire}</p>

                                    <h6>TACHES ET RESPONSABILITES DU STAGIAIRE</h6>
                                    <div className="form-group">
                                        <label>Description :</label>
                                        <textarea className="form-control" name="description" value={formData.description} onChange={handleChange} required />
                                    </div>

                                    <h6>RESPONSABILITES</h6>
                                    <div className="form-row">
                                        <div className="form-group col-md-4 p-1 ">
                                            <label>Le Collège s’engage à :</label>
                                            <textarea className="form-control" name="collegeEngagement" value={formData.collegeEngagement} onChange={handleChange} />
                                        </div>
                                        <div className="form-group col-md-4 p-1 ">
                                            <label>L’Entreprise s’engage à :</label>
                                            <textarea className="form-control" name="entrepriseEngagement" value={formData.entrepriseEngagement} onChange={handleChange} />
                                        </div>
                                        <div className="form-group col-md-4 p-1">
                                            <label>L’Étudiant s’engage à :</label>
                                            <textarea className="form-control" name="etudiantEngagement" value={formData.etudiantEngagement} onChange={handleChange} />
                                        </div>
                                    </div>
                                </div>
                                <div className="modal-footer">
                                    <button type="button" className="btn btn-secondary" onClick={handleCloseModal}>Fermer</button>
                                    <button type="submit" className="btn btn-primary">Générer</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            )}
        </>
    );
}

export default ListeCandidature;
