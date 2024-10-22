import { Link } from "react-router-dom";
import { FaCalendarAlt, FaClock } from "react-icons/fa";
import { FaLocationPinLock } from "react-icons/fa6";
import '../CSS/MesEntrevues.css';

function AffichageEntrevue({ entrevue, t }) {
    // Extraire les détails de l'entrevue
    const { id, titre, offreDeStage, status, location, dateHeure } = entrevue;

    // Formatter la date et l'heure
    const date = new Date(dateHeure);
    const formattedDate = date.toLocaleDateString(); // Formater selon la locale
    const formattedTime = date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }); // Format 24 heures

    return (
        <div className="col-12" key={id}>
            <div className={`d-inline-flex card offre-card shadow w-100 ${status ? status.toLowerCase() : 'sans-cv'}`}>
                <Link to={`/detailsEntrevues/${id}`} className="text-decoration-none">
                    <div className="card-body text-start">
                        <div className="card-title">
                            <div className="d-flex justify-content-between">
                                <h6 className="m-0">{titre}</h6>
                                <FaCalendarAlt />
                            </div>
                            <h4>{offreDeStage}</h4>
                        </div>
                        <div className="card-text">
                            <FaLocationPinLock /> &nbsp;
                            <span style={{ verticalAlign: "middle" }}>
                                <b>{location}</b>
                            </span>
                            <br />
                            <FaClock /> &nbsp;
                            <span style={{ verticalAlign: "middle" }}>
                                <b>{formattedDate}</b> {t('à')} <b>{formattedTime}</b>
                            </span>
                        </div>
                    </div>
                </Link>
            </div>
        </div>
    );
}

export default AffichageEntrevue;
