// src/utils/dateUtils.js
export const calculateNextSessions = () => {
    const currentDates = new Date();
    const currentMonths = currentDates.getMonth();
    const currentYears = currentDates.getFullYear() % 100;
    let nextSessions;

    if (currentMonths >= 8) {
        nextSessions = `HIVER${currentYears + 1}`;
    } else if (currentMonths >= 4) {
        nextSessions = `AUTOMNE${currentYears}`;
    } else {
        nextSessions = `ETE ${currentYears}`;
    }
    return nextSessions;
};
