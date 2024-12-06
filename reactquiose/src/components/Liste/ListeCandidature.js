import React, { useEffect, useState } from 'react';
import GestionnaireHeader from '../Header/GestionnaireHeader';
import '../../CSS/ListeCandidature.css';
import { useTranslation } from "react-i18next";
import {useLocation} from "react-router-dom";
import {Icon} from "react-icons-kit";
import {eye, eyeOff} from "react-icons-kit/feather";
import TableauContrat from "../Contrat/TableauContrat";
import {If, Then} from "react-if";

function ListeCandidature() {
    const [candidatures, setCandidatures] = useState([]);
    const [showContractModal, setShowContractModal] = useState(false);
    const [contrats, setContrats] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [minHeuresParSemaine, setMinHeuresParSemaine] = useState(0);
    const [selectedCandidat, setSelectedCandidat] = useState(null);
    const [selectedContrat, setSelectedContrat] = useState(null);
    const [icon, setIcon] = useState(eyeOff);
    const [errorMessageHeures, setErrorMessageHeures] = useState('');
    const [errorMessageSalaire, setErrorMessageSalaire] = useState('');
    const [showModal, setShowModal] = useState(false);
    const { t } = useTranslation();
    const [showPasswordModal, setShowPasswordModal] = useState(false);
    const [modalMessage] = useState('');
    const [type, setType] = useState('password');
    const [password, setPassword] = useState('');
    const location = useLocation();
    const userData = location.state?.userData;
    const salaireMinimumHoraire = 15.75;
    const [formData, setFormData] = useState({
        lieuStage: '',
        dateDebut: '',
        dateFin: '',
        nbSemaines: '',
        heureHorraireDebut: '',
        heureHorraireFin: '',
        heuresParSemaine: '',
        tauxHoraire: '',
        description: '',
        collegeEngagement: '',
        entrepriseEngagement: '',
        etudiantEngagement: '',
    });

    const togglePasswordVisibility = () => {
        setIcon(type === 'password' ? eye : eyeOff);
        setType(type === 'password' ? 'text' : 'password');
    };

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

    const genererPDF = async () => {
        try {
            const response = await fetch("http://localhost:8081/generatePDF/contrat", {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(selectedContrat)
            });

            if (!response.ok) {
                throw new Error("Erreur lors de la génération du PDF");
            }

            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);

            const a = document.createElement('a');
            a.href = url;
            a.download = 'Contrat_Stage.pdf';
            a.click();

            window.URL.revokeObjectURL(url);
        } catch (error) {
            console.error("Erreur lors du téléchargement du PDF :", error);
        }
    };

    useEffect(() => {
        let session = localStorage.getItem('session');
        fetch(`http://localhost:8081/candidatures/session/${session}`)
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
                setLoading(false);
            })
            .then(() => {
                fetch(`http://localhost:8081/contrat/session/${session}`)
                    .then(response => response.json())
                    .then(data => {
                        setContrats(data);
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

    const handleOpenPasswordModal = () => {
        setShowPasswordModal(true);
    };

    const handleOpenContractModal = () => {
        setShowContractModal(true);
    };

    const handleClosePasswordModal = () => {
        setShowPasswordModal(false);
        setPassword('');
    };

    const handleCloseContractModal = () => {
        setShowContractModal(false);
        setSelectedContrat(null);
    }

    const handlePasswordSubmit = (e) => {
        e.preventDefault();
        handleSignContract(selectedContrat.uuid, userData.credentials.email);
        handleClosePasswordModal();
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        const updatedFormData = { ...formData, [name]: value };

        if (name === "dateDebut" || name === "dateFin") {
            const dateDebut = new Date(updatedFormData.dateDebut);
            const dateFin = new Date(updatedFormData.dateFin);

            if (dateDebut && dateFin && dateDebut <= dateFin) {
                const diffInWeeks = Math.ceil((dateFin - dateDebut) / (7 * 24 * 60 * 60 * 1000));
                updatedFormData.nbSemaines = diffInWeeks;
            } else {
                updatedFormData.nbSemaines = '';
            }
        }

        if (name === "heureHorraireDebut" || name === "heureHorraireFin") {
            const startHour = updatedFormData.heureHorraireDebut.split(":");
            const endHour = updatedFormData.heureHorraireFin.split(":");
            const startDate = new Date();
            const endDate = new Date();

            if (startHour.length === 2 && endHour.length === 2) {
                startDate.setHours(startHour[0], startHour[1]);
                endDate.setHours(endHour[0], endHour[1]);
                const diffInHours = (endDate - startDate) / (1000 * 60 * 60);

                setMinHeuresParSemaine(diffInHours);
            }
        }

        if (name === "heuresParSemaine") {
            if (parseFloat(value) < minHeuresParSemaine) {
                setErrorMessageHeures(`Le nombre d'heures par semaine doit être supérieur ou égal à ${minHeuresParSemaine}h.`);
            } else {
                setErrorMessageHeures('');
            }
        }

        if (name === "tauxHoraire") {
            const regex = /^\d+(\.\d{0,2})?$/;

            if (!regex.test(value)) {
                setErrorMessageSalaire('Le salaire horaire doit être un nombre valide.');
            }

            if (parseFloat(value) < salaireMinimumHoraire) {
                setErrorMessageSalaire(`Le salaire horaire doit être supérieur ou égal à ${salaireMinimumHoraire}$.`);
            } else {
                setErrorMessageSalaire('');
            }
        }

        setFormData(updatedFormData);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        await fetch('http://localhost:8081/contrat/creerContrat', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(formData)
        });

        setContrats([...contrats, formData]);
        handleCloseModal();
    };

    const handleSignContract = async (uuid, email) => {
        try {
            const response = await fetch(`http://localhost:8081/contrat/signer-gestionnaire/${uuid}/${email}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ password })
            });

            if (response.ok) {
                setContrats(prevContrats =>
                    prevContrats.map(contrat =>
                        contrat.uuid === uuid ? { ...contrat, gestionnaireSigne: true, dateSignatureGestionnaire: new Date() } : contrat
                    )
                );
                return "Contrat signé avec succès.";
            } else {
                const errorMessage = await response.text();
                return `Échec de la signature : ${errorMessage}`; 
            }
        } catch (error) {
            console.error("Erreur lors de la signature :", error);
            return "Une erreur s'est produite lors de la signature.";
        }
    };

    const isContractSigned = (contrat) => {
        return contrat.etudiantSigne && contrat.employeurSigne && contrat.gestionnaireSigne;
    };

    const verifificationFiltre = (data) => {
        fetchBySession(data.session);
    };

    const fetchBySession = (session) => {
        fetch(`http://localhost:8081/candidatures/session/${session}`)
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

                const filteredCandidatures = candidatsWithEntrevues.filter(candidat => candidat.accepte);
                setCandidatures(filteredCandidatures);
                setLoading(false);
            })
            .then(() => {
                fetch(`http://localhost:8081/contrat/session/${session}`)
                    .then(response => response.json())
                    .then(data => {
                        setContrats(data);
                    })
                    .catch(err => {
                        setError(err.message);
                    });
            })
            .catch(err => {
                setError(err.message);
                setLoading(false);
            });


    };

    const getStatus = (candidat) => {
        const contrat = contrats.find(contrat => contrat.candidature.id === candidat.id);

        if (!contrat) return "Générer un contrat";
        if (isContractSigned(contrat)) return "générer version PDF du contrat";
        return "En attente des signatures";
    };

    const sortedCandidatures = candidatures.sort((a, b) => {
        const statusOrder = ["Générer un contrat", "En attente des signatures", "générer version PDF du contrat"];
        return statusOrder.indexOf(getStatus(a)) - statusOrder.indexOf(getStatus(b));
    });

    if (loading) {
        return <div className="text-center mt-5">
            <div className="spinner-border" role="status"></div>
            <br/>
            <span className="sr-only">{t('chargementsDesCandidatures')}</span>
        </div>;
    }

    if (error) return  <p className="text-center mt-5 text-danger">Error: {error}</p>;

    return (
        <>
            <GestionnaireHeader onSendData={verifificationFiltre} style={{ marginBottom: '50px' }} />
            <div className="container-fluid p-4 page-liste-candidature">
                <div className="container text-center">
                    <h1 className="mb-4 display-4">{t('ListeCandidaturesEtDetailsEntrevue')}</h1>

                    <If condition={candidatures.length === 0}>
                        <Then>
                            <p className="text-center mt-5">{t('AucuneCandidatureTrouvee')}</p>
                        </Then>
                    </If>

                    <If condition={candidatures.length > 0}>
                        <Then>
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
                                {sortedCandidatures.map(candidat => {

                                    const hasContrat = contrats.some(contrat => contrat.candidature.id === candidat.id);
                                    const contrat = contrats.find(contrat => contrat.candidature.id === candidat.id);

                                    return (
                                        <tr key={candidat.id}>
                                            <td>{candidat.entrevueDetails ? `${candidat.entrevueDetails.etudiantDTO.firstName} ${candidat.entrevueDetails.etudiantDTO.lastName}` : "N/A"}</td>
                                            <td>{candidat.entrevueDetails ? candidat.entrevueDetails.offreDeStageDTO.titre : "N/A"}</td>
                                            <td>{candidat.entrevueDetails ? `${candidat.entrevueDetails.offreDeStageDTO.employeur.firstName} ${candidat.entrevueDetails.offreDeStageDTO.employeur.lastName}` : "N/A"}</td>
                                            <td>
                                                {!hasContrat ? (
                                                    <button className="btn btn-success"
                                                            onClick={() => handleGenerateContract(candidat)}>
                                                        {t('GenererContrat')}
                                                    </button>
                                                ) : (
                                                    <>
                                                        {isContractSigned(contrat) ? (
                                                            <button className="btn btn-primary"
                                                                    onClick={() => {
                                                                        setSelectedContrat(contrat);
                                                                        handleOpenContractModal()
                                                                    }}
                                                            >
                                                                {t('GenererPDF')}
                                                            </button>
                                                        ) : (
                                                            <button
                                                                className={`btn ${contrat.etudiantSigne && contrat.employeurSigne ? 'btn-success' : 'btn-warning fw-bold'}`}
                                                                disabled={!(contrat.etudiantSigne && contrat.employeurSigne)}
                                                                onClick={() => {
                                                                    setSelectedContrat(contrat);
                                                                    handleOpenPasswordModal();
                                                                }}
                                                            >
                                                                {contrat.etudiantSigne && contrat.employeurSigne
                                                                    ? t('SignerContrat')
                                                                    : t('EnAttenteDesSignatures')}
                                                            </button>
                                                        )}
                                                    </>
                                                )}
                                            </td>
                                        </tr>
                                    );
                                })}
                                </tbody>
                            </table>
                        </Then>
                    </If>
                </div>
            </div>

            {showModal && (
                <div className="modal fade show page-liste-candidature" style={{display: 'block'}}>
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
                                    <p>{t('NombreTotalSemaines')} : {formData.nbSemaines}</p>

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
                                        <input
                                            type="number"
                                            className="form-control"
                                            name="heuresParSemaine"
                                            value={formData.heuresParSemaine}
                                            onChange={handleChange}
                                            step="0.05"
                                            min={minHeuresParSemaine}
                                            required
                                        />
                                        {errorMessageHeures && (
                                            <div className='alert alert-danger' style={{ textAlign: 'center', fontSize: '2vmin' }}>
                                                {errorMessageHeures}
                                            </div>
                                        )}
                                    </div>

                                    <h6>{t('Salaire')}</h6>
                                    <div className="form-group">
                                        <label>{t('SalaireHoraire')} :</label>
                                        <input
                                            type="number"
                                            className="form-control"
                                            name="tauxHoraire"
                                            value={formData.tauxHoraire}
                                            onChange={handleChange}
                                            step="0.05"
                                            min={salaireMinimumHoraire}
                                            required
                                        />
                                        {errorMessageSalaire && (
                                            <div className='alert alert-danger' style={{ textAlign: 'center', fontSize: '2vmin' }}>
                                                {errorMessageSalaire}
                                            </div>
                                        )}
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

            {showPasswordModal && (
                <div className="modal fade show" style={{display: 'block'}}>
                    <div className="modal-dialog" onClick={e => e.stopPropagation()}>
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">{t('SignerContrat')}</h5>
                            </div>

                            <TableauContrat contrat={selectedContrat}/>

                            <form onSubmit={handlePasswordSubmit}>
                                <div className="modal-body">
                                    <div className="form-group">
                                        <label>{t('EntrezMotDePasse')}</label>
                                        <div className="d-flex align-items-center">
                                            <input
                                                type={type}
                                                className="form-control"
                                                value={password}
                                                onChange={(e) => setPassword(e.target.value)}
                                                required
                                            />

                                            <span onClick={togglePasswordVisibility} className="icon-toggle ps-2">
                                                <Icon icon={icon} size={20}/>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                                {modalMessage && <p>{modalMessage}</p>}
                                <div className="modal-footer">
                                    <button type="button" className="btn btn-danger"
                                            onClick={handleClosePasswordModal}>
                                        {t('close')}
                                    </button>
                                    <button type="submit" className="btn btn-success">
                                        {t('Signer')}
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            )}

            {showContractModal && (
                <div className="modal fade show page-liste-candidature" style={{ display: 'block' }}>
                    <div className="modal-dialog modal-lg" onClick={e => e.stopPropagation()}>
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">{t('Contrat Signe')}</h5>
                            </div>
                            <div className="modal-body">
                                <TableauContrat contrat={selectedContrat}/>
                                <div className="modal-footer">
                                    <button type="button" className="btn btn-danger"
                                            onClick={handleCloseContractModal}>
                                        {t('Fermer')}
                                    </button>
                                    <button type="submit" 
                                            className="btn btn-success"
                                            onClick={() => {
                                                setSelectedContrat(selectedContrat);
                                                genererPDF();
                                            }}
                                    >
                                        {t('GenererPDF')}
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            )}

        </>
    );
}

export default ListeCandidature;
