import "bootstrap/dist/css/bootstrap.min.css";
import logo from '../images/logo.png';
import Inscription from "./Inscription";
import Connexion from "./Connexion";
import {If, Then} from "react-if";
import React from "react";

function Formulaire(props) {
    const { title } = props;
    const formHeight = title === 'Inscription' ? "85vh" : "50vh";
    return (
        <div className='App-image row'>
            <div className='App-header-connexion col-lg-7 col-md-4 col-9 m-auto'>
                <h1>Qui-ose</h1>
                <p>La plateforme de gestion de stage</p>
            </div>

            <div className='form-compte col-lg-4 col-md-6 col-9 m-auto' style={{height: formHeight}}>
                <div style={{ display: "inline-flex", paddingTop: "0.5em"}}>
                    <img src={logo} style={{width: '4em', borderRadius: '1em'}} alt='logo'/>
                    &nbsp;
                    &nbsp;
                    <h2 className='m-auto'>{title}</h2>
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
