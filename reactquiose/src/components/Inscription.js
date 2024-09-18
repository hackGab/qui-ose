import React, { useState } from 'react';
import "bootstrap/dist/css/bootstrap.min.css";
import InputMask from 'react-input-mask';

function Inscription() {
    const [prenom, setPrenom] = useState('');
    const [nom, setNom] = useState('');
    const [email, setEmail] = useState('');
    const [mpd, setMpd] = useState('');
    const [mpdConfirm, setMpdConfirm] = useState('');
    const [num, setNum] = useState('');

    const handleSubmit = (event) => {
        event.preventDefault();

        if (mpd !== mpdConfirm) {
            alert('Les mots de passe ne correspondent pas.');
            console.error('Les mots de passe ne correspondent pas.');
            return;
        }

        const etudiantData = {
            firstName: prenom,
            lastName: nom,
            credentials: {
                email,
                password: mpd
            }
        };

        console.log('Données envoyées au backend:', etudiantData);

        // Envoi d'une requête POST au backend
        fetch('http://localhost:8080/etudiant/creerEtudiant', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(etudiantData),
        })
            .then(response => {
                console.log('Réponse du backend:', response.status);
                if (response.status === 201) {
                    return response.json();
                } else if (response.status === 409) {
                    alert("L'utilisateur existe déjà.");
                    console.error("L'utilisateur existe déjà.");
                } else {
                    alert('Erreur lors de la création de l\'étudiant.');
                    console.error('Erreur lors de la création de l\'étudiant.');
                }
            })
            .then(data => {
                if (data) {
                    alert('Étudiant créé avec succès');
                    console.log('Données reçues du backend:', data);
                }
            })
            .catch(error => {
                console.error('Erreur:', error);
            });
    };

    return (
        <form onSubmit={handleSubmit}>
            <legend>Champs obligatoires*</legend>

            <div className="form-group">
                <label htmlFor="prenom">Prénom*</label>
                <input type="text" className="form-control" id="prenom" name="prenom" placeholder="John"
                       value={prenom} onChange={(e) => setPrenom(e.target.value)}
                       pattern="[A-Za-z]+" autoFocus={true} required/>
            </div>

            <div className="form-group">
                <label htmlFor="nom">Nom*</label>
                <input type="text" className="form-control" id="nom" name="nom" placeholder="Doe"
                       value={nom} onChange={(e) => setNom(e.target.value)}
                       pattern="[A-Za-z]+" required/>
            </div>

            <div className="form-group">
                <label htmlFor="mpd">Mot de passe*</label>
                <input type="password" className="form-control" id="mpd" name="mpd" placeholder="********"
                       value={mpd} onChange={(e) => setMpd(e.target.value)} required/>
            </div>

            <div className="form-group">
                <label htmlFor="mpdConfirm">Confirmation du mot de passe*</label>
                <input type="password" className="form-control" id="mpdConfirm" name="mpdConfirm"
                       placeholder="********" value={mpdConfirm} onChange={(e) => setMpdConfirm(e.target.value)}
                       required/>
            </div>

            <div className="form-group">
                <label htmlFor="email">Email*</label>
                <input type="email" className="form-control" id="email" name="email"
                       value={email} onChange={(e) => setEmail(e.target.value)}
                       placeholder="johndoe@gmail.com" required/>
            </div>

            <div className="form-group">
                <label className="form-label" htmlFor="num">Numéro de téléphone</label>
                <InputMask
                    className="form-control"
                    mask="(999)-999-9999"
                    maskChar={null}
                    id="num"
                    placeholder="(514)-123-4567"
                    value={num} onChange={(e) => setNum(e.target.value)}
                    name="num"
                >
                    {(inputProps) => <input type="tel" {...inputProps} />}
                </InputMask>
            </div>

            <button className="btn btn-primary w-50" type="submit">S'inscrire</button>
            <br/>
            <small>Déjà un compte? <a href="/login">Connectez-vous</a></small>
        </form>
    );
}

export default Inscription;
