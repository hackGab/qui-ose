import React, { useState } from 'react';
import "bootstrap/dist/css/bootstrap.min.css";
import InputMask from 'react-input-mask';
import {Else, If, Then} from 'react-if';

function Inscription() {
    const [prenom, setPrenom] = useState('');
    const [nom, setNom] = useState('');
    const [email, setEmail] = useState('');
    const [mpd, setMpd] = useState('');
    const [mpdConfirm, setMpdConfirm] = useState('');
    const [num, setNum] = useState('');
    const [role, setRole] = useState('');

    const handleSubmit = (event) => {
        event.preventDefault();

        if (mpd !== mpdConfirm) {
            alert('Les mots de passe ne correspondent pas.');
            console.error('Les mots de passe ne correspondent pas.');
            return;
        }

        const userData = {
            firstName: prenom,
            lastName: nom,
            credentials: {
                email,
                password: mpd
            },
            phoneNumber: num,
        };

        let url;
        console.log('Role:', role);
        switch (role) {
            case 'etudiant':
                url = 'http://localhost:8080/etudiant/creerEtudiant';
                break;
            case 'prof':
                url = 'http://localhost:8080/professeur/creerProfesseur';
                break;
            case 'employeur':
                url = 'http://localhost:8080/employeur/creerEmployeur';
                break;
            default:
                console.error('Rôle inconnu');
                return;
        }

        console.log('Données envoyées au backend:', userData);
        console.log('URL:', url);
        // Envoi d'une requête POST au backend
        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',

            },
            body: JSON.stringify(userData),
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
                    alert('Utilisateur créé avec succès');
                    console.log('Données reçues du backend:', data);
                }
            })
            .catch(error => {
                console.error('Erreur:', error);
            });
    };

    return (
        <form className='pt-0' onSubmit={handleSubmit}>
            <legend>Champs obligatoires*</legend>
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
                            required>
                            <option value='etudiant'>Étudiant</option>
                            <option value='prof'>Professeur</option>
                            <option value='employeur'>Employeur</option>
                        </select>
                    </div>
                </div>

            </div>


            <div className='row'>
                <div className="form-group">
                    <label htmlFor="prenom">Prénom*</label>
                    <input type="text" className="form-control" id="prenom" name="prenom" placeholder="John"
                           value={prenom} onChange={(e) => setPrenom(e.target.value)}
                           pattern={"[A-Za-z]+"} autoFocus={true} required/>
                </div>

                <div className="form-group">
                    <label htmlFor="nom">Nom*</label>
                    <input type="text" className="form-control" id="nom" name="nom" placeholder="Doe"
                           value={nom} onChange={(e) => setNom(e.target.value)}
                           pattern={"[A-Za-z]+"} required/>
                </div>
                <div>
                    {/* Si le role est un employeur, ajouter ce champ */}
                    <If condition={role === 'employeur'}>
                        <Then>
                            <div className='col-lg-12 col-md-6 col-6 m-auto'>
                                <div className="form-group">
                                    <label htmlFor="nomEntreprise">Nom de l'entreprise*</label>
                                    <input type="text" className="form-control" id="nomEntreprise"
                                           name="nomEntreprise"
                                           placeholder="Ville de Montréal"
                                           pattern={"[A-Za-z]+"}
                                            required/>
                                </div>
                            </div>
                        </Then>
                        <Else>
                            <div className="form-group">
                                <label htmlFor="departement">Département*</label>
                                <input type="text" className="form-control" id="departement" name="departement"

                                       placeholder="Technique de l'informatique" required/>
                            </div>
                        </Else>
                    </If>
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
                        name="num">
                        {(inputProps) => <input type="tel" {...inputProps} />}
                    </InputMask>
                </div>

                <div className="form-group">
                    <label htmlFor="mpd">Mot de passe*</label>
                    <input type="password" className="form-control" id="mpd" name="mpd" placeholder="********"
                           value={mpd} onChange={(e) => setMpd(e.target.value)}
                           required/>
                </div>

                <div className="form-group">
                    <label htmlFor="mpdConfirm">Confirmation du mot de passe*</label>
                    <input type="password" className="form-control" id="mpdConfirm" name="mpdConfirm"
                           value={mpdConfirm} onChange={(e) => setMpdConfirm(e.target.value)}
                           placeholder="********" required/>
                </div>
            </div>

            <button className="btn btn-primary w-50" type="submit">S'inscrire</button>

            <small>Déjà un compte? <a href="/login">Connectez-vous</a></small>
        </form>
    )
}

export default Inscription;
