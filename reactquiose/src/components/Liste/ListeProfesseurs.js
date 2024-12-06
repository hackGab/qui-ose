import React, { useEffect, useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Link } from 'react-router-dom';
import {FaEnvelope, FaPhone, FaChevronDown, FaChevronUp, FaSearch} from 'react-icons/fa';
import { useTranslation } from 'react-i18next';
import GestionnaireHeader from "../Header/GestionnaireHeader";
import '../../CSS/ListeProfesseurs.css';

function ListeProfesseurs() {
    const { t } = useTranslation();
    const [professeurs, setProfesseurs] = useState([]);
    const [departments, setDepartments] = useState({});
    const [collapsed, setCollapsed] = useState({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');



    useEffect(() => {
        fetch('http://localhost:8081/professeur/all', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('lors de la récupération des données');
                }
                return response.json();
            })
            .then(data => {
                const groupedByDepartment = data.reduce((acc, prof) => {
                    if (!acc[prof.departement]) {
                        acc[prof.departement] = [];
                    }
                    acc[prof.departement].push(prof);
                    return acc;
                }, {});
                setDepartments(groupedByDepartment);
                setProfesseurs(data);
                setLoading(false);
            })
            .catch(error => {
                setError(error.message);
                setLoading(false);
            });
    }, []);

    const toggleCollapse = (department) => {
        setCollapsed(prevState => ({
            ...prevState,
            [department]: !prevState[department]
        }));
    };

    const collapseAll = () => {
        const newCollapsed = {};
        Object.keys(departments).forEach(department => {
            newCollapsed[department] = true;
        });
        setCollapsed(newCollapsed);
    };

    const expandAll = () => {
        const newCollapsed = {};
        Object.keys(departments).forEach(department => {
            newCollapsed[department] = false;
        });
        setCollapsed(newCollapsed);
    };

    const filteredProfessors = professeurs.filter(prof =>
        prof.email.toLowerCase().includes(searchTerm.toLowerCase()) ||
        prof.firstName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        prof.lastName.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const verificationSession = () => {};


    if (loading) {
        return <div className="text-center mt-5">
            <div className="spinner-border" role="status"></div>
            <br/>
            <span className="sr-only">{t('chargementProfesseurs')}</span>
        </div>;
    }

    if (error) {
        return <p className="text-center mt-5 text-danger">Erreur: {error}</p>;
    }

    return (
        <>
            <GestionnaireHeader onSendData={verificationSession}/>
            <div className="container-fluid p-4">
                <div className="container flex-grow-1 pt-5 mt-5">
                    <h1 className="mb-4 text-center">{t('profListTitle')}</h1>

                    {Object.keys(departments).length === 0 ? (
                        <p className="text-center mt-5">{t('AucunProfesseurTrouve')}</p>
                    ) : (
                        <p className="text-center mb-4">{t('profListSubtitle')}</p>
                    )}

                    <div className="d-flex justify-content-between mb-4">
                        <div className="input-group w-25 search-bar">
                            <input
                                type="text"
                                className="form-control"
                                placeholder={t('searchProfessors')}
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value)}
                            />
                            <div className="input-group-append">
                                <span className="input-group-text"><FaSearch/></span>
                            </div>
                        </div>
                        <div>
                            <button className="btn btn-secondary" onClick={collapseAll}>
                                <FaChevronUp/> {t('collapseAll')}</button>
                            &nbsp;
                            <button className="btn btn-secondary" onClick={expandAll}>
                                <FaChevronDown/> {t('expandAll')}
                            </button>
                        </div>
                    </div>

                    <div className="row">
                        {Object.keys(departments).map((department) => {
                            const filteredDepartmentProfessors = filteredProfessors.filter(prof => prof.departement === department);
                            return (
                                <div key={department} className="col-12 col-md-6 col-lg-4 mb-4">
                                    <div className="department-header entrevuesTitreBox"
                                         onClick={() => toggleCollapse(department)}>
                                        {department.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, char => char.toUpperCase())}
                                        <span>
                                            ({filteredDepartmentProfessors.length})
                                            &nbsp;
                                            {collapsed[department] ? <FaChevronUp/> : <FaChevronDown/>}
                                        </span>
                                    </div>
                                    <div className={`collapse ${collapsed[department] ? 'show' : ''}`}>
                                        <div>
                                            <div className="row p-1 shadow w-100 m-auto entrevueBox border-0">
                                                {filteredDepartmentProfessors.map((prof) => {
                                                    return (
                                                        <div className="col-12 mb-4" key={prof.id}>
                                                            <Link
                                                                to={`/detailsProfesseur/${prof.email}`}
                                                                className="text-decoration-none"
                                                                state={{professeur: prof}}>

                                                                <div className="card shadow w-100">
                                                                    <div className="card-body overflow-hidden text-nowrap">
                                                                        <h5 className="card-title text-capitalize">{prof.firstName + " " + prof.lastName}</h5>
                                                                        <p className="card-text mb-2">
                                                                            <FaEnvelope/> {prof.email} <br/>
                                                                            <FaPhone/> {prof.phoneNumber} <br/>
                                                                            <span
                                                                                className="badge bg-info badge-responsive">
                                                                                {t('department')}: {prof.departement.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, char => char.toUpperCase())}
                                                                            </span>
                                                                        </p>
                                                                    </div>
                                                                </div>
                                                            </Link>
                                                        </div>
                                                    );
                                                })}
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            );
                        })}
                    </div>
                </div>
            </div>
        </>
    );
}

export default ListeProfesseurs;