import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Link } from 'react-router-dom';
import { FaEnvelope, FaPhone } from 'react-icons/fa';
import GestionnaireHeader from "./GestionnaireHeader";
import '../CSS/ListeEtudiants.css';

function ListeEtudiants() {
    const etudiants = [
        {
            id: 1,
            email: 'etudiant1@example.com',
            first_name: 'Jean',
            last_name: 'Dupont',
            phone_number: '123-456-7890',
            departement: 'Informatique',
            status: 'validé',
        },
        {
            id: 2,
            email: 'etudiant2@example.com',
            first_name: 'Marie',
            last_name: 'Curie',
            phone_number: '098-765-4321',
            departement: 'Chimie',
            status: 'refusé',
        },
        {
            id: 3,
            email: 'etudiant3@example.com',
            first_name: 'Paul',
            last_name: 'Martin',
            phone_number: '321-654-0987',
            departement: 'Mathématiques',
            status: 'en attente',
        },
    ];

    return (
        <div className="container-fluid d-flex flex-column min-vh-100">
            <GestionnaireHeader />
            <div className="container flex-grow-1 pt-5 mt-5">
                <h1 className="mb-4 text-center">Liste des Étudiants</h1>
                <p className="text-center mb-4">Voici la liste des étudiants inscrits à notre programme de stages.</p>
                <div className="row">
                    {etudiants.map((etudiant) => (
                        <div className="col-12 col-md-6 col-lg-4 mb-4" key={etudiant.id}>
                            <Link to={`/details/${etudiant.id}`} className="text-decoration-none">
                                <div className={`card shadow w-100 ${etudiant.status}`}> {/* Ajout de la classe de statut */}
                                    <div className="card-body">
                                        <h5 className="card-title">{`${etudiant.first_name} ${etudiant.last_name}`}</h5>
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
        </div>
    );
}

export default ListeEtudiants;
