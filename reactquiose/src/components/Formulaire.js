import "bootstrap/dist/css/bootstrap.min.css";
import logo from '../images/logo.png';
import Inscription from "./Inscription";

function Formulaire(props) {
    const { title } = props;
    return (
        <div className='App-image row'>
            <div className='App-header-connexion col-lg-7 col-md-4 col-9 m-auto'>
                <h1>Qui-ose</h1>
                <p>La plateforme de gestion de stage</p>
            </div>

            <div className='form-compte col-lg-4 col-md-6 col-9 m-auto'>
                <div style={{ display: "inline-flex", paddingTop: "0.5em"}}>
                    <img src={logo} style={{width: '4em', borderRadius: '1em'}} alt='logo'/>
                    &nbsp;
                    &nbsp;
                    <h2 className='m-auto'>{title}</h2>
                </div>
                <hr/>

                <Inscription title={title}/>

            </div>

        </div>
    )
}

export default Formulaire;