import React, { useState } from 'react';
import "bootstrap/dist/css/bootstrap.min.css";

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
        <form className='pt-0 m-auto' onSubmit={handleSubmit}>
            <div className='row' >
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

            <button className="btn btn-primary w-50" type="submit">Connecter</button>

            <small>Je n'ai pas de compte ? <a href="/signUp">S'inscrire</a></small>
        </form>
    )
}

export default Connexion;