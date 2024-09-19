import React, { useState } from 'react';
import "bootstrap/dist/css/bootstrap.min.css";
import InputMask from 'react-input-mask';
import  {If, Then } from 'react-if';
import Inscription from "./Inscription";

function Connexion() {
    const [email, setEmail] = useState('');
    const [mpd, setMpd] = useState('');

    const handleSubmit = (event) => {
        event.preventDefault();
        const userData = {
            credentials: {
                email,
                password: mpd
            }
        };
        console.log('Données envoyées au backend:', userData);
        // Envoi d'une requête POST au backend
        fetch('http://localhost:8080/connexion', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(userData),
        })
            .then((response) => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error('Connexion échouée');
            })
            .then((data) => {
                console.log('Réponse du serveur:', data);
                // Rediriger l'utilisateur vers la page d'accueil
                window.location.href = '/';
            })
            .catch((error) => {
                console.error('Erreur lors de la connexion:', error);
                alert('Erreur lors de la connexion');
            });
    }



    return(
        <form className='pt-0' onSubmit={handleSubmit}>

            <div className='row'>
                <div>
                    <div className='form-group' style={{display: "inline-flex"}}>
                        <label htmlFor='role' className='col-6 m-auto'>Je suis un*</label>
                        &nbsp;
                        <select
                            className='form-control col-6'
                            id='role'
                            name='role'
                            value={role}
                            onChange={(e) => setRole(e.target.value)}
                            required
                        >
                            <option value='etudiant'>Étudiant</option>
                            <option value='prof'>Professeur</option>
                            <option value='employeur'>Employeur</option>
                        </select>
                    </div>
                </div>
            </div>

            <div className='row'>
                <div className="form-group">
                    <label htmlFor="email">Email*</label>
                    <input type="email" className="form-control" id="email" name="email"
                           value={email} onChange={(e) => setEmail(e.target.value)}
                           placeholder="johndoe@gmail.com" required/>
                </div>

                <div className="form-group">
                    <label htmlFor="mpd">Mot de passe*</label>
                    <input type="password" className="form-control" id="mpd" name="mpd" placeholder="********"
                           value={mpd} onChange={(e) => setMpd(e.target.value)}
                           required/>
                </div>

            </div>


            <button className="btn btn-primary w-50" type="submit">S'inscrire</button>

            <small>Je n'ai pas de compte :<a href="/signUp">S'incrire</a></small>
        </form>
    )
}

export default Connexion;