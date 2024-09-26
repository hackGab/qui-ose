import React, { useState } from 'react';
import "bootstrap/dist/css/bootstrap.min.css";
import InputMask from 'react-input-mask';
import { Else, If, Then } from 'react-if';
import { Icon } from 'react-icons-kit';
import { eyeOff } from 'react-icons-kit/feather/eyeOff';
import { eye } from 'react-icons-kit/feather/eye';
import {useNavigate} from "react-router-dom";
import {useTranslation} from 'react-i18next';
import i18n from "i18next";

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
    const {t} = useTranslation();

    const handleSubmit = (event) => {
        event.preventDefault();
        setErrorMessages('');

        // Trim all input fields
        const trimmedPrenom = prenom.trim();
        const trimmedNom = nom.trim();
        const trimmedEmail = email.trim();
        const trimmedMpd = mpd.trim();
        const trimmedMpdConfirm = mpdConfirm.trim();
        const trimmedNum = num.trim();
        const trimmedDepartement = departement.trim();
        const trimmedNomEntreprise = nomEntreprise.trim();

        if (trimmedMpd !== trimmedMpdConfirm) {
            setErrorMessages(t('mpdNonIdentique'));
            return;
        }

        const userData = {
            firstName: trimmedPrenom,
            lastName: trimmedNom,
            credentials: {
                email: trimmedEmail,
                password: trimmedMpd
            },
            phoneNumber: trimmedNum,
            departement: role === 'employeur' ? undefined : trimmedDepartement,
            entreprise: role === 'employeur' ? trimmedNomEntreprise : undefined
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
                    setErrorMessages(t('utilisateurExiste'));
                } else {
                    setErrorMessages(t('erreurLorsCreationUser'));
                }
            })
            .then(data => {
                if (data) {
                    navigate('/login');
                }
            })
            .catch(error => {
                console.error('Erreur:', error);
                setErrorMessages(t('erreurConnexionServeur'));
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
           <legend>{t('ChampsObligatoires')} </legend>
            {errorMessages && <div className='alert alert-danger' style={{textAlign: 'center'}}>{errorMessages}</div>}
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
                           pattern={"^\\s*([a-zA-ZÀ-ÿ' ]+\\s*)+$"} autoFocus={true} required/>
                </div>

                <div className="form-group">
                    <label htmlFor="nom">{t('nom')}</label>
                    <input type="text" className="form-control" id="nom" name="nom" placeholder="Doe"
                           value={nom} onChange={(e) => setNom(e.target.value)}
                           pattern={"^\\s*([a-zA-ZÀ-ÿ' ]+\\s*)+$"} required/>
                </div>

                <div>
                    {/* Si le role est un employeur, ajouter ce champ */}
                    <If condition={role === 'employeur'}>
                        <Then>
                            <div>
                                <div className="form-group">
                                    <label htmlFor="nomEntreprise">{t('nomEntreprise')}</label>
                                    <input type="text" className="form-control" id="nomEntreprise"
                                           name="nomEntreprise"
                                           placeholder="Nom de l'entreprise"
                                           value={nomEntreprise} onChange={(e) => setNomEntreprise(e.target.value)}
                                           pattern={"^\\s*([a-zA-ZÀ-ÿ' ]+\\s*)+$"}
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
                                       pattern={"^\\s*([a-zA-ZÀ-ÿ' ]+\\s*)+$"}
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

                <button type="submit" className="btn btn-primary">{t('Sinscrire')}</button>
                <small style={{marginTop: '10px'}}>{t('DejaUnCompte')} <a href="/login">{t('connectezVous')}</a></small>
            </div>
        </form>
    );
}

export default Inscription;
