import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import '../CSS/TableauContrat.css';


function TableauContrat({ contrat }) {
    const { t } = useTranslation();
    const [isExpanded, setIsExpanded] = useState(true);

    const toggleExpand = () => {
        setIsExpanded(!isExpanded);
    };

    const renderRows = () => {
        return Object.keys(contrat).map((key) => (
            <tr key={key}>
                <td><strong>{t(key)} :</strong></td>
                <td>{contrat[key]}</td>
            </tr>
        ));
    };

    return (
        <div className="tableau-contrat">
            <button onClick={toggleExpand} className="toggle-button">
                {isExpanded ? t('ReplierContrat') : t('DeroulerContrat')}
            </button>
            {isExpanded && (
                <table className="table-contrat">
                    <tbody>
                    {renderRows()}
                    </tbody>
                </table>
            )}
        </div>
    );
}

export default TableauContrat;