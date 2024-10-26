import React, { useEffect, useState } from 'react';
import GestionnaireHeader from './GestionnaireHeader';
import '../CSS/ListeCandidature.css';
import { useTranslation } from "react-i18next";

function ListeCandidature() {
    const [candidatures, setCandidatures] = useState([]);
    const [contrats, setContrats] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedCandidat, setSelectedCandidat] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const { t } = useTranslation();
    const [formData, setFormData] = useState({
        lieuStage: '',
        dateDebut: '',
        dateFin: '',
        semaines: '',
        heureHorraireDebut: '',
        heureHorraireFin: '',
        heuresParSemaine: '',
        tauxHoraire: '',
        description: '',
        collegeEngagement: '',
        entrepriseEngagement: '',
        etudiantEngagement: '',
    });

    const generateHoursOptions = () => {
        const options = [];
        for (let hour = 0; hour < 24; hour++) {
            for (let minute = 0; minute < 60; minute += 30) {
                const formattedHour = String(hour).padStart(2, '0');
                const formattedMinute = String(minute).padStart(2, '0');
                options.push(`${formattedHour}:${formattedMinute}`);
            }
        }
        return options;
    };

    useEffect(() => {
        fetch('http://localhost:8081/candidatures/all')
            .then(response => response.json())
            .then(data => {
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
                setCandidatures(candidatsWithEntrevues);
                console.log(candidatsWithEntrevues);
                setLoading(false);
            })
            .then(() => {
                fetch('http://localhost:8081/contrat/all')
                    .then(response => response.json())
                    .then(data => {
                        setContrats(data);
                        console.log(data);
                    })
                    .catch(err => {
                        setError(err.message);
                    });
            })
            .catch(err => {
                setError(err.message);
                setLoading(false);
            });
    }, []);

    const hoursOptions = generateHoursOptions();

    const handleGenerateContract = (candidat) => {
        setSelectedCandidat(candidat);
        setShowModal(true);
        setFormData({
            etudiantSigne: false,
            employeurSigne: false,
            gestionnaireSigne: false,
            dateSignatureEtudiant: '',
            dateSignatureEmployeur: '',
            dateSignatureGestionnaire: '',
            candidature: candidat,
            collegeEngagement: '',
            dateDebut: '',
            dateFin: '',
            description: candidat.entrevueDetails.offreDeStageDTO.description || '',
            entrepriseEngagement: '',
            etudiantEngagement: '',
            heuresParSemaine: '',
            heureHorraireDebut: '',
            heureHorraireFin: '',
            lieuStage: candidat.entrevueDetails.offreDeStageDTO.localisation,
            nbSemaines: '',
            tauxHoraire: '',
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

    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log(formData);

        await fetch('http://localhost:8081/contrat/creerContrat', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(formData)
        });

        setContrats([...contrats, formData]);
        handleCloseModal();
    };

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error}</div>;

    return (
        <>
            <GestionnaireHeader style={{ marginBottom: '50px' }} />
            <div className="container-fluid p-4">
                <div className="container text-center">
                    <h1 className="mb-4">{t('ListeCandidaturesEtDetailsEntrevue')}</h1>
                    <table className="table table-striped table-hover">
                        <thead className="thead-dark">
                        <tr>
                            <th>{t('Etudiant')}</th>
                            <th>{t('OffreDeStage')}</th>
                            <th>{t('Employeur')}</th>
                            <th>{t('Actions')}</th>
                        </tr>
                        </thead>
                        <tbody>
                        {candidatures.map(candidat => {
                            const hasContrat = contrats.some(contrat => contrat.candidature.id === candidat.id);

                            return (
                                <tr key={candidat.id}>
                                    <td>
                                        {candidat.entrevueDetails ?
                                            `${candidat.entrevueDetails.etudiantDTO.firstName} ${candidat.entrevueDetails.etudiantDTO.lastName}` :
                                            "N/A"}
                                    </td>
                                    <td>
                                        {candidat.entrevueDetails ? candidat.entrevueDetails.offreDeStageDTO.titre : "N/A"}
                                    </td>
                                    <td>
                                        {candidat.entrevueDetails ?
                                            `${candidat.entrevueDetails.offreDeStageDTO.employeur.firstName} ${candidat.entrevueDetails.offreDeStageDTO.employeur.lastName}` :
                                            "N/A"}
                                    </td>
                                    <td>
                                        {!hasContrat ? (
                                            <button className="btn btn-success"
                                                    onClick={() => handleGenerateContract(candidat)}>
                                                {t('GenererContrat')}
                                            </button>
                                        ) : <button className="btn btn-success disabled"
                                                    onClick={() => handleGenerateContract(candidat)}>
                                            {t('EnAttenteDeSignatures')}
                                            </button>
                                        }
                                    </td>
                                </tr>
                            );
                        })}
                        </tbody>
                    </table>
                </div>
            </div>

            {showModal && (
                <div className="modal fade show" style={{display: 'block'}} onClick={handleCloseModal}>
                    <div className="modal-dialog modal-lg" onClick={e => e.stopPropagation()}>
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">{t('GenerationContrat')}</h5>
                            </div>
                            <form onSubmit={handleSubmit}>
                                <div className="modal-body">
                                    <h6>{t('EndroitDuStage')}</h6>
                                    <p>{t('Adresse')} : {formData.lieuStage}</p>

                                    <h6>{t('DureeDuStage')}</h6>
                                    <div className="form-row">
                                        <div className="form-group col-md-6 p-1">
                                            <label>{t('DateDeDebut')} :</label>
                                            <input type="date" className="form-control" name="dateDebut"
                                                   value={formData.dateDebut} onChange={handleChange} required/>
                                        </div>
                                        <div className="form-group col-md-6 p-1">
                                            <label>{t('DateDeFin')} :</label>
                                            <input type="date" className="form-control" name="dateFin"
                                                   value={formData.dateFin} onChange={handleChange} required/>
                                        </div>
                                    </div>
                                    <div className="form-group">
                                        <label>{t('NombreTotalSemaines')} :</label>
                                        <input type="number" className="form-control" name="semaines"
                                               value={formData.semaines} onChange={handleChange} required/>
                                    </div>

                                    <h6>{t('HorraireTravail')}</h6>
                                    <div className="form-row">
                                        <div className="form-group col-md-6">
                                            <label>{t('HeureDeDebut')} :</label>
                                            <select className="form-control" name="heureHorraireDebut"
                                                    value={formData.heureHorraireDebut} onChange={handleChange}
                                                    required>
                                                <option value="">{t('SelectionnezUneHeure')}</option>
                                                {hoursOptions.map(time => (
                                                    <option key={time} value={time}>{time}</option>
                                                ))}
                                            </select>
                                        </div>
                                        <div className="form-group col-md-6">
                                            <label>{t('HeureDeFin')} :</label>
                                            <select className="form-control" name="heureHorraireFin"
                                                    value={formData.heureHorraireFin} onChange={handleChange} required>
                                                <option value="">{t('SelectionnezUneHeure')}</option>
                                                {hoursOptions.map(time => (
                                                    <option key={time} value={time}>{time}</option>
                                                ))}
                                            </select>
                                        </div>
                                    </div>

                                    <div className="form-group">
                                        <label>{t('NombreTotalHeuresParSemaine')} :</label>
                                        <input type="number" className="form-control" name="heuresParSemaine"
                                               value={formData.heuresParSemaine} onChange={handleChange} required/>
                                    </div>

                                    <h6>{t('Salaire')}</h6>
                                    <div className="form-group">
                                        <label>{t('SalaireHoraire')} :</label>
                                        <input
                                            type="number"
                                            className="form-control"
                                            name="tauxHoraire"
                                            value={formData.tauxHoraire}
                                            onChange={(e) => {
                                                const {name, value} = e.target;
                                                const regex = /^\d+(\.\d{0,2})?$/;

                                                if (regex.test(value) || value === "") {
                                                    setFormData({...formData, [name]: value});
                                                }
                                            }}
                                            step="0.01"
                                            min="0"
                                            required
                                        />
                                    </div>
                                    <h6>{t('TachesEtResponsabilitesDuStage')}</h6>
                                    <div className="form-group">
                                        <label>Description :</label>
                                        <textarea className="form-control" name="description"
                                                  value={formData.description} onChange={handleChange} required/>
                                    </div>

                                    <h6>{t('Responsabilites')}</h6>
                                    <div className="form-row">
                                    <div className="form-group col-md-4 p-1 ">
                                            <label>{t('LeCollegeSEngageA')} :</label>
                                            <textarea className="form-control" name="collegeEngagement"
                                                      value={formData.collegeEngagement} onChange={handleChange}/>
                                        </div>
                                        <div className="form-group col-md-4 p-1 ">
                                            <label>{t('EntrepriseSEngageA')} :</label>
                                            <textarea className="form-control" name="entrepriseEngagement"
                                                      value={formData.entrepriseEngagement} onChange={handleChange}/>
                                        </div>
                                        <div className="form-group col-md-4 p-1">
                                            <label>{t('EtudiantSEngageA')} :</label>
                                            <textarea className="form-control" name="etudiantEngagement"
                                                      value={formData.etudiantEngagement} onChange={handleChange}/>
                                        </div>
                                    </div>
                                </div>
                                <div className="modal-footer">
                                    <button type="button" className="btn btn-secondary"
                                            onClick={handleCloseModal}>{t('close')}</button>
                                    <button type="submit" className="btn btn-success">{t('GenererContrat')}</button>
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
