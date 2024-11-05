import React, { useEffect, useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useLocation } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import GestionnaireHeader from "./GestionnaireHeader";

function DetailsProfesseur() {
    const { t } = useTranslation();
    const location = useLocation();
    const professeur = location.state?.professeur;
    const [etudiants, setEtudiants] = useState([]);
    const [etudiantsSelectionner, setEtudiantsSelectionner] = useState([]);
    const [loading, setLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    console.log('Professeur details:', professeur);
    console.log('Etudiants:', etudiants);

    // Fetch students by department
    useEffect(() => {
        if (professeur && professeur.departement) {
            fetch(`http://localhost:8081/user/etudiants/departement/${professeur.departement}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                }
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Erreur lors de la récupération des étudiants');
                    }
                    return response.json();
                })
                .then(data => {
                    setEtudiants(data);
                    setLoading(false);
                })
                .catch(error => {
                    console.error('Error fetching etudiants:', error);
                    setLoading(false);
                });
        }
    }, [professeur]);

    const handleStudentSelection = (etudiantEmail) => {
        setEtudiantsSelectionner(prevState =>
            prevState.includes(etudiantEmail)
                ? prevState.filter(email => email !== etudiantEmail)
                : [...prevState, etudiantEmail]
        );
    };

    const assignStudentsToProfessor = () => {
        if (etudiantsSelectionner.length === 0 || !professeur) {
            return;
        }

        fetch(`http://localhost:8081/professeur/assignerEtudiants/${professeur.email}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(etudiantsSelectionner)
        })
            .then(response => response.json())
            .then(data => {
                setSuccessMessage(data.message);
                setEtudiants(prevEtudiants => {
                    const updatedEtudiants = prevEtudiants.map(etudiant => {
                        if (etudiantsSelectionner.includes(etudiant.email)) {
                            return { ...etudiant, professeur: { email: professeur.email } };
                        }
                        return etudiant;
                    });
                    return updatedEtudiants;
                });
                setEtudiantsSelectionner([]);
                setTimeout(() => setSuccessMessage(''), 3000);
            })
            .catch(error => console.error('Error assigning students:', error));
    };

    const unassignStudentFromProfessor = (etudiantEmail) => {
        console.log('Unassigning student:', etudiantEmail);
        fetch(`http://localhost:8081/gestionnaire/etudiants/deassignerProfesseur/${encodeURIComponent(etudiantEmail)}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Erreur lors de la désassignation de l\'étudiant');
                }
                return response.json();
            })
            .then(data => {
                setSuccessMessage('Étudiant désassigné avec succès');
                setEtudiants(prevEtudiants => {
                    const updatedEtudiants = prevEtudiants.map(etudiant => {
                        if (etudiant.email === etudiantEmail) {
                            return { ...etudiant, professeur: null };
                        }
                        return etudiant;
                    });
                    return updatedEtudiants;
                });
                setTimeout(() => setSuccessMessage(''), 3000);
            })
            .catch(error => console.error('Error unassigning student:', error));
    };


    if (loading) {
        return <div className="text-center mt-5">
            <div className="spinner-border" role="status"></div>
            <br />
            <span className="sr-only">Loading...</span>
        </div>;
    }

    const unassignedStudents = etudiants.filter(etudiant =>
        !etudiant.professeur && etudiant.email.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const assignedStudents = etudiants.filter(etudiant =>
        etudiant.professeur && etudiant.professeur.email === professeur.email
    );

    return (
        <>
            <GestionnaireHeader />
            <div className="details-container">
                <h1 className="mb-4 detail-title">{t('profDetailsTitle')}</h1>
                <div className="row">
                    <div className="col-md-6">
                        <h5>{t('personalInfo')}</h5>
                        <div className="details-info">
                            <p className="text-capitalize">
                                <strong>{t('nomDetail')}:</strong> {professeur.firstName} {professeur.lastName}</p>
                            <p><strong>{t('emailDetail')}:</strong> {professeur.credentials.email}</p>
                            <p><strong>{t('telephoneDetail')}:</strong> {professeur.phoneNumber}</p>
                            <p>
                                <strong>{t('departmentDetail')}:</strong> {professeur.departement.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, char => char.toUpperCase())}
                            </p>
                        </div>
                    </div>

                    <div className="col-md-6">
                        <h5 className="mb-3">{t('studentsInSameDepartment')}</h5>
                        <input
                            type="text"
                            className="form-control mb-3"
                            placeholder={t('searchStudents')}
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />
                        <h6>{t('unassignedStudents')}</h6>
                        <ul className="list-group">
                            {unassignedStudents.map(etudiant => (
                                <li key={etudiant.email} className="list-group-item d-flex align-items-center">
                                    <div className="form-check form-switch">
                                        <input
                                            className="form-check-input"
                                            type="checkbox"
                                            checked={etudiantsSelectionner.includes(etudiant.email)}
                                            onChange={() => handleStudentSelection(etudiant.email)}
                                        />
                                    </div>
                                    &nbsp;
                                    {etudiant.email}
                                </li>
                            ))}
                        </ul>
                        <button className="btn btn-primary mt-3"
                                onClick={assignStudentsToProfessor}
                                disabled={etudiantsSelectionner.length === 0}>
                            {t('assignStudents')}
                        </button>

                        {successMessage && <div className="alert alert-success mt-3">{successMessage}</div>}

                        {assignedStudents.length !== 0 && (
                            <div>
                                <h6 className="mt-4">{t('assignedStudents')}</h6>
                                <ul className="list-group">
                                    {assignedStudents.map(etudiant => (
                                        <li key={etudiant.email} className="list-group-item d-flex align-items-center">
                                            <div className="form-check form-switch">
                                                <input
                                                    className="form-check-input"
                                                    type="checkbox"
                                                    checked
                                                    disabled
                                                />
                                            </div>
                                            &nbsp;
                                            {etudiant.email}
                                            <button
                                                className="btn btn-danger btn-sm ms-auto"
                                                onClick={() => unassignStudentFromProfessor(etudiant.email)}
                                            >
                                                {t('unassign')}
                                            </button>
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </>
    );
}

export default DetailsProfesseur;
