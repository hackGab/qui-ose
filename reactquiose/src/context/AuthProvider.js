import React, { createContext, useContext, useState, useEffect } from "react";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [authState, setAuthState] = useState(() => {
        try {
            const storedAuth = localStorage.getItem("authState");
            return storedAuth ? JSON.parse(storedAuth) : { isAuthenticated: false, userData: null, accessToken: null };
        } catch (error) {
            console.error("Error parsing localStorage data:", error);
            return { isAuthenticated: false, userData: null, accessToken: null };
        }
    });

    useEffect(() => {
        try {
            localStorage.setItem("authState", JSON.stringify(authState));
        } catch (error) {
            console.error("Error setting localStorage data:", error);
        }
    }, [authState]);

    const login = (userDataWithToken) => {
        if (userDataWithToken && userDataWithToken.role) {
            setAuthState({ isAuthenticated: true, userData: userDataWithToken });
            console.log("User logged in:", userDataWithToken);
            return true;
        } else {
            console.error("Invalid user data received:", userDataWithToken);
            return false;
        }
    };


    const logout = () => {
        setAuthState({ isAuthenticated: false, userData: null, accessToken: null });
        localStorage.removeItem("authState");
        localStorage.removeItem("accessToken");
        localStorage.removeItem("userData");
        console.log("User logged out");
    };

    return (
        <AuthContext.Provider value={{ authState, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
