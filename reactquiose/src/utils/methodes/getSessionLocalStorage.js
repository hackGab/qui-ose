export const getLocalStorageSession = () => {
    const session = localStorage.getItem('session');
    if (session) {
        return session;
    } else {
        return '';
    }
}