import { useState } from 'react';
import {FaCalendarAlt, FaClock} from 'react-icons/fa';
import { FaLocationPinLock } from 'react-icons/fa6';
import { Modal, Button, Card } from 'react-bootstrap';
import '../../CSS/MesEntrevues.css';
import i18n from "i18next";

function AffichageEntrevue({ entrevue, t, onAccept, onReject }) {
    const [showModal, setShowModal] = useState(false);

    const emailEtudiant = entrevue.etudiantDTO.email;
    const idOffreDeStage = entrevue.offreDeStageDTO.id;

    const handleAccept = async () => {
        try {
            const response = await fetch(`http://localhost:8081/entrevues/changerStatus/${emailEtudiant}/${idOffreDeStage}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: ("accepter"),
            });

            if (response.ok) {
                if (onAccept) onAccept(entrevue);
            }
        } catch (error) {
            console.error('Erreur réseau:', error);
        }
        setShowModal(false);
    };

    const handleRefuse = async () => {
        try {
            const response = await fetch(`http://localhost:8081/entrevues/changerStatus/${emailEtudiant}/${idOffreDeStage}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: ("refuser"),
            });

            if (response.ok) {
                if (onReject) onReject(entrevue);
            }

        } catch (error) {
            console.error('Erreur réseau:', error);
        }
        setShowModal(false);
    };

    const handleShow = () => setShowModal(true);
    const handleClose = () => setShowModal(false);

    const { id, offreDeStageDTO, location, dateHeure, status } = entrevue;

    const formatDate = (dateString) => {
        const options = { day: '2-digit', month: 'long', year: 'numeric', hour: '2-digit', minute: '2-digit' };
        return new Date(dateString).toLocaleDateString(i18n.language, options);
    }

    const afficheEntrevues = () => {
        return (
            <div className="card-body text-start">
                <div className="card-title">
                    <div className="d-flex justify-content-between">
                        <h6 className="m-0">{offreDeStageDTO.employeur.entreprise}</h6>
                        <FaCalendarAlt/>
                    </div>
                    <h4>{offreDeStageDTO.titre}</h4>
                </div>
                <div className="card-text">
                    <FaLocationPinLock/> &nbsp;
                    <span style={{verticalAlign: "middle"}}>
                        <b>{location}</b>
                    </span>
                    <br/>
                    <FaClock/> &nbsp;
                    <span style={{verticalAlign: "middle"}}>
                        <b>{formatDate(dateHeure)}</b>
                    </span>
                </div>
            </div>
        );
    }

    return (
        <div className="col-12" key={id}>
            <div className={`d-inline-flex card offre-card shadow w-100 ${status ? status.toLowerCase() : 'sans-cv'}`}>
                {status !== 'accepter' ? (
                    <div onClick={handleShow} className="text-decoration-none">
                        {afficheEntrevues()}
                    </div>
                ) : (
                    afficheEntrevues()
                )}
            </div>

            <Modal show={showModal} onHide={handleClose} centered>
                <Modal.Header closeButton className="bg-primary text-white">
                    <Modal.Title>Confirmation d'Entrevue</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Card className="text-center border-0">
                        <Card.Body>
                            <Card.Title className="text-primary">{offreDeStageDTO.titre}</Card.Title>
                            <Card.Text>
                                Êtes-vous sûr de vouloir accepter ou refuser cette entrevue ?
                            </Card.Text>
                        </Card.Body>
                    </Card>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="success" onClick={handleAccept}>
                        Accepter
                    </Button>
                    <Button variant="danger" onClick={handleRefuse}>
                        Refuser
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
}

export default AffichageEntrevue;
