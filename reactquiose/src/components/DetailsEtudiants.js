import React, { useEffect, useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useParams } from 'react-router-dom';
import '../CSS/DetailsEtudiants.css';
import GestionnaireHeader from "./GestionnaireHeader"; // Import du header

function DetailsEtudiants() {
    const { id } = useParams();
    const [student, setStudent] = useState(null);

    const etudiants = [
        {
            id: 1,
            email: 'etudiant1@example.com',
            first_name: 'Jean',
            last_name: 'Dupont',
            phone_number: '123-456-7890',
            departement: 'Informatique',
            cvUrl: 'https://example.com/cv1.pdf',
        },
        {
            id: 2,
            email: 'etudiant2@example.com',
            first_name: 'Marie',
            last_name: 'Curie',
            phone_number: '098-765-4321',
            departement: 'Chimie',
            cvUrl: 'https://example.com/cv2.pdf',
        },
    ];

    useEffect(() => {
        const foundStudent = etudiants.find(student => student.id === parseInt(id));
        setStudent(foundStudent);
    }, [id]);

    if (!student) {
        return <div>Loading...</div>;
    }

    return (
        <div className="details-container">
            <GestionnaireHeader />

            <h1 className="mb-4 detail-title">Détails de l'Étudiant</h1>

            <div className="row">
                <div className="col-md-6">
                    <h5>Informations Personnelles</h5>
                    <div className="details-info">
                        <p><strong>Nom :</strong> {student.first_name} {student.last_name}</p>
                        <p><strong>Email :</strong> {student.email}</p>
                        <p><strong>Téléphone :</strong> {student.phone_number}</p>
                        <p><strong>Département :</strong> {student.departement}</p>
                    </div>
                </div>

                <div className="col-md-6">
                    <h5 className="mb-3">CV de l'Étudiant</h5>
                    <div className="iframe-container">
                        <iframe
                            src={student.cvUrl}
                            title="CV de l'étudiant"
                            className="cv-frame"
                        ></iframe>
                    </div>

                    <div className="mt-4">
                        <h5>Actions</h5>
                        <div className="btn-group-vertical w-100">
                            <button className="btn btn-success mb-2">Valider</button>
                            <button className="btn btn-danger mb-2">Rejeter</button>
                            <button className="btn btn-primary">Confirmer</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default DetailsEtudiants;
