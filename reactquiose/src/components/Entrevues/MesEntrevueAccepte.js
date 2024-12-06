import {useLocation} from "react-router-dom";
import React, {useEffect, useState} from "react";
import {format} from 'date-fns';
import {useTranslation} from "react-i18next";
import EmployeurHeader from "../Header/EmployeurHeader";
import "../../CSS/MesEntrevueAccepte.css";
import {FaCalendarAlt, FaCheck, FaClipboardList, FaFilePdf, FaListAlt, FaPercent} from "react-icons/fa";
import ConfirmModal from "../ConfirmModal";
import i18n from "i18next";
import {FaLocationPinLock} from "react-icons/fa6";
import {getLocalStorageSession} from "../../utils/methodes/getSessionLocalStorage";
import Modal from "react-bootstrap/Modal";

function MesEntrevueAccepte() {
    const today = format(new Date(), 'dd/MM/yyyy');

    const location = useLocation();
    const userData = location.state?.userData;
    const employeurEmail = userData.credentials.email;
    const [entrevues, setEntrevues] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const [statusMessages, setStatusMessages] = useState({});
    const [showModal, setShowModal] = useState(false);
    const [showDetailsModal, setShowDetailsModal] = useState(false);
    const [currentAction, setCurrentAction] = useState(null);
    const [currentEntrevue, setCurrentEntrevue] = useState(null);
    const [evaluationCree, setEvaluationCree] = useState(false);
    const [selectedEntrevue, setSelectedEntrevue] = useState(null);
    const [evaluations, setEvaluations] = useState([]);
    const { t } = useTranslation();
    const [session, setSession] = useState(getLocalStorageSession());

    const [filters, setFilters] = useState(["all"]);

    const EvaluationConformiteOptions = [
        {label: t("TotalementEnAccord"), value: "TOTAL_EN_ACCORD"},
        {label: t("PlutotEnAccord"), value: "PLUTOT_EN_ACCORD"},
        {label: t("PlutotEnDesaccord"), value: "PLUTOT_EN_DESACCORD"},
        {label: t("TotalementEnDesaccord"), value: "TOTAL_EN_DESACCORD"},
        {label: t("ImpossibleDeSePrononcer"), value: "IMPOSSIBLE_SE_PRONONCER"}
    ];

    const [evaluation, setEvaluation] = useState({
        employeur: userData,
        etudiant: "",
        nomEleve: "",
        programmeEtude: "",
        nomEntreprise: userData.entreprise,
        nomSuperviseur: userData.firstName + " " + userData.lastName,
        fonction: "",
        telephone: "",
        // Productivity Section
        planifOrganiserTravail: "",
        comprendreDirectives: "",
        maintenirRythmeTravail: "",
        etablirPriorites: "",
        respecterEcheanciers: "",
        commentairesProductivite: "",
        // Quality of Work Section
        respecterMandats: "",
        attentionAuxDetails: "",
        verifierTravail: "",
        perfectionnement: "",
        analyseProblemes: "",
        commentairesQualiteTravail: "",
        // Interpersonal Relations Section
        etablirContacts: "",
        contribuerTravailEquipe: "",
        adapterCultureEntreprise: "",
        accepterCritiques: "",
        respectueux: "",
        ecouteActive: "",
        commentairesRelationsInterpersonnelles: "",
        // Personal Skills Section
        interetMotivationTravail: "",
        exprimerIdees: "",
        initiative: "",
        travailSecuritaire: "",
        sensResponsabilite: "",
        ponctualiteAssiduite: "",
        habiletePersonnelles: "",
        // Overall Appreciation
        appreciationGlobale: "",
        commentairesAppreciation: "",
        // Additional Fields
        evaluationDiscuteeAvecStagiaire: false,
        heuresEncadrementParSemaine: 0.0,
        entrepriseSouhaiteProchainStage: "",
        commentairesFormationTechnique: "",
        signatureEmployeur: userData.credentials.email,
        dateSignature: today
    });

    const fetchSession = async (session) => {
        try {


            const responseEntrevuesAccepte = await fetch(
                `http://localhost:8081/entrevues/acceptees/employeur/${employeurEmail}/session/${session}`
            );


            if (!responseEntrevuesAccepte.ok) {
                if (responseEntrevuesAccepte.status === 404 || responseEntrevuesAccepte.status === 204) {

                    setEntrevues([]);
                    return;
                } else {
                    throw new Error(
                        `Erreur du serveur: ${responseEntrevuesAccepte.statusText}`
                    );
                }
            }
            else{
                setEntrevues([]);
            }

            const entrevuesAccepteData = await responseEntrevuesAccepte.json();

            if (entrevuesAccepteData.status === 204) {

                setEntrevues([]);
                return;
            }

            setEntrevues(entrevuesAccepteData);

        } catch (error) {
            console.error("Erreur lors de la récupération des entrevues:", error);
        } finally {
            setIsLoading(false);
        }
    };


    useEffect(() => {
        if (filters.length === 0) {
            setFilters(["all"]);
        }
    }, [filters]);


    useEffect(() => {
        const fetchOffresEntrevues = async () => {
            if (!employeurEmail) {
                setError("Email employeur non fourni");
                setIsLoading(false);
                return;
            }
            await fetchSession(session);
        };

        fetchOffresEntrevues();
    }, [employeurEmail, session]);

    useEffect(() => {
        entrevues.forEach(entrevue => {
            setDecisionCandidate(entrevue)
        });
    }, [entrevues]);

    useEffect(() => {
        const fetchEvaluations = async () => {
            try {
                const response = await fetch("http://localhost:8081/employeur/evaluationEmployeur/all", {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                    },
                });

                if (!response.ok) {
                    return;
                }

                const data = await response.json();
                setEvaluations(data);
            } catch (error) {
                console.error("Erreur réseau:", error);
            }
        };

        fetchEvaluations();
    }, []);

    const handleCandidatureAcceptee = (entrevueAcceptee) => {
        setEntrevues(prevEntrevues =>
            prevEntrevues.map(entrevue =>
                entrevue.etudiantgetEvaluationEtudiantDTO === entrevueAcceptee.etudiantDTO && entrevue.offreDeStageDTO === entrevueAcceptee.offreDeStageDTO
                    ? {...entrevue, status: 'accepter'}
                    : entrevue
            )
        );

        setDecisionCandidate(entrevueAcceptee)
    }

    const handleCandidatureRejete = (entrevueRejete) => {
        setEntrevues(prevEntrevues =>
            prevEntrevues.map(entrevue =>
                entrevue.etudiantDTO === entrevueRejete.etudiantDTO && entrevue.offreDeStageDTO === entrevueRejete.offreDeStageDTO
                    ? {...entrevue, status: 'refuser'}
                    : entrevue
            )
        );

        setDecisionCandidate(entrevueRejete)
    }

    const setDecisionCandidate = async (entrevue) => {
        const decision = await getDecisionCandidate(entrevue);
        if (decision !== null) {
            setStatusMessages(prevStatusMessages => ({
                ...prevStatusMessages,
                [entrevue.id]: decision ? t('CandidatureAcceptee') : t('CandidatureRejetee')
            }));
        }
    }

    const handleAccept = async (entrevue) => {
        setCurrentAction(() => () => acceptEntrevue(entrevue));
        setCurrentEntrevue(entrevue);
        setShowModal(true);
    };

    const handleRefuse = async (entrevue) => {
        setCurrentAction(() => () => refuseEntrevue(entrevue));
        setCurrentEntrevue(entrevue);
        setShowModal(true);
    };

    const handleInterviewClick = async (entrevue) => {

        const candidature = asCandidature(entrevue);


        const evaluation = await getEvaluationEtudiant(employeurEmail, entrevue.etudiantDTO.email);
        if(evaluation) {
            setShowDetailsModal(false);
            return;
        }

        if(candidature && entrevue.etudiantDTO.professeur){
            setSelectedEntrevue(entrevue);
            setShowDetailsModal(true);

        }
    };

    const closeDetailsModal = () => {
        setShowDetailsModal(false);
        setSelectedEntrevue(null);
    };

    const handleChange = (e, field) => {

        const { value } = e.target;
        setEvaluation((prevEvaluation) => ({
            ...prevEvaluation,
            [field]: value,
        }));
    };

    const handleInputChange = (e, field) => {
        setEvaluation({
            ...evaluation,
            [field]: e.target.value
        });
    };

    const acceptEntrevue = async (entrevue) => {
        try {
            const response = await fetch(`http://localhost:8081/candidatures/accepter/${entrevue.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: ("accepter"),
            });

            if (response.ok) {
                handleCandidatureAcceptee(entrevue);
            }
        } catch (error) {
            console.error('Erreur réseau:', error);
        }
        setShowModal(false);
    };

    const refuseEntrevue = async (entrevue) => {
        try {
            const response = await fetch(`http://localhost:8081/candidatures/refuser/${entrevue.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: ("refuser"),
            });

            if (response.ok) {
                handleCandidatureRejete(entrevue);
            }
        } catch (error) {
            console.error('Erreur réseau:', error);
        }
        setShowModal(false);
    };


    const getDecisionCandidate = async (entrevue) => {
        try {
            const response = await fetch(`http://localhost:8081/candidatures/${entrevue.id}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (response.status === 404) {

                return null;
            }

            if (response.ok) {
                const data = await response.json();
                if (data === null || data === undefined) {

                    return null;
                }


                return data.accepte;
            }
        } catch (error) {
            console.error('Erreur réseau:', error);
        }
        return null;
    }

    const asCandidature = async (entrevue) => {
        try {
            const response = await fetch(`http://localhost:8081/candidatures/${entrevue.id}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (response.status === 404) {

                closeDetailsModal()
                return false;
            }

            if (response.ok) {
                return true
            }

        } catch (error) {
            console.error('Erreur réseau:', error);
        }
    }

    const groupInterviewsByOffer = (entrevues) => {
        return entrevues.reduce((acc, entrevue) => {
            const offerId = entrevue.offreDeStageDTO.id;
            if (!acc[offerId]) {
                acc[offerId] = {
                    offer: entrevue.offreDeStageDTO,
                    entrevues: []
                };
            }
            acc[offerId].entrevues.push(entrevue);
            return acc;
        }, {});
    }

    const getEtudiantByEmail = async (email) => {
        try {
            const response = await fetch(`http://localhost:8081/etudiant/credentials/${email}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                }
            });

            if (!response.ok) {
               
                return;
            }

            const etudiantData = await response.json();

            return etudiantData;

        } catch (error) {
            console.error('Erreur réseau:', error);
        }
    };

    const creerEvaluationEtudiant = async (emailEmployeur, emailEtudiant, evaluationStageEmployeur) => {
        try {
            const etudiant = await getEtudiantByEmail(emailEtudiant);
            if (!etudiant) {

                return;
            }

            evaluationStageEmployeur.etudiant = etudiant;
            evaluationStageEmployeur.nomEleve = etudiant.firstName + " " + etudiant.lastName;
            evaluationStageEmployeur.telephone = etudiant.phoneNumber;



            const response = await fetch(`http://localhost:8081/employeur/creerEvaluationEtudiant/${emailEmployeur}/${emailEtudiant}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(evaluationStageEmployeur),
            });

            evaluations.push(evaluationStageEmployeur);

            if (!response.ok) {
                throw new Error('Erreur lors de la création de l\'évaluation');
            }

            if (response.status === 201) {
                await response.json();
                setEvaluationCree(true);
                closeDetailsModal()
            }

        } catch (error) {
            console.error('Erreur lors de la création de l\'évaluation:', error);
        }
    }

    const getEvaluationEtudiant = async (emailEmployeur, emailEtudiant) => {
        try {
            const response = await fetch(`http://localhost:8081/employeur/evaluationEmployeur/${emailEmployeur}/${emailEtudiant}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                }
            });



            if (!response.ok) {
                return;
            }

            return await response.json();

        } catch (error) {
            console.error('Erreur réseau:', error);
        }
    }

    const genererPdf = async (entrevue) => {
        const evaluationChoisit = await getEvaluationEtudiant(employeurEmail, entrevue.etudiantDTO.email);
        await fetch('http://localhost:8081/generatePDF/evaluationEmployeur', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(evaluationChoisit),
        })
            .then(response => response.blob())
            .then(blob => {
                const url = window.URL.createObjectURL(new Blob([blob]));
                const link = document.createElement('a');
                link.href = url;
                link.setAttribute('download', 'Evaluation_Stage_Employeur.pdf');
                document.body.appendChild(link);
                link.click();
                link.parentNode.removeChild(link);
            })
            .catch(error => console.error('Error downloading PDF:', error));

    };

    if (isLoading) {
        return <div className="text-center mt-5">
            <div className="spinner-border" role="status"></div>
            <br/>
            <span className="sr-only">{t('ChargementDesEntrevues')}</span>
        </div>;
    }

    if (error) {
        return <div style={{ fontSize: "1.5rem" }}>{t('Erreur')} {error}</div>;
    }


    const handleFilterChange = (e) => {
        const { value, checked } = e.target;
        if (value === "all") {
            setFilters(checked ? ["all"] : []);
        } else {
            setFilters(prevFilters =>
                checked ? prevFilters.filter(filter => filter !== "all").concat(value) : prevFilters.filter(filter => filter !== value)
            );
        }
    };

    const showButtonsIfDateBeforeToday = (entrevue) => {
        const today = new Date();
        const dateEntrevue = new Date(entrevue.dateHeure);
        return today > dateEntrevue;
    }

    const getFilteredCount = (filter) => {
        return entrevues.filter(entrevue => {
            switch (filter) {
                case "all":
                    return true;
                case "withButtons":
                    return statusMessages[entrevue.id] == null;
                case "withEvaluation":
                    return !evaluations.some(evaluation => evaluation.etudiant && evaluation.etudiant.email === entrevue.etudiantDTO.email) && entrevue.etudiantDTO.professeur;
                case "withPdf":
                    return evaluations.some(evaluation => evaluation.etudiant && evaluation.etudiant.email === entrevue.etudiantDTO.email) && entrevue.etudiantDTO.professeur;
                case "acceptedOnly":
                    return statusMessages[entrevue.id] !== null && statusMessages[entrevue.id] === "Candidature acceptée" && !entrevue.etudiantDTO.professeur;
                default:
                    return true;
            }
        }).length;
    };

    const filterOptions = [
        { value: "all", label: t('Tous'), icon: <FaListAlt className="filter-option-icon" /> },
        { value: "withButtons", label: t('Embaucher / Refuser'), icon: <FaClipboardList className="filter-option-icon" /> },
        { value: "withEvaluation", label: t('Avec Évaluation'), icon: <FaPercent className="filter-option-icon" /> },
        { value: "withPdf", label: t('Avec PDF'), icon: <FaFilePdf className="filter-option-icon" /> },
        { value: "acceptedOnly", label: t('Acceptée Seulement'), icon: <FaCheck className="filter-option-icon" /> }
    ];

    const filteredInterviews = entrevues.filter(entrevue => {
        if (filters.length === 0 || filters.includes("all")) {
            return true;
        }
        return filters.some(filter => {
            switch (filter) {
                case "withButtons":
                    return statusMessages[entrevue.id] == null;
                case "withEvaluation":
                    return !evaluations.some(evaluation => evaluation.etudiant && evaluation.etudiant.email === entrevue.etudiantDTO.email) && entrevue.etudiantDTO.professeur;
                case "withPdf":
                    return evaluations.some(evaluation => evaluation.etudiant && evaluation.etudiant.email === entrevue.etudiantDTO.email) && entrevue.etudiantDTO.professeur;
                case "acceptedOnly":
                    return statusMessages[entrevue.id] !== null && statusMessages[entrevue.id] === "Candidature acceptée" && !entrevue.etudiantDTO.professeur;
                default:
                    return true;
            }
        });
    });


    const formatDate = (dateString) => {
        const options = { day: '2-digit', month: 'long', year: 'numeric', hour: '2-digit', minute: '2-digit' };
        return new Date(dateString).toLocaleDateString(i18n.language, options);
    }

    const verificationSession = (data) => {

        setSession(data.session);
        fetchSession(data.session);
    }


    const groupedInterviews = groupInterviewsByOffer(entrevues);


    return (
        <>
            <EmployeurHeader userData={userData} onSendData={verificationSession}/>
            <div className="container-fluid p-4 mes-entrevues-container">
                <div className="container mt-5">
                    <h1 className="text-center mt-5 page-title">{t('vosEntrevues')}</h1>

                    <legend className="mb-5 mt-0"><i>*{t('AttendreDateEntrevue')}</i></legend>

                    <div className="filters">
                        {filterOptions.map(option => (
                            <label key={option.value} className="filter-option">
                                <input
                                    type="checkbox"
                                    value={option.value}
                                    checked={filters.includes(option.value)}
                                    onChange={handleFilterChange}
                                    disabled={option.value === "all" && filters.includes("all")}
                                />
                                {option.icon}
                                {option.label} ({getFilteredCount(option.value)})
                            </label>
                        ))}
                    </div>


                    {Object.keys(entrevues).length === 0 ? (
                        <div className="alert alert-info mt-3 no-offres-alert">{t('AccuneOffreTrouve')}</div>
                    ) : (
                        <div className="row mt-3 mb-3">
                            {Object.values(groupInterviewsByOffer(filteredInterviews)).map(({offer, entrevues}) => (
                                <div key={offer.id} className="col-md-12 offre-card">
                                    <h5 className="offre-title">{t('Offre')} #{offer.id}: {offer.titre}</h5>
                                    <hr/>
                                    <ul className="entrevue-list">
                                        {entrevues.map((entrevue) => (
                                            <li
                                                key={entrevue.id}
                                                className="entrevue-item text-capitalize"
                                                onClick={() => handleInterviewClick(entrevue)}
                                            >
                                                <div style={{minWidth: "15em", marginRight: "3em"}}>
                                                    <span style={{fontSize: "1rem"}}>
                                                        <strong>{t('Entrevue')}</strong> - {entrevue.etudiantDTO.firstName} {entrevue.etudiantDTO.lastName}
                                                    </span>
                                                    <br/>

                                                    <span className="entrevue-details">
                                                        <FaCalendarAlt/> &nbsp;
                                                        {formatDate(entrevue.dateHeure)}
                                                    </span>
                                                    <br/>


                                                    <span className="entrevue-details">
                                                        <FaLocationPinLock/> &nbsp;
                                                        {entrevue.location}
                                                    </span>
                                                </div>

                                                {entrevue.etudiantDTO.professeur && (
                                                    <div className="evaluation-possible">
                                                        {evaluations.some(
                                                            evaluation =>
                                                                evaluation.etudiant &&
                                                                evaluation.etudiant.email === entrevue.etudiantDTO.email
                                                        ) ? (
                                                            <button
                                                                className="btn btn-success"
                                                                onClick={() => genererPdf(entrevue)}
                                                            >
                                                                {t('GenererEvaluationEmployeurPDF')}
                                                            </button>
                                                        ) : (
                                                            <strong
                                                                className="evaluation-possible-text">{t('EvaluationDisponible')}
                                                            </strong>
                                                        )}
                                                    </div>
                                                )}
                                                
                                                {showButtonsIfDateBeforeToday(entrevue) && (
                                                    <div className="m-auto d-flex">
                                                        {statusMessages[entrevue.id] ? (
                                                            <div
                                                                className="status-message">{statusMessages[entrevue.id]}</div>
                                                        ) : (
                                                            <div className="entrevue-actions">
                                                                <div className="icon-block">
                                                                    <button
                                                                        className={`btn btn-lg rounded-start-pill custom-btn icon-accept`}
                                                                        onClick={() => handleAccept(entrevue)}
                                                                        style={{margin: "0", fontSize: "1.2rem"}}
                                                                    >
                                                                        {t('Embaucher')}
                                                                    </button>
                                                                </div>
                                                                <div className="icon-block">
                                                                    <button
                                                                        className={`btn btn-lg rounded-end-pill custom-btn icon-refuse`}
                                                                        onClick={() => handleRefuse(entrevue)}
                                                                        style={{margin: "0", fontSize: "1.2rem"}}
                                                                    >
                                                                        {t('Refuser')}
                                                                    </button>
                                                                </div>
                                                            </div>
                                                        )}
                                                    </div>
                                                )}
                                            </li>
                                        ))}
                                    </ul>
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>
            <ConfirmModal
                show={showModal}
                onClose={() => setShowModal(false)}
                onConfirm={currentAction}
                message={t('ConfirmerVotreChoix')}
            />

            {selectedEntrevue && (
                <Modal show={showDetailsModal} onHide={closeDetailsModal} size="lg">
                    <form>
                        <Modal.Header closeButton>
                            <Modal.Title>{t('EvaluationDe')} {selectedEntrevue?.etudiantDTO.firstName} {selectedEntrevue?.etudiantDTO.lastName}</Modal.Title>
                        </Modal.Header>
                        <Modal.Body>
                            <h5 className="section-title">
                                {t('PRODUCTIVITE')}
                            </h5>
                            <p>{t('CapaciteOptimiserRendementTravail')}</p>
                            <table className="table table-bordered">
                                <thead>
                                <tr>
                                    <th>{t('Criteres')}</th>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <th key={option.value} className="text-center">{option.label}</th>
                                    ))}
                                </tr>
                                </thead>
                                <tbody>
                                {/* Critère: Planifier et organiser son travail */}
                                <tr>
                                    <td>a) {t('PlanifierOrganiserTravailEfficace')}</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                id={`radio-${option.value}`}
                                                name="planifOrganiserTravail"
                                                value={option.value}
                                                checked={evaluation.planifOrganiserTravail === option.value}
                                                onChange={(e) => handleChange(e, "planifOrganiserTravail")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                {/* Critère: Comprendre rapidement les directives */}
                                <tr>
                                    <td>b) {t('ComprendreDirectivesTravail')}</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="comprendreDirectives"
                                                value={option.value}
                                                checked={evaluation.comprendreDirectives === option.value}
                                                onChange={(e) => handleChange(e, "comprendreDirectives")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                {/* Critère: Maintenir un rythme de travail soutenu */}
                                <tr>
                                    <td>c) {t('MaintenirRythmeTravailSoutenu')}</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="maintenirRythmeTravail"
                                                value={option.value}
                                                checked={evaluation.maintenirRythmeTravail === option.value}
                                                onChange={(e) => handleChange(e, "maintenirRythmeTravail")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                {/* Critère: Établir ses priorités */}
                                <tr>
                                    <td>d) {t('EtablirPriorites')}</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="etablirPriorites"
                                                value={option.value}
                                                checked={evaluation.etablirPriorites === option.value}
                                                onChange={(e) => handleChange(e, "etablirPriorites")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                {/* Critère: Respecter ses échéanciers */}
                                <tr>
                                    <td>e) {t('RespecterEcheanciers')}</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="respecterEcheanciers"
                                                value={option.value}
                                                checked={evaluation.respecterEcheanciers === option.value}
                                                onChange={(e) => handleChange(e, "respecterEcheanciers")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                </tbody>
                            </table>

                            {/* Commentaires Section */}
                            <h5 className="section-title">
                                {t('Commentaires')}
                            </h5>
                            <div className="mb-3">
                                <textarea
                                    className="form-control"
                                    name="commentairesProductivite"
                                    value={evaluation.commentairesProductivite || ""}
                                    onChange={(e) => setEvaluation({
                                        ...evaluation,
                                        commentairesProductivite: e.target.value
                                    })}
                                    placeholder={t('AjoutezVosCommentairesIci')}
                                    required
                                />
                            </div>

                            {/* QUALITÉ DU TRAVAIL Section */}
                            <h5 className="section-title">
                                {t('qualite_du_travail')}
                            </h5>
                            <p>{t('capacité_de_s_acquitter_des_tâches')}</p>
                            <table className="table table-bordered">
                                <thead>
                                <tr>
                                    <th>{t('Criteres')}</th>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <th key={option.value} className="text-center">{option.label}</th>
                                    ))}
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>{t('respecter_les_mandats')}</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="respecterMandats"
                                                value={option.value}
                                                checked={evaluation.respecterMandats === option.value}
                                                onChange={(e) => handleChange(e, "respecterMandats")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                <tr>
                                    <td>{t('porter_attention_aux_details')}</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="attentionAuxDetails"
                                                value={option.value}
                                                checked={evaluation.attentionAuxDetails === option.value}
                                                onChange={(e) => handleChange(e, "attentionAuxDetails")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                <tr>
                                    <td>{t('verifier_son_travail')}</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="verifierTravail"
                                                value={option.value}
                                                checked={evaluation.verifierTravail === option.value}
                                                onChange={(e) => handleChange(e, "verifierTravail")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                <tr>
                                    <td>{t('rechercher_des_occurrences_de_se_perfectionner')}</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="perfectionnement"
                                                value={option.value}
                                                checked={evaluation.perfectionnement === option.value}
                                                onChange={(e) => handleChange(e, "perfectionnement")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                <tr>
                                    <td>{t('faire_une_bonne_analyse_des_problemes')}</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="analyseProblemes"
                                                value={option.value}
                                                checked={evaluation.analyseProblemes === option.value}
                                                onChange={(e) => handleChange(e, "analyseProblemes")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                </tbody>
                            </table>

                            {/* Commentaires Section */}
                            <h5 className="section-title">
                                {t('commentaires')}
                            </h5>
                            <div className="mb-3">
                            <textarea
                                className="form-control"
                                name="commentairesQualiteTravail"
                                value={evaluation.commentairesQualiteTravail || ""}
                                onChange={(e) => setEvaluation({
                                    ...evaluation,
                                    commentairesQualiteTravail: e.target.value
                                })}
                                placeholder={t('AjoutezVosCommentairesIci')}
                                required
                            />
                            </div>


                            {/* Section: QUALITÉS DES RELATIONS INTERPERSONNELLES */}
                            <h5 className="section-title">
                                {t('QUALITÉS_DES_RELATIONS_INTERPERSONNELLES')}
                            </h5>
                            <p>{t('Capacité_détablir_des_interrelations_harmonieuses_dans_son_milieu_de_travail')}</p>
                            <table className="table table-bordered">
                                <thead>
                                <tr>
                                    <th>{t('Criteres')}</th>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <th key={option.value} className="text-center">{option.label}</th>
                                    ))}
                                </tr>
                                </thead>
                                <tbody>
                                {/* Critère: Établir des contacts */}
                                <tr>
                                    <td>{t('a_Etablir_facilement_des_contacts_avec_les_gens')}</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="etablirContacts"
                                                value={option.value}
                                                checked={evaluation.etablirContacts === option.value}
                                                onChange={(e) => handleChange(e, "etablirContacts")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                {/* Critère: Contribuer au travail d’équipe */}
                                <tr>
                                    <td>{t('b_Contribuer_activement_au_travail_déquipe')}</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="contribuerTravailEquipe"
                                                value={option.value}
                                                checked={evaluation.contribuerTravailEquipe === option.value}
                                                onChange={(e) => handleChange(e, "contribuerTravailEquipe")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                {/* Critère: S’adapter à la culture */}
                                <tr>
                                    <td>{t('c_Sadapter_facilement_à_la_culture_de_lentreprise')}</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="adapterCultureEntreprise"
                                                value={option.value}
                                                checked={evaluation.adapterCultureEntreprise === option.value}
                                                onChange={(e) => handleChange(e, "adapterCultureEntreprise")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                {/* Critère: Accepter les critiques */}
                                <tr>
                                    <td>{t('d_Accepter_les_critiques_constructives')}</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="accepterCritiques"
                                                value={option.value}
                                                checked={evaluation.accepterCritiques === option.value}
                                                onChange={(e) => handleChange(e, "accepterCritiques")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                {/* Critère: Respectueux envers les gens */}
                                <tr>
                                    <td>{t('e_Etre_respectueux_envers_les_gens')}</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="respectueux"
                                                value={option.value}
                                                checked={evaluation.respectueux === option.value}
                                                onChange={(e) => handleChange(e, "respectueux")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                {/* Critère: Écoute active */}
                                <tr>
                                    <td>{t('f_Faire_preuve_d_ecoute_active_en_essayant_de_comprendre_le_point_de_vue_de_lautre')}</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="ecouteActive"
                                                value={option.value}
                                                checked={evaluation.ecouteActive === option.value}
                                                onChange={(e) => handleChange(e, "ecouteActive")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                </tbody>
                            </table>

                            {/* Commentaires Section */}
                            <h5 className="section-title">
                                {t('commentaires')}
                            </h5>
                            <div className="mb-3">
                                <textarea
                                    className="form-control"
                                    name="commentairesRelationsInterpersonnelles"
                                    value={evaluation.commentairesRelationsInterpersonnelles || ""}
                                    onChange={(e) => setEvaluation({
                                        ...evaluation,
                                        commentairesRelationsInterpersonnelles: e.target.value
                                    })}
                                    placeholder={t('AjoutezVosCommentairesIci')}
                                    rows="4"
                                ></textarea>
                            </div>


                            <h5 className="section-title">
                                {t('HABILETÉS_PERSONNELLES')}
                            </h5>
                            <p>{t('Criteres')}</p>
                            <table className="table table-bordered">
                                <thead>
                                <tr>
                                    <th>{t('Criteres')}</th>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <th key={option.value} className="text-center">{t(option.label)}</th>
                                    ))}
                                </tr>
                                </thead>
                                <tbody>
                                {/* Critère: Démontrer de l’intérêt */}
                                <tr>
                                    <td>{t('a_Démontrer_de_l_intérêt_et_de_la_motivation_au_travail')}</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="interetMotivationTravail"
                                                value={option.value}
                                                checked={evaluation.interetMotivationTravail === option.value}
                                                onChange={(e) => handleChange(e, "interetMotivationTravail")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                {/* Critère: Exprimer clairement ses idées */}
                                <tr>
                                    <td>{t('b_Exprimer_clairement_ses_idées')}</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="exprimerIdees"
                                                value={option.value}
                                                checked={evaluation.exprimerIdees === option.value}
                                                onChange={(e) => handleChange(e, "exprimerIdees")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                {/* Critère: Faire preuve d'initiative */}
                                <tr>
                                    <td>{t('c_Faire_preuve_d_initiative')}</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="initiative"
                                                value={option.value}
                                                checked={evaluation.initiative === option.value}
                                                onChange={(e) => handleChange(e, "initiative")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                {/* Critère: Travailler de façon sécuritaire */}
                                <tr>
                                    <td>{t('d_Travailler_de_façon_sécuritaire')}</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="travailSecuritaire"
                                                value={option.value}
                                                checked={evaluation.travailSecuritaire === option.value}
                                                onChange={(e) => handleChange(e, "travailSecuritaire")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                {/* Critère: Bon sens des responsabilités */}
                                <tr>
                                    <td>{t('e_Démontrer_un_bon_sens_des_responsabilités')}</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="sensResponsabilite"
                                                value={option.value}
                                                checked={evaluation.sensResponsabilite === option.value}
                                                onChange={(e) => handleChange(e, "sensResponsabilite")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                {/* Critère: Ponctualité et assiduité */}
                                <tr>
                                    <td>{t('f_Être_ponctuel_et_assidu_à_son_travail')}</td>
                                    {EvaluationConformiteOptions.map((option) => (
                                        <td key={option.value} className="text-center">
                                            <input
                                                type="radio"
                                                name="ponctualiteAssiduite"
                                                value={option.value}
                                                checked={evaluation.ponctualiteAssiduite === option.value}
                                                onChange={(e) => handleChange(e, "ponctualiteAssiduite")}
                                                required
                                            />
                                        </td>
                                    ))}
                                </tr>
                                </tbody>
                            </table>

                            {/* Commentaires Section */}
                            <h5 className="section-title">
                                {t('Commentaires')}
                            </h5>
                            <div className="mb-3">
                                <textarea
                                    className="form-control"
                                    name="habiletePersonnelles"
                                    value={evaluation.habiletePersonnelles}
                                    onChange={(e) => setEvaluation({
                                        ...evaluation,
                                        habiletePersonnelles: e.target.value
                                    })}
                                    rows="4"
                                    placeholder={t('AjoutezVosCommentairesIci')}
                                ></textarea>
                            </div>

                            <form>
                                {/* Section Appréciation globale */}
                                <h5>{t('APPRÉCIATION_GLOBALE_DU_STAGIAIRE')}</h5>
                                <div className="form-group">
                                    <label>
                                        <input
                                            className="me-3"
                                            type="radio"
                                            name="appreciationGlobale"
                                            value="DEPASSE_BEAUCOUP"
                                            checked={evaluation.appreciationGlobale === "DEPASSE_BEAUCOUP"}
                                            onChange={(e) => handleChange(e, "appreciationGlobale")}
                                        />
                                        {t('Les_habiletés_démontrées_dépassent_de_beaucoup_les_attentes')}
                                    </label>
                                    <br/>
                                    <label>
                                        <input
                                            className="me-3"
                                            type="radio"
                                            name="appreciationGlobale"
                                            value="DEPASSE"
                                            checked={evaluation.appreciationGlobale === "DEPASSE"}
                                            onChange={(e) => handleChange(e, "appreciationGlobale")}
                                        />
                                        {t('Les_habiletés_démontrées_dépassent_les_attentes')}
                                    </label>
                                    <br/>
                                    <label>
                                        <input
                                            className="me-3"
                                            type="radio"
                                            name="appreciationGlobale"
                                            value="REPOND_PLEINEMENT"
                                            checked={evaluation.appreciationGlobale === "REPOND_PLEINEMENT"}
                                            onChange={(e) => handleChange(e, "appreciationGlobale")}
                                        />
                                        {t('Les_habiletés_démontrées_répondent_pleinement_aux_attentes')}
                                    </label>
                                    <br/>
                                    <label>
                                        <input
                                            className="me-3"
                                            type="radio"
                                            name="appreciationGlobale"
                                            value="REPOND_PARTIELLEMENT"
                                            checked={evaluation.appreciationGlobale === "REPOND_PARTIELLEMENT"}
                                            onChange={(e) => handleChange(e, "appreciationGlobale")}
                                        />
                                        {t('Les_habiletés_démontrées_répondent_partiellement_aux_attentes')}
                                    </label>
                                    <br/>
                                    <label>
                                        <input
                                            className="me-3"
                                            type="radio"
                                            name="appreciationGlobale"
                                            value="NE_REPOND_PAS"
                                            checked={evaluation.appreciationGlobale === "NE_REPOND_PAS"}
                                            onChange={(e) => handleChange(e, "appreciationGlobale")}
                                        />
                                        {t('Les_habiletés_démontrées_ne_répondent_pas_aux_attentes')}
                                    </label>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="appreciationComment">{t('PRÉCISEZ_VOTRE_APPRÉCIATION')}:</label>
                                    <textarea
                                        id="commentairesAppreciation"
                                        className="form-control"
                                        value={evaluation.commentairesAppreciation}
                                        onChange={(e) => setEvaluation({
                                            ...evaluation,
                                            commentairesAppreciation: e.target.value
                                        })} rows="4"
                                        placeholder={t('AjoutezVosCommentairesIci')}
                                    ></textarea>
                                </div>

                                {/* Discussion et nombre d'heures d'encadrement */}
                                <div className="form-group">
                                    <label>{t('Cette_évaluation_a_été_discutée_avec_le_stagiaire')} :</label>
                                    <div>
                                        <label>
                                            <input
                                                className="me-2"
                                                type="radio"
                                                name="evaluationDiscuteeAvecStagiaire"
                                                value="true"
                                                checked={evaluation.evaluationDiscuteeAvecStagiaire === "true"}
                                                onChange={(e) => handleChange(e, "evaluationDiscuteeAvecStagiaire")}
                                            />
                                            {t('Oui')}
                                        </label>
                                        <label>
                                            <input
                                                className="me-2 ms-3"
                                                type="radio"
                                                name="evaluationDiscuteeAvecStagiaire"
                                                value="false"
                                                checked={evaluation.evaluationDiscuteeAvecStagiaire === "false"}
                                                onChange={(e) => handleChange(e, "evaluationDiscuteeAvecStagiaire")}
                                            />
                                            {t('Non')}
                                        </label>
                                    </div>
                                </div>
                                <div className="form-group">
                                    <label>{t('Veuillez_indiquer_le_nombre_d_heures_réel_par_semaine_d_encadrement_accordé_au_stagiaire')} :</label>
                                    <input
                                        type="number"
                                        name="heuresEncadrementParSemaine"
                                        className="form-control"
                                        value={evaluation.heuresEncadrementParSemaine}
                                        onChange={(e) => handleInputChange(e, "heuresEncadrementParSemaine")}
                                        required
                                    />
                                </div>

                                {/* Section pour l'entreprise */}
                                <h5>{t('L_ENTREPRISE_AIMERAIT_ACCUEILLIR_CET_ÉLÈVE_POUR_SON_PROCHAIN_STAGE')} :</h5>
                                <div className="form-group">
                                    <label>
                                        <input
                                            className="me-2"
                                            type="radio"
                                            name="entrepriseSouhaiteProchainStage"
                                            value="OUI"
                                            checked={evaluation.entrepriseSouhaiteProchainStage === "OUI"}
                                            onChange={(e) => handleChange(e, "entrepriseSouhaiteProchainStage")}
                                        />
                                        {t('Oui')}
                                    </label>
                                    <label>
                                        <input
                                            className="me-2 ms-3"
                                            type="radio"
                                            name="entrepriseSouhaiteProchainStage"
                                            value="NON"
                                            checked={evaluation.entrepriseSouhaiteProchainStage === "NON"}
                                            onChange={(e) => handleChange(e, "entrepriseSouhaiteProchainStage")}
                                        />
                                        {t('Non')}
                                    </label>
                                    <label>
                                        <input
                                            className="me-2 ms-3"
                                            type="radio"
                                            name="entrepriseSouhaiteProchainStage"
                                            value="PEUT_ETRE"
                                            checked={evaluation.entrepriseSouhaiteProchainStage === "PEUT_ETRE"}
                                            onChange={(e) => handleChange(e, "entrepriseSouhaiteProchainStage")}
                                        />
                                        {t('Peut-être')}
                                    </label>
                                </div>

                                <div className="form-group">
                                    <label>{t('La_formation_technique_du_stagiaire_était_elle_suffisante_pour_accomplir_le_mandat_de_stage')}?</label>
                                    <div>
                                        <input
                                            type="text"
                                            name="commentairesFormationTechnique"
                                            className="form-control"
                                            value={evaluation.commentairesFormationTechnique}
                                            onChange={(e) => setEvaluation({
                                                ...evaluation,
                                                commentairesFormationTechnique: e.target.value
                                            })}/>
                                    </div>
                                </div>

                                {/* Signature */}
                                <div className="form-group">
                                    <label>{t('Nom')}</label>
                                    <p>{userData.firstName} {userData.lastName}</p>
                                </div>
                                <div className="form-group">
                                    <label>{t('Fonction')}</label>
                                    <input
                                        type="text"
                                        name="fonction"
                                        className="form-control"
                                        value={evaluation.fonction}
                                        onChange={(e) => handleInputChange(e, "fonction")}
                                    />
                                </div>
                                <div className="form-group">
                                    <label>{t('Date')} : {today}</label>
                                </div>
                            </form>

                        </Modal.Body>
                        <Modal.Footer>
                            <button type="button" className="btn btn-success"
                                    onClick={() => creerEvaluationEtudiant(userData.credentials.email, selectedEntrevue.etudiantDTO.email, evaluation)}>
                                {t('creer_evaluation')}
                            </button>
                            <button type="button" className="btn btn-danger" onClick={closeDetailsModal}>{t('close')}</button>
                        </Modal.Footer>

                    </form>
                </Modal>
            )}
        </>
    )
        ;
}

export default MesEntrevueAccepte;
