import React, { useState } from 'react';
import "bootstrap/dist/css/bootstrap.min.css";
import InputMask from 'react-input-mask';
import { Else, If, Then } from 'react-if';
import { Icon } from 'react-icons-kit';
import { eyeOff } from 'react-icons-kit/feather/eyeOff';
import { eye } from 'react-icons-kit/feather/eye';
import {useNavigate} from "react-router-dom";
import {useTranslation} from 'react-i18next';
import {changeLanguage} from "i18next";

function Inscription() {
    const [prenom, setPrenom] = useState('');
    const [nom, setNom] = useState('');
    const [email, setEmail] = useState('');
    const [mpd, setMpd] = useState('');
    const [mpdConfirm, setMpdConfirm] = useState('');
    const [num, setNum] = useState('');
    const [role, setRole] = useState('etudiant');
    const [type, setType] = useState('password');
    const [icon, setIcon] = useState(eyeOff);
    const [typeConf, setTypeConf] = useState('password');
    const [iconConf, setIconConf] = useState(eyeOff);
    const [departement, setDepartement] = useState('');
    const [nomEntreprise, setNomEntreprise] = useState('');
    const [errorMessages, setErrorMessages] = useState('');
    const navigate = useNavigate();
    const {t, i18n} = useTranslation();

    const handleSubmit = (event) => {
        event.preventDefault();
        setErrorMessages('');

        if (mpd !== mpdConfirm) {
            setErrorMessages('Les mots de passe ne sont pas identique.');
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
            departement: role === 'employeur' ? undefined : departement,
            entreprise: role === 'employeur' ? nomEntreprise : undefined
        };

        const changeLanguage = (lng) => {
            i18n.changeLanguage(lng);
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
            print: userData
        })
            .then(response => {
                console.log('Réponse du backend:', response.status);
                if (response.status === 201) {
                    return response.json();
                } else if (response.status === 409) {
                    setErrorMessages("L'utilisateur existe déjà.");
                } else {
                    setErrorMessages('Erreur lors de la création de l\'utilisateur.');
                }
            })
            .then(data => {
                if (data) {
                    navigate('/login');
                }
            })
            .catch(error => {
                console.error('Erreur:', error);
                setErrorMessages('Erreur lors de la connexion au serveur.');
            });
    };

    const afficherMdp = () => {
        setIcon(type === 'password' ? eye : eyeOff);
        setType(type === 'password' ? 'text' : 'password');
    };

    const afficherMdpConf = () => {
        setIconConf(typeConf === 'password' ? eye : eyeOff);
        setTypeConf(typeConf === 'password' ? 'text' : 'password');
    };

    return (
        <form className='pt-0' onSubmit={handleSubmit}>
            <button onClick={() => changeLanguage('fr')}>FR</button>
            <button onClick={() => changeLanguage('en')}>EN</button>
            <legend>{t('ChampsObligatoires')} </legend>
            <div className='row'>
                <div className='form-group' style={{ display: "inline-flex" }}>
                    <label htmlFor='role' className='col-6 m-auto'>{t('Jesuisun')}</label>
                    &nbsp;
                    <select
                        className='form-control col-6'
                        id='role'
                        name='role'
                        value={role}
                        onChange={(e) => {
                            setRole(e.target.value);
                            setNomEntreprise('');
                            setDepartement('');
                        }}
                        required>
                        <option value='etudiant'>{t('etudiant')}</option>
                        <option value='prof'>{t('prof')}</option>
                        <option value='employeur'>{t('employeur')}</option>
                    </select>
                </div>

            </div>


            <div className='row'>
                <div className="form-group">
                    <label htmlFor="prenom">{t('prenom')}</label>
                    <input type="text" className="form-control" id="prenom" name="prenom" placeholder="John"
                           value={prenom} onChange={(e) => setPrenom(e.target.value)}
                           pattern={"[A-Za-z]+"} autoFocus={true} required/>
                </div>

                <div className="form-group">
                    <label htmlFor="nom">{t('nom')}</label>
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
                                    <label htmlFor="nomEntreprise">{t('nomEntreprise')}</label>
                                    <input type="text" className="form-control" id="nomEntreprise"
                                           name="nomEntreprise"
                                           placeholder="Nom de l'entreprise"
                                           value={nomEntreprise} onChange={(e) => setNomEntreprise(e.target.value)}
                                           required/>
                                </div>
                            </div>
                        </Then>
                        <Else>
                            <div className="form-group">
                                <label htmlFor="departement">{t('Departement')}</label>
                                <input type="text" className="form-control" id="departement" name="departement"
                                       placeholder={t('PlaceHolderDepartement')}
                                       value={departement} onChange={(e) => setDepartement(e.target.value)}
                                       required/>
                            </div>
                        </Else>
                    </If>
                </div>

                <div className="form-group">
                    <label htmlFor="email">{t('Email')}</label>
                    <input type="email" className="form-control" id="email" name="email"
                           value={email} onChange={(e) => setEmail(e.target.value)}
                           placeholder="johndoe@gmail.com" required/>
                </div>

                <div className="form-group">
                    <label className="form-label" htmlFor="num">{t('Telephone')}</label>
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
                    <label htmlFor="mpd">{t('MotDePasse')}</label>
                    <div className="input-group">
                        <input type={type}
                               className="form-control"
                               id="mpd"
                               name="mpd"
                               placeholder="********"
                               value={mpd} onChange={(e) => setMpd(e.target.value)}
                               autoComplete="current-password"
                               required/>
                        <div className="input-group-append">
                            <span className="input-group-text" onClick={afficherMdp}>
                                <Icon icon={icon} size={20}/>
                            </span>
                        </div>
                    </div>
                </div>

                <div className="form-group">
                    <label htmlFor="mpdConfirm">{t('ConfirmerMotDePasse')}</label>
                    <div className="input-group">
                        <input type={typeConf}
                               className="form-control"
                               id="mpdConfirm"
                               name="mpdConfirm"
                               placeholder="********"
                               value={mpdConfirm} onChange={(e) => setMpdConfirm(e.target.value)}
                               autoComplete="current-password"
                               required/>
                        <div className="input-group-append">
                            <span className="input-group-text" onClick={afficherMdpConf}>
                                <Icon icon={iconConf} size={20}/>
                            </span>
                        </div>
                    </div>
                </div>

                <button type="submit" className="btn btn-primary">S'inscrire</button>
                <small style={{marginTop: '10px'}}>Déjà un compte? <a href="/login">Connectez-vous</a></small>
                {errorMessages && <div className='alert alert-danger' style={{marginTop: '20px', textAlign: 'center'}}>{errorMessages}</div>}
            </div>

            <button className="btn btn-primary w-50" type="submit">{t('Sinscrire')}</button>

            <small>{t('DejaUnCompte')} <a href="/login">{t('connectezVous')}</a></small>
        </form>
    );
}

export default Inscription;
