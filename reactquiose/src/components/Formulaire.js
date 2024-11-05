import "bootstrap/dist/css/bootstrap.min.css";
import logo from '../images/logo.png';
import Inscription from "./Inscription";
import Connexion from "./Connexion";
import {If, Then} from "react-if";
import React from "react";
import {useTranslation} from "react-i18next";
import i18n from "i18next";


function Formulaire(props) {
    const { title } = props;
    const {t} = useTranslation();

    const getFormHeight = () => {
        const windowWidth = window.innerWidth;
        if (title === 'Inscription') {
            return windowWidth < 1200 ? '85vh' : '98vh';
        } else if (title === 'Connexion') {
            return '55vh';
        }
    };

    const formHeight = getFormHeight();

    const changeLanguage = (lng) => {
        i18n.changeLanguage(lng);
    };

    return (
        <div className='App-image row'>
            <div className='App-header-connexion col-lg-7 col-md-4 col-9 m-auto'>
                <h1>Qui-ose</h1>
                <p>{t('PlateForme')}</p>
            </div>

            <div className='form-compte col-lg-4 col-md-6 col-9 m-auto' style={{height: formHeight}}>
                <div style={{display: "inline-flex", paddingTop: "0.5em", width: "100%"}}>
                    <img src={logo} style={{width: '4em', borderRadius: '1em'}} alt='logo'/>
                    &nbsp;
                    &nbsp;
                    <h2 className='m-auto'>{t(title)}</h2>

                    <div className='mt-auto mb-auto'>
                        <button onClick={() => changeLanguage('fr')} className="language-button">FR</button>
                        <button onClick={() => changeLanguage('en')} className="language-button">EN</button>
                    </div>
                </div>
                <hr/>

                <If condition={title === 'Connexion'}>
                    <Then>
                        <Connexion/>
                    </Then>
                </If>
                <If condition={title === 'Inscription'}>
                    <Then>
                        <Inscription/>

                    </Then>
                </If>


            </div>

        </div>
    )
}

export default Formulaire;
