import {Link} from "react-router-dom";
import {FaCalendarAlt, FaClock} from "react-icons/fa";
import {FaLocationPinLock} from "react-icons/fa6";
import '../CSS/MesEntrevues.css';


function AffichageEntrevue({entrevue, t}) {

return (
    <div className="col-12" key={entrevue.id}>
        <div className={`d-inline-flex card offre-card shadow w-100
                ${entrevue.status ? entrevue.status.toLowerCase() : 'sans-cv'}`}>
            <Link to={`/detailsEntrevues/${entrevue.id}`} className="text-decoration-none">
                <div className="card-body text-start">
                    <div className="card-title">
                        <div className="d-flex justify-content-between">
                            <h6 className="m-0">{entrevue.titre}</h6>
                            <FaCalendarAlt/>
                        </div>
                        <h4>{entrevue.offre}</h4>
                    </div>
                    <div className="card-text">
                        <FaLocationPinLock/> &nbsp;
                        <span style={{verticalAlign: "middle"}}>
                                <b>{entrevue.localisation}</b> ({t('pour')} {entrevue.entreprise})
                            </span>
                        <br/>
                        <FaClock/> &nbsp;
                        <span style={{verticalAlign: "middle"}}>
                                <b>{entrevue.date}</b> {t('à')} <b>{entrevue.heureDebut}-{entrevue.heureFin}</b> ({entrevue.duree} min)
                            </span>
                    </div>
                </div>
            </Link>
        </div>
    </div>
);
}

export default AffichageEntrevue;