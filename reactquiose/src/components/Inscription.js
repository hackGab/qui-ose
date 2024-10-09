import React, { useState } from 'react';
import "bootstrap/dist/css/bootstrap.min.css";
import InputMask from 'react-input-mask';
import { Else, If, Then } from 'react-if';
import { Icon } from 'react-icons-kit';
import { eyeOff } from 'react-icons-kit/feather/eyeOff';
import { eye } from 'react-icons-kit/feather/eye';
import { useNavigate } from "react-router-dom";
import { useTranslation } from 'react-i18next';
import '../CSS/BoutonLangue.css'
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
    const { t } = useTranslation();

    const handleSubmit = async (event) => {
        event.preventDefault();
        setErrorMessages('');

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
        switch (role) {
            case 'etudiant':
                url = 'http://localhost:8081/etudiant/creerEtudiant';
                break;
            case 'prof':
                url = 'http://localhost:8081/professeur/creerProfesseur';
                break;
            case 'employeur':
                url = 'http://localhost:8081/employeur/creerEmployeur';
                break;
            default:
                console.error('Rôle inconnu');
                return;
        }
        const handleLogin = async (userData) => {
            try {
                const response = await fetch('http://localhost:8081/user/login', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(userData),
                });

                if (!response.ok) {
                    throw new Error(t('connexionEchouee'));
                }

                const data = await response.json();
                const accessToken = data.accessToken;

                const userResponse = await fetch('http://localhost:8081/user/me', {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${accessToken}`
                    }
                });

                const userDataResponse = await userResponse.json();

                return { userData: userDataResponse, accessToken };

            } catch (error) {
                console.error('Erreur lors de la connexion:', error);
                throw new Error(error.message || t('erreurLorsConnexion'));
            }
        };

        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(userData)
            });

            if (response.status === 201) {
                const loginData = {
                    email: trimmedEmail,
                    password: trimmedMpd
                };

                try {
                    const { userData: fetchedUserData } = await handleLogin(loginData);
                    navigateToDashboard(fetchedUserData);

                } catch (error) {
                    console.error('Erreur lors de la connexion:', error);
                    setErrorMessages(t('erreurLorsConnexion'));
                }
            } else if (response.status === 409) {
                setErrorMessages(t('utilisateurExiste'));
            } else {
                setErrorMessages(t('erreurLorsCreationUser'));
            }
        } catch (error) {
            console.error('Erreur lors de l\'inscription:', error);
            setErrorMessages(error.message || t('erreurLorsInscription'));
        }
    };

    const afficherMdp = () => {
        setIcon(type === 'password' ? eye : eyeOff);
        setType(type === 'password' ? 'text' : 'password');
    };

    const afficherMdpConf = () => {
        setIconConf(typeConf === 'password' ? eye : eyeOff);
        setTypeConf(typeConf === 'password' ? 'text' : 'password');
    };

    const changeLanguage = (lng) => {
        i18n.changeLanguage(lng);
    };

    const navigateToDashboard = (userData) => {
        const path = `/${
            userData.role === 'ETUDIANT' ? 'accueilEtudiant' :
                userData.role === 'EMPLOYEUR' ? 'accueilEmployeur' :
                    userData.role === 'GESTIONNAIRE' ? 'accueilGestionnaire' :
                        'accueilProfesseur'
        }`;
        navigate(path, { state: { userData } });
    };

    return (
        <div>
            <div>
                <button onClick={() => changeLanguage('fr')} className="language-button">FR</button>
                <button onClick={() => changeLanguage('en')} className="language-button">EN</button>
            </div>
        <form className='pt-0' onSubmit={handleSubmit}>
            <legend>{t('ChampsObligatoires')} </legend>
            {errorMessages && <div className='alert alert-danger' style={{textAlign: 'center', fontSize: '2vmin'}}>
                    {errorMessages}
                </div>}
                <div className='row'>
                    <div className='form-group' style={{display: "inline-flex"}}>
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
                        <If condition={role === 'employeur'}>
                            <Then>
                                <div className="form-group">
                                    <label htmlFor="nomEntreprise">{t('nomEntreprise')}</label>
                                    <input type="text" className="form-control" id="nomEntreprise"
                                           name="nomEntreprise"
                                           placeholder="Nom de l'entreprise"
                                           value={nomEntreprise} onChange={(e) => setNomEntreprise(e.target.value)}
                                           pattern={"^\\s*([a-zA-ZÀ-ÿ' ]+\\s*)+$"}
                                           autoComplete={"off"}
                                           required/>
                                </div>
                            </Then>
                            <Else>
                                <div className="form-group">
                                    <label htmlFor="departement">{t('Departement')}</label>
                                    <input type="text" className="form-control" id="departement" name="departement"
                                           placeholder={t('PlaceHolderDepartement')}
                                           value={departement} onChange={(e) => setDepartement(e.target.value)}
                                           pattern={"^\\s*([a-zA-ZÀ-ÿ' ]+\\s*)+$"}
                                           autoComplete={"off"}
                                           required/>
                                </div>
                            </Else>
                        </If>
                    </div>

                    <div className="form-group">
                        <label htmlFor="email">{t('Email')}</label>
                        <input type="email" className="form-control" id="email" name="email"
                               value={email} onChange={(e) => setEmail(e.target.value)}
                               placeholder="johndoe@gmail.com"
                               autoComplete={"on"}
                               required/>
                    </div>

                    <div className="form-group">
                        <label className="form-label" htmlFor="num">{t('Telephone')}</label>
                        <InputMask
                            className="form-control"
                            mask="(999)-999-9999"
                            maskChar={null}
                            id="num"
                            placeholder="(514)-123-4567"
                            autoComplete={"off"}
                            value={num} onChange={(e) => setNum(e.target.value)}
                            required
                        />
                    </div>


                    <div className="form-group mt-2">
                        <label htmlFor="mpd">{t('MotDePasse')}</label>
                        <div className="d-flex">
                            <div className="input-group">
                                <input
                                    type={type}
                                    className="form-control m-0"
                                    id="mpd"
                                    name="mpd"
                                    placeholder={t('PlaceHolderMdp')}
                                    value={mpd} onChange={(e) => setMpd(e.target.value)}
                                    required
                                />
                            </div>

                            <span onClick={afficherMdp} style={{cursor: 'pointer', margin: "auto", marginLeft: "0.5em"}}>
                                <Icon icon={icon} size={20}/>
                            </span>
                        </div>
                    </div>

                    <div className="form-group mt-3">
                        <label htmlFor="mpdConfirm">{t('ConfirmerMotDePasse')}</label>
                        <div className="d-flex">
                            <div className="input-group">
                                <input
                                    type={typeConf}
                                    className="form-control m-0"
                                    id="mpdConfirm"
                                    name="mpdConfirm"
                                    placeholder={t('PlaceHolderConfMdp')}
                                    value={mpdConfirm} onChange={(e) => setMpdConfirm(e.target.value)}
                                    autoComplete={"off"}
                                    required
                                />
                            </div>

                            <span onClick={afficherMdpConf} style={{cursor: 'pointer', margin: "auto", marginLeft: "0.5em"}}>
                                <Icon icon={icon} size={20}/>
                            </span>
                        </div>
                    </div>

                    <button type="submit" className="btn btn-primary w-50 mt-4 m-auto">{t('submit')}</button>

                    <small style={{marginTop: '10px'}}>
                        {t('DejaUnCompte')} <a href="/login">{t('connectezVous')}</a>
                    </small>
                </div>
            </form>
        </div>
    );
}

export default Inscription;
