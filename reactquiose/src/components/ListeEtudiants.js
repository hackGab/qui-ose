import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Link } from 'react-router-dom';
import { FaEnvelope, FaPhone } from 'react-icons/fa';

function ListeEtudiants() {
    const etudiants = [
        {
            id: 1,
            email: 'etudiant1@example.com',
            first_name: 'Jean',
            last_name: 'Dupont',
            phone_number: '123-456-7890',
            departement: 'Informatique',
        },
        {
            id: 2,
            email: 'etudiant2@example.com',
            first_name: 'Marie',
            last_name: 'Curie',
            phone_number: '098-765-4321',
            departement: 'Chimie',
        },
    ];

    return (
        <div className="container mt-5">
            <h1 className="mb-4 text-center">Liste des Étudiants</h1>
            <p className="text-center mb-4">Voici la liste des étudiants inscrits à notre programme de stages.</p>
            <div className="row">
                {etudiants.map((etudiant) => (
                    <div className="col-md-4 mb-4" key={etudiant.id}>
                        <Link to={`/details/${etudiant.id}`} className="text-decoration-none">
                            <div className="card shadow">
                                <div className="card-body">
                                    <h5 className="card-title">{`${etudiant.first_name} ${etudiant.last_name}`}</h5>
                                    <h6 className="card-subtitle mb-2 text-muted">{etudiant.role}</h6>
                                    <p className="card-text">
                                        <FaEnvelope /> {etudiant.email}<br />
                                        <FaPhone /> {etudiant.phone_number}<br />
                                        <span className="badge bg-info">{etudiant.departement}</span>
                                    </p>
                                </div>
                            </div>
                        </Link>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default ListeEtudiants;
