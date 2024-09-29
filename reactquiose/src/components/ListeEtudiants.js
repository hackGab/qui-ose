import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';

function ListeEtudiants() {
    // Exemple de données d'étudiants
    const etudiants = [
        {
            id: 1,
            email: 'etudiant1@example.com',
            password: 'password1',
            role: 'Étudiant',
            first_name: 'Jean',
            last_name: 'Dupont',
            phone_number: '123-456-7890',
            departement: 'Informatique'
        },
        {
            id: 2,
            email: 'etudiant2@example.com',
            password: 'password2',
            role: 'Étudiant',
            first_name: 'Marie',
            last_name: 'Curie',
            phone_number: '098-765-4321',
            departement: 'Chimie'
        },
        // Ajoutez plus d'étudiants ici
    ];

    return (
        <div className="container mt-5">
            <h1 className="mb-4">Liste des étudiants</h1>
            <table className="table table-striped table-bordered">
                <thead className="thead-dark">
                <tr>
                    <th>ID</th>
                    <th>Email</th>
                    <th>Password</th>
                    <th>Rôle</th>
                    <th>Prénom</th>
                    <th>Nom</th>
                    <th>Téléphone</th>
                    <th>Département</th>
                </tr>
                </thead>
                <tbody>
                {etudiants.map((etudiant) => (
                    <tr key={etudiant.id}>
                        <td>{etudiant.id}</td>
                        <td>{etudiant.email}</td>
                        <td>{etudiant.password}</td>
                        <td>{etudiant.role}</td>
                        <td>{etudiant.first_name}</td>
                        <td>{etudiant.last_name}</td>
                        <td>{etudiant.phone_number}</td>
                        <td>{etudiant.departement}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default ListeEtudiants;
