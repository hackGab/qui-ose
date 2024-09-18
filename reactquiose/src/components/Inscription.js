import "bootstrap/dist/css/bootstrap.min.css";
import InputMask from 'react-input-mask';

function Inscription() {
    return (
        <form>
            <legend>Champs obligatoires*</legend>

            <div className='row'>
                <div className='col-lg-12 col-md-4 col-4 m-auto'>
                    <div className="form-group">
                        <label htmlFor="prenom">Prénom*</label>
                        <input type="text" className="form-control" id="prenom" name="prenom" placeholder="John"
                                   pattern={"[A-Za-z]+"} autoFocus={true} required/>
                    </div>

                    <div className="form-group">
                        <label htmlFor="nom">Nom*</label>
                        <input type="text" className="form-control" id="nom" name="nom" placeholder="Doe"
                               pattern={"[A-Za-z]+"} required/>
                    </div>

                    <div className="form-group">
                        <label htmlFor="mpd">Mot de passe*</label>
                        <input type="password" className="form-control" id="mpd" name="mpd" placeholder="********"
                               required/>
                    </div>
                </div>

                <div className='col-lg-12 col-md-6 col-6 m-auto'>

                    <div className="form-group">
                        <label htmlFor="mpdConfirm">Confirmation du mot de passe*</label>
                        <input type="password" className="form-control" id="mpdConfirm" name="mpdConfirm"
                               placeholder="********" required/>
                    </div>

                    <div className="form-group">
                        <label htmlFor="email">Email*</label>
                        <input type="email" className="form-control" id="email" name="email"
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
                            name="num">
                            {(inputProps) => <input type="tel" {...inputProps} />}
                        </InputMask>
                    </div>
                </div>
            </div>


            <button className="btn btn-primary w-50" type="submit">S'inscrire</button>


            <small>Déjà un compte? <a href="/login">Connectez-vous</a></small>
        </form>
    )
}

export default Inscription;