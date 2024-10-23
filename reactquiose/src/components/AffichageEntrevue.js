import { useState } from 'react';
import { Link } from 'react-router-dom';
import { FaCalendarAlt, FaClock } from 'react-icons/fa';
import { FaLocationPinLock } from 'react-icons/fa6';
import { Modal, Button, Card } from 'react-bootstrap';
import '../CSS/MesEntrevues.css';

function AffichageEntrevue({ entrevue, t }) {
    const [showModal, setShowModal] = useState(false);
    const [action, setAction] = useState(null);

    const emailEtudiant = entrevue.etudiantDTO.email;
    const idOffreDeStage = entrevue.offreDeStageDTO.id;

    const handleAccept = async () => {
        try {
            const response = await fetch(`http://localhost:8081/entrevues/changeStatus/${emailEtudiant}/${idOffreDeStage}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ status: "accepter" }),
            });

            if (response.ok) {
                console.log('Entrevue acceptée:', entrevue);
            } else {
                console.error('Erreur lors de l\'acceptation de l\'entrevue');
            }
        } catch (error) {
            console.error('Erreur réseau:', error);
        }
        setShowModal(false);
    };

    const handleRefuse = async () => {
        try {
            const response = await fetch(`http://localhost:8081/entrevues/changeStatus/${emailEtudiant}/${idOffreDeStage}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ status: "refuser" }),
            });

            if (response.ok) {
                console.log('Entrevue refusée:', entrevue);
            } else {
                console.error('Erreur lors du refus de l\'entrevue');
            }
        } catch (error) {
            console.error('Erreur réseau:', error);
        }
        setShowModal(false);
    };

    const handleShow = () => setShowModal(true);
    const handleClose = () => setShowModal(false);

    const { id, titre, offreDeStageDTO, status, location, dateHeure } = entrevue;

    const date = new Date(dateHeure);
    const formattedDate = date.toLocaleDateString();
    const formattedTime = date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

    return (
        <div className="col-12" key={id}>
            <div className={`d-inline-flex card offre-card shadow w-100 ${status ? status.toLowerCase() : 'sans-cv'}`}>
                <div onClick={handleShow} className="text-decoration-none">
                    <div className="card-body text-start">
                        <div className="card-title">
                            <div className="d-flex justify-content-between">
                                <h6 className="m-0">{offreDeStageDTO.employeur.entreprise}</h6>
                                <FaCalendarAlt />
                            </div>
                            <h4>{offreDeStageDTO.titre}</h4>
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
                </div>
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