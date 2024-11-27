export const fetchOffresWithDetails = async (employeurEmail, session) => {
    const response = await fetch(`http://localhost:8081/offreDeStage/offresEmployeur/${employeurEmail}/session/${session}`);
    if (!response.ok) throw new Error("Erreur lors du chargement des offres.");

    const offresData = await response.json();
    return Promise.all(
        offresData.map(async (offre) => {
            const [etudiantsResponse, entrevueResponse] = await Promise.all([
                fetch(`http://localhost:8081/offreDeStage/${offre.id}/etudiants`),
                fetch(`http://localhost:8081/entrevues/offre/${offre.id}`),
            ]);

            const etudiantsData = await etudiantsResponse.json();
            const entrevueData = entrevueResponse.ok ? await entrevueResponse.json() : [];
            return { ...offre, etudiantsData, entrevueData };
        })
    );
};
