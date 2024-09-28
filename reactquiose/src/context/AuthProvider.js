import React, { createContext, useContext, useState, useEffect } from "react";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [authState, setAuthState] = useState(() => {
        const storedAuth = localStorage.getItem("authState");
        return storedAuth ? JSON.parse(storedAuth) : { isAuthenticated: false, role: null };
    });

    const login = (userData) => {
        setAuthState({ isAuthenticated: true, role: userData.role });
        localStorage.setItem("authState", JSON.stringify({ isAuthenticated: true, role: userData.role }));
    };

    const logout = () => {
        setAuthState({ isAuthenticated: false, role: null });
        localStorage.removeItem("authState");
    };

    return (
        <AuthContext.Provider value={{ authState, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
